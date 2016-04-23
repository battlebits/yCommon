package br.com.battlebits.ycommon.bungee.managers;

import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.banmanager.constructors.Ban;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import br.com.battlebits.ycommon.common.utils.DateUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		for(ProxiedPlayer proxiedP : BungeeCord.getInstance().getPlayers()) {
			BattlePlayer pl = BattlebitsAPI.getAccountCommon().getBattlePlayer(proxiedP.getUniqueId());
			if(pl.hasGroupPermission(Group.TRIAL)) {
				String banSuccess = Translate.getTranslation(pl.getLanguage(), "ban-success");
				banSuccess = banSuccess.replace("%player%", player.getUserName());
				banSuccess = banSuccess.replace("%banned-By%", ban.getBannedBy());
				banSuccess = banSuccess.replace("%reason%", ban.getReason());
				proxiedP.sendMessage(TextComponent.fromLegacyText(banSuccess));
			}
		}
	}

	public Ban getIpBan(InetSocketAddress address) {
		return banCache.asMap().get(address);
	}

	public static String getBanKickMessage(Ban ban, Language lang) {
		String reason = "";
		if (ban.isPermanent()) {
			reason = Translate.getTranslation(lang, "banned-permanent");
		} else {
			reason = Translate.getTranslation(lang, "banned-temp");
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(ban.getBanTime());
		reason = reason.replace("%day%", calendar.getTime().toString());
		reason = reason.replace("%banned-By%", ban.getBannedBy());
		reason = reason.replace("%reason%", ban.getReason());
		reason = reason.replace("%duration%", DateUtils.formatDifference(lang, (ban.getDuration() - System.currentTimeMillis()) / 1000));
		reason = reason.replace("%forum%", BattlebitsAPI.FORUM_WEBSITE);
		reason = reason.replace("%store%", BattlebitsAPI.STORE);
		return reason;
	}
}
