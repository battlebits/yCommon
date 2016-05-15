package br.com.battlebits.ycommon.bungee.commands.register;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.Command;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.commands.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterCommand extends CommandClass {

	@Command(name = "broadcast", aliases = { "bc", "alert", "tweet" }, groupToUse = Group.STREAMER, noPermMessageId = "command-broadcast-no-access")
	public void tweet(CommandArgs cmdArgs) {
		Language language = BattlebitsAPI.getDefaultLanguage();
		if (cmdArgs.isPlayer()) {
			language = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getLanguage();
		}
		String[] args = cmdArgs.getArgs();
		if(args.length <= 0) {
			cmdArgs.getSender().sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(language, "command-broadcast-usage").replace("%command%", cmdArgs.getLabel())));
			return;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			String espaco = " ";
			if (i >= args.length - 1)
				espaco = "";
			builder.append(args[i] + espaco);
		}
		BungeeMain.getPlugin().getProxy().broadcast(TextComponent.fromLegacyText(""));
		BungeeMain.getPlugin().getProxy().broadcast(TextComponent.fromLegacyText(Translate.getTranslation(language, "broadcast") + " " + ChatColor.WHITE + builder.toString()));
		BungeeMain.getPlugin().getProxy().broadcast(TextComponent.fromLegacyText(""));
		final Language lang = language;
		BungeeMain.getPlugin().getProxy().getScheduler().runAsync(BungeeMain.getPlugin(), new Runnable() {
			public void run() {
				String consumerKey = "Y2lYtwoymGslYCVakGK1M0nvv";
				String consumerSecret = "OcjNWd1wzN1AVEGjKAhd1e88otlfB5DJ60FNINSEuzmFIXzGlc";
				String accessToken = "2972745022-92JuPoZ7dkHzc0YMDJusC17gcRO8qjyjeTwa6CW";
				String accessSecret = "iLGnslOn8Lh9sQY28l4uIsnLCceRRAYZzcPy03AzRJ3Ji";
				ConfigurationBuilder cb = new ConfigurationBuilder();
				cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret).setOAuthAccessToken(accessToken).setOAuthAccessTokenSecret(accessSecret);
				Twitter twitter = new TwitterFactory(cb.build()).getInstance();
				try {
					twitter.updateStatus(builder.toString());
					cmdArgs.getSender().sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(lang, "tweet-success").replace("%message%", builder.toString())));
				} catch (TwitterException e) {
					e.printStackTrace();
				}
			}
		});

	}
}
