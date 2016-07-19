package br.com.battlebits.ycommon.bukkit.utils.mojang;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.battlebits.ycommon.common.utils.mojang.UUIDFetcher;

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
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			huc.setConnectTimeout(1000);
			InputStream is = huc.getInputStream();
			InputStreamReader streamReader = new InputStreamReader(is, Charset.forName("UTF-8"));
			JsonElement element = parser.parse(streamReader);
			JsonObject object = null;
			if (element.isJsonArray())
				object = element.getAsJsonArray().get(0).getAsJsonObject();
			else
				object = element.getAsJsonObject();
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
		if (input.length() == 32 || input.length() == 36) {
			try {
				UUID uuid = getUUIDFromString(input);
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