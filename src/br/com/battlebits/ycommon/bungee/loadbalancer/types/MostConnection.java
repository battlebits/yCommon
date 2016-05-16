package br.com.battlebits.ycommon.bungee.loadbalancer.types;

import br.com.battlebits.ycommon.bungee.loadbalancer.BaseBalancer;
import br.com.battlebits.ycommon.bungee.loadbalancer.elements.LoadBalancerObject;
import br.com.battlebits.ycommon.bungee.loadbalancer.elements.NumberConnection;

public class MostConnection<T extends LoadBalancerObject & NumberConnection> extends BaseBalancer<T> {

	@Override
	public T next() {
		T obj = null;
		if (nextObj != null)
			if (!nextObj.isEmpty())
				for (T item : nextObj) {
					if (!item.canBeSelected())
						continue;
					if (obj == null) {
						obj = item;
						continue;
					}
					if (obj.getActualNumber() < item.getActualNumber())
						obj = item;
				}
		return obj;
	}
}
