package br.com.battlebits.ycommon.bungee.listeners;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.managers.BanManager;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.banmanager.constructors.Ban;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.utils.GeoIpUtils;
import br.com.battlebits.ycommon.common.utils.GeoIpUtils.IpCityResponse;
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
				String timeZoneCode = "0";
				try {
					IpCityResponse responde = GeoIpUtils.getIpStatus(ipAdress.getHostString());
					if (responde != null)
						countryCode = responde.getCountryCode();
					timeZoneCode = responde.getTimeZone();
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
					boolean success = false;
					if (result.next()) {
						try {
							BattlePlayer player = BattlebitsAPI.getGson().fromJson(result.getString("json"), BattlePlayer.class);
							player.setJoinData(userName, ipAdress, countryCode, timeZoneCode);
							BattlebitsAPI.getAccountCommon().loadBattlePlayer(uuid, player);
							BattlebitsAPI.debug("ACCOUNT > LOADED");
							success = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (!success) {
						BattlebitsAPI.getAccountCommon().loadBattlePlayer(uuid, new BattlePlayer(userName, uuid, ipAdress, countryCode, timeZoneCode));
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

				if (player.getBanHistory() != null)
					if (player.getBanHistory().getActualBan() == null) {
						Entry<UUID, Ban> ipBan = BungeeMain.getPlugin().getBanManager().getIpBan(ipAdress.getHostString());
						if (ipBan != null) {
							if (!ipBan.getKey().equals(player.getUuid()))
								BungeeMain.getPlugin().getBanManager().ban(player, new Ban("CONSOLE", ipAdress.getHostString(), "proxy", Translate.getTranslation(player.getLanguage(), "alt-account")));
						}
					}
				Ban ban = null;
				if (player.getBanHistory() != null)
					ban = player.getBanHistory().getActualBan();
				if (ban != null) {
					event.setCancelled(true);
					event.setCancelReason(BanManager.getBanKickMessage(ban, player.getLanguage(), player.getTimeZone()));
				}
				BattlebitsAPI.debug("BANNING > FINISHED");
				event.completeIntent(BungeeMain.getPlugin());
				if (event.isCancelled()) {
					BattlebitsAPI.getAccountCommon().saveBattlePlayer(player);
				}
				player = null;
				ban = null;
			}
		});
	}

}
