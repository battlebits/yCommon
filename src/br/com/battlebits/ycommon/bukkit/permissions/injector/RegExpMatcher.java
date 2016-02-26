package br.com.battlebits.ycommon.bukkit.permissions.injector;

/**
 * Este codigo nao pertence ao autor do plugin.
 * Este codigo pertence ao criador do PermissionEX
 * 
 */
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import br.com.battlebits.ycommon.bukkit.permissions.injector.loaders.LoaderNetUtil;
import br.com.battlebits.ycommon.bukkit.permissions.injector.loaders.LoaderNormal;

public class RegExpMatcher implements PermissionMatcher {
	public static final String RAW_REGEX_CHAR = "$";
	protected static Pattern rangeExpression = Pattern.compile("(\\d+)-(\\d+)");
	private Object patternCache;

	public RegExpMatcher() {
		Class<?> cacheBuilder = getClassGuava("com.google.common.cache.CacheBuilder");
		Class<?> cacheLoader = getClassGuava("com.google.common.cache.CacheLoader");
		try {
			Object obj = cacheBuilder.getMethod("newBuilder").invoke(null);
			Method maximumSize = obj.getClass().getMethod("maximumSize", long.class);
			Object obj2 = maximumSize.invoke(obj, 500);
			Object loader = null;
			if (hasNetUtil())
				loader = new LoaderNetUtil();
			else
				loader = new LoaderNormal();
			Method build = obj2.getClass().getMethod("build", cacheLoader);
			patternCache = build.invoke(obj2, loader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isMatches(String expression, String permission) {
		try {
			Method get = patternCache.getClass().getMethod("get", Object.class);
			get.setAccessible(true);
			Object obj = get.invoke(patternCache, expression);
			return ((Pattern) obj).matcher(permission).matches();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return false;
	}

	private Class<?> getClassGuava(String str) {
		Class<?> clasee = null;
		try {
			if (hasNetUtil()) {
				str = "net.minecraft.util." + str;
			}
			clasee = Class.forName(str);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clasee;
	}

	@SuppressWarnings("static-method")
	private boolean hasNetUtil() {
		try {
			Class.forName("net.minecraft.util.com.google.common.cache.LoadingCache");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}