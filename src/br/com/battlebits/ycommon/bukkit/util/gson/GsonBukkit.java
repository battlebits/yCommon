package br.com.battlebits.ycommon.bukkit.util.gson;

import java.lang.reflect.Type;

import com.google.gson.Gson;

import br.com.battlebits.ycommon.common.utils.gson.GsonInterface;

public class GsonBukkit extends GsonInterface {
	private Gson gson;

	public GsonBukkit() {
		gson = new Gson();
	}
	
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
