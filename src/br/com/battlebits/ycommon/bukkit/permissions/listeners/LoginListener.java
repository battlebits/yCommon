package br.com.battlebits.ycommon.bukkit.permissions.listeners;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ycommon.bukkit.permissions.PermissionManager;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.enums.ServerType;
import br.com.battlebits.ycommon.common.payment.constructors.Expire;
import br.com.battlebits.ycommon.common.permissions.enums.Group;

public class LoginListener implements Listener {
	private final Map<UUID, PermissionAttachment> attachments;
	private PermissionManager manager;

	public LoginListener(PermissionManager manager) {
		attachments = new HashMap<>();
		this.manager = manager;
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				for (Player player : manager.getServer().getOnlinePlayers()) {
					updateAttachment(player, getServerGroup(player));
				}
			}
		}.runTaskLater(manager.getPlugin(), 10);
	}

	private Group getServerGroup(Player bukkitPlayer) {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(bukkitPlayer.getUniqueId());
		Group group = Group.NORMAL;
		if (!player.getGroups().isEmpty()) {
			if (player.getGroups().containsKey(manager.getServerType())) {
				group = player.getGroups().get(manager.getServerType());
			} else if (player.getGroups().containsKey(ServerType.NETWORK)) {
				group = player.getGroups().get(ServerType.NETWORK);
			} else {
				group = Group.ULTIMATE;
			}
		} else if (!player.getRanks().isEmpty()) {
			Collection<Expire> expires = player.getRanks().values();
			Expire expire = null;
			for (Expire expireRank : expires) {
				if (expireRank == null) {
					expire = expireRank;
				} else if (expireRank.getRankType().ordinal() > expire.getRankType().ordinal()) {
					expire = expireRank;
				}
			}
			if (expire != null)
				group = Group.valueOf(expire.getRankType().name());
		}
		return group;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(PlayerLoginEvent event) {
		final Player player = event.getPlayer();
		updateAttachment(player, getServerGroup(player));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMonitorLogin(PlayerLoginEvent event) {
		if (event.getResult() != Result.ALLOWED) {
			removeAttachment(event.getPlayer());
		}
	}

	protected void updateAttachment(Player player, Group group) {
		PermissionAttachment attach = (PermissionAttachment) attachments.get(player.getUniqueId());
		Permission playerPerm = getCreateWrapper(player, player.getUniqueId().toString());
		if (attach == null) {
			attach = player.addAttachment(manager.getPlugin());
			attachments.put(player.getUniqueId(), attach);
			attach.setPermission(playerPerm, true);
		}
		playerPerm.getChildren().clear();
		for (String perm : group.getGroup().getPermissions()) {
			if (!playerPerm.getChildren().containsKey(perm)) {
				playerPerm.getChildren().put(perm, true);
			}
		}
		player.recalculatePermissions();
	}

	private Permission getCreateWrapper(Player player, String name) {
		Permission perm = manager.getServer().getPluginManager().getPermission(name);
		if (perm == null) {
			perm = new Permission(name, "BATTLEBITS Permissao Interna", PermissionDefault.FALSE);
			manager.getServer().getPluginManager().addPermission(perm);
		}
		return perm;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		removeAttachment(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onKick(PlayerKickEvent event) {
		removeAttachment(event.getPlayer());
	}

	protected void removeAttachment(Player player) {
		PermissionAttachment attach = (PermissionAttachment) this.attachments.remove(player.getUniqueId());
		if (attach != null) {
			attach.remove();
		}
		manager.getServer().getPluginManager().removePermission(player.getUniqueId().toString());
	}

	public void onDisable() {
		for (PermissionAttachment attach : attachments.values()) {
			attach.remove();
		}
		attachments.clear();
	}
}
