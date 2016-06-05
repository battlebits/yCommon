package br.com.battlebits.ycommon.bukkit.bungee;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.bungee.managers.BanManager;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.banmanager.constructors.Mute;
import br.com.battlebits.ycommon.common.enums.ServerType;
import br.com.battlebits.ycommon.common.payment.enums.RankType;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.tag.Tag;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.utils.DateUtils;

public class MessageListener implements PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals(BattlebitsAPI.getBungeeChannel()))
			return;
		BattlebitsAPI.debug("MENSAGEM > " + System.currentTimeMillis());
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		switch (subchannel) {
		case "Ban": {
			for (Player online : Bukkit.getOnlinePlayers()) {
				BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(online.getUniqueId());
				online.sendMessage(Translate.getTranslation(bP.getLanguage(), "command-ban-bukkit-banned").replace("%player%", player.getName()));
			}
			break;
		}
		case "Mute": {
			Mute mute = BattlebitsAPI.getGson().fromJson(in.readUTF(), Mute.class);
			BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
			bP.getBanHistory().getMuteHistory().add(mute);
			player.sendMessage(BanManager.getMuteMessage(mute, bP.getLanguage()));
			break;
		}
		case "Groupset": {
			Group group = Group.valueOf(in.readUTF());
			ServerType serverType = ServerType.valueOf(in.readUTF());
			BukkitPlayer bP = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
			if (group == Group.NORMAL) {
				bP.getGroups().remove(serverType.getStaffType());
			} else {
				bP.getGroups().put(serverType.getStaffType(), group);
			}
			bP.loadTags();
			bP.setTag(Tag.valueOf(group.toString()));
			break;
		}
		case "Givevip": {
			BukkitPlayer bP = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
			RankType rank = RankType.valueOf(in.readUTF());
			long expiresCheck = in.readLong();
			long newAdd = System.currentTimeMillis();
			if (bP.getRanks().containsKey(rank)) {
				newAdd = bP.getRanks().get(rank);
			}
			newAdd = newAdd + expiresCheck;
			bP.getRanks().put(rank, newAdd);
			String givevip = Translate.getTranslation(bP.getLanguage(), "command-givevip-player-added");
			givevip = givevip.replace("%rank%", Tag.valueOf(rank.name()).getPrefix(bP.getLanguage()));
			givevip = givevip.replace("%duration%", DateUtils.formatDifference(bP.getLanguage(), expiresCheck / 1000));
			player.sendMessage("");
			player.sendMessage(givevip);
			player.sendMessage("");
			bP.loadTags();
			bP.setTag(Tag.valueOf(bP.getServerGroup().toString()));
			break;
		}
		case "Removevip": {
			BukkitPlayer bP = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
			RankType rank = RankType.valueOf(in.readUTF());
			bP.getRanks().remove(rank);
			String givevip = Translate.getTranslation(bP.getLanguage(), "command-removevip-player-removed");
			givevip = givevip.replace("%rank%", Tag.valueOf(rank.name()).getPrefix(bP.getLanguage()));
			player.sendMessage("");
			player.sendMessage(givevip);
			player.sendMessage("");
			bP.loadTags();
			bP.setTag(Tag.valueOf(bP.getServerGroup().toString()));
			break;
		}
		case "Unmute": {
			UUID uuid = UUID.fromString(in.readUTF());
			String userName = in.readUTF();
			BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
			bP.getBanHistory().getActualMute().unmute(uuid, userName);
			String msg = Translate.getTranslation(bP.getLanguage(), "command-unmute-prefix") + " " + Translate.getTranslation(bP.getLanguage(), "command-unmute-player");
			msg = msg.replace("%unmutedBy%", userName);
			player.sendMessage(msg);
			break;
		}
		case "UnmuteConsole": {
			BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
			bP.getBanHistory().getActualMute().unmute();
			String msg = Translate.getTranslation(bP.getLanguage(), "command-unmute-prefix") + " " + Translate.getTranslation(bP.getLanguage(), "command-unmute-player");
			msg = msg.replace("%unmutedBy%", "CONSOLE");
			player.sendMessage(msg);
			break;
		}
		default:
			break;
		}
	}

}
