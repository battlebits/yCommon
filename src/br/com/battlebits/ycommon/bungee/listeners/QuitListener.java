package br.com.battlebits.ycommon.bungee.listeners;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.banmanager.constructors.Ban;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class QuitListener implements Listener {

	@EventHandler
	public void onQuit(PlayerDisconnectEvent event) {
		final ProxiedPlayer p = event.getPlayer();
		ProxyServer.getInstance().getScheduler().runAsync(BungeeMain.getPlugin(), new Runnable() {
			@Override
			public void run() {
				BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
				player.setLeaveData();
				BattlebitsAPI.getAccountCommon().saveBattlePlayer(player);
				player = null;
			}
		});
	}

	@EventHandler
	public void onChangeServer(ServerDisconnectEvent event) {
		ServerInfo info = event.getTarget();
		if (!info.getName().equals("ss.battlebits.com.br"))
			return;
		BattlePlayer bp = BattlePlayer.getPlayer(event.getPlayer().getUniqueId());
		if (bp.isScreensharing()) {
			Ban ban = new Ban("CONSOLE", bp.getIpAddress().getHostString(), bp.getServerConnected(), "ScreenShare leave");
			BungeeMain.getPlugin().getBanManager().ban(bp, ban);
			return;
		}
		for (ProxiedPlayer player : info.getPlayers()) {
			if (player.getUniqueId() == event.getPlayer().getUniqueId())
				continue;
			if (BattlePlayer.getPlayer(player.getUniqueId()).hasGroupPermission(Group.MODPLUS)) {
				return;
			}
		}
		for (ProxiedPlayer proxied : info.getPlayers()) {
			BattlePlayer player = BattlePlayer.getPlayer(proxied.getUniqueId());
			player.setScreensharing(false);
			if (player.getLastServer().isEmpty()) {
				proxied.connect(BungeeMain.getPlugin().getServerManager().getLobbyBalancer().next().getServerInfo());
			} else {
				proxied.connect(BungeeMain.getPlugin().getProxy().getServerInfo(player.getLastServer()));
			}
			proxied.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(player.getLanguage(), "command-screenshare-prefix") + " " + Translate.getTranslation(player.getLanguage(), "command-screenshare-moderator-leave")));
		}
	}

}
