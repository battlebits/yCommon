package br.com.battlebits.ycommon.bungee.managers;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.banmanager.constructors.Ban;

public class BanManager {
	private Cache<InetSocketAddress, Ban> banCache = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.MINUTES).build(new CacheLoader<InetSocketAddress, Ban>() {
		@Override
		public Ban load(InetSocketAddress name) throws Exception {
			return null;
		}
	});

	public void ban(BattlePlayer player, Ban ban) {
		player.getBanHistory().getBanHistory().add(ban);
		banCache.put(player.getIpAddress(), ban);
	}

	public Ban getIpBan(InetSocketAddress address) throws ExecutionException {
		return banCache.asMap().get(address);
	}
}
