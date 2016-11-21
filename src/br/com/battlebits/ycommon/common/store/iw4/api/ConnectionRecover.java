package br.com.battlebits.ycommon.common.store.iw4.api;

public class ConnectionRecover {
	private static long timeToReconnect = System.currentTimeMillis() + 300000;

	public static void reconnect() {
		if (timeToReconnect < System.currentTimeMillis()) {
			timeToReconnect = System.currentTimeMillis() + 300000;
			return;
		}
		try {
			br.com.iwnetwork.iw4.plugin.System.getApi().auth();
		} catch (Exception e) {
		}
	}
}
