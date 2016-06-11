package br.com.battlebits.ycommon.bukkit.api.bossbar;

import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.event.update.UpdateEvent;
import br.com.battlebits.ycommon.bukkit.event.update.UpdateEvent.UpdateType;

public class BarAPI implements Listener {
	public static HashMap<UUID, FakeDragon> players = new HashMap<UUID, FakeDragon>();
	private static HashMap<UUID, Integer> timers = new HashMap<UUID, Integer>();

	public static void clear() {
		for (Player player : BukkitMain.getPlugin().getServer().getOnlinePlayers()) {
			quit(player);
		}

		players.clear();

		for (int timerID : timers.values()) {
			Bukkit.getScheduler().cancelTask(timerID);
		}

		timers.clear();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void PlayerLoggout(PlayerQuitEvent event) {
		quit(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerKick(PlayerKickEvent event) {
		quit(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleport(final PlayerTeleportEvent event) {
		handleTeleport(event.getPlayer(), event.getTo().clone());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleport(final PlayerRespawnEvent event) {
		handleTeleport(event.getPlayer(), event.getRespawnLocation().clone());
	}

	private void handleTeleport(final Player player, final Location loc) {

		if (!hasBar(player))
			return;

		Bukkit.getScheduler().runTaskLater(BukkitMain.getPlugin(), new Runnable() {

			@Override
			public void run() {
				if (!hasBar(player))
					return;

				FakeDragon oldDragon = getDragon(player, "");

				float health = oldDragon.health;
				String message = oldDragon.name;

				Util.sendPacket(player, getDragon(player, "").getDestroyPacket());

				players.remove(player.getUniqueId());

				FakeDragon dragon = addDragon(player, loc, message);
				dragon.health = health;

				sendDragon(dragon, player);
			}

		}, 2L);
	}

	public static void quit(Player player) {
		removeBar(player);
	}

	public static void setMessage(String message) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			setMessage(player, message);
		}
	}

	public static void setMessage(Player player, String message) {
		FakeDragon dragon = getDragon(player, message);

		dragon.name = cleanMessage(message);
		dragon.health = dragon.getMaxHealth();

		cancelTimer(player);

		sendDragon(dragon, player);
	}

	public static void setMessage(String message, int seconds) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			setMessage(player, message, seconds);
		}
	}

	private int ticks = 3;

	@EventHandler
	public void onTick(UpdateEvent event) {
		if (event.getType() != UpdateType.TICK)
			return;
		if (players.isEmpty())
			return;
		--ticks;
		if (ticks <= 0) {
			ticks = 3;
			for (UUID uuid : players.keySet()) {
				Player p = Bukkit.getPlayer(uuid);
				if (p == null) {
					continue;
				}
				Util.sendPacket(p, players.get(uuid).getTeleportPacket(BarAPI.getDragonLocation(p)));
			}
		}
	}

	public static void setMessage(final Player player, String message, int seconds) {
		Validate.isTrue(seconds > 0, "Seconds must be above 1 but was: ", seconds);

		FakeDragon dragon = getDragon(player, message);

		dragon.name = cleanMessage(message);
		dragon.health = dragon.getMaxHealth();

		cancelTimer(player);

		timers.put(player.getUniqueId(), Bukkit.getScheduler().runTaskLater(BukkitMain.getPlugin(), new Runnable() {
			@Override
			public void run() {
				removeBar(player);
				cancelTimer(player);
			}
		}, 20L * seconds).getTaskId());
		sendDragon(dragon, player);
	}

	public static boolean hasBar(Player player) {
		return players.get(player.getUniqueId()) != null;
	}

	public static void removeBar(Player player) {
		if (!hasBar(player))
			return;

		Util.sendPacket(player, getDragon(player, "").getDestroyPacket());

		players.remove(player.getUniqueId());

		cancelTimer(player);
	}

	public static String getMessage(Player player) {
		if (!hasBar(player))
			return "";

		return getDragon(player, "").name;
	}

	private static String cleanMessage(String message) {
		if (message.length() > 64)
			message = message.substring(0, 63);

		return message;
	}

	private static void cancelTimer(Player player) {
		Integer timerID = timers.remove(player.getUniqueId());

		if (timerID != null) {
			Bukkit.getScheduler().cancelTask(timerID);
		}
	}

	private static void sendDragon(FakeDragon dragon, Player player) {
		Util.sendPacket(player, dragon.getMetaPacket(dragon.getWatcher()));
		Util.sendPacket(player, dragon.getTeleportPacket(getDragonLocation(player)));
	}

	private static FakeDragon getDragon(Player player, String message) {
		if (hasBar(player)) {
			return players.get(player.getUniqueId());
		} else
			return addDragon(player, cleanMessage(message));
	}

	private static FakeDragon addDragon(Player player, String message) {
		FakeDragon dragon = Util.newDragon(message, getDragonLocation(player));

		Util.sendPacket(player, dragon.getSpawnPacket());

		players.put(player.getUniqueId(), dragon);

		return dragon;
	}

	private static FakeDragon addDragon(Player player, Location loc, String message) {
		FakeDragon dragon = Util.newDragon(message, getDragonLocation(player));

		Util.sendPacket(player, dragon.getSpawnPacket());

		players.put(player.getUniqueId(), dragon);

		return dragon;
	}

	public static Location getDragonLocation(Player p) {
		Location loc = p.getLocation();

		float pitch = loc.getPitch();

		if (pitch >= 55) {
			loc.add(0, -32, 0);
		} else if (pitch <= -55) {
			loc.add(0, 32, 0);
		} else {
			loc = loc.getBlock().getRelative(getDirection(loc), 16 * 2).getLocation();
		}

		return loc;
	}

	private static BlockFace getDirection(Location loc) {
		float dir = Math.round(loc.getYaw() / 90);
		if (dir == -4 || dir == 0 || dir == 4)
			return BlockFace.SOUTH;
		if (dir == -1 || dir == 3)
			return BlockFace.EAST;
		if (dir == -2 || dir == 2)
			return BlockFace.NORTH;
		if (dir == -3 || dir == 1)
			return BlockFace.WEST;
		return null;
	}
}