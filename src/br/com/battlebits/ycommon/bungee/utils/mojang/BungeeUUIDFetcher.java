package br.com.battlebits.ycommon.bungee.utils.mojang;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.utils.mojang.UUIDFetcher;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeUUIDFetcher extends UUIDFetcher {

	private JsonParser parser = new JsonParser();

	private Cache<String, UUID> nameUUID = CacheBuilder.newBuilder().expireAfterWrite(1L, TimeUnit.DAYS).build(new CacheLoader<String, UUID>() {
		@Override
		public UUID load(String name) throws Exception {
			return loadUUID(name);
		}
	});

	private UUID loadUUID(String name) {
		UUID id = null;
		ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(name);
		if (pp != null) {
			id = pp.getUniqueId();
			pp = null;
		} else {
			id = loadFromMojang(name);
			if (id == null) {
				id = loadFromCraftAPI(name);
			}
		}
		return id;
	}

	private UUID loadFromMojang(String name) {
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
			BattlebitsAPI.getLogger().warning("Erro ao tentar obter UUID do jogador " + name + " utilizando a API da Mojang!");
		}
		return id;
	}

	private UUID loadFromCraftAPI(String name) {
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

	@Override
	public UUID getUUID(String name) {
		try {
			return nameUUID.get(name, new Callable<UUID>() {
				@Override
				public UUID call() throws Exception {
					return loadUUID(name);
				}
			});
		} catch (Exception e) {
			return null;
		}
	}
}
