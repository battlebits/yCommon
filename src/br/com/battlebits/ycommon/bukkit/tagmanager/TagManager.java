package br.com.battlebits.ycommon.bukkit.tagmanager;

import java.util.UUID;

import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.BukkitCommon;
import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;

public class TagManager extends BukkitCommon {
	public TagManager(BukkitMain main) {
		super(main);
	}

	@Override
	public void onEnable() {
		registerListener(new TagListener(this));
	}

	public void updatePlayerTag(Player p) {
		BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
		
	}

	public void removePlayerTag(Player p) {

	}

	public static boolean isNadhyneOuGustavo(UUID uuid) {
		if (uuid.toString().equals("2a759cc7-0b01-4b7c-8f4a-a081a74dfab7"))
			return true;
		if (uuid.toString().equals("e24695ad-6618-471e-826a-2438f043a293"))
			return true;
		return false;
	}

	public static Tag getPlayerDefaultTag(BukkitPlayer p) {
		if (TagManager.isNadhyneOuGustavo(p.getUuid())) {
			return Tag.ESTRELA;
		}
		return Tag.valueOf(p.getServerGroup().toString());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {
		for (Player player : getServer().getOnlinePlayers()) {
			removePlayerTag(player);
		}
	}
}
