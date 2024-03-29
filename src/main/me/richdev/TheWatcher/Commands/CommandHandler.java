package me.richdev.TheWatcher.Commands;

import me.richdev.TheWatcher.Commands.List.GetMyGold;
import me.richdev.TheWatcher.Commands.List.Ping;
import me.richdev.TheWatcher.Commands.Music.Select;
import me.richdev.TheWatcher.GuildSystem.GuildInfo;
import me.richdev.TheWatcher.Commands.Music.Join;
import me.richdev.TheWatcher.Commands.Music.Play;
import me.richdev.TheWatcher.Main;
import me.richdev.TheWatcher.Utils.ColorUtil;
import me.richdev.TheWatcher.Utils.MessageUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class CommandHandler extends ListenerAdapter {

    private Set<Command> registeredCommands;
    public CommandHandler() {
        registeredCommands = new HashSet<>();

        // GENERAL COMMANDS
        registeredCommands.add(new Ping());
        registeredCommands.add(new GetMyGold());

        // MUSIC COMMANDS
        registeredCommands.add(new Join());
        registeredCommands.add(new Play());
        registeredCommands.add(new Select());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if(e.getAuthor() == null || e.getChannel() == null) return;
        if(e.getAuthor().isBot()) return;
        if(e.isFromType(ChannelType.PRIVATE)) return; // NEED TO FIX THIS.

        GuildInfo guildInfo = Main.getInstance().getGuildsHandler().getGuild(e.getGuild().getId());
        ChatSender chat = new ChatSender(e);

        if(guildInfo == null) {
            chat.sendBeautifulMessage(new EmbedBuilder().setTitle("Something went wrong.").setDescription("Please report this to an admin").setColor(Color.RED).build());
            return;
        }

        if(!isCMD(Main.getInstance().getGuildsHandler().getGuild(e.getGuild().getId()), e.getMessage())) return;

        String[] args = translateArguments(e.getMessage());

        if(args[0].length() == 1) return;


        String cmd = args[0].substring(1); // > CMD || Retrieving cmd without the prefix.

        if (cmd.equalsIgnoreCase("help")) {

            StringBuilder sb = new StringBuilder();
            for (Command registeredCommand : registeredCommands) {
                CommandInfo commandInfo = registeredCommand.getClass().getAnnotation(CommandInfo.class);

                // TODO: MAKE A BETTER MESSAGE :)

                sb.append(registeredCommand.getClass().getSimpleName()).append(" >> ")
                    .append(guildInfo.translate(commandInfo.descriptionID()))
                .append("\n");

            }

            chat.sendMessage(sb.toString());
            return;
        }

        Command search = searchCommand(cmd);
        if (search == null) {
            chat.sendBeautifulMessage(new EmbedBuilder().setTitle(guildInfo.translate("commands.error.notfound.title")).setDescription(guildInfo.translate("commands.error.notfound.description")).setColor(Color.RED).build());
            return;
        }

        // Nota: La elimincación del mensaje debe de atrasarse unos segundos ya que el cliente de discord
        // se buguea si se elimina el mensaje instantáneamente. Si el mensaje se buguea el mensaje se
        // vuelve irremovible y no se puede editar (un mensaje fake, debido a que ya no existe).
        e.getMessage().delete().queueAfter(3, TimeUnit.SECONDS); // BORRAMOS EL MENSAJE DEL JUGADOR.

         CommandInfo commandInfo = search.getClass().getAnnotation(CommandInfo.class);

        if(!commandInfo.fromPrivateChat() && e.isFromType(ChannelType.PRIVATE)) {
            chat.sendBeautifulMessage(new EmbedBuilder().setTitle(guildInfo.translate("commands.error.justguild.title")).setDescription(guildInfo.translate("commands.error.justguild.description")).build());
        } else {
            //Nota: Eliminamos el args[0], recorremos el resto de argumentos a la izquierda ya que la variable
            //"cmd" contiene el label del comando ejecutado.
            String[] newArgs = new String[args.length - 1];
            System.arraycopy(args, 1, newArgs, 0, args.length - 1);

            try {
                search.execute(cmd, newArgs, e, chat);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    private String[] translateArguments(Message msg) {
        return msg.getContentRaw().split(" ");
    }

    public static boolean isCMD(GuildInfo c, Message msg) {
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
        /** Channel involved in this ChatSender object. Never null. */
        MessageChannel channel;
        /** Member involved in this ChatSender object. Can be null, see: {@link #getAsMember()}  */
        Member member;
        /** User involved in this ChatSender object. Can be null, see: {@link #getAsUser()} ()}  */
        User user;

        final int MIN_TIME_TO_DELETE = 15;

        public ChatSender(MessageReceivedEvent event) {
            this.channel = event.getChannel();
            this.member = event.getMember();
            this.user = event.getAuthor();
        }

        /**
         * Short usage of {@link #sendMessage(String, Consumer, Consumer)}
         */
        public void sendMessage(String msg) {
            sendMessage(msg, null, null);
        }

        public void sendMessage(String msg, Consumer<Message> success, Consumer<Throwable> error) {
            sendMessage(msg, MIN_TIME_TO_DELETE, success, error);
        }

        /**
         * Short usage of {@link #sendMessage(String, boolean, Color, Consumer, Consumer)}
         */
        public void sendMessage(String msg, boolean embed, Color color) {
            sendMessage(msg, embed, color, null, null);
        }

        public void sendMessage(String msg, boolean embed, Color color, Consumer<Message> success, Consumer<Throwable> error) {
            if(embed) {
                sendMessage(new EmbedBuilder().appendDescription(msg).setColor(color), success, error);
            } else {
                sendMessage(msg, success, error);
            }
        }

        /**
         * Short usage of {@link #sendMessage(String, boolean, Consumer, Consumer)}
         */
        public void sendMessage(String msg, boolean embed) {
            sendMessage(msg, embed, null, null);
        }

        public void sendMessage(String msg, boolean embed, Consumer<Message> success, Consumer<Throwable> error) {
            if(embed) {
                sendMessage(new EmbedBuilder().appendDescription(msg), success, error);
            } else {
                sendMessage(msg, success, error);
            }
        }

        /**
         * Short usage of {@link #sendMessage(String, String, boolean, Consumer, Consumer)}
         */
        public void sendMessage(String msg, String title, boolean embed) {
            sendMessage(msg, title, embed, null, null);
        }

        public void sendMessage(String msg, String title, boolean embed, Consumer<Message> success, Consumer<Throwable> error) {
            if(embed) {
                sendMessage(new EmbedBuilder().appendDescription(msg).setTitle(title), success, error);
            } else {
                sendMessage(msg, success, error);
            }
        }

        /**
         * A short usage of {@link #sendBeautifulMessage(MessageEmbed, int, Consumer, Consumer)}
         */
        public void sendBeautifulMessage(MessageEmbed embed) {
            sendBeautifulMessage(embed, null, null);
        }

        /**
         * A short usage of {@link #sendBeautifulMessage(MessageEmbed, int, Consumer, Consumer)}
         */
        public void sendBeautifulMessage(MessageEmbed embed, int time) {
            sendBeautifulMessage(embed, time,null, null);
        }

        /**
         * A short usage of {@link #sendBeautifulMessage(MessageEmbed, int, Consumer, Consumer)}
         */
        public void sendBeautifulMessage(MessageEmbed embed, Consumer<Message> success, Consumer<Throwable> error) {
            sendBeautifulMessage(embed, MIN_TIME_TO_DELETE, success, error);
        }

        /**
         *     Method to send a beautiful message to the channel of this ChatSender.<br>
         *     In comparison to another methods, this method always send a Embed Message with a
         *     lightest color and with the legend "Requested by {User}" at the embed footer.<br><br>
         *     Method written by Wirlie.
         * @param embed Embed to send to the channel of this Chat Sender. If color is not specified then
         *              the Embed will be fixed with a lightest color. If footer is not specified then the
         *              legend "Requested by {User}" will be added otherwise if the footer is specified then
         *              the legend will be append at the end of the footer message like to: "{Footer} | {Legend}".
         */
        public void sendBeautifulMessage(MessageEmbed embed, int time, Consumer<Message> success, Consumer<Throwable> error) {
            EmbedBuilder fixEmbed = new EmbedBuilder(embed);

            if(embed.getColor() == null) {
                fixEmbed.setColor(ColorUtil.getLightestColor());
            }

            if(embed.getFooter() == null) {
                fixEmbed.setFooter("Solicitado por " + member.getEffectiveName(), user.getEffectiveAvatarUrl());
            } else {
                fixEmbed.setFooter(embed.getFooter() + " | Solicitado por " + member.getEffectiveName(), user.getEffectiveAvatarUrl());
            }

            embed = fixEmbed.build();

            MessageUtils.sendMessage(embed, channel, time, success, error);
        }

        /**
         * Short usage of {@link #sendError(String, Consumer, Consumer)}
         */
        public void sendError(String msg) {
            sendError(msg, null, null);
        }

        public void sendError(String msg, Consumer<Message> success, Consumer<Throwable> error) {
            sendBeautifulMessage(new EmbedBuilder().appendDescription(msg).setTitle("ERROR!").setColor(Color.RED).build(), success, error);
        }

        /**
         * Short usage of {@link #sendInfo(String, Consumer, Consumer)}
         */
        public void sendInfo(String msg) {
            sendInfo(msg, null, null);
        }

        public void sendInfo(String msg, Consumer<Message> success, Consumer<Throwable> error) {
            sendBeautifulMessage(new EmbedBuilder().appendDescription(msg).setTitle("INFO").build(), success, error);
        }

        /**
         * Short usage of {@link #sendMessage(Message, Consumer, Consumer)}
         */
        public void sendMessage(Message msg) {
            sendMessage(msg, null, null);
        }

        public void sendMessage(Message msg, Consumer<Message> success, Consumer<Throwable> error) {
            sendMessage(msg, MIN_TIME_TO_DELETE, success, error);
        }

        /**
         * Short usage of {@link #sendMessage(EmbedBuilder, Consumer, Consumer)}
         */
        public void sendMessage(EmbedBuilder builder) {
            sendMessage(builder, null, null);
        }

        public void sendMessage(EmbedBuilder builder, Consumer<Message> success, Consumer<Throwable> error) {
            sendMessage(builder, MIN_TIME_TO_DELETE, success, error);
        }

        /**
         * Short usage of {@link #sendMessage(EmbedBuilder, int, Consumer, Consumer)}
         */
        public void sendMessage(EmbedBuilder embed, int time) {
            sendMessage(embed, time, null, null);
        }

        public void sendMessage(EmbedBuilder embed, int time, Consumer<Message> success, Consumer<Throwable> error) {
            MessageUtils.sendMessage(embed.build(), channel, time, success, error);
        }

        /**
         * Short usage of {@link #sendMessage(String, int, Consumer, Consumer)}
         */
        public void sendMessage(String msg, int time) {
            sendMessage(msg, time, null, null);
        }

        public void sendMessage(String msg, int time, Consumer<Message> success, Consumer<Throwable> error) {
            MessageUtils.sendMessage(msg, channel, time, success, error);
        }

        /**
         * Short usage of {@link #sendMessage(Message, int, Consumer, Consumer)}
         */
        public void sendMessage(Message msg, int time) {
            sendMessage(msg, time, null, null);
        }

        public void sendMessage(Message msg, int time, Consumer<Message> success, Consumer<Throwable> error) {
            MessageUtils.sendMessage(msg, channel, time, success, error);
        }

        /**
         * Short usage of {@link #sendNormalMessage(String, int, Consumer, Consumer)}
         */
        public void sendNormalMessage(String msg, int time) {
            sendNormalMessage(msg, time, null, null);
        }

        public void sendNormalMessage(String msg, int time, Consumer<Message> success, Consumer<Throwable> error) {
            MessageUtils.sendMessage(msg, channel, time, success, error);
        }

        /**
         *      Get the Member of this ChatSender object. Note that not all ChatSender objects has a Member, it
         *      depends on the type of Event (for example a message send by a GroupChannel may not have a Member
         *      object because Member is only for guilds).<br><br>
         *      If you are not sure you can use {@link #hasMember()} to verify if this ChatSender object has a
         *      Member involved.<br><br>
     *          Method written by Wirlie.
         * @return The Member involved in this ChatSender, the returned value can be null if the event is not
         * from a Guild TextChannel, be careful.
         */
        public Member getAsMember() {
            return member;
        }

        /**
         *      Get the User of this ChatSender object. Note that not all ChatSender objects has a User, it
         *      depends on the type of Event (for example a message send via a WebHook may not have a User
         *      object because WebHooks Messages are represented by a fake user).<br><br>
         *      If you are not sure you can use {@link #hasUser()} to verify if this ChatSender object has a
         *      User involved.<br><br>
     *          Method written by Wirlie.
         * @return The User involved in this ChatSender, the returned value can be null if the event is from a
         * WebHook Message, be careful.
         */
        public User getAsUser() {
            return user;
        }

        /**
         * As described in {@link #getAsMember()} this method only returns true if this ChatSender object
         * has a Member involved.<br><br>
         * Method written by Wirlie.
         * @return true if this ChatSender object has a Member involved.
         */
        public boolean hasMember() {
            return member != null;
        }

        /**
         * As described in {@link #getAsUser()} ()} this method only returns true if this ChatSender object
         * has a User involved.<br><br>
         * Method written by Wirlie.
         * @return true if this ChatSender object has a User involved.
         */
        public boolean hasUser() {
            return user != null;
        }
    }
}
