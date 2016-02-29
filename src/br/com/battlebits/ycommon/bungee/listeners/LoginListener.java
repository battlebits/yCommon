package br.com.battlebits.ycommon.bungee.listeners;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.utils.GeoIpUtils;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.banmanager.constructors.Ban;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.utils.DateUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginListener implements Listener {

	@EventHandler(priority = -128)
	public void onLogin(final LoginEvent event) {
		BattlebitsAPI.debug("ACCOUNT > STARTING");
		event.registerIntent(BungeeMain.getPlugin());
		final String userName = event.getConnection().getName();
		final InetSocketAddress ipAdress = event.getConnection().getVirtualHost();
		final UUID uuid = event.getConnection().getUniqueId();
		ProxyServer.getInstance().getScheduler().runAsync(BungeeMain.getPlugin(), new Runnable() {
			@Override
			public void run() {
				String countryCode = "UNDEFINED";
				/*try {
					countryCode = GeoIpUtils.getIpStatus(ipAdress.getHostString()).getCountryCode();
				} catch (IOException e1) {
					e1.printStackTrace();
				}*/
				BattlebitsAPI.debug("CONNECTION > STARTING");

				Connection connection = BungeeMain.getPlugin().getConnection().getConnection();
				try {
					BattlebitsAPI.debug("CONNECTION > STARTING");
					PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `account` WHERE `uuid`='" + uuid.toString().replace("-", "") + "';");
					ResultSet result = stmt.executeQuery();
					BattlebitsAPI.debug("ACCOUNT > EXCUTED");
					if (result.next()) {
						BattlePlayer player = BungeeMain.getGson().fromJson(result.getString("json"), BattlePlayer.class);
						player.setJoinData(ipAdress, countryCode);
						BattlebitsAPI.getAccountCommon().loadBattlePlayer(uuid, player);
						BattlebitsAPI.debug("ACCOUNT > LOADED");
					} else {
						BattlebitsAPI.getAccountCommon().loadBattlePlayer(uuid, new BattlePlayer(userName, uuid, ipAdress, countryCode));
						BattlebitsAPI.debug("ACCOUNT > NEW");
					}
					result.close();
					stmt.close();
					result = null;
					stmt = null;
					BattlebitsAPI.debug("ACCOUNT > CLOSE");
				} catch (SQLException e) {
					event.setCancelled(true);
					String accountLoadFailed = Translate.getTranslation(BattlebitsAPI.getDefaultLanguage(), "account-load-failed");
					event.setCancelReason(accountLoadFailed);
					e.printStackTrace();
					return;
				}
				BattlebitsAPI.debug("BANNING > STARTING");
				BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(event.getConnection().getUniqueId());
				Ban ban = player.getBanHistory().getActualBan();
				if (ban != null) {
					event.setCancelled(true);

					String reason = "";
					if (ban.isPermanent()) {
						reason = Translate.getTranslation(player.getLanguage(), "banned-permanent");
						// VOCE FOI BANIDO(A) PERMANENTEMENTE
						// POR %banned-By% NO DIA %day%
						// MOTIVO: %reason%
						//
						// BANIDO(A) INCORRETAMENTE? PEÇA APPEAL: %forum%
						// COMPRE UNBAN EM %store% PARA ACESSAR NOVAMENTE
					} else {
						reason = Translate.getTranslation(player.getLanguage(), "banned-temp");
						// VOCE FOI BANIDO(A) TEMPORARIAMENTE
						// POR %banned-By% NO DIA %day%
						// MOTIVO: %reason%
						//
						// DURAÇAO DE BANIMENTO: %duration%
					}
					reason = reason.replace("%banned-By%", ban.getBannedBy());
					reason = reason.replace("%reason%", ban.getReason());
					reason = reason.replace("%duration%", DateUtils.formatDifference(player.getLanguage(), (ban.getDuration() - System.currentTimeMillis()) / 1000));
					reason = reason.replace("%forum%", BattlebitsAPI.FORUM_WEBSITE);
					reason = reason.replace("%store%", BattlebitsAPI.STORE);
					event.setCancelReason(reason);
					reason = null;
				}
				BattlebitsAPI.debug("BANNING > FINISHED");
				event.completeIntent(BungeeMain.getPlugin());
				player = null;
				ban = null;
				// CRIAR QUERIES E QUALQUER COISA NO MYSQL PARA GERAR UM
				// BATTLEPLAYER
				/*
				 * int fichas = 0; int xp = 0; int money = 0; Liga liga =
				 * Liga.FIRST; String lastAddressIp = ""; long onlineTime = 0;
				 * long firstJoin = System.currentTimeMillis(); long lastJoin =
				 * System.currentTimeMillis(); boolean ignoreAll = false;
				 * Map<ServerType, Group> groups = new HashMap<>(); Map<Group,
				 * Expire> ranks = new HashMap<>(); Map<UUID, Friend> friends =
				 * new HashMap<>(); Map<UUID, Request> friendRequests = new
				 * HashMap<>(); Map<UUID, Blocked> blockedPlayers = new
				 * HashMap<>(); Clan clan = null; Party party = null; String
				 * skype = ""; boolean skypeFriendsOnly = true; String twitter =
				 * ""; String youtubeChannel = ""; String steam = ""; String
				 * countryCode = "BR"; Language language = Language.PORTUGUES;
				 * HGStatus hgStatus = null; BattlecraftStatus pvpStatus = null;
				 * GameStatus gameStatus = null; BanHistory banHistory = null;
				 * List<String> nameHistory = null; BattlePlayer player = new
				 * BattlePlayer(userName, uuid, userName, fichas, money, xp,
				 * liga, event.getConnection().getAddress(), lastAddressIp,
				 * event.getConnection().getVirtualHost(), onlineTime, lastJoin,
				 * firstJoin, ignoreAll, groups, ranks, friends, friendRequests,
				 * blockedPlayers, clan, party, skype, skypeFriendsOnly,
				 * twitter, youtubeChannel, steam, countryCode, language,
				 * hgStatus, pvpStatus, gameStatus, banHistory, nameHistory);
				 * BattlebitsAPI.getAccountCommon().loadBattlePlayer(uuid,
				 * player); event.completeIntent(BungeeMain.getPlugin()); liga =
				 * null; lastAddressIp = null; groups = null; ranks = null;
				 * friends = null; friendRequests = null; blockedPlayers = null;
				 * clan = null; party = null; skype = null; twitter = null;
				 * youtubeChannel = null; steam = null; countryCode = null;
				 * language = null; hgStatus = null; pvpStatus = null;
				 * gameStatus = null; banHistory = null; nameHistory = null;
				 * connection = null;
				 */
			}
		});
	}

}
