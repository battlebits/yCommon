package br.com.battlebits.ycommon.common.store.iw4.api;

public class ConnectionRecover {
	private static long timeToReconnect = System.currentTimeMillis() + 300000;

	public static boolean reconnect() {
		if (timeToReconnect > System.currentTimeMillis()) {
			timeToReconnect = System.currentTimeMillis() + 300000;
			return false;
		}
		timeToReconnect = System.currentTimeMillis() + 300000;
		return auth();
	}

	public static boolean auth() {
		return br.com.iwnetwork.iw4.plugin.System.getApi().auth();
	}
}
