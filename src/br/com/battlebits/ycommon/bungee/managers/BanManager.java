package br.com.battlebits.ycommon.bungee.managers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.report.ReportManager;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.banmanager.constructors.Ban;
import br.com.battlebits.ycommon.common.banmanager.constructors.Mute;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.time.TimeZone;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import br.com.battlebits.ycommon.common.twitter.TweetUtils;
import br.com.battlebits.ycommon.common.twitter.TwitterAccount;
import br.com.battlebits.ycommon.common.utils.DateUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BanManager {
	private Cache<String, Entry<UUID, Ban>> banCache;

	public BanManager() {
		banCache = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.MINUTES).build(new CacheLoader<String, Entry<UUID, Ban>>() {
			@Override
			public Entry<UUID, Ban> load(String name) throws Exception {
				return null;
			}
		});
	}

	public void ban(BattlePlayer player, Ban ban) {
		player.getBanHistory().getBanHistory().add(ban);
		for (ProxiedPlayer proxiedP : BungeeCord.getInstance().getPlayers()) {
			BattlePlayer pl = BattlebitsAPI.getAccountCommon().getBattlePlayer(proxiedP.getUniqueId());
			if (pl.hasGroupPermission(Group.TRIAL)) {
				String banSuccess = "";
				if (ban.isPermanent()) {
					banSuccess = Translate.getTranslation(pl.getLanguage(), "command-ban-prefix") + " " + Translate.getTranslation(pl.getLanguage(), "command-ban-success");
				} else {
					banSuccess = Translate.getTranslation(pl.getLanguage(), "command-tempban-prefix") + " " + Translate.getTranslation(pl.getLanguage(), "command-tempban-success");
				}
				banSuccess = banSuccess.replace("%player%", player.getUserName() + "(" + player.getUuid().toString().replace("-", "") + ")");
				banSuccess = banSuccess.replace("%banned-By%", ban.getBannedBy());
				banSuccess = banSuccess.replace("%reason%", ban.getReason());
				banSuccess = banSuccess.replace("%duration%", DateUtils.formatDifference(pl.getLanguage(), ban.getDuration() / 1000));
				proxiedP.sendMessage(TextComponent.fromLegacyText(banSuccess));
			}
		}
		if (ban.isPermanent()) {
			ReportManager.banPlayer(player);
			TweetUtils.tweet(TwitterAccount.BATTLEBANS, "Jogador banido: " + player.getUserName() + "\nBanido por: " + ban.getBannedBy() + "\nMotivo: " + ban.getReason() + "\n\nServidor: " + player.getServerConnected());
		}
		if (!player.isOnline())
			BattlebitsAPI.getAccountCommon().saveBattlePlayer(player);
		ProxiedPlayer pPlayer = BungeeMain.getPlugin().getProxy().getPlayer(player.getUuid());
		if (pPlayer != null) {
			if (ban.isPermanent()) {
				if (player.getIpAddress() != null)
					banCache.put(player.getIpAddress().getHostString(), new AbstractMap.SimpleEntry<UUID, Ban>(player.getUuid(), ban));
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("Ban");
				if (pPlayer.getServer() != null)
					pPlayer.getServer().sendData(BattlebitsAPI.getBungeeChannel(), out.toByteArray());
			}
			pPlayer.disconnect(getBanKickMessageBase(ban, player.getLanguage(), player.getTimeZone()));
		}
	}

	public void unban(BattlePlayer bannedByPlayer, BattlePlayer player, Ban ban) {
		if (bannedByPlayer != null)
			ban.unban(bannedByPlayer);
		else
			ban.unban();
		for (ProxiedPlayer proxiedP : BungeeCord.getInstance().getPlayers()) {
			BattlePlayer pl = BattlebitsAPI.getAccountCommon().getBattlePlayer(proxiedP.getUniqueId());
			if (pl.hasGroupPermission(Group.TRIAL)) {
				String unbanSuccess = Translate.getTranslation(pl.getLanguage(), "command-unban-prefix") + " " + Translate.getTranslation(pl.getLanguage(), "command-unban-success");
				unbanSuccess = unbanSuccess.replace("%player%", player.getUserName() + "(" + player.getUuid().toString().replace("-", "") + ")");
				unbanSuccess = unbanSuccess.replace("%unbannedBy%", ban.getUnbannedBy());
				proxiedP.sendMessage(TextComponent.fromLegacyText(unbanSuccess));
			}
		}
		BattlebitsAPI.getAccountCommon().saveBattlePlayer(player);
	}

	public void mute(BattlePlayer player, Mute mute) {
		player.getBanHistory().getMuteHistory().add(mute);
		for (ProxiedPlayer proxiedP : BungeeCord.getInstance().getPlayers()) {
			BattlePlayer pl = BattlebitsAPI.getAccountCommon().getBattlePlayer(proxiedP.getUniqueId());
			if (pl.hasGroupPermission(Group.HELPER)) {
				String banSuccess = "";
				if (mute.isPermanent()) {
					banSuccess = Translate.getTranslation(pl.getLanguage(), "command-mute-prefix") + " " + Translate.getTranslation(pl.getLanguage(), "command-mute-success");
				} else {
					banSuccess = Translate.getTranslation(pl.getLanguage(), "command-tempmute-prefix") + " " + Translate.getTranslation(pl.getLanguage(), "command-tempmute-success");
				}
				banSuccess = banSuccess.replace("%player%", player.getUserName() + "(" + player.getUuid().toString().replace("-", "") + ")");
				banSuccess = banSuccess.replace("%muted-By%", mute.getMutedBy());
				banSuccess = banSuccess.replace("%reason%", mute.getReason());
				banSuccess = banSuccess.replace("%duration%", DateUtils.formatDifference(pl.getLanguage(), mute.getDuration() / 1000));
				proxiedP.sendMessage(TextComponent.fromLegacyText(banSuccess));
			}
		}
		if (mute.isPermanent())
			ReportManager.mutePlayer(player);
		if (!player.isOnline())
			BattlebitsAPI.getAccountCommon().saveBattlePlayer(player);
		ProxiedPlayer pPlayer = BungeeMain.getPlugin().getProxy().getPlayer(player.getUuid());
		if (pPlayer != null) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Mute");
			out.writeUTF(BattlebitsAPI.getGson().toJson(mute));
			if (pPlayer.getServer() != null)
				pPlayer.getServer().sendData(BattlebitsAPI.getBungeeChannel(), out.toByteArray());
		}
	}

	public void unmute(BattlePlayer mutedByPlayer, BattlePlayer player, Mute mute) {
		if (mutedByPlayer != null)
			mute.unmute(mutedByPlayer);
		else
			mute.unmute();
		for (ProxiedPlayer proxiedP : BungeeCord.getInstance().getPlayers()) {
			BattlePlayer pl = BattlebitsAPI.getAccountCommon().getBattlePlayer(proxiedP.getUniqueId());
			if (pl.hasGroupPermission(Group.HELPER)) {
				String unbanSuccess = Translate.getTranslation(pl.getLanguage(), "command-unmute-prefix") + " " + Translate.getTranslation(pl.getLanguage(), "command-unmute-success");
				unbanSuccess = unbanSuccess.replace("%player%", player.getUserName() + "(" + player.getUuid().toString().replace("-", "") + ")");
				unbanSuccess = unbanSuccess.replace("%unmutedBy%", mute.getUnmutedBy());
				proxiedP.sendMessage(TextComponent.fromLegacyText(unbanSuccess));
			}
		}
		if (!player.isOnline())
			BattlebitsAPI.getAccountCommon().saveBattlePlayer(player);
		ProxiedPlayer pPlayer = BungeeMain.getPlugin().getProxy().getPlayer(player.getUuid());
		if (pPlayer != null) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			if (mutedByPlayer != null) {
				out.writeUTF("Unmute");
				out.writeUTF(mutedByPlayer.getUuid().toString());
				out.writeUTF(mutedByPlayer.getUserName());
			} else {
				out.writeUTF("UnmuteConsole");
			}
			if (pPlayer.getServer() != null)
				pPlayer.getServer().sendData(BattlebitsAPI.getBungeeChannel(), out.toByteArray());
		}
	}

	public Entry<UUID, Ban> getIpBan(String address) {
		return banCache.asMap().get(address);
	}

	public static String getBanKickMessage(Ban ban, Language lang, TimeZone zone) {
		String reason = "";
		if (ban.isPermanent()) {
			reason = Translate.getTranslation(lang, "banned-permanent");
		} else {
			reason = Translate.getTranslation(lang, "banned-temp");
		}
		Date date = new Date(ban.getBanTime());
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		reason = reason.replace("%day%", df.format(date));
		reason = reason.replace("%banned-By%", ban.getBannedBy());
		reason = reason.replace("%reason%", ban.getReason());
		reason = reason.replace("%duration%", DateUtils.formatDifference(lang, (ban.getExpire() - System.currentTimeMillis()) / 1000));
		reason = reason.replace("%forum%", BattlebitsAPI.FORUM_WEBSITE);
		reason = reason.replace("%store%", BattlebitsAPI.STORE);
		return reason;
	}

	public static String getMuteMessage(Mute mute, Language lang) {
		String message = "";
		if (mute.isPermanent()) {
			message = Translate.getTranslation(lang, "command-mute-prefix") + " " + Translate.getTranslation(lang, "command-mute-muted");
		} else {
			message = Translate.getTranslation(lang, "command-unmute-prefix") + " " + Translate.getTranslation(lang, "command-tempmute-muted");
		}
		message = message.replace("%duration%", DateUtils.formatDifference(lang, (mute.getExpire() - System.currentTimeMillis()) / 1000));
		message = message.replace("%forum%", BattlebitsAPI.FORUM_WEBSITE);
		message = message.replace("%store%", BattlebitsAPI.STORE);
		message = message.replace("%muted-By%", mute.getMutedBy());
		message = message.replace("%reason%", mute.getReason());
		return message;
	}

	public static BaseComponent[] getBanKickMessageBase(Ban ban, Language lang, TimeZone zone) {
		return TextComponent.fromLegacyText(getBanKickMessage(ban, lang, zone));
	}
}
