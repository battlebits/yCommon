package br.com.battlebits.ycommon.common.account.game;

import java.util.HashMap;
import java.util.Map;

import br.com.battlebits.ycommon.common.BattlebitsAPI;

public class GameStatus {
	private Map<String, String> minigameStatus; // Minigame Name, Status JSON

	public GameStatus() {
		minigameStatus = new HashMap<>();
	}

	public Map<String, String> getMinigameStatus() {
		return minigameStatus;
	}

	public void setMinigameStatus(Map<String, String> status) {
		this.minigameStatus = status;
	}

	public <T> T getMinigame(GameType type, Class<?> cls) {
		if (!minigameStatus.containsKey(type.getServerId()))
			return null;
		T game = BattlebitsAPI.getGson().fromJson(minigameStatus.get(type.getServerId()), cls);
		return game;
	}

	public void updateMinigame(GameType key, Object mini) {
		updateMinigame(key.getServerId(), BattlebitsAPI.getGson().toJson(mini));
	}

	public void updateMinigame(String key, String mini) {
		minigameStatus.put(key, mini);
	}

}
