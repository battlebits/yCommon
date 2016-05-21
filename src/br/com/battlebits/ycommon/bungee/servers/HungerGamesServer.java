package br.com.battlebits.ycommon.bungee.servers;

public class HungerGamesServer extends BattleServer {

	private int tempo;
	private HungerGamesState state;

	public HungerGamesServer(String serverId, int onlinePlayers, boolean joinEnabled) {
		super(serverId, onlinePlayers, 100, joinEnabled);
		this.state = HungerGamesState.WAITING;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}

	public int getTempo() {
		return tempo;
	}

	public HungerGamesState getState() {
		return state;
	}

	public void setState(HungerGamesState state) {
		this.state = state;
	}

	@Override
	public int getActualNumber() {
		return super.getActualNumber();
	}

	@Override
	public boolean canBeSelected() {
		return super.canBeSelected() && !(getState() == HungerGamesState.PREGAME && tempo >= 15);
	}

	public static enum HungerGamesState {
		WAITING, PREGAME, INVENCIBILITY, GAMETIME, NONE;
	}

}
