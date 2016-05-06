package br.com.battlebits.ycommon.common;

import java.util.UUID;
import java.util.logging.Logger;

import br.com.battlebits.ycommon.bukkit.util.gson.GsonBukkit;
import br.com.battlebits.ycommon.bukkit.util.json.TypeBukkitUtils;
import br.com.battlebits.ycommon.bukkit.util.mojang.BukkitPremiumChecker;
import br.com.battlebits.ycommon.bukkit.util.mojang.BukkitUUIDFetcher;
import br.com.battlebits.ycommon.bungee.utils.gson.GsonBungee;
import br.com.battlebits.ycommon.bungee.utils.json.TypeBungeeUtils;
import br.com.battlebits.ycommon.bungee.utils.mojang.BungeePremiumChecker;
import br.com.battlebits.ycommon.bungee.utils.mojang.BungeeUUIDFetcher;
import br.com.battlebits.ycommon.common.enums.BattleInstance;
import br.com.battlebits.ycommon.common.manager.AccountCommon;
import br.com.battlebits.ycommon.common.manager.ClanCommon;
import br.com.battlebits.ycommon.common.time.TimeZone;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import br.com.battlebits.ycommon.common.utils.gson.GsonInterface;
import br.com.battlebits.ycommon.common.utils.json.TypeUtils;
import br.com.battlebits.ycommon.common.utils.mojang.PremiumChecker;
import br.com.battlebits.ycommon.common.utils.mojang.UUIDFetcher;

public class BattlebitsAPI {

	private static AccountCommon accountCommon = new AccountCommon();
	private static ClanCommon clanCommon = new ClanCommon();

	private static GsonInterface gson;
	private static BattleInstance battleInstance;
	private static TypeUtils typeUtils;
	private static UUIDFetcher uuidFetcher;
	private static PremiumChecker premiumChecker;
	private static Logger logger;
	private static boolean debugMode = false;
	private static boolean useRedisBungee = false;
	private static TimeZone serverTimeZone = TimeZone.GMT03;
	private static TimeZone defaultTimeZone = TimeZone.GMT03;
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

	public static GsonInterface getGson() {
		return gson;
	}

	public static TypeUtils getTypeUtils() {
		return typeUtils;
	}

	public static boolean debugModeEnabled() {
		return debugMode;
	}

	public static Language getDefaultLanguage() {
		return defaultLanguage;
	}

	public static BattleInstance getBattleInstance() {
		return battleInstance;
	}

	public static UUID getUUIDOf(String name) {
		return uuidFetcher.getUUID(name);
	}

	public static boolean isPremium(String username) {
		return premiumChecker.isPremium(username.toLowerCase());
	}

	public static void setDefaultLanguange(Language language) {
		defaultLanguage = language;
	}

	public static void setBattleInstance(BattleInstance instance) {
		battleInstance = instance;
		switch (instance) {
		case BUKKIT:
			gson = new GsonBukkit();
			typeUtils = new TypeBukkitUtils();
			uuidFetcher = new BukkitUUIDFetcher();
			premiumChecker = new BukkitPremiumChecker();
			logger = org.bukkit.Bukkit.getLogger();
			break;
		case BUNGEECORD:
			gson = new GsonBungee();
			typeUtils = new TypeBungeeUtils();
			uuidFetcher = new BungeeUUIDFetcher();
			premiumChecker = new BungeePremiumChecker();
			logger = net.md_5.bungee.BungeeCord.getInstance().getLogger();
			break;
		}
	}

	public static void debug(String debugStr) {
		if (debugMode)
			logger.info("[DEBUG] " + debugStr);
	}

	public static String getBungeeCordChannel() {
		return ((useRedisBungee) ? "RedisBungee" : "BungeeCord");
	}
	
	public static TimeZone getServerTimeZone() {
		return serverTimeZone;
	}
	
	public static TimeZone getDefaultTimeZone() {
		return defaultTimeZone;
	}

}
