package br.com.battlebits.ycommon.bukkit.bungee;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import net.minecraft.util.com.google.common.io.ByteArrayDataInput;
import net.minecraft.util.com.google.common.io.ByteStreams;
import net.minecraft.util.com.google.gson.Gson;

public class MessageListener implements PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		BattlebitsAPI.debug(channel);
		if (!channel.equals(BattlebitsAPI.getBungeeChannel()))
			return;
		BattlebitsAPI.debug("MENSAGEM > " + System.currentTimeMillis());
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("Account")) {
			String jsonAccont = in.readUTF();
			BattlePlayer battlePlayer = new Gson().fromJson(jsonAccont, BattlePlayer.class);
			BattlebitsAPI.getAccountCommon().loadBattlePlayer(player.getUniqueId(), battlePlayer);
			BattlebitsAPI.debug(jsonAccont);
		}
	}

}
