package br.com.battlebits.ycommon.bukkit.permissions.injector.loaders;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.minecraft.util.com.google.common.cache.CacheLoader;

public class LoaderNetUtil extends CacheLoader<String, Pattern> {
	public static final String RAW_REGEX_CHAR = "$";

	@Override
	public Pattern load(String arg0) throws Exception {
		return createPattern(arg0);
	}

	protected static Pattern createPattern(String expression) {
		try {
			return Pattern.compile(prepareRegexp(expression), Pattern.CASE_INSENSITIVE);
		} catch (PatternSyntaxException e) {
			return Pattern.compile(Pattern.quote(expression), Pattern.CASE_INSENSITIVE);
		}
	}

	public static String prepareRegexp(String expression) {
		if (expression.startsWith("-")) {
			expression = expression.substring(1);
		}
		if (expression.startsWith("#")) {
			expression = expression.substring(1);
		}
		boolean rawRegexp = expression.startsWith(RAW_REGEX_CHAR);
		if (rawRegexp) {
			expression = expression.substring(1);
		}
		String regexp = rawRegexp ? expression : expression.replace(".", "\\.").replace("*", "(.*)");
		return regexp;
	}
}
