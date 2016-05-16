package br.com.battlebits.ycommon.bungee.loadbalancer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseBalancer<T extends LoadBalancerObject> implements LoadBalancer<T> {

	private Map<String, T> objects;
	protected List<T> nextObj;

	public BaseBalancer() {
		objects = new HashMap<>();
	}

	public BaseBalancer(Map<String, T> map) {
		addAll(map);
	}

	public void add(String id, T obj) {
		objects.put(id, obj);
		update();
	}

	public T get(String id) {
		return objects.get(id);
	}

	public void remove(String id) {
		objects.remove(id);
		update();
	}

	public void addAll(Map<String, T> map) {
		if (objects != null)
			objects.clear();
		objects = map;
		update();
	}

	public void update() {
		if (nextObj != null)
			nextObj.clear();
		nextObj = new ArrayList<>();
		nextObj.addAll(objects.values());
	}
}
