package br.com.battlebits.ycommon.common.translate;

import java.util.HashMap;
import java.util.Map;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class Translate {

	// banned-permanent
	// banned-temp

	// muted-permanent
	// muted-temp

	// day
	// hour
	// minute
	// second

	// alt-account

	// account-load-failed

	// owner
	// star
	// admin
	// streamer
	// mod
	// trial
	// helper
	// staff
	// builder
	// developer
	// youtuber
	// tournament
	// winner
	private static Map<Language, Map<String, String>> languageTranslations = new HashMap<>();

	public static String getTranslation(Language language, String messageId) {
		if (!languageTranslations.containsKey(language)) {
			BattlebitsAPI.debug(language.toString() + " > NAO ENCONTRADA");
			return null;
		}
		if (!languageTranslations.get(language).containsKey(messageId)) {
			BattlebitsAPI.debug(language.toString() + " > " + messageId + " > NAO ENCONTRADA");
			return null;
		}
		return languageTranslations.get(language).get(messageId);
	}

	public static String getMapTranslation(Language language) {
		if (!languageTranslations.containsKey(language)) {
			BattlebitsAPI.debug(language.toString() + " > NAO ENCONTRADA");
			return null;
		}
		return BattlebitsAPI.getGson().toJson(languageTranslations.get(language));
	}

	public static void loadTranslations(Language lang, String json) {
		languageTranslations.put(lang, 
				BattlebitsAPI
				.getGson().
				fromJson(json, 
						BattlebitsAPI
						.getTypeUtils()
						.getTranslateMap()));
	}

	/*
	 * { "PORTUGUESE": { "day": "dia", "hour": "hora", "minute": "minuto",
	 * "second": "segundo", "alt-account": "Conta Alternativa",
	 * "account-load-failed":
	 * "Sua conta falhou ao ser carregada. Por favor, tente novamente",
	 * "banned-permanent":
	 * "VOCE FOI BANIDO(A) TEMPORARIAMENTE\nPOR %banned-By% NO DIA %day%\nMOTIVO: %reason%\n\nDURAÇÃO DO BANIMENTO: %duration%"
	 * , "banned-temp": "", "muted-permanent": "", "muted-temp": "", "owner":
	 * "dono", "star": "estrela", "admin": "admin", "streamer": "streamer",
	 * "mod": "mod", "trial": "trial", "helper": "ajudante", "staff": "staff",
	 * "builder": "construtor", "developer": "coder", "youtuber": "youtuber",
	 * "tournament": "torneio", "winner": "winner" }, "ENGLISH": { "day": "day",
	 * "hour": "hour", "minute": "minute", "second": "second", "alt-account":
	 * "Alternative Account", "account-load-failed":
	 * "Your account failed to load. Please, try again.", "banned-permanent":
	 * "YOU WAS PERMANENT BANNED\nBY %banned-By% ON %day%\nREASON: %reason%\n\nBANNED INCORRECTLY? APPEAL ON: %forum%\nBUY UNBAN IN %store% TO JOIN AGAIN"
	 * , "banned-temp":
	 * "YOU WAS TEMPORARILY BANNED\nBY %banned-By% ON %day%\nREASON: %reason%\n\nBAN DURATION: %duration%"
	 * , "muted-permanent": "", "muted-temp": "", "owner": "owner", "star":
	 * "star", "admin": "admin", "streamer": "streamer", "mod": "mod", "trial":
	 * "trial", "helper": "helper", "staff": "staff", "builder": "builder",
	 * "developer": "developer", "youtuber": "youtuber", "tournament":
	 * "tournament", "winner": "winner" } }
	 */

}
