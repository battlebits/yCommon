package br.com.battlebits.ycommon.bukkit.accounts;

import br.com.battlebits.ycommon.bukkit.networking.PacketSender;
import br.com.battlebits.ycommon.common.account.game.GameStatus;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateGameStatus;

public class BukkitGameStatus extends GameStatus {

	private transient BukkitPlayer player;

	public BukkitGameStatus(BukkitPlayer player) {
		this.player = player;
		super.setMinigameStatus(player.getGameStatus().getMinigameStatus());
	}

	@Override
	public void updateMinigame(String key, String mini) {
		super.updateMinigame(key, mini);
		try {
			PacketSender.sendPacket(new CPacketUpdateGameStatus(player.getUuid(), key, getMinigameStatus().get(key)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
