package me.richdev.TheWatcher.Commands;

import me.richdev.TheWatcher.Commands.List.Ping;
import me.richdev.TheWatcher.GuildSystem.GuildConfiguration;
import me.richdev.TheWatcher.Main;
import me.richdev.TheWatcher.Utils.MessageUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CommandHandler extends ListenerAdapter {

    private Set<Command> registeredCommands;
    public CommandHandler() {
        registeredCommands = new HashSet<>();

        registeredCommands.add(new Ping());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if(e.getAuthor() == null || e.getChannel() == null) return;

        if(!isCMD(Main.getInstance().getGuildsHandler().getGuild(e.getGuild().getId()), e.getMessage())) return;

        if(e.getAuthor().isBot()) return;

        String[] args = translateArguments(e.getMessage());

        if(args[0].length() == 1) return;
        ChatSender chat = new ChatSender(e);
        String cmd = args[0].substring(1); // > CMD || Retrieving cmd without the prefix.

        Command search = searchCommand(cmd);

        if (search == null) {
            chat.sendError("Comando no encontrado.");
            return;
        }
        e.getMessage().delete().queue(); // BORRAMOS EL MENSAJE DEL JUGADOR.

         CommandInfo commandInfo = search.getClass().getAnnotation(CommandInfo.class);

        if(!commandInfo.fromPrivateChat() && e.isFromType(ChannelType.PRIVATE)) {
            MessageUtils.sendMessage("**Este comando solo se puede usar en una guild.**", e.getChannel(), 10);
        } else {
            try {
                search.execute(cmd, args, e, chat);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    private String[] translateArguments(Message msg) {
        return msg.getContentRaw().split(" ");
    }

    private boolean isCMD(GuildConfiguration c, Message msg) {
        return msg.getContentRaw().startsWith(c.getPrefixCommand());
    }

    private Command searchCommand(String aliase) {
        for (Command command : registeredCommands) {
            if(Arrays.asList(command.getClass().getAnnotation(CommandInfo.class).aliases()).contains(aliase)) {
                return command;
            }
        }
        return null;
    }

    public class ChatSender {
        MessageReceivedEvent event;
        final int MIN_TIME_TO_DELETE = 15;

        public ChatSender(MessageReceivedEvent event) {
            this.event = event;
            event.getChannel().sendTyping().queue();
        }

        public void sendMessage(String msg) {
            sendMessage(msg, MIN_TIME_TO_DELETE);
        }

        public void sendMessage(String msg, boolean embed, Color color) {
            if(embed) {
                sendMessage(new EmbedBuilder().appendDescription(msg).setColor(color));
            } else {
                sendMessage(msg);
            }
        }

        public void sendMessage(String msg, boolean embed) {
            if(embed) {
                sendMessage(new EmbedBuilder().appendDescription(msg));
            } else {
                sendMessage(msg);
            }
        }

        public void sendMessage(String msg, String title, boolean embed) {
            if(embed) {
                sendMessage(new EmbedBuilder().appendDescription(msg).setTitle(title));
            } else {
                sendMessage(msg);
            }
        }

        public void sendError(String msg) {
            sendMessage(new EmbedBuilder().appendDescription(msg).setTitle("ERROR").setColor(Color.RED));
        }

        public void sendInfo(String msg) {
            sendMessage(new EmbedBuilder().appendDescription(msg).setTitle("INFO"));
        }


        public void sendMessage(Message msg) {
            sendMessage(msg, MIN_TIME_TO_DELETE);
        }

        public void sendMessage(EmbedBuilder builder) {
            sendMessage(builder, MIN_TIME_TO_DELETE);
        }

        public void sendMessage(EmbedBuilder embed, int time) {
            MessageUtils.sendMessage(embed.build(), event.getChannel(), time);
        }

        public void sendMessage(String msg, int time) {
            MessageUtils.sendMessage(msg, event.getChannel(), time);
        }

        public void sendMessage(Message msg, int time) {
            MessageUtils.sendMessage(msg, event.getChannel(), time);
        }

        public void sendNormalMessage(String msg, int time) {
            MessageUtils.sendMessage(msg, event.getChannel(), time);
        }
    }
}
