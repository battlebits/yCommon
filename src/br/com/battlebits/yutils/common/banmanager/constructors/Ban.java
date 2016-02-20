package br.com.battlebits.yutils.common.banmanager.constructors;

import java.util.UUID;

public class Ban {

	private UUID bannedPlayer;
	private String bannedBy;
	private String bannedIp;

	private UUID bannedByUUID;
	private long banTime;
	private String reason;

	private boolean unbanned;
	private String unbannedBy;
	private UUID unbannedByUUID;

	private long expire;
	private long duration;

}
