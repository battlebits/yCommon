package br.com.battlebits.ycommon.common.manager;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.enums.BattleInstance;

public class AccountCommon {

	/**
	 * Sistema que substituirá o PermissionsManager, RankingManager, BuyManager
	 * e BanManager.
	 * 
	 * Sistema de contas do Battle, cada jogador possui sua conta única onde
	 * possui todas as suas informações
	 * 
	 * Possui seu histórico de banimentos, Seus grupos, Seus Ranks temporários,
	 * suas permissoes
	 * 
	 * Possui sistema de ligas, contas e xp para melhor compreensão de todos os
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
		return players.containsKey(uuid) ? players.get(uuid) : null;
	}

	public void unloadBattlePlayer(UUID uuid) {
		players.remove(uuid);
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
