package br.com.battlebits.ycommon.common.translate;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import net.md_5.bungee.api.ChatColor;

public class Translate {

	private static Map<String, Map<Language, Map<String, String>>> languageTranslations = new HashMap<>();

	public static String getTranslation(Language language, String messageId) {
		String message = null;

		for (Map<Language, Map<String, String>> translations : languageTranslations.values()) {
			if (!translations.containsKey(language)) {
				BattlebitsAPI.debug(language.toString() + " > NAO ENCONTRADA");
				continue;
			}
			if (!translations.get(language).containsKey(messageId)) {
				continue;
			}
			message = translations.get(language).get(messageId);
			break;
		}

		if (message == null) {
			message = "[NOT FOUND: '" + messageId + "']";
			BattlebitsAPI.debug(language.toString() + " > " + messageId + " > NAO ENCONTRADA");
		}

		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static String getyCommonMapTranslation(Language language) {
		if (!languageTranslations.get(BattlebitsAPI.TRANSLATION_ID).containsKey(language)) {
			BattlebitsAPI.debug(language.toString() + " > NAO ENCONTRADA");
			return null;
		}
		return BattlebitsAPI.getGson().toJson(languageTranslations.get(BattlebitsAPI.TRANSLATION_ID).get(language));
	}

	public static void loadTranslations(String translationType, Language lang, String json) {
		Map<Language, Map<String, String>> map = languageTranslations.get(translationType);
		if (map == null) {
			map = new HashMap<>();
			languageTranslations.put(translationType, map);
		}
		map.put(lang, BattlebitsAPI.getGson().fromJson(json, new TypeToken<HashMap<String, String>>() {
		}.getType()));
	}

}
