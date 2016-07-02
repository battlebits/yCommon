package br.com.battlebits.ycommon.bukkit.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.bukkit.api.chat.ChatAPI;
import br.com.battlebits.ycommon.bungee.managers.BanManager;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.banmanager.constructors.Mute;
import br.com.battlebits.ycommon.common.permissions.enums.Group;

public class ChatListener implements Listener {

	//REGEX: #\w\w+
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onChatEnabled(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
		if(player == null) {
			event.setCancelled(true);
			return;
		}
		switch (ChatAPI.getInstance().getChatState()) {
		case DISABLED:
			event.setCancelled(true);
			break;
		case STAFF:
			if (!player.isStaff()) {
				event.setCancelled(true);
				break;
			}
			break;
		case YOUTUBER:
			if (player.getServerGroup().ordinal() < Group.YOUTUBER.ordinal()) {
				event.setCancelled(true);
				break;
			}
			break;
		default:
			break;
		}
		if (event.isCancelled()) {
			player.sendMessage("command-chat-prefix", "command-chat-cant-talk");
		}
		p = null;
		player = null;
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onMute(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
		if(player == null) {
			event.setCancelled(true);
			return;
		}
		Mute mute = player.getBanHistory().getActualMute();
		if (mute == null)
			return;
		p.sendMessage(BanManager.getMuteMessage(mute, player.getLanguage()));
		event.setCancelled(true);
		mute = null;
		p = null;
		player = null;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(event.getPlayer().getUniqueId());
		if(player == null) {
			event.setCancelled(true);
			return;
		}
		for (Player r : event.getRecipients()) {
			try {
				BukkitPlayer receiver = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(r.getUniqueId());
				if (receiver == null) {
					new BukkitRunnable() {
						@Override
						public void run() {
							r.kickPlayer("BUG");
						}
					}.runTask(BukkitMain.getPlugin());
					continue;
				}
				if ((!receiver.getConfiguration().isIgnoreAll()) && (!receiver.getBlockedPlayers().containsKey(player.getUuid()) && (!player.getBlockedPlayers().containsKey(receiver.getUuid())))) {
					String tag = player.getTag().getPrefix(receiver.getLanguage());
					String format = tag + (ChatColor.stripColor(tag).trim().length() > 0 ? " " : "") + event.getPlayer().getName() + ChatColor.GRAY + " (" + player.getLiga().getSymbol() + ChatColor.GRAY + ") " + ChatColor.WHITE + ": ";
					if (player.getActualClan() != null) {
						format = "[" + player.getActualClan().getAbbreviation() + "] " + format;
					}
					r.sendMessage(format + event.getMessage());
					format = null;
				}
				receiver = null;
			} catch (Exception e) {
			}
		}
		BukkitMain.getPlugin().getLogger().info("<" + player.getUserName() + "> " + event.getMessage());
		player = null;
	}
}
