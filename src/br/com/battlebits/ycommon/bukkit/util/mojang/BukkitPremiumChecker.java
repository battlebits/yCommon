package br.com.battlebits.ycommon.bukkit.util.mojang;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import br.com.battlebits.ycommon.common.utils.mojang.PremiumChecker;
import net.minecraft.util.com.google.common.cache.Cache;
import net.minecraft.util.com.google.common.cache.CacheBuilder;
import net.minecraft.util.com.google.common.cache.CacheLoader;

public class BukkitPremiumChecker extends PremiumChecker {

	private Cache<String, Boolean> isPremium = CacheBuilder.newBuilder().expireAfterWrite(1L, TimeUnit.DAYS)
			.build(new CacheLoader<String, Boolean>() {
				@Override
				public Boolean load(String name) throws Exception {
					return checkInMojang(name);
				}
			});

	@Override
	public boolean isPremium(String nickname) {
		try {
			return isPremium.get(nickname, new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return checkInMojang(nickname);
				}
			});
		} catch (Exception e) {
			return true;
		}
	}

}
