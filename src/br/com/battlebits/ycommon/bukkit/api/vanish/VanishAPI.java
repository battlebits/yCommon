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

	private static VanishAPI instance;

	{
		instance = new VanishAPI();
	}

	public VanishAPI() {
		vanishedToGroup = new HashMap<>();
	}

	public void hidePlayerToGroup(Player player, Group group) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(online.getUniqueId());
			if (bP.hasGroupPermission(group))
				if (online.canSee(player))
					continue;
			online.showPlayer(player);
		}
	}

	public void showPlayerToGroup(Player player, Group group) {

	}

	public void showPlayerToAll(Player player) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (online.canSee(player))
				continue;
			online.showPlayer(player);
		}
	}
}
