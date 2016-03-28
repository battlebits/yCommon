package br.com.battlebits.ycommon.common.utils.gson;

import java.lang.reflect.Type;

public abstract class GsonInterface {

	public abstract <T> T fromJson(String json, Class<?> type);
	
	public abstract <T> T fromJson(String json, Type type);
	
	public abstract String toJson(Object obj);
}