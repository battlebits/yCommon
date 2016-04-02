package br.com.battlebits.ycommon.common.utils.mojang;

import java.util.UUID;

import br.com.battlebits.ycommon.common.BattlebitsAPI;

public abstract class UUIDFetcher {

	public static UUID getUUIDFromString(String id) {
		return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
	}

	public abstract UUID getUuid(String name) throws Exception;
	
	public static UUID getUUIDOf(String name) throws Exception {
		return BattlebitsAPI.getUUIDOf(name);
	}
}