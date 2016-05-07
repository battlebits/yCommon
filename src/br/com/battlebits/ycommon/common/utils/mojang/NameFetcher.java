package br.com.battlebits.ycommon.common.utils.mojang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import br.com.battlebits.ycommon.common.BattlebitsAPI;

public abstract class NameFetcher {

	private ArrayList<String> servers;
	private HashMap<String, Integer> fails;
	private int current;

	public NameFetcher() {
		servers = new ArrayList<>();
		servers.add("https://sessionserver.mojang.com/session/minecraft/profile/%player-uuid%#name#id");
		servers.add("https://craftapi.com/api/user/username/%player-uuid%#username#uuid");
		servers.add("https://us.mc-api.net/v3/name/%player-uuid%#name#uuid");
		servers.add("https://mcapi.ca/name/uuid/%player-uuid%#name#uuid");
		// URL # CAMPO NOME # CAMPO ID
		fails = new HashMap<>();
		current = 0;
	}

	private String getNextServer() {
		if (current == (servers.size() - 1)) {
			current = 0;
		} else {
			current += 1;
		}
		return servers.get(current);
	}

	public String loadFromServers(UUID id) {
		String name = null;
		String server1 = getNextServer();
		name = load(id, server1);
		if (name == null) {
			name = load(id, getNextServer());
			if (name != null) {
				if (fails.containsKey(server1)) {
					fails.put(server1, fails.get(server1) + 1);
				} else {
					fails.put(server1, 1);
				}
				BattlebitsAPI.getLogger().warning(server1 + " falhou em tentar obter o nome do jogador " + id.toString());
			}
		}
		server1 = null;
		return name;
	}

	public abstract String load(UUID id, String server);

	public abstract String getUsername(UUID id);
}