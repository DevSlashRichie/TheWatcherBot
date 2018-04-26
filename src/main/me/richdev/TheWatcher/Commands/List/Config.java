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

        // EXAMPLE: >config {config_id} {value}
        //          >config wb_msg = true
        //          >config welcome_msg_str = "Hello how are you"

       if(args.length == 0) {
           StringBuilder sb = new StringBuilder();

           guildInfo.getConfigWithIds().forEach((k,v) -> sb.append(k).append(" >> ").append(v.getObject().toString()).append("\n"));
           sender.sendMessage(sb.toString(), "Your configuration at now.", true);
       }
        if(args.length < 1) return;

        ConfigObject found = guildInfo.getConfigData(args[0]);

        if(found == null) {
            sender.sendError("Esa seccción de configuración existe. \n ejectua >config para ver tu configuración y la lista de configurables.");
            return;
        }

        String o = args[1];
        Class type = found.getType();

        // sender.sendInfo("DEBUG: \n" + type.getSimpleName());

        if(type == Boolean.class){
            if(!(o.equalsIgnoreCase("true") || o.equalsIgnoreCase("false")
                    || o.equalsIgnoreCase("yes") || o.equalsIgnoreCase("no"))) {

                sender.sendError("Para esta configuración solo puedes usar true o false");

            } else {
                found.setObject(Boolean.parseBoolean(o));
                sender.sendInfo("**" + args[0] + "** >> Changed to: " + args[1]);
            }
        } else if(type == String.class) {

            StringBuilder ib = new StringBuilder();

            for (int i = 1; i < args.length; i++) {
                ib.append(args[i]).append(" ");
            }

            found.setObject(ib.toString());
            sender.sendInfo(args[0] + " >> Changed to: " + args[1]);

        } else if(type == Integer.class) {

            found.setObject(Integer.parseInt(o));
            sender.sendInfo(args[0] + " >> Changed to: " + args[1]);

        } else {
            sender.sendInfo("Type not found.");
        }

    }
}
