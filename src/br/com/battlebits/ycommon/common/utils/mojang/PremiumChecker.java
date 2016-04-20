package br.com.battlebits.ycommon.common.utils.mojang;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import br.com.battlebits.ycommon.common.BattlebitsAPI;

public abstract class PremiumChecker {

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

	public abstract boolean isPremium(String username);
}
