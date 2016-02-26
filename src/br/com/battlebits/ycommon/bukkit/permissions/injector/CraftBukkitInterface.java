package br.com.battlebits.ycommon.bukkit.permissions.injector;

/**
 * Este codigo nao pertence ao autor do plugin.
 * Este codigo pertence ao criador do PermissionEX
 * 
 */
import org.bukkit.Bukkit;

public class CraftBukkitInterface {
	private static final String VERSION;

	static {
		Class<?> serverClass = Bukkit.getServer().getClass();
		if (!serverClass.getSimpleName().equals("CraftServer")) {
			VERSION = null;
		} else if (serverClass.getName().equals("org.bukkit.craftbukkit.CraftServer")) {
			VERSION = ".";
		} else {
			String name = serverClass.getName();
			name = name.substring("org.bukkit.craftbukkit".length());
			name = name.substring(0, name.length() - "CraftServer".length());
			VERSION = name;
		}
	}

	public static String getCBClassName(String simpleName) {
		if (VERSION == null) {
			return null;
		}
		return "org.bukkit.craftbukkit" + VERSION + simpleName;
	}

	public static Class<?> getCBClass(String name) {
		if (VERSION == null) {
			return null;
		}
		try {
			return Class.forName(getCBClassName(name));
		} catch (ClassNotFoundException e) {
		}
		return null;
	}
}
