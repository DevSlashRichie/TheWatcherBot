package me.richdev.TheWatcher.Commands.List;

import me.richdev.TheWatcher.Commands.Command;
import me.richdev.TheWatcher.Commands.CommandHandler;
import me.richdev.TheWatcher.Commands.CommandInfo;
import me.richdev.TheWatcher.GuildSystem.Configuration.ConfigObject;
import me.richdev.TheWatcher.GuildSystem.GuildInfo;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@SuppressWarnings("unchecked")
@CommandInfo(aliases = {"config"}, permissionGroup = "", fromPrivateChat = false, descriptionID = "test")
public class Config extends Command {
    @Override
    public void execute(String cmd, String[] args, MessageReceivedEvent e, CommandHandler.ChatSender sender, GuildInfo guildInfo) {

        // EXAMPLE: >config {config_module;config_ID} {value}
        //          >config WBM;wb_msg_active = true
        //          >config WBM;welcome_msg_str = "Hello how are you"

       if(args.length == 0) {
           StringBuilder sb = new StringBuilder();

           guildInfo.getConfigModules().forEach((module) -> {
               sb.append(module.getCallID()).append(";");
               module.getConfigurations().forEach((id, object) -> sb.append(id).append(" >> ").append(object.toString()));
           });

           sender.sendMessage(sb.toString(), "Your configuration at now.", true);
       }
        if(args.length < 1) return;

       String[] search = args[0].split(";");

       if(search.length < 1) {
           sender.sendError("Porfavor inserta un modulo y un ID de configuración.");
           return;
       }

        me.richdev.TheWatcher.GuildSystem.Configuration.Config  moduleConfig = guildInfo.getConfigModule(search[0]);

        if(moduleConfig == null) {
            sender.sendError("Ese module de configuración no existe. \n ejectua >config para ver tu configuración y la lista de configurables.");
            return;
        }

        ConfigObject configSection = moduleConfig.getConfig(search[1]);

        if(configSection == null) {
            sender.sendInfo("Esta seccción de configuración no existe.");
            return;
        }

        StringBuilder info = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            info.append(args[i]);
        }

        if (configSection.getType() == Boolean.class) {
            try {
                configSection.setData(Boolean.parseBoolean(args[1]));
            } catch (Exception ex) {
                sender.sendError("Ese argumento no puede ir en esta configuracion, solo true o false.");
            }
        } else if(configSection.getType() == String.class) {
            configSection.setData(info.toString());
        } else if(configSection.getType() == int.class) {
            if (!args[1].matches("[0-9]+")) {
                sender.sendError("Esta configuación solo acepta números.");
                return;
            }
            configSection.setData(Integer.parseInt(args[1]));
        } else {
            sender.sendInfo("Type not found.");
        }

    }

    private Class<?> recognizeType(Object object) {
        return object.getClass();
    }
}
