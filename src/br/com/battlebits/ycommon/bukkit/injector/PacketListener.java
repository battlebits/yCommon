package br.com.battlebits.ycommon.bukkit.injector;

import org.bukkit.entity.Player;

import io.netty.channel.Channel;
import net.minecraft.server.v1_7_R4.Packet;

public interface PacketListener {

	public abstract void onPacketReceive(PacketObject pacote);

	public abstract void onPacketSend(PacketObject pacote);

	public class PacketObject {
		private boolean canceled;
		private Player player;
		private Channel channel;
		private Packet packet;

		public PacketObject(Player player, Channel channel, Packet packet) {
			this.player = player;
			this.channel = channel;
			this.packet = packet;
			this.canceled = false;
		}

		public Channel getChannel() {
			return channel;
		}

		public Packet getPacket() {
			return packet;
		}

		public Player getPlayer() {
			return player;
		}

		public boolean isCancelled() {
			return canceled;
		}
		
		public void setPacket(Packet packet) {
			this.packet = packet;
		}

		public void setCancelled(boolean canceled) {
			this.canceled = canceled;
		}
	}
}
