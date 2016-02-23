package br.com.battlebits.yutils.common.utils.reflection;

import java.lang.reflect.Field;

public class ReflectionUtils {
	public static Object getPrivateFieldObject(Object classFrom, String fieldStr) {
		Object obj = null;
		try {
			Field field = classFrom.getClass().getDeclaredField(fieldStr);
			field.setAccessible(true);
			obj = field.get(classFrom);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static void setPrivateFieldObject(Object classFrom, String fieldStr, Object toPrivate) {
		try {
			Field field = classFrom.getClass().getDeclaredField(fieldStr);
			field.setAccessible(true);
			field.set(classFrom, toPrivate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
