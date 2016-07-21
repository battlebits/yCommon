package br.com.battlebits.ycommon.bungee.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.networking.client.CommonClient;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.clans.Clan;
import br.com.battlebits.ycommon.common.networking.packets.CPacketClanLoad;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ClanManager {

	public Clan loadClan(String clanName) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Connection connection = BungeeMain.getPlugin().getConnection().getConnection();
		PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `clans` WHERE `name`=?;");
		stmt.setString(1, clanName.toLowerCase());
		ResultSet result = stmt.executeQuery();
		BattlebitsAPI.debug("CLAN > EXCUTED");
		if (result.next()) {
			Clan clan = BattlebitsAPI.getGson().fromJson(result.getString("json"), Clan.class);
			clan.updateCache();
			BattlebitsAPI.getClanCommon().loadClan(clan);
			BattlebitsAPI.debug("CLAN > LOADED");
		}
		result.close();
		stmt.close();
		result = null;
		stmt = null;
		BattlebitsAPI.debug("CLAN > CLOSE");
		return BattlebitsAPI.getClanCommon().getClan(clanName);
	}

	public boolean clanNameExists(String clanName) {
		boolean clanNameExists = false;
		try {
			Connection connection = BungeeMain.getPlugin().getConnection().getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT `name` FROM `clans` WHERE `name`=?;");
			stmt.setString(1, clanName.toLowerCase());
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
			PreparedStatement stmt = connection.prepareStatement("SELECT `abbreviation` FROM `clans` WHERE `abbreviation`=?;");
			stmt.setString(1, abbreviation.toLowerCase());
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

	public void updateClan(Clan clan) {
		for (UUID participante : clan.getParticipants()) {
			ProxiedPlayer proxied = BungeeCord.getInstance().getPlayer(participante);
			if (proxied == null)
				continue;
			BattlePlayer battlePlayer = BattlebitsAPI.getAccountCommon().getBattlePlayer(proxied.getUniqueId());
			if (battlePlayer != null) {
				CommonClient client = BungeeMain.getPlugin().getCommonServer().getClient(battlePlayer.getServerConnected());
				if (client != null) {
					client.sendPacket(new CPacketClanLoad(clan));
				}
			}
		}
		BattlebitsAPI.getClanCommon().saveClan(clan);
	}

	public void disbandClan(Clan clan) {
		try {
			Connection connection = BungeeMain.getPlugin().getConnection().getConnection();
			Statement stmt = connection.createStatement();
			stmt.execute("DELETE FROM `clans` WHERE `name`='" + clan.getClanName().toLowerCase() + "';");
			BattlebitsAPI.debug("CLAN > EXCUTED");
			stmt.close();
			stmt = null;
			BattlebitsAPI.debug("CLAN > CLOSE");
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
