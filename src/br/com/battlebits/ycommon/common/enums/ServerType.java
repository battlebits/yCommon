package br.com.battlebits.ycommon.common.enums;

public enum ServerType {
	NETWORK, HUNGERGAMES, BATTLECRAFT, SKYWARS, LOBBY, RAID, GARTICCRAFT, TESTSERVER, NONE;

	public String getName() {
		return getServerName(super.toString().toLowerCase());
	}

	private static String getServerName(String server) {
		String serverName = server;
		char[] stringArray = serverName.toCharArray();
		stringArray[0] = Character.toUpperCase(stringArray[0]);
		serverName = new String(stringArray);
		return serverName;
	}
}
