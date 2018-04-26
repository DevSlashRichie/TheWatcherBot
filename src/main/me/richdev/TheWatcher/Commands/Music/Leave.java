package me.richdev.TheWatcher.Commands.Music;

import me.richdev.TheWatcher.Commands.Command;
import me.richdev.TheWatcher.Commands.CommandHandler;
import me.richdev.TheWatcher.Commands.CommandInfo;
import me.richdev.TheWatcher.GuildSystem.GuildInfo;
import me.richdev.TheWatcher.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@CommandInfo(aliases = { "leave" }, permissionGroup = "", fromPrivateChat = true, descriptionID = "test")
public class Leave extends Command {

	@Override
	public void execute(String cmd, String[] args, MessageReceivedEvent e, CommandHandler.ChatSender sender, GuildInfo guildInfo) {
		Member member = sender.getAsMember();
		GuildVoiceState senderVS = member.getVoiceState();

		if(!senderVS.inVoiceChannel()) {
			sender.sendBeautifulMessage(
					new EmbedBuilder()
							.setTitle(Main.getInstance().getSelfUser().getName() + " no se puede unir.")
							.setDescription("No estás conectado a un canal de voz. Debes estar en un canal de voz antes de usar este comando.")
							.build()
			);
		} else {
			Guild guild = e.getGuild();
			Member selfMember = guild.getSelfMember();
			GuildVoiceState botVS = selfMember.getVoiceState();

			if(botVS.inVoiceChannel() && senderVS.inVoiceChannel()) {
				VoiceChannel botChannel = botVS.getChannel();
				VoiceChannel userChannel = senderVS.getChannel();

				if(botChannel.getId().equals(userChannel.getId())) {
					sender.sendBeautifulMessage(
							new EmbedBuilder()
									.setTitle(Main.getInstance().getSelfUser().getName() + " ya está unido.")
									.setDescription("El bot ya se encuentra en el canal de voz en el que estás conectado.")
									.build()
					);
					return;
				}
			}

			Main.getInstance().getPlayerManager().connectToVoiceChannel(senderVS.getChannel());
		}
	}

}
