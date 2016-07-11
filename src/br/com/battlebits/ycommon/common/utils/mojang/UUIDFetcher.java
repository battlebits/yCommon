package br.com.battlebits.ycommon.common.utils.mojang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.battlebits.ycommon.common.BattlebitsAPI;

public abstract class UUIDFetcher {

	private ArrayList<String> servers;
	private HashMap<String, Integer> fails;
	private int current;
	private Pattern namePattern;

	public UUIDFetcher() {
		servers = new ArrayList<>();
		servers.add("https://api.mojang.com/users/profiles/minecraft/%player-name%#id#name");
		servers.add("https://mcapi.ca/uuid/player/%player-name%#id#name");
		servers.add("https://us.mc-api.net/v3/uuid/%player-name%#full_uuid#name");
		// URL # CAMPO ID # CAMPO NOME
		fails = new HashMap<>();
		current = 0;
		namePattern = Pattern.compile("[a-zA-Z0-9_]{1,16}");
	}

	private String getNextServer() {
		if (current == (servers.size() - 1)) {
			current = 0;
		} else {
			current += 1;
		}
		return servers.get(current);
	}

	public UUID loadFromServers(String name) {
		UUID id = null;
		String server1 = getNextServer();
		id = load(name, server1);
		if (id == null) {
			id = load(name, getNextServer());
			if (id == null) {
				if (fails.containsKey(server1)) {
					fails.put(server1, fails.get(server1) + 1);
				} else {
					fails.put(server1, 1);
				}
				BattlebitsAPI.getLogger().warning(server1 + " falhou em tentar obter a UUID do jogador " + name);
			}
		}
		server1 = null;
		return id;
	}

	public boolean isValidName(String username) {
		if (username.length() < 3)
			return false;
		if (username.length() > 16)
			return false;
		Matcher matcher = namePattern.matcher(username);
		return matcher.matches();
	}

	public UUID getUUIDFromString(String id) {
		if (id.length() == 36) {
			return UUID.fromString(id);
		} else if (id.length() == 32) {
			return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
		} else {
			return null;
		}
	}

	public abstract UUID load(String name, String server);

	public abstract UUID getUUID(String name);
}