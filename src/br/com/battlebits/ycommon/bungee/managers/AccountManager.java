package br.com.battlebits.ycommon.bungee.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;

public class AccountManager {

	public BattlePlayer loadBattlePlayer(UUID uuid) {
		try {
			Connection connection = BungeeMain.getPlugin().getConnection().getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `account` WHERE `uuid`='" + uuid.toString().replace("-", "") + "';");
			ResultSet result = stmt.executeQuery();
			BattlebitsAPI.debug("ACCOUNT > EXCUTED");
			if (result.next()) {
				BattlePlayer player = BattlebitsAPI.getGson().fromJson(result.getString("json"), BattlePlayer.class);
				BattlebitsAPI.getAccountCommon().loadBattlePlayer(uuid, player);
				BattlebitsAPI.debug("ACCOUNT > LOADED");
			}
			result.close();
			stmt.close();
			result = null;
			stmt = null;
			BattlebitsAPI.debug("ACCOUNT > CLOSE");
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
	}
}
