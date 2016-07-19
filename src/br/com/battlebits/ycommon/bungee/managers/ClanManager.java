package br.com.battlebits.ycommon.bungee.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.clans.Clan;

public class ClanManager {

	public Clan loadClan(String clanName) {
		try {
			Connection connection = BungeeMain.getPlugin().getConnection().getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `clans` WHERE `name`='" + clanName + "';");
			ResultSet result = stmt.executeQuery();
			BattlebitsAPI.debug("CLAN > EXCUTED");
			if (result.next()) {
				Clan clan = BattlebitsAPI.getGson().fromJson(result.getString("json"), Clan.class);
				BattlebitsAPI.getClanCommon().loadClan(clan);
				BattlebitsAPI.debug("CLAN > LOADED");
			}
			result.close();
			stmt.close();
			result = null;
			stmt = null;
			BattlebitsAPI.debug("CLAN > CLOSE");
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return BattlebitsAPI.getClanCommon().getClan(clanName);
	}

	public boolean clanNameExists(String clanName) {
		boolean clanNameExists = false;
		try {
			Connection connection = BungeeMain.getPlugin().getConnection().getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT `name` FROM `clans` WHERE `name`='" + clanName + "';");
			ResultSet result = stmt.executeQuery();
			BattlebitsAPI.debug("CLAN > EXCUTED");
			if (result.next()) {
				clanNameExists = true;
			}
			result.close();
			stmt.close();
			result = null;
			stmt = null;
			BattlebitsAPI.debug("CLAN > CLOSE");
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return clanNameExists;
	}

	public boolean clanAbbreviationExists(String abbreviation) {
		boolean clanAbbreviationExists = false;
		try {
			Connection connection = BungeeMain.getPlugin().getConnection().getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT `abbreviation` FROM `clans` WHERE `abbreviation`='" + abbreviation + "';");
			ResultSet result = stmt.executeQuery();
			BattlebitsAPI.debug("CLAN > EXCUTED");
			if (result.next()) {
				clanAbbreviationExists = true;
			}
			result.close();
			stmt.close();
			result = null;
			stmt = null;
			BattlebitsAPI.debug("CLAN > CLOSE");
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return clanAbbreviationExists;
	}
	
	public void disbandClan(Clan clan) {
		try {
			Connection connection = BungeeMain.getPlugin().getConnection().getConnection();
			Statement stmt = connection.createStatement();
			stmt.execute("DELETE FROM `clans` WHERE `name`='" + clan.getClanName() + "';");
			BattlebitsAPI.debug("CLAN > EXCUTED");
			stmt.close();
			stmt = null;
			BattlebitsAPI.debug("CLAN > CLOSE");
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
