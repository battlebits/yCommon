package br.com.battlebits.yutils.common;

import java.util.logging.Logger;

import br.com.battlebits.yutils.common.manager.AccountCommon;

public class BattlebitsAPI {

	private static AccountCommon accountCommon = new AccountCommon();
	private static Logger logger;
	
	public static AccountCommon getAccountCommon() {
		return accountCommon;
	}
	
	public static Logger getLogger() {
		return logger;
	}

}
