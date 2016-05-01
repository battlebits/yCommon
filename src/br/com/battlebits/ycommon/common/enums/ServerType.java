package br.com.battlebits.ycommon.common.enums;

public enum ServerType {
	NETWORK("bbits.com.br"), HUNGERGAMES("battle-hg.com"), BATTLECRAFT("battlecraft.com.br"), SKYWARS("sw.battlebits.com.br"), LOBBY("lobby.battlebits.com.br"), RAID("raid.battlebits.com.br"), GARTICCRAFT("gc.battlebits.com.br"), TESTSERVER("teste.battlebits.com.br"), NONE("none");

	private String suffix;

	private ServerType(String ends) {
		this.suffix = ends;
	}

	public String getSuffix() {
		return suffix;
	}

	public static ServerType getServerType(String serverHostname) {
		for (ServerType type : values()) {
			if (serverHostname.endsWith(type.suffix))
				return type;
		}
		return NONE;
	}

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
