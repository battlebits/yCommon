package br.com.battlebits.ycommon.bukkit.api.vanish;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.permissions.enums.Group;

public class VanishAPI {
	private HashMap<UUID, Group> vanishedToGroup;

	private final static VanishAPI instance = new VanishAPI();

	public VanishAPI() {
		vanishedToGroup = new HashMap<>();
	}

	public void setPlayerVanishToGroup(Player player, Group group) {
		switch (group) {
		case NORMAL:
			if (vanishedToGroup.containsKey(player.getUniqueId()))
				vanishedToGroup.remove(player.getUniqueId());
			break;
		default:
			vanishedToGroup.put(player.getUniqueId(), group);
			break;
		}
		for (Player online : Bukkit.getOnlinePlayers()) {
			BattlePlayer onlineP = BattlebitsAPI.getAccountCommon().getBattlePlayer(online.getUniqueId());
			if (!onlineP.hasGroupPermission(group)) {
				if (!online.canSee(player))
					continue;
				online.hidePlayer(player);
				continue;
			}
			if (online.canSee(player))
				continue;
			online.showPlayer(player);
		}
	}

	public void updateVanishToPlayer(Player player) {
		BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
		for (Player online : Bukkit.getOnlinePlayers()) {
			Group group = vanishedToGroup.get(online);
			if (group != null) {
				if (!bP.hasGroupPermission(group)) {
					if (player.canSee(online)) {
						player.hidePlayer(online);
					}
				}
			}
			if (player.canSee(online))
				continue;
			player.showPlayer(online);
		}
	}

	public Group hidePlayer(Player player) {
		BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
		Group group = Group.values()[bP.getServerGroup().ordinal() - 1];
		setPlayerVanishToGroup(player, group);
		return group;
	}

	public void showPlayer(Player player) {
		setPlayerVanishToGroup(player, Group.NORMAL);
	}

	public void removeVanish(Player p) {
		vanishedToGroup.remove(p.getUniqueId());
	}

	public static VanishAPI getInstance() {
		return instance;
	}
}
