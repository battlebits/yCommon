package br.com.battlebits.ycommon.bukkit.util.mojang;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.common.utils.mojang.UUIDFetcher;
import net.minecraft.util.com.google.common.cache.Cache;
import net.minecraft.util.com.google.common.cache.CacheBuilder;
import net.minecraft.util.com.google.common.cache.CacheLoader;
import net.minecraft.util.com.google.gson.JsonObject;
import net.minecraft.util.com.google.gson.JsonParser;

public class BukkitUUIDFetcher extends UUIDFetcher {

	private JsonParser parser = new JsonParser();

	private Cache<String, UUID> nameUUID = CacheBuilder.newBuilder().expireAfterWrite(1L, TimeUnit.DAYS).build(new CacheLoader<String, UUID>() {
		@Override
		public UUID load(String name) throws Exception {
			return loadUUID(name);
		}
	});

	private UUID loadUUID(String name) {
		UUID id = null;
		Player p = Bukkit.getPlayerExact(name);
		if (p != null) {
			id = p.getUniqueId();
			p = null;
		} else {
			id = loadFromServers(name);
		}
		return id;
	}

	@Override
	public UUID load(String name, String server) {
		UUID id = null;
		try {
			String[] infos = server.split("#");
			String serverUrl = infos[0].replace("%player-name%", name);
			URL url = new URL(serverUrl);
			InputStream is = url.openStream();
			InputStreamReader streamReader = new InputStreamReader(is, Charset.forName("UTF-8"));
			JsonObject object = parser.parse(streamReader).getAsJsonObject();
			if (object.get(infos[2]).getAsString().equalsIgnoreCase(name)) {
				id = (getUUIDFromString(object.get(infos[1]).getAsString()));
			}
			streamReader.close();
			is.close();
			object = null;
			streamReader = null;
			is = null;
			url = null;
			serverUrl = null;
			infos = null;
			server = null;
		} catch (Exception ex) {
			return null;
		}
		return id;
	}

	@Override
	public UUID getUUID(String input) {
		if (input.length() == 32) {
			try {
				UUID uuid = getUUIDFromString(input);
				return uuid;
			} catch (Exception e) {
				return null;
			}
		} else if (input.length() == 36) {
			try {
				UUID uuid = UUID.fromString(input);
				return uuid;
			} catch (Exception e) {
				return null;
			}
		} else if (isValidName(input)) {
			try {
				return nameUUID.get(input, new Callable<UUID>() {
					@Override
					public UUID call() throws Exception {
						return loadUUID(input);
					}
				});
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}
}