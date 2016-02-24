package br.com.battlebits.ycommon.common;

import java.util.logging.Logger;

import br.com.battlebits.ycommon.common.manager.AccountCommon;

public class BattlebitsAPI {

	private static AccountCommon accountCommon = new AccountCommon();
	private static Logger logger = Logger.getLogger("yCommon");
	private static boolean debugMode = true;

	public static AccountCommon getAccountCommon() {
		return accountCommon;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static boolean debugModeEnabled() {
		return debugMode;
	}

	public static void debug(String debugStr) {
		if (debugMode)
			logger.info("[DEBUG] " + debugStr);
	}

}
