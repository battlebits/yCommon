package br.com.battlebits.ycommon.common.manager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.clans.Clan;
import br.com.battlebits.ycommon.common.enums.BattleInstance;

public class ClanCommon {

	/**
	 * Sistema de Clans que irá fazer mais jogadores entrar no Battle para criar
	 * seus próprios clans e continuar a jogar no Battle
	 * 
	 * Sistema de clans do Battle, cada clan possui informações para
	 * rankeamento, etc
	 * 
	 */

	private HashMap<String, Clan> clans;

	public ClanCommon() {
		clans = new HashMap<>();
	}

	public void loadClan(Clan clan) {
		clans.put(clan.getClanName().toLowerCase(), clan);
	}

	public void unloadClan(String clanName) {
		if (clans.containsKey(clanName.toLowerCase()))
			clans.remove(clanName.toLowerCase());
		else
			BattlebitsAPI.getLogger().log(Level.SEVERE, "NAO FOI POSSIVEL ENCONTRAR CLAN " + clanName);
	}

	public void saveClan(Clan clan) {
		if (BattlebitsAPI.getBattleInstance() == BattleInstance.BUKKIT)
			return;
		String json = BattlebitsAPI.getGson().toJson(clan);
		try {
			PreparedStatement stmt = BungeeMain.getPlugin().getConnection().prepareStatment("INSERT INTO `clans`(`name`,`abbreviation`, `json`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `json` = ?, `abbreviation` = ?;");
			stmt.setString(1, clan.getClanName().toLowerCase());
			stmt.setString(2, clan.getAbbreviation().toLowerCase());
			stmt.setString(3, json);
			stmt.setString(4, json);
			stmt.setString(5, clan.getAbbreviation().toLowerCase());
			stmt.executeUpdate();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public Clan getClan(String clanName) {
		Clan clan = clans.get(clanName.toLowerCase());
		if (clan != null)
			clan.updateCache();
		return clan;
	}

	public Collection<Clan> getClans() {
		return clans.values();
	}

}
