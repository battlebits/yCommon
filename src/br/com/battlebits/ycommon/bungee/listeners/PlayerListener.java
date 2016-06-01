package br.com.battlebits.ycommon.bungee.listeners;

import java.util.Iterator;

import br.com.battlebits.ycommon.bungee.event.UpdateEvent;
import br.com.battlebits.ycommon.bungee.event.UpdateEvent.UpdateType;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.tag.Tag;
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
		if (player.getConfiguration().isStaffChatEnabled())
			sendMessage(player, event.getMessage());
	}

	@EventHandler(priority = (byte) -128)
	public void onConnect(ServerConnectedEvent event) {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(event.getPlayer().getUniqueId());
		player.connect(event.getServer().getInfo().getName());
	}

	public static void sendMessage(BattlePlayer bP, String message) {
		for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
			BattlePlayer onlineBp = BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
			if (!onlineBp.hasGroupPermission(Group.YOUTUBERPLUS))
				continue;
			String tag = Tag.valueOf(bP.getServerGroup().toString()).getPrefix(onlineBp.getLanguage());
			String format = tag + (ChatColor.stripColor(tag).trim().length() > 0 ? " " : "") + bP.getUserName() + ChatColor.WHITE + ": ";

			player.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW + "" + ChatColor.BOLD + "[STAFF] " + format + message));
		}
	}
}
