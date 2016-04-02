package br.com.battlebits.ycommon.bukkit.util.json;

import java.lang.reflect.Type;
import java.util.HashMap;

import org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken;

import br.com.battlebits.ycommon.common.utils.json.TypeUtils;

public class TypeBukkitUtils extends TypeUtils {

	@Override
	public Type getTranslateMap() {
		return new TypeToken<HashMap<String, String>>() {
		}.getType();
	}

}
