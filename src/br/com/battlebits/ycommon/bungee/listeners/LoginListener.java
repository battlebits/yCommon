package br.com.battlebits.ycommon.bungee.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.account.battlecraft.BattlecraftStatus;
import br.com.battlebits.ycommon.common.account.game.GameStatus;
import br.com.battlebits.ycommon.common.account.hungergames.HGStatus;
import br.com.battlebits.ycommon.common.banmanager.constructors.Ban;
import br.com.battlebits.ycommon.common.banmanager.history.BanHistory;
import br.com.battlebits.ycommon.common.clans.Clan;
import br.com.battlebits.ycommon.common.enums.Liga;
import br.com.battlebits.ycommon.common.enums.ServerType;
import br.com.battlebits.ycommon.common.friends.Friend;
import br.com.battlebits.ycommon.common.friends.block.Blocked;
import br.com.battlebits.ycommon.common.friends.request.Request;
import br.com.battlebits.ycommon.common.party.Party;
import br.com.battlebits.ycommon.common.payment.constructors.Expire;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginListener implements Listener {

	@EventHandler(priority = -128)
	public void onLogin(final LoginEvent event) {
		event.registerIntent(BungeeMain.getPlugin());
		final String userName = event.getConnection().getName();
		final UUID uuid = event.getConnection().getUniqueId();
		ProxyServer.getInstance().getScheduler().runAsync(BungeeMain.getPlugin(), new Runnable() {
			@Override
			public void run() {
				// CRIAR QUERIES E QUALQUER COISA NO MYSQL PARA GERAR UM
				// BATTLEPLAYER
				int fichas = 0;
				int xp = 0;
				int money = 0;
				Liga liga = Liga.FIRST;
				String lastAddressIp = "";
				long onlineTime = 0;
				long firstJoin = System.currentTimeMillis();
				long lastJoin = System.currentTimeMillis();
				boolean ignoreAll = false;
				Map<ServerType, Group> groups = new HashMap<>();
				Map<Group, Expire> ranks = new HashMap<>();
				Map<UUID, Friend> friends = new HashMap<>();
				Map<UUID, Request> friendRequests = new HashMap<>();
				Map<UUID, Blocked> blockedPlayers = new HashMap<>();
				Clan clan = null;
				Party party = null;
				String skype = "";
				boolean skypeFriendsOnly = true;
				String twitter = "";
				String youtubeChannel = "";
				String steam = "";
				String countryCode = "BR";
				Language language = Language.PORTUGUES;
				HGStatus hgStatus = null;
				BattlecraftStatus pvpStatus = null;
				GameStatus gameStatus = null;
				BanHistory banHistory = null;
				List<String> nameHistory = null;
				BattlePlayer player = new BattlePlayer(userName, uuid, userName, fichas, money, xp, liga, event.getConnection().getAddress(), lastAddressIp, event.getConnection().getVirtualHost(), onlineTime, lastJoin, firstJoin, ignoreAll, groups, ranks, friends, friendRequests, blockedPlayers, clan, party, skype, skypeFriendsOnly, twitter, youtubeChannel, steam, countryCode, language, hgStatus, pvpStatus, gameStatus, banHistory, nameHistory);
				BattlebitsAPI.getAccountCommon().loadBattlePlayer(uuid, player);
				event.completeIntent(BungeeMain.getPlugin());
			}
		});
	}

	@EventHandler(priority = 0)
	public void onCheckBanned(LoginEvent event) {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(event.getConnection().getUniqueId());
		Ban ban = player.getBanHistory().getActualBan();
		if (ban != null) {
			event.setCancelled(true);
			// VOCE ESTÁ BANIDO
			//
			// VOCE FOI BANIDO POR %banned-By% NO DIA %day%
			// MOTIVO: %reason%
			//
			// BANIDO INCORRETAMENTE? PEÇA APPEAL: %forum%
			//
			// COMPRE UNBAN EM %store% PARA ACESSAR NOVAMENTE
			String reason = Translate.getTranslation(player.getLanguage(), "login-banned");
			reason = reason.replace("%banned-By%", ban.getBannedBy());
			reason = reason.replace("%reason%", ban.getReason());
			reason = reason.replace("%forum%", BattlebitsAPI.FORUM_WEBSITE);
			reason = reason.replace("%store%", BattlebitsAPI.STORE);
			event.setCancelReason(reason);
		}
	}

}
