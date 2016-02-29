package br.com.battlebits.ycommon.common;

import java.util.logging.Logger;

import br.com.battlebits.ycommon.common.manager.AccountCommon;
import br.com.battlebits.ycommon.common.manager.ClanCommon;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class BattlebitsAPI {

	private static AccountCommon accountCommon = new AccountCommon();
	private static ClanCommon clanCommon = new ClanCommon();
	private static Logger logger = Logger.getLogger("Minecraft");
	private static boolean debugMode = true;
	public final static String FORUM_WEBSITE = "http://forum.battlebits.com.br";
	public final static String WEBSITE = "http://battlebits.com.br";
	public final static String STORE = "http://loja.battlebits.com.br";
	public final static String ADMIN_EMAIL = "admin@battlebits.com.br";
	public final static String TWITTER = "@BattlebitsMC";
	public final static String HUNGERGAMES_ADDRESS = "battle-hg.com";
	public static Language defaultLanguage = Language.PORTUGUES;

	public static AccountCommon getAccountCommon() {
		return accountCommon;
	}

	public static ClanCommon getClanCommon() {
		return clanCommon;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getBungeeChannel() {
		return "yCommon";
	}

	public static boolean debugModeEnabled() {
		return debugMode;
	}

	public static Language getDefaultLanguage() {
		return defaultLanguage;
	}

	public static void setDefaultLanguange(Language language) {
		defaultLanguage = language;
	}

	public static void debug(String debugStr) {
		if (debugMode)
			logger.info("[DEBUG] " + debugStr);
	}

}
