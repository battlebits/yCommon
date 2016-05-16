package br.com.battlebits.ycommon.bungee.loadbalancer;

public interface LoadBalancer<T extends LoadBalancerObject> {

	public T next();

}
