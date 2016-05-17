package br.com.battlebits.ycommon.common.utils.mojang;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import br.com.battlebits.ycommon.common.BattlebitsAPI;

public class PremiumChecker {

	private Cache<String, Boolean> isPremium = CacheBuilder.newBuilder().expireAfterWrite(1L, TimeUnit.DAYS).build(new CacheLoader<String, Boolean>() {
		@Override
		public Boolean load(String name) throws Exception {
			return checkInMojang(name);
		}
	});

	public boolean checkInMojang(String username) {
		boolean isPremium = false;
		try {
			URL url = new URL("https://minecraft.net/haspaid.jsp?user=" + username);
			InputStream stream = url.openStream();
			InputStreamReader streamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
			BufferedReader bufferedReader = new BufferedReader(streamReader);
			if (bufferedReader.readLine().equals("true")) {
				isPremium = true;
			}
			bufferedReader.close();
			streamReader.close();
			stream.close();
			bufferedReader = null;
			streamReader = null;
			stream = null;
			url = null;
		} catch (Exception e) {
			BattlebitsAPI.getLogger().warning("Não foi possível verificar se o jogador " + username + " é Premium!");
		}
		return isPremium;
	}

	public boolean isPremium(String nickname) {
		try {
			return isPremium.get(nickname, new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return checkInMojang(nickname);
				}
			});
		} catch (Exception e) {
			return true;
		}
	}
}
