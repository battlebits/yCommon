package br.com.battlebits.ycommon.bukkit.api.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.api.vanish.VanishAPI;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.permissions.enums.Group;

public class AdminMode {
	private ArrayList<Player> admin;
	private static AdminMode instance;

	{
		instance = new AdminMode();
	}

	public AdminMode() {
		admin = new ArrayList<Player>();
	}

	public static AdminMode getInstance() {
		return instance;
	}

	public void setAdmin(Player p) {
		if (!admin.contains(p))
			admin.add(p);
		p.setGameMode(GameMode.CREATIVE);
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
		Group group = VanishAPI.getInstance().hidePlayer(p);
		Map<String, String> map = new HashMap<>();
		map.put("%invisible%", group.toString());
		player.sendMessage("command-admin-prefix", "command-admin-enable");
		player.sendMessage("command-vanish-prefix", "command-vanish-invisible", map);
	}

	public void setPlayer(Player p) {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
		if (admin.contains(p)) {
			player.sendMessage("command-admin-disabled");
		}
		player.sendMessage("command-vanish-prefix", "command-vanish-visible-all");
		admin.remove(p);
		p.setGameMode(GameMode.SURVIVAL);
		VanishAPI.getInstance().showPlayer(p);
	}

	public boolean isAdmin(Player p) {
		return admin.contains(p);
	}

	public void removeAdmin(Player p) {
		admin.remove(p);
	}
}
