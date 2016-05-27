package br.com.battlebits.ycommon.bukkit.accounts;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.BukkitCommon;
import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.networking.BukkitHandler;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAccountRequest;

public class BukkitAccount extends BukkitCommon {

	public BukkitAccount(BukkitMain main) {
		super(main);
	}

	@Override
	public void onEnable() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			try {
				loadPlayer(p.getUniqueId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		registerListener(new AccountListener());
	}

	@Override
	public void onDisable() {
	}

	public BattlePlayer loadPlayer(UUID uuid) throws InterruptedException {
		BukkitMain.getPlugin().getClient().sendPacket(new CPacketAccountRequest(uuid));
		synchronized (BukkitHandler.LOCK) {
			BukkitHandler.LOCK.wait(3250);
		}
		return BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
	}

}
