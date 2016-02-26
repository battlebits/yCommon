package br.com.battlebits.ycommon.bukkit.injector;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import br.com.battlebits.ycommon.bukkit.injector.PacketListener.PacketObject;
import net.minecraft.server.v1_7_R4.NetworkManager;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.util.io.netty.channel.Channel;

public class Injector {
	public static void createTinyProtocol(final Plugin plugin) {
		new TinyProtocol(plugin) {
			@Override
			public Object onPacketOutAsync(final Player reciever, Channel channel, Object packet) {
				if (!(packet instanceof Packet))
					return super.onPacketOutAsync(reciever, channel, packet);
				if (channel == null) {
					return super.onPacketOutAsync(reciever, channel, packet);
				}
				PacketObject object = new PacketObject(reciever, channel, (Packet) packet);
				@SuppressWarnings("unchecked")
				Iterator<PacketListener> iterator = ((List<PacketListener>) PacketListenerAPI.getListeners().clone()).iterator();
				while (iterator.hasNext()) {
					iterator.next().onPacketSend(object);
				}
				if (object.isCancelled())
					return null;
				return object.getPacket();
			}

			@SuppressWarnings("unchecked")
			@Override
			public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
				if (!(packet instanceof Packet))
					return super.onPacketInAsync(sender, channel, packet);
				if (channel == null) {
					return super.onPacketInAsync(sender, channel, packet);
				}
				PacketObject object = new PacketObject(sender, channel, (Packet) packet);
				Iterator<PacketListener> iterator = ((List<PacketListener>) PacketListenerAPI.getListeners().clone()).iterator();
				while (iterator.hasNext()) {
					iterator.next().onPacketReceive(object);
				}
				if (object.isCancelled())
					return null;
				return object.getPacket();
			}
		};
		PacketListenerAPI.addListener(new PacketListener() {

			@Override
			public void onPacketSend(PacketObject object) {
				Channel channel = object.getChannel();
				Packet packet = object.getPacket();
				if (channel.attr(NetworkManager.protocolVersion).get() != 47)
					return;
				try {
					if (packet instanceof PacketPlayOutPlayerInfo) {
						PacketPlayOutPlayerInfo packetCopy = (PacketPlayOutPlayerInfo) packet;
						final PacketPlayOutPlayerInfo packetplayoutplayerinfo = new PacketPlayOutPlayerInfo();
						Field action = packetplayoutplayerinfo.getClass().getDeclaredField("action");
						action.setAccessible(true);
						action.set(packetplayoutplayerinfo, getField(packetCopy, "action"));
						Field player = packetplayoutplayerinfo.getClass().getDeclaredField("player");
						player.setAccessible(true);
						player.set(packetplayoutplayerinfo, getField(packetCopy, "player"));
						Field gamemode = packetplayoutplayerinfo.getClass().getDeclaredField("gamemode");
						gamemode.setAccessible(true);
						gamemode.set(packetplayoutplayerinfo, getField(packetCopy, "gamemode"));
						Field ping = packetplayoutplayerinfo.getClass().getDeclaredField("ping");
						ping.setAccessible(true);
						ping.set(packetplayoutplayerinfo, getField(packetCopy, "ping"));
						Field username = packetCopy.getClass().getDeclaredField("username");
						username.setAccessible(true);
						username.set(packetplayoutplayerinfo, null);
						object.setPacket(packetplayoutplayerinfo);
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onPacketReceive(PacketObject object) {

			}
		});
	}

	private static Object getField(Object packet, String fieldName) {
		try {
			Field field = packet.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
