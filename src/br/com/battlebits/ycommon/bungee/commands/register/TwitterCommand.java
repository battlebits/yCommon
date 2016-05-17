package br.com.battlebits.ycommon.bungee.commands.register;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.Command;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import br.com.battlebits.ycommon.common.twitter.TweetUtils;
import br.com.battlebits.ycommon.common.twitter.TwitterAccount;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class TwitterCommand extends CommandClass {

	@Command(name = "broadcast", aliases = { "bc", "alert", "tweet" }, groupToUse = Group.STREAMER, noPermMessageId = "command-broadcast-no-access")
	public void tweet(CommandArgs cmdArgs) {
		Language language = BattlebitsAPI.getDefaultLanguage();
		if (cmdArgs.isPlayer()) {
			language = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getLanguage();
		}
		String[] args = cmdArgs.getArgs();
		if (args.length <= 0) {
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
				if (TweetUtils.tweet(TwitterAccount.BATTLEBITSMC, builder.toString())) {
					cmdArgs.getSender().sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(lang, "tweet-success").replace("%message%", builder.toString())));
				}
			}
		});

	}
}
