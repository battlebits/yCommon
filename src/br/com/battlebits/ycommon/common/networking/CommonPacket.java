package br.com.battlebits.ycommon.common.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

import br.com.battlebits.ycommon.common.networking.packets.CPacketAccountLoad;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAccountRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAddBlockedPlayer;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAddFriend;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAddFriendRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAddGroup;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAddRank;
import br.com.battlebits.ycommon.common.networking.packets.CPacketBanPlayer;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeFichas;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeLanguage;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeLiga;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeMoney;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeXp;
import br.com.battlebits.ycommon.common.networking.packets.CPacketCreateParty;
import br.com.battlebits.ycommon.common.networking.packets.CPacketDisbandParty;
import br.com.battlebits.ycommon.common.networking.packets.CPacketJoinParty;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveBlockedPlayer;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveFriend;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveFriendRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveGroup;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveRank;
import br.com.battlebits.ycommon.common.networking.packets.CPacketTranslationsLoad;
import br.com.battlebits.ycommon.common.networking.packets.CPacketTranslationsRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUnbanPlayer;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateClan;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateGameStatus;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateProfile;

public abstract class CommonPacket {
	private static final Map<Byte, Class<?>> MAP_CLASS = new HashMap<Byte, Class<?>>();
	private static final Map<Class<?>, Byte> MAP_ID = new HashMap<Class<?>, Byte>();
	private static final Map<Byte, Short> MAP_VERSION = new HashMap<Byte, Short>();
	private static final Map<Byte, Boolean> MAP_VALID = new HashMap<Byte, Boolean>();

	static {
		/** 0x0X: Account **/
		register((byte) 0x00, CPacketAccountRequest.class);
		register((byte) 0x01, CPacketAccountLoad.class);

		/** 0x1X: Translations **/
		register((byte) 0x10, CPacketTranslationsRequest.class);
		register((byte) 0x11, CPacketTranslationsLoad.class);

		/** 0x2X: Change Profile **/
		register((byte) 0x20, CPacketChangeLanguage.class);
		register((byte) 0x21, CPacketChangeXp.class);
		register((byte) 0x22, CPacketChangeFichas.class);
		register((byte) 0x23, CPacketChangeLiga.class);
		register((byte) 0x24, CPacketChangeMoney.class);

		/** 0x3X: Update **/
		register((byte) 0x31, CPacketUpdateGameStatus.class);
		register((byte) 0x32, CPacketUpdateProfile.class);
		register((byte) 0x33, CPacketUpdateClan.class);
		
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

		/** 0x7X: Bans **/
		register((byte) 0x70, CPacketBanPlayer.class);
		register((byte) 0x71, CPacketUnbanPlayer.class);
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
			System.out.println(MAP_CLASS.get(i).getSimpleName());
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

	public final byte id() {
		return MAP_ID.get(getClass());
	}

	public abstract void read(DataInputStream in) throws Exception;

	public abstract void write(DataOutputStream out) throws Exception;

	public abstract void handle(CommonHandler handler) throws Exception;
}