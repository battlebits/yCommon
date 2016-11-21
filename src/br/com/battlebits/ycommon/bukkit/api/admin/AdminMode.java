package br.com.battlebits.ycommon.bukkit.api.admin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.api.vanish.VanishAPI;
import br.com.battlebits.ycommon.bukkit.event.admin.PlayerAdminModeEvent;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.permissions.enums.Group;

public class AdminMode {
	private Set<UUID> admin;
	private static final AdminMode instance = new AdminMode();

	public AdminMode() {
		admin = new HashSet<UUID>();
	}

	public static AdminMode getInstance() {
		return instance;
	}

	public void setAdmin(Player p) {
		if (!admin.contains(p.getUniqueId()))
			admin.add(p.getUniqueId());
		PlayerAdminModeEvent event = new PlayerAdminModeEvent(p, br.com.battlebits.ycommon.bukkit.event.admin.PlayerAdminModeEvent.AdminMode.ADMIN);
		BukkitMain.getPlugin().getServer().getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		p.setGameMode(GameMode.CREATIVE);
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
		Group group = VanishAPI.getInstance().hidePlayer(p);
		Map<String, String> map = new HashMap<>();
		map.put("%invisible%", group.toString());
		player.sendMessage("command-admin-prefix", "command-admin-enabled");
		player.sendMessage("command-vanish-prefix", "command-vanish-invisible", map);
	}

	public void setPlayer(Player p) {
		PlayerAdminModeEvent event = new PlayerAdminModeEvent(p, br.com.battlebits.ycommon.bukkit.event.admin.PlayerAdminModeEvent.AdminMode.PLAYER);
		BukkitMain.getPlugin().getServer().getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
		if (admin.contains(p.getUniqueId())) {
			player.sendMessage("command-admin-prefix", "command-admin-disabled");
			admin.remove(p.getUniqueId());
		}
		player.sendMessage("command-vanish-prefix", "command-vanish-visible-all");
		p.setGameMode(GameMode.SURVIVAL);
		VanishAPI.getInstance().showPlayer(p);
	}

	public boolean isAdmin(Player p) {
		return p != null && admin.contains(p.getUniqueId());
	}

	public int playersInAdmin() {
		return admin.size();
	}

	public void removeAdmin(Player p) {
		admin.remove(p.getUniqueId());
	}
}
