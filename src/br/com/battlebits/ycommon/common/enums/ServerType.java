package br.com.battlebits.ycommon.common.enums;

public enum ServerType {
	NETWORK("bbits.com.br", ServerStaff.NETWORK), //
	HUNGERGAMES("battle-hg.com", ServerStaff.HUNGERGAMES), //
	FAIRPLAY("fp.battle-hg.com", ServerStaff.HUNGERGAMES), //
	PVP_FULLIRON("fulliron.pvp.battlebits.com.br", ServerStaff.BATTLECRAFT), //
	PVP_SIMULATOR("simulator.pvp.battlebits.com.br", ServerStaff.BATTLECRAFT), //
	SKYWARS("sw.battlebits.com.br", ServerStaff.SKYWARS), //
	LOBBY("lobby.battlebits.com.br", ServerStaff.LOBBY), //
	RAID("raid.battlebits.com.br", ServerStaff.RAID), //
	GARTICCRAFT("gc.battlebits.com.br", ServerStaff.GARTICCRAFT), //
	TESTSERVER("teste.battlebits.com.br", ServerStaff.TESTSERVER), //
	NONE("none", ServerStaff.NONE);

	private String suffix;
	private ServerStaff staffType;

	private ServerType(String ends, ServerStaff staffType) {
		this.suffix = ends;
		this.staffType = staffType;
	}

	public String getSuffix() {
		return suffix;
	}

	public ServerStaff getStaffType() {
		return staffType;
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
