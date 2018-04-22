package me.richdev.TheWatcher.Utils;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class MessageUtils {

    public static boolean canTalk(TextChannel textChannel) {
        if(textChannel == null) return false;
        Member member  = textChannel.getGuild().getSelfMember();
        return member != null
                || member.hasPermission(textChannel, Permission.MESSAGE_READ)
                || member.hasPermission(textChannel, Permission.MESSAGE_WRITE);
    }


    public static void sendMessage(Message message, MessageChannel messageChannel, int time, Consumer<Message> success, Consumer<Throwable> error) {
        if(messageChannel instanceof TextChannel && !canTalk((TextChannel) messageChannel)) return;

        messageChannel.sendMessage(message).queue(m -> {
            if(success != null) {
                success.accept(m);
            }

            if(time != -1)
                m.delete().queueAfter(time, TimeUnit.SECONDS);
        }, e -> {
            if(error != null) {
                error.accept(e);
            }
        });

    }

    public static void sendMessage(String msg, MessageChannel channel, int time, Consumer<Message> success, Consumer<Throwable> error) {
        sendMessage(new MessageBuilder().append(msg).build(), channel, time, success, error);
    }

    public static void sendMessage(MessageEmbed msg, MessageChannel channel, int time, Consumer<Message> success, Consumer<Throwable> error) {
        if(channel instanceof TextChannel && !canTalk((TextChannel) channel)) return;

        sendMessage(new MessageBuilder().setEmbed(msg).build(), channel, time, success, error);
    }

}
