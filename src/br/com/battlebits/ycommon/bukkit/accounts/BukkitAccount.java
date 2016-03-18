package br.com.battlebits.ycommon.bukkit.accounts;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.BukkitCommon;
import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.networking.PacketSender;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAccountRequest;

public class BukkitAccount extends BukkitCommon {

	public BukkitAccount(BukkitMain main) {
		super(main);
	}

	@SuppressWarnings("deprecation")
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

	public void loadPlayer(UUID uuid) throws Exception {
		PacketSender.sendPacketReturn(new CPacketAccountRequest(uuid), BukkitMain.getPlugin().getPacketHandler());
	}

}
