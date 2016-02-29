package br.com.battlebits.ycommon.bungee.networking;

import java.io.DataOutputStream;
import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class CommonHandler {

	public static void handleAccountRequest(UUID uuid, DataOutputStream output) throws Exception {
		output.writeUTF("Account");
		String json = BungeeMain.getGson().toJson(BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid));
		output.writeUTF(json);
		output.flush();
	}

	public static void handleTranslationsLoad(Language lang, DataOutputStream output) throws Exception {
		output.writeUTF("Translations");
		String json = BungeeMain.getGson().toJson(Translate.getMapTranslation(lang));
		output.writeUTF(json);
		output.flush();
	}

}
