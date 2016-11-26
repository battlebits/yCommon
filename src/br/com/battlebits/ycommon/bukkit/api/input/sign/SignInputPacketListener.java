package br.com.battlebits.ycommon.bukkit.api.input.sign;

import br.com.battlebits.ycommon.bukkit.injector.PacketListener;
import net.minecraft.server.v1_7_R4.PacketPlayInUpdateSign;

class SignInputPacketListener implements PacketListener {

	private SignInputManager searchManager;

	public SignInputPacketListener(SignInputManager manager) {
		this.searchManager = manager;
	}

	@Override
	public void onPacketReceive(PacketObject p) {
		if (p.getPacket() instanceof PacketPlayInUpdateSign) {
			p.setCancelled(searchManager.handle(p.getPlayer(), ((PacketPlayInUpdateSign) p.getPacket()).f()));
		}

	}

	@Override
	public void onPacketSend(PacketObject unsed) {
		// Nao vou usar '-'
	}

}
