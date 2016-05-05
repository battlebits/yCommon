package br.com.battlebits.ycommon.bukkit.api.npc;

import java.lang.reflect.Field;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import de.inventivegames.holograms.Hologram;
import de.inventivegames.holograms.HologramAPI;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.NetworkManager;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import net.minecraft.server.v1_7_R4.PlayerInteractManager;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.io.netty.channel.AbstractChannel;
import net.minecraft.util.io.netty.channel.ChannelConfig;
import net.minecraft.util.io.netty.channel.ChannelMetadata;
import net.minecraft.util.io.netty.channel.ChannelOutboundBuffer;
import net.minecraft.util.io.netty.channel.DefaultChannelConfig;
import net.minecraft.util.io.netty.channel.EventLoop;
import net.minecraft.util.io.netty.util.AttributeKey;

public class CustomPlayerNPC {

	private Plugin pl;
	private Location location;
	private GameProfile profile;
	private EntityPlayer fakePlayer;
	private CraftPlayer fakeCraftPlayer;
	public PacketPlayOutPlayerInfo removePlayer;
	private static HashMap<Integer, CustomPlayerNPC> activePlayers = new HashMap<>();
	private Hologram hologram;

	@SuppressWarnings("deprecation")
	public CustomPlayerNPC(final String name, final UUID uuid, final Location loc, final String id, final Plugin plugin, final ItemStack itemInHand, final String... lines) {
		pl = plugin;
		location = loc;
		Bukkit.getScheduler().scheduleAsyncDelayedTask(pl, new Runnable() {
			public void run() {

				profile = new GameProfile(uuid, name);
				try {
					profile.getProperties().put("textures", CustomPlayerAPI.Textures.get(profile, null));
				} catch (ExecutionException e) {
					e.printStackTrace();
				}

				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
					public void run() {

						WorldServer nmsWorld = CustomPlayerAPI.getNmsWorld(loc.getWorld());

						fakePlayer = new EntityPlayer(CustomPlayerAPI.getNmsServer(), nmsWorld, profile, new PlayerInteractManager(nmsWorld));
						fakePlayer.playerConnection = new PlayerConnection(CustomPlayerAPI.getNmsServer(), new NPCNetworkManager(), fakePlayer);

						fakeCraftPlayer = new CraftPlayer(CustomPlayerAPI.getNmsServer().server, fakePlayer);
						fakeCraftPlayer.setMetadata("NPC", new FixedMetadataValue(pl, ""));
						fakeCraftPlayer.setPlayerListName(id);
						fakeCraftPlayer.setItemInHand(itemInHand);

						fakePlayer.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
						fakePlayer.abilities.isInvulnerable = true;
						activePlayers.put(fakePlayer.getId(), CustomPlayerNPC.this);
						removePlayer = PacketPlayOutPlayerInfo.removePlayer(fakePlayer);
						nmsWorld.addEntity(fakePlayer);
						nmsWorld.players.remove(fakePlayer);

						CustomPlayerAPI.setHeadYaw(fakePlayer, loc.getYaw());

						List<String> texts = Arrays.asList(lines);

						hologram = HologramAPI.createWorldHologram(loc.add(0, 2.35, 0), texts.get(0));
						hologram.spawn();
						for (String str : texts) {
							if (str != texts.get(0)) {
								hologram.addLineAbove(str.replace("&", "§"));
							}
						}

					}
				});

			}
		});

	}

	public CraftPlayer getFakeCraftPlayer() {
		return fakeCraftPlayer;
	}

	public GameProfile getProfile() {
		return profile;
	}

	public Location getLocation() {
		return location;
	}

	public UUID getUuid() {
		return profile.getId();
	}

	public String getName() {
		return profile.getName();
	}

	public Hologram getHologram() {
		return hologram;
	}

	public static CustomPlayerNPC isPlayerNPC(int entityId) {
		return activePlayers.get(entityId);
	}

	class NPCNetworkManager extends NetworkManager {

		@SuppressWarnings("unchecked")
		public NPCNetworkManager() {
			super(false);

			try {
				Field channel = NetworkManager.class.getDeclaredField("m");
				Field address = NetworkManager.class.getDeclaredField("n");

				channel.setAccessible(true);
				address.setAccessible(true);

				NPCChannel parent = new NPCChannel(null);
				try {
					Field protocolVersion = NetworkManager.class.getDeclaredField("protocolVersion");
					parent.attr(((AttributeKey<Integer>) protocolVersion.get(null))).set(5);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
				channel.set(this, parent);
				address.set(this, new SocketAddress() {
					private static final long serialVersionUID = 6994835504305404545L;
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	class NPCChannel extends AbstractChannel {
		private final ChannelConfig config = new DefaultChannelConfig(this);

		protected NPCChannel(net.minecraft.util.io.netty.channel.Channel parent) {
			super(parent);
		}

		@Override
		public ChannelConfig config() {
			config.setAutoRead(true);
			return config;
		}

		@Override
		public boolean isActive() {
			return false;
		}

		@Override
		public boolean isOpen() {
			return false;
		}

		@Override
		public ChannelMetadata metadata() {
			return null;
		}

		@Override
		protected void doBeginRead() throws Exception {
		}

		@Override
		protected void doBind(SocketAddress arg0) throws Exception {
		}

		@Override
		protected void doClose() throws Exception {
		}

		@Override
		protected void doDisconnect() throws Exception {
		}

		@Override
		protected void doWrite(ChannelOutboundBuffer arg0) throws Exception {
		}

		@Override
		protected boolean isCompatible(EventLoop arg0) {
			return true;
		}

		@Override
		protected SocketAddress localAddress0() {
			return null;
		}

		@Override
		protected AbstractUnsafe newUnsafe() {
			return null;
		}

		@Override
		protected SocketAddress remoteAddress0() {
			return null;
		}
	}

}
