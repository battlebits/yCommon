package br.com.battlebits.ycommon.bukkit.permissions.injector.regexperms;

/**
 * Este codigo nao pertence ao autor do plugin.
 * Este codigo pertence ao criador do PermissionEX
 * 
 */
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Logger;

public abstract class PermissibleInjector {
	protected final String clazzName, fieldName;
	protected final boolean copyValues;

	public PermissibleInjector(String clazzName, String fieldName, boolean copyValues) {
		this.clazzName = clazzName;
		this.fieldName = fieldName;
		this.copyValues = copyValues;
	}

	public Permissible inject(Player player, Permissible permissible) throws NoSuchFieldException, IllegalAccessException {
		Field permField = getPermissibleField(player);
		if (permField == null) {
			return null;
		}
		Permissible oldPerm = (Permissible) permField.get(player);
		if (copyValues && permissible instanceof PermissibleBase) {
			PermissibleBase newBase = (PermissibleBase) permissible;
			PermissibleBase oldBase = (PermissibleBase) oldPerm;
			copyValues(oldBase, newBase);
		}
		permField.set(player, permissible);
		return oldPerm;
	}

	public Permissible getPermissible(Player player) throws NoSuchFieldException, IllegalAccessException {
		return (Permissible) getPermissibleField(player).get(player);
	}

	private Field getPermissibleField(Player player) throws NoSuchFieldException {
		Class<?> humanEntity;
		try {
			humanEntity = Class.forName(clazzName);
		} catch (ClassNotFoundException e) {
			Logger.getLogger("yUtils").warning("[yUtils] Instance nao encontrada");
			return null;
		}

		if (!humanEntity.isAssignableFrom(player.getClass())) {
			Logger.getLogger("yUtils").warning("[yUtils] Erro estranho enquanto injeta permissivel");
			return null;
		}

		Field permField = humanEntity.getDeclaredField(fieldName);
		permField.setAccessible(true);
		return permField;
	}

	@SuppressWarnings({ "unchecked", "static-method" })
	private void copyValues(PermissibleBase old, PermissibleBase newPerm) throws NoSuchFieldException, IllegalAccessException {
		// Attachments
		Field attachmentField = PermissibleBase.class.getDeclaredField("attachments");
		attachmentField.setAccessible(true);
		List<Object> attachmentPerms = (List<Object>) attachmentField.get(newPerm);
		attachmentPerms.clear();
		attachmentPerms.addAll((List<Object>) attachmentField.get(old));
		newPerm.recalculatePermissions();
	}

	public abstract boolean isApplicable(Player player);

	public static class ServerNamePermissibleInjector extends PermissibleInjector {
		protected final String serverName;

		public ServerNamePermissibleInjector(String clazz, String field, boolean copyValues, String serverName) {
			super(clazz, field, copyValues);
			this.serverName = serverName;
		}

		@Override
		public boolean isApplicable(Player player) {
			return player.getServer().getName().equalsIgnoreCase(serverName);
		}
	}

	public static class ClassPresencePermissibleInjector extends PermissibleInjector {

		public ClassPresencePermissibleInjector(String clazzName, String fieldName, boolean copyValues) {
			super(clazzName, fieldName, copyValues);
		}

		@Override
		public boolean isApplicable(Player player) {
			try {
				return Class.forName(clazzName).isInstance(player);
			} catch (ClassNotFoundException e) {
				return false;
			}
		}
	}

	public static class ClassNameRegexPermissibleInjector extends PermissibleInjector {
		private final String regex;

		public ClassNameRegexPermissibleInjector(String clazz, String field, boolean copyValues, String regex) {
			super(clazz, field, copyValues);
			this.regex = regex;
		}

		@Override
		public boolean isApplicable(Player player) {
			return player.getClass().getName().matches(regex);
		}
	}
}