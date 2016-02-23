package br.com.battlebits.yutils.common.clans;

import java.util.ArrayList;
import java.util.List;

import br.com.battlebits.yutils.common.account.BattlePlayer;
import br.com.battlebits.yutils.common.clans.ranking.ClanRank;

public class Clan {
	private String clanName;
	private String abbreviation;
	private ClanRank rank;
	private BattlePlayer owner;
	private List<BattlePlayer> participants;

	public Clan(String clanName, String abbreviation, BattlePlayer owner) {
		this.owner = owner;
		this.clanName = clanName;
		this.abbreviation = abbreviation;
		this.rank = ClanRank.INITIAL;
		participants = new ArrayList<>();
		participants.add(owner);
	}

	public String getClanName() {
		return clanName;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public ClanRank getClanRank() {
		return rank;
	}

	public BattlePlayer getOwner() {
		return owner;
	}

	public List<BattlePlayer> getParticipants() {
		return participants;
	}

	public void addParticipant(BattlePlayer player) {
		participants.remove(player);
	}

	public boolean promote(BattlePlayer player) {
		if (!participants.contains(player))
			return false;
		owner = player;
		return true;
	}

	public boolean removeParticipant(BattlePlayer player) {
		if (owner == player)
			return false;
		participants.remove(player);
		return true;
	}

}
