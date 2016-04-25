package br.com.battlebits.ycommon.bukkit.bungee;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import br.com.battlebits.ycommon.bungee.managers.BanManager;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.banmanager.constructors.Mute;
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
		case "Mute":
			Mute mute = BattlebitsAPI.getGson().fromJson(in.readUTF(), Mute.class);
			BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
			bP.getBanHistory().getMuteHistory().add(mute);
			player.sendMessage(BanManager.getMuteMessage(mute, bP.getLanguage()));
			break;
		default:
			break;
		}
	}

}
