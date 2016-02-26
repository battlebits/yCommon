package br.com.battlebits.ycommon.bukkit.permissions.injector.regexperms;
/**
 * Este codigo nao pertence ao autor do plugin.
 * Este codigo pertence ao criador do PermissionEX
 * 
 */
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import br.com.battlebits.ycommon.bukkit.permissions.PermissionManager;
import br.com.battlebits.ycommon.bukkit.permissions.injector.FieldReplacer;
import br.com.battlebits.ycommon.bukkit.permissions.injector.PermissionCheckResult;

@SuppressWarnings("rawtypes")
public class PermissiblePEX extends PermissibleBase {
	private static final FieldReplacer<PermissibleBase, Map> PERMISSIONS_FIELD = new FieldReplacer<>(PermissibleBase.class, "permissions", Map.class);
	private static final FieldReplacer<PermissibleBase, List> ATTACHMENTS_FIELD = new FieldReplacer<>(PermissibleBase.class, "attachments", List.class);
	private static final Method CALC_CHILD_PERMS_METH;

	static {
		try {
			CALC_CHILD_PERMS_METH = PermissibleBase.class.getDeclaredMethod("calculateChildPermissions", Map.class, boolean.class, PermissionAttachment.class);
		} catch (NoSuchMethodException e) {
			throw new ExceptionInInitializerError(e);
		}
		CALC_CHILD_PERMS_METH.setAccessible(true);
	}

	private final Map<String, PermissionAttachmentInfo> permissions;
	private final List<PermissionAttachment> attachments;
	private static final AtomicBoolean LAST_CALL_ERRORED = new AtomicBoolean(false);

	protected final Player player;
	protected final PermissionManager plugin;
	private Permissible previousPermissible = null;
	protected final Map<String, PermissionCheckResult> cache = new ConcurrentHashMap<>();
	private final Object permissionsLock = new Object();

	@SuppressWarnings("unchecked")
	public PermissiblePEX(Player player, PermissionManager plugin) {
		super(player);
		this.player = player;
		this.plugin = plugin;
		permissions = new LinkedHashMap<String, PermissionAttachmentInfo>() {

			private static final long serialVersionUID = 1L;

			@Override
			public PermissionAttachmentInfo put(String k, PermissionAttachmentInfo v) {
				PermissionAttachmentInfo existing = this.get(k);
				if (existing != null) {
					return existing;
				}
				return super.put(k, v);
			}
		};
		PERMISSIONS_FIELD.set(this, permissions);
		this.attachments = ATTACHMENTS_FIELD.get(this);
		recalculatePermissions();
	}

	public Permissible getPreviousPermissible() {
		return previousPermissible;
	}

	public void setPreviousPermissible(Permissible previousPermissible) {
		this.previousPermissible = previousPermissible;
	}

	@Override
	public boolean hasPermission(String permission) {
		PermissionCheckResult res = permissionValue(permission);
		switch (res) {
		case TRUE:
		case FALSE:
			return res.toBoolean();
		case UNDEFINED:
		default:
			if (super.isPermissionSet(permission)) {
				final boolean ret = super.hasPermission(permission);
				return ret;
			} else {
				Permission perm = player.getServer().getPluginManager().getPermission(permission);
				return perm == null ? Permission.DEFAULT_PERMISSION.getValue(player.isOp()) : perm.getDefault().getValue(player.isOp());
			}
		}
	}

	@Override
	public boolean hasPermission(Permission permission) {
		PermissionCheckResult res = permissionValue(permission.getName());
		switch (res) {
		case TRUE:
		case FALSE:
			return res.toBoolean();
		case UNDEFINED:
		default:
			if (super.isPermissionSet(permission.getName())) {
				final boolean ret = super.hasPermission(permission);
				return ret;
			} else {
				return permission.getDefault().getValue(player.isOp());
			}
		}
	}

	@Override
	public void recalculatePermissions() {
		if (cache != null && permissions != null && attachments != null) {
			synchronized (permissionsLock) {
				clearPermissions();
				cache.clear();
				for (ListIterator<PermissionAttachment> it = this.attachments.listIterator(this.attachments.size()); it.hasPrevious();) {
					PermissionAttachment attach = it.previous();
					calculateChildPerms(attach.getPermissions(), false, attach);
				}
				for (Permission p : player.getServer().getPluginManager().getDefaultPermissions(isOp())) {
					this.permissions.put(p.getName(), new PermissionAttachmentInfo(player, p.getName(), null, true));
					calculateChildPerms(p.getChildren(), false, null);
				}
			}
		}
	}

	protected void calculateChildPerms(Map<String, Boolean> children, boolean invert, PermissionAttachment attachment) {
		try {
			CALC_CHILD_PERMS_METH.invoke(this, children, invert, attachment);
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isPermissionSet(String permission) {
		return super.isPermissionSet(permission) || permissionValue(permission) != PermissionCheckResult.UNDEFINED;
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		synchronized (permissionsLock) {
			return new LinkedHashSet<>(permissions.values());
		}
	}

	private PermissionCheckResult checkSingle(String expression, String permission, boolean value) {
		if (plugin.getPermissionMatcher().isMatches(expression, permission)) {
			PermissionCheckResult res = PermissionCheckResult.fromBoolean(value);
			return res;
		}
		return PermissionCheckResult.UNDEFINED;
	}

	protected PermissionCheckResult permissionValue(String permission) {
		try {
			Validate.notNull(permission, "Permissions being checked must not be null!");
			permission = permission.toLowerCase();
			PermissionCheckResult res = cache.get(permission);
			if (res != null) {
				return res;
			}

			res = PermissionCheckResult.UNDEFINED;

			synchronized (permissionsLock) {
				for (PermissionAttachmentInfo pai : permissions.values()) {
					if ((res = checkSingle(pai.getPermission(), permission, pai.getValue())) != PermissionCheckResult.UNDEFINED) {
						break;
					}
				}
			}
			if (res == PermissionCheckResult.UNDEFINED) {
				for (Map.Entry<String, Boolean> ent : plugin.getRegexPerms().getPermissionList().getParents(permission)) {
					if ((res = permissionValue(ent.getKey())) != PermissionCheckResult.UNDEFINED) {
						res = PermissionCheckResult.fromBoolean(!(res.toBoolean() ^ ent.getValue()));
						break;
					}
				}
			}
			cache.put(permission, res);
			LAST_CALL_ERRORED.set(false);
			return res;
		} catch (Throwable t) {
			if (LAST_CALL_ERRORED.compareAndSet(false, true)) {
				t.printStackTrace();
			}
			return PermissionCheckResult.UNDEFINED;
		}
	}
}