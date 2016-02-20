package br.com.battlebits.yutils.common.banmanager.constructors;

import java.util.UUID;

public class Mute {

	private UUID mutedPlayer;
	private String mutedBy;
	private String mutedIp;

	private UUID mutedByUUID;
	private long muteTime;
	private String reason;

	private boolean unmuted;
	private String unmutedBy;
	private UUID mutedUUID;

	private long expire;
	private long duration;

}
