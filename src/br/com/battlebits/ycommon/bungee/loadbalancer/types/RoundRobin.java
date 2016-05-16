package br.com.battlebits.ycommon.bungee.loadbalancer.types;

import br.com.battlebits.ycommon.bungee.loadbalancer.BaseBalancer;
import br.com.battlebits.ycommon.bungee.loadbalancer.elements.LoadBalancerObject;

public class RoundRobin<T extends LoadBalancerObject> extends BaseBalancer<T> {

	private int next = 0;

	@Override
	public T next() {
		T obj = null;
		if (nextObj != null)
			if (!nextObj.isEmpty()) {
				while (next < nextObj.size()) {
					obj = nextObj.get(next);
					++next;
					if (obj == null)
						continue;
					if (!obj.canBeSelected()) {
						obj = null;
						continue;
					}
					break;
				}
			}
		if (next + 1 >= nextObj.size())
			next = 0;
		return obj;
	}

}
