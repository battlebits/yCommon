package br.com.battlebits.ycommon.bukkit.util.gson;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;

import br.com.battlebits.ycommon.common.utils.gson.TypeInterface;

public class TypeBukkit<T> extends TypeInterface<T>{

	@Override
	public Type getType() {
		return new TypeToken<T>() {
		}.getType();
	}

}
