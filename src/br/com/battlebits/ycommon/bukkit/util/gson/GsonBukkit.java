package br.com.battlebits.ycommon.bukkit.util.gson;

import java.lang.reflect.Type;

import br.com.battlebits.ycommon.common.utils.gson.GsonInterface;
import net.minecraft.util.com.google.gson.Gson;

public class GsonBukkit extends GsonInterface {
	private static Gson gson = new Gson();

	@Override
	public <T> T fromJson(String json, Class<?> type) {
		return gson.fromJson(json, type);
	}
	
	@Override
	public <T> T fromJson(String json, Type type) {
		return gson.fromJson(json, type);
	}

	@Override
	public String toJson(Object obj) {
		return gson.toJson(obj);
	}

}
