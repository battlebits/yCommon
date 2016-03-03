package br.com.battlebits.ycommon.common.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

import br.com.battlebits.ycommon.common.networking.packets.CPacketAccountLoad;

public abstract class CommonPacket {
	private static final Map<Byte, Class<?>> MAP_CLASS = new HashMap<Byte, Class<?>>();
	private static final Map<Class<?>, Byte> MAP_ID = new HashMap<Class<?>, Byte>();
	private static final Map<Byte, Short> MAP_VERSION = new HashMap<Byte, Short>();
	private static final Map<Byte, Boolean> MAP_VALID = new HashMap<Byte, Boolean>();

	static {
		/** 0x0X: Protocol **/
		register((byte) 0x00, CPacketAccountLoad.class);
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

	public abstract void handle(CommonHandler handler);
}
