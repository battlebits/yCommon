package br.com.battlebits.ycommon.common.account.game;

import java.util.HashMap;
import java.util.Map;

public class GameStatus {
	private Map<String, String> minigameStatus; // Minigame Name, Status JSON
	
	public GameStatus() {
		minigameStatus = new HashMap<>();
	}
	
	public Map<String, String> getMinigameStatus() {
		return minigameStatus;
	}
}
