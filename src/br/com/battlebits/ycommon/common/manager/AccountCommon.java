package br.com.battlebits.ycommon.common.manager;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.enums.BattleInstance;

public class AccountCommon {

	/**
	 * Sistema que substituir� o PermissionsManager, RankingManager, BuyManager
	 * e BanManager.
	 * 
	 * Sistema de contas do Battle, cada jogador possui sua conta �nica onde
	 * possui todas as suas informa��es
	 * 
	 * Possui seu hist�rico de banimentos, Seus grupos, Seus Ranks tempor�rios,
	 * suas permissoes
	 * 
	 * Possui sistema de ligas, contas e xp para melhor compreens�o de todos os
	 * jogadores.
	 * 
	 */

	private HashMap<UUID, BattlePlayer> players;

	public AccountCommon() {
		players = new HashMap<>();
	}

	public void loadBattlePlayer(UUID uuid, BattlePlayer player) {
		if (players.containsKey(uuid))
			return;
		players.put(uuid, player);
	}

	public BattlePlayer getBattlePlayer(UUID uuid) {
		if (!players.containsKey(uuid))
			return null;
		players.get(uuid).updateCache();
		return players.get(uuid);
	}

	public void unloadBattlePlayer(UUID uuid) {
		if (players.containsKey(uuid))
			players.remove(uuid);
		else
			BattlebitsAPI.getLogger().log(Level.SEVERE, "NAO FOI POSSIVEL ENCONTRAR " + uuid.toString());
	}

	public void saveBattlePlayer(BattlePlayer player) {
		if (BattlebitsAPI.getBattleInstance() == BattleInstance.BUKKIT)
			return;
		String json = BattlebitsAPI.getGson().toJson(player);
		try {
			BungeeMain.getPlugin().getConnection().update("INSERT INTO `account`(`uuid`, `json`) VALUES ('" + player.getUuid().toString().replace("-", "") + "','" + json + "') ON DUPLICATE KEY UPDATE `json` ='" + json + "';");
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public Collection<BattlePlayer> getPlayers() {
		return players.values();
	}

}
