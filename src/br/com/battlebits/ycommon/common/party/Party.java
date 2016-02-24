package br.com.battlebits.ycommon.common.party;

import java.util.ArrayList;
import java.util.List;

import br.com.battlebits.ycommon.common.account.BattlePlayer;

public class Party {
	private List<BattlePlayer> administrators;
	private List<BattlePlayer> participants;

	public Party() {
		administrators = new ArrayList<>();
		participants = new ArrayList<>();
	}

	public void addAdministrator(BattlePlayer player) {
		administrators.add(player);
	}

	public void addParticipant(BattlePlayer player) {
		participants.remove(player);
	}

	public boolean promote(BattlePlayer player) {
		if (!participants.contains(player))
			return false;
		if (administrators.contains(player))
			return false;
		participants.remove(player);
		administrators.add(player);
		return true;
	}

	public boolean demote(BattlePlayer player) {
		if (!administrators.contains(player))
			return false;
		if (participants.contains(player))
			return false;
		administrators.remove(player);
		participants.add(player);
		return true;
	}

	public void removeParticipant(BattlePlayer player) {
		participants.remove(player);
	}

	public void removeAdministrator(BattlePlayer player) {
		administrators.remove(player);
	}

}
