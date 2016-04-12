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

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.utils.mojang.UUIDFetcher;
import net.minecraft.util.com.google.common.cache.Cache;
import net.minecraft.util.com.google.common.cache.CacheBuilder;
import net.minecraft.util.com.google.common.cache.CacheLoader;
import net.minecraft.util.com.google.gson.JsonObject;
import net.minecraft.util.com.google.gson.JsonParser;

public class BukkitUUIDFetcher extends UUIDFetcher {

	private static JsonParser parser = new JsonParser();
	private static String mojangURL = "https://api.mojang.com/users/profiles/minecraft";
	private static String craftApiURL = "https://craftapi.com/api/user/uuid";

	private static Cache<String, UUID> nameUUID = CacheBuilder.newBuilder().expireAfterWrite(1L, TimeUnit.DAYS)
			.build(new CacheLoader<String, UUID>() {
				@Override
				public UUID load(String name) throws Exception {
					return loadUUID(name);
				}
			});

	private static UUID loadUUID(String name) {
		UUID id = null;
		Player p = Bukkit.getPlayerExact(name);
		if (p != null) {
			id = p.getUniqueId();
			p = null;
		} else {
			id = loadFromMojang(name);
			if (id == null) {
				id = loadFromCraftAPI(name);
			}
		}
		return id;
	}

	private static UUID loadFromMojang(String name) {
		UUID id = null;
		try {
			URL url = new URL(mojangURL + "/" + name);
			InputStream is = url.openStream();
			InputStreamReader streamReader = new InputStreamReader(is, Charset.forName("UTF-8"));
			JsonObject object = parser.parse(streamReader).getAsJsonObject();
			if (object.get("name").getAsString().equalsIgnoreCase(name)) {
				id = (getUUIDFromString(object.get("id").getAsString()));
			}
			streamReader.close();
			is.close();
		} catch (Exception e) {
			BattlebitsAPI.getLogger()
					.warning("Erro ao tentar obter UUID do jogador " + name + " utilizando a API da Mojang!");
		}
		return id;
	}

	private static UUID loadFromCraftAPI(String name) {
		UUID id = null;
		try {
			URL url = new URL(craftApiURL + "/" + name);
			InputStream is = url.openStream();
			InputStreamReader streamReader = new InputStreamReader(is, Charset.forName("UTF-8"));
			JsonObject object = parser.parse(streamReader).getAsJsonObject();
			if (object.get("username").getAsString().equalsIgnoreCase(name)) {
				id = (getUUIDFromString(object.get("uuid").getAsString()));
			}
			streamReader.close();
			is.close();
		} catch (Exception e) {
			BattlebitsAPI.getLogger().warning("Erro ao tentar obter UUID do jogador " + name + " utilizando a CraftAPI!");
		}
		return id;
	}

	public static UUID getUUIDFromString(String id) {
		return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-"
				+ id.substring(20, 32));
	}

	@Override
	public UUID getUuid(String name) throws Exception {
		Player t = Bukkit.getPlayerExact(name);
		if (t != null) {
			return t.getUniqueId();
		} else {
			return nameUUID.get(name, new Callable<UUID>() {
				@Override
				public UUID call() throws Exception {
					return loadUUID(name);
				}
			});
		}
	}
}