package br.com.battlebits.ycommon.bukkit.bungee;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import br.com.battlebits.ycommon.bungee.managers.BanManager;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.banmanager.constructors.Mute;
import br.com.battlebits.ycommon.common.translate.Translate;
import net.minecraft.util.com.google.common.io.ByteArrayDataInput;
import net.minecraft.util.com.google.common.io.ByteStreams;

public class MessageListener implements PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals(BattlebitsAPI.getBungeeChannel()))
			return;
		BattlebitsAPI.debug("MENSAGEM > " + System.currentTimeMillis());
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		switch (subchannel) {
		case "Mute": {
			Mute mute = BattlebitsAPI.getGson().fromJson(in.readUTF(), Mute.class);
			BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
			bP.getBanHistory().getMuteHistory().add(mute);
			player.sendMessage(BanManager.getMuteMessage(mute, bP.getLanguage()));
			break;
		}
		case "Unmute": {
			UUID uuid = UUID.fromString(in.readUTF());
			String userName = in.readUTF();
			BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
			bP.getBanHistory().getActualMute().unmute(uuid, userName);
			String msg = Translate.getTranslation(bP.getLanguage(), "unmute-prefix") + " " + Translate.getTranslation(bP.getLanguage(), "unmute-player");
			msg = msg.replace("%unmutedBy%", userName);
			player.sendMessage(msg);
			break;
		}
		case "UnmuteConsole": {
			BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
			bP.getBanHistory().getActualMute().unmute();
			String msg = Translate.getTranslation(bP.getLanguage(), "unmute-prefix") + " " + Translate.getTranslation(bP.getLanguage(), "unmute-player");
			msg = msg.replace("%unmutedBy%", "CONSOLE");
			player.sendMessage(msg);
			break;
		}
		default:
			break;
		}
	}

}
