package br.com.battlebits.ycommon.bungee.utils.gson;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;

import br.com.battlebits.ycommon.common.utils.gson.TypeInterface;

public class TypeBungee<T> extends TypeInterface<T>{

	@Override
	public Type getType() {
		return new TypeToken<T>() {
		}.getType();
	}
}
