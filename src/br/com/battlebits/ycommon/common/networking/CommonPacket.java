package br.com.battlebits.ycommon.common.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.battlebits.ycommon.common.exception.HandlePacketException;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAccountConfiguration;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAccountLoad;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAccountRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAddBlockedPlayer;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAddFriend;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAddFriendRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAddGroup;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAddRank;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeAccount;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeLanguage;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeLiga;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeTag;
import br.com.battlebits.ycommon.common.networking.packets.CPacketClanLoad;
import br.com.battlebits.ycommon.common.networking.packets.CPacketCommandRun;
import br.com.battlebits.ycommon.common.networking.packets.CPacketCreateParty;
import br.com.battlebits.ycommon.common.networking.packets.CPacketDisbandParty;
import br.com.battlebits.ycommon.common.networking.packets.CPacketJoinParty;
import br.com.battlebits.ycommon.common.networking.packets.CPacketKeepAlive;
import br.com.battlebits.ycommon.common.networking.packets.CPacketPlayerJoinClan;
import br.com.battlebits.ycommon.common.networking.packets.CPacketPlayerLeaveClan;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveBlockedPlayer;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveFriend;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveFriendRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveGroup;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveRank;
import br.com.battlebits.ycommon.common.networking.packets.CPacketServerInfo;
import br.com.battlebits.ycommon.common.networking.packets.CPacketServerNameLoad;
import br.com.battlebits.ycommon.common.networking.packets.CPacketServerRecall;
import br.com.battlebits.ycommon.common.networking.packets.CPacketServerStart;
import br.com.battlebits.ycommon.common.networking.packets.CPacketServerStop;
import br.com.battlebits.ycommon.common.networking.packets.CPacketTranslationsLoad;
import br.com.battlebits.ycommon.common.networking.packets.CPacketTranslationsRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateClan;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateGameStatus;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateProfile;

public abstract class CommonPacket {
	private static final Map<Byte, Class<?>> MAP_CLASS = new HashMap<Byte, Class<?>>();
	private static final Map<Class<?>, Byte> MAP_ID = new HashMap<Class<?>, Byte>();
	private static final Map<Byte, Short> MAP_VERSION = new HashMap<Byte, Short>();
	private static final Map<Byte, Boolean> MAP_VALID = new HashMap<Byte, Boolean>();

	static {
		/** 0x0X Server **/
		register((byte) 0x00, CPacketKeepAlive.class);
		register((byte) 0x01, CPacketServerStart.class);
		register((byte) 0x02, CPacketServerStop.class);
		register((byte) 0x03, CPacketServerRecall.class);
		register((byte) 0x04, CPacketServerNameLoad.class);
		register((byte) 0x05, CPacketServerInfo.class);
		register((byte) 0x06, CPacketCommandRun.class);
		register((byte) 0x07, CPacketTranslationsRequest.class);
		register((byte) 0x08, CPacketTranslationsLoad.class);
		register((byte) 0x09, CPacketClanLoad.class);

		/** 0x1X: Account **/
		register((byte) 0x10, CPacketAccountRequest.class);
		register((byte) 0x11, CPacketAccountLoad.class);
		register((byte) 0x12, CPacketAccountConfiguration.class);

		/** 0x2X: Change Profile **/
		register((byte) 0x20, CPacketChangeLanguage.class);
		register((byte) 0x21, CPacketChangeAccount.class);
		register((byte) 0x22, CPacketChangeTag.class);
		register((byte) 0x23, CPacketChangeLiga.class);
		register((byte) 0x24, CPacketUpdateGameStatus.class);
		register((byte) 0x25, CPacketUpdateProfile.class);
		register((byte) 0x26, CPacketUpdateClan.class);

		/** 0x4X: Friends **/
		register((byte) 0x40, CPacketAddFriend.class);
		register((byte) 0x41, CPacketRemoveFriend.class);
		register((byte) 0x42, CPacketAddFriendRequest.class);
		register((byte) 0x43, CPacketRemoveFriendRequest.class);
		register((byte) 0x44, CPacketAddBlockedPlayer.class);
		register((byte) 0x45, CPacketRemoveBlockedPlayer.class);

		/** 0x5X: Groups **/
		register((byte) 0x50, CPacketAddGroup.class);
		register((byte) 0x51, CPacketRemoveGroup.class);
		register((byte) 0x52, CPacketAddRank.class);
		register((byte) 0x53, CPacketRemoveRank.class);

		/** 0x6X: Party **/
		register((byte) 0x60, CPacketCreateParty.class);
		register((byte) 0x61, CPacketJoinParty.class);
		register((byte) 0x62, CPacketDisbandParty.class);

		register((byte) 0x65, CPacketPlayerJoinClan.class);
		register((byte) 0x66, CPacketPlayerLeaveClan.class);

	}

	private static void register(byte i, Class<? extends CommonPacket> c, short v) {
		i += Byte.MIN_VALUE;
		MAP_CLASS.put((byte) i, c);
		MAP_ID.put(c, (byte) i);
		MAP_VERSION.put((byte) i, v);
		MAP_VALID.put((byte) i, true);
	}

	private static void register(byte i, Class<? extends CommonPacket> c) {
		register(i, c, (short) 0);
	}

	public static CommonPacket get(byte i) {
		try {
			return (CommonPacket) MAP_CLASS.get(i).newInstance();
		} catch (Exception e) {
			System.out.println("ERRO NA CLASSE: " + MAP_CLASS.get(i).getSimpleName());
			return null;
		}
	}

	public static short getVersion(byte i) {
		if (!MAP_VERSION.containsKey(i))
			return 0;
		return MAP_VERSION.get(i).shortValue();
	}

	public static boolean getValid(byte i) {
		return MAP_VALID.get(i).booleanValue();
	}

	public static void setValid(byte i, boolean v) {
		MAP_VALID.put(i, v);
	}

	public final Byte id() {
		return MAP_ID.get(getClass());
	}

	public abstract void read(DataInputStream in) throws IOException;

	public abstract void write(DataOutputStream out) throws IOException;

	public abstract void handle(CommonHandler handler) throws HandlePacketException;
}
