package br.com.battlebits.ycommon.bungee.listeners;

import java.util.Iterator;
import java.util.UUID;

import br.com.battlebits.ycommon.bungee.event.UpdateEvent;
import br.com.battlebits.ycommon.bungee.event.UpdateEvent.UpdateType;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.clans.Clan;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.tag.Tag;
import br.com.battlebits.ycommon.common.translate.Translate;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {

	@EventHandler
	public void onUpdate(UpdateEvent event) {
		if (event.getType() != UpdateType.SECOND)
			return;
		Iterator<BattlePlayer> players = BattlebitsAPI.getAccountCommon().getPlayers().iterator();
		while (players.hasNext()) {
			BattlePlayer player = players.next();
			if (BungeeCord.getInstance().getPlayer(player.getUuid()) == null) {
				if (player.isCacheExpired()) {
					players.remove();
					BattlebitsAPI.debug("REMOVENDO BATTLEPLAYER " + player.getUserName() + " DO CACHE");
				}
			}
		}

		Iterator<Clan> clans = BattlebitsAPI.getClanCommon().getClans().iterator();
		while (clans.hasNext()) {
			Clan clan = clans.next();
			boolean offline = true;
			for (UUID uuid : clan.getParticipants()) {
				if (BungeeCord.getInstance().getPlayer(uuid) != null) {
					offline = false;
					break;
				}
			}
			if (offline) {
				if (clan.isCacheExpired()) {
					BattlebitsAPI.getClanCommon().saveClan(clan);
					clans.remove();
					BattlebitsAPI.debug("REMOVENDO CLAN " + clan.getClanName() + " DO CACHE");
				}
			}
		}
	}

	@EventHandler
	public void onChat(ChatEvent event) {
		if (event.isCommand())
			return;
		if (event.isCancelled())
			return;
		if (!(event.getSender() instanceof ProxiedPlayer))
			return;
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(((ProxiedPlayer) event.getSender()).getUniqueId());
		if (player.getConfiguration().isStaffChatEnabled()) {
			sendStaffMessage(player, event.getMessage());
			event.setCancelled(true);
		} else if (player.getConfiguration().isClanChatEnabled() && player.getClan() != null) {
			sendClanMessage(player, event.getMessage());
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = (byte) -128)
	public void onConnect(ServerConnectedEvent event) {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(event.getPlayer().getUniqueId());
		player.connect(event.getServer().getInfo().getName());
	}

	public static void sendStaffMessage(BattlePlayer bP, String message) {
		for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
			BattlePlayer onlineBp = BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
			if (!onlineBp.hasGroupPermission(Group.YOUTUBERPLUS))
				continue;
			String tag = Tag.valueOf(bP.getServerGroup().toString()).getPrefix(onlineBp.getLanguage());
			String format = tag + (ChatColor.stripColor(tag).trim().length() > 0 ? " " : "") + bP.getUserName() + ChatColor.WHITE + ": ";

			player.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW + "" + ChatColor.BOLD + "[STAFF] " + format + message));
		}
	}

	public static void sendClanMessage(BattlePlayer bP, String message) {
		for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
			BattlePlayer onlineBp = BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
			if (onlineBp == null)
				continue;
			Clan clan = bP.getClan();
			if (!clan.isParticipant(onlineBp))
				continue;

			String tag = "";
			if (clan.isOwner(bP)) {
				tag = ChatColor.WHITE + " - " + ChatColor.DARK_RED + "" + Translate.getTranslation(onlineBp.getLanguage(), "owner");
			} else if (clan.isAdministrator(bP)) {
				tag = ChatColor.WHITE + " - " + ChatColor.RED + "" + Translate.getTranslation(onlineBp.getLanguage(), "admin");
			}
			String format = ChatColor.DARK_RED + "[CLAN" + tag.toUpperCase() + ChatColor.DARK_RED + "] " + ChatColor.GRAY + bP.getUserName() + ChatColor.WHITE + ": ";

			player.sendMessage(TextComponent.fromLegacyText(format + message));
		}

	}
}
