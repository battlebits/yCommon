package br.com.battlebits.ycommon.bukkit.permissions.injector;

/**
 * Este codigo nao pertence ao autor do plugin.
 * Este codigo pertence ao criador do PermissionEX
 * 
 */
import java.lang.reflect.Field;

public class FieldReplacer<Instance, Type> {
	private final Class<Type> requiredType;
	private final Field field;

	public FieldReplacer(Class<? extends Instance> clazz, String fieldName, Class<Type> requiredType) {
		this.requiredType = requiredType;
		this.field = getField(clazz, fieldName);
		if (this.field == null) {
			throw new ExceptionInInitializerError("No such field " + fieldName + " in class " + clazz);
		}
		this.field.setAccessible(true);
		if (!requiredType.isAssignableFrom(this.field.getType())) {
			throw new ExceptionInInitializerError("Field of wrong type");
		}
	}

	public Type get(Instance instance) {
		try {
			return this.requiredType.cast(this.field.get(instance));
		} catch (IllegalAccessException e) {
			throw new Error(e);
		}
	}

	public void set(Instance instance, Type newValue) {
		try {
			this.field.set(instance, newValue);
		} catch (IllegalAccessException e) {
			throw new Error(e);
		}
	}

	private static Field getField(Class<?> clazz, String fieldName) {
		while ((clazz != null) && (clazz != Object.class)) {
			try {
				return clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
		}
		return null;
	}
}
