package br.com.battlebits.ycommon.bungee.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.enums.Liga;
import br.com.battlebits.ycommon.common.enums.ServerType;
import br.com.battlebits.ycommon.common.friends.Friend;
import br.com.battlebits.ycommon.common.friends.block.Blocked;
import br.com.battlebits.ycommon.common.friends.request.Request;
import br.com.battlebits.ycommon.common.payment.constructors.Expire;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginListener implements Listener {

	@EventHandler
	public void onLogin(final LoginEvent event) {
		event.registerIntent(BungeeMain.getPlugin());
		final String userName = event.getConnection().getName();
		final UUID uuid = event.getConnection().getUniqueId();
		ProxyServer.getInstance().getScheduler().runAsync(BungeeMain.getPlugin(), new Runnable() {
			@Override
			public void run() {
				// CRIAR QUERIES E QUALQUER COISA NO MYSQL PARA GERAR UM
				// BATTLEPLAYER
				Map<ServerType, Group> groups = new HashMap<>();
				Map<Group, Expire> ranks = new HashMap<>();
				Map<UUID, Friend> friends = new HashMap<>();
				Map<UUID, Request> friendRequests = new HashMap<>();
				Map<UUID, Blocked> blockedPlayers = new HashMap<>();
				BattlePlayer player = new BattlePlayer(userName, uuid, userName, null, 0, 0, 0, Liga.FIRST, event.getConnection().getAddress(), 0, System.currentTimeMillis(), System.currentTimeMillis(), "localhost", false, groups, ranks, friends, friendRequests, blockedPlayers, null, null, "", false, "", "", "BR", Language.PORTUGUES, null, null, null, null);
				BattlebitsAPI.getAccountCommon().loadBattlePlayer(uuid, player);
				event.completeIntent(BungeeMain.getPlugin());
			}
		});
	}

}
