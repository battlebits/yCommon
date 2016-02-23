package br.com.battlebits.yutils.common.manager;

import java.util.HashMap;
import java.util.UUID;

import br.com.battlebits.yutils.common.account.BattlePlayer;

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
		players.put(uuid, player);
	}

	public BattlePlayer getBattlePlayer(UUID uuid) {
		return players.containsKey(uuid) ? players.get(uuid) : players.put(uuid, new BattlePlayer());
	}

	public void unloadBattlePlayer(UUID uuid) {
		players.remove(uuid);
	}

}
