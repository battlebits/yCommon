package br.com.battlebits.ycommon.bungee.listeners;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.managers.BanManager;
import br.com.battlebits.ycommon.bungee.utils.GeoIpUtils;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.banmanager.constructors.Ban;
import br.com.battlebits.ycommon.common.payment.constructors.Expire;
import br.com.battlebits.ycommon.common.payment.enums.RankType;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
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
		final InetSocketAddress ipAdress = event.getConnection().getAddress();
		final UUID uuid = event.getConnection().getUniqueId();
		ProxyServer.getInstance().getScheduler().runAsync(BungeeMain.getPlugin(), new Runnable() {
			@Override
			public void run() {
				String countryCode = "-";
				try {
					countryCode = GeoIpUtils.getIpStatus(ipAdress.getHostString()).getCountryCode();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				BattlebitsAPI.debug("CONNECTION > STARTING");
				try {
					Connection connection = BungeeMain.getPlugin().getConnection().getConnection();
					BattlebitsAPI.debug("CONNECTION > STARTING");
					PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `account` WHERE `uuid`='" + uuid.toString().replace("-", "") + "';");
					ResultSet result = stmt.executeQuery();
					BattlebitsAPI.debug("ACCOUNT > EXCUTED");
					if (result.next()) {
						BattlePlayer player = BattlebitsAPI.getGson().fromJson(result.getString("json"), BattlePlayer.class);
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
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
					event.setCancelled(true);
					String accountLoadFailed = Translate.getTranslation(BattlebitsAPI.getDefaultLanguage(), "account-load-failed");
					event.setCancelReason(accountLoadFailed);
					e.printStackTrace();
					return;
				}
				BattlebitsAPI.debug("BANNING > STARTING");
				BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(event.getConnection().getUniqueId());

				if (player.getBanHistory().getActualBan() == null) {
					Ban ipBan = BungeeMain.getPlugin().getBanManager().getIpBan(ipAdress);
					if (ipBan != null) {
						if (!ipBan.getBannedPlayer().equals(player.getUuid()))
							BungeeMain.getPlugin().getBanManager().ban(player, new Ban(player.getUuid(), "CONSOLE", ipAdress.getHostString(), Translate.getTranslation(player.getLanguage(), "alt-account"), "proxy"));
					}
				}
				Ban ban = player.getBanHistory().getActualBan();
				if (ban != null) {
					event.setCancelled(true);
					event.setCancelReason(BanManager.getBanKickMessage(ban, player.getLanguage()));
				}
				BattlebitsAPI.debug("BANNING > FINISHED");
				event.completeIntent(BungeeMain.getPlugin());
				if (event.isCancelled()) {
					BattlebitsAPI.getAccountCommon().saveBattlePlayer(player);
					BattlebitsAPI.getAccountCommon().unloadBattlePlayer(uuid);
				}
				player.getRanks().put(Group.ULTIMATE, new Expire(player.getUuid(), System.currentTimeMillis() + 360000, RankType.ULTIMATE));
				player = null;
				ban = null;
			}
		});
	}

}
