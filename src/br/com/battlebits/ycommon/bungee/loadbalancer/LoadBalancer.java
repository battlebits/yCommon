package br.com.battlebits.ycommon.bungee.loadbalancer;

import br.com.battlebits.ycommon.bungee.loadbalancer.elements.LoadBalancerObject;

public interface LoadBalancer<T extends LoadBalancerObject> {

	public T next();

}
