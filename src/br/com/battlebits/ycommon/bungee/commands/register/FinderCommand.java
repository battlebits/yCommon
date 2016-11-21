package br.com.battlebits.ycommon.bungee.commands.register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.Command;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.tag.Tag;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FinderCommand extends CommandClass {

	@Command(name = "finder", usage = "/<command> [player]", groupToUse = Group.MOD, noPermMessageId = "command-finder-no-access")
	public void finder(CommandArgs cmdArgs) {
		final CommandSender sender = cmdArgs.getSender();
		final String[] args = cmdArgs.getArgs();
		Language lang = BattlebitsAPI.getDefaultLanguage();
		if (cmdArgs.isPlayer()) {
			lang = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getLanguage();
		}
		final Language language = lang;
		final String finderPrefix = Translate.getTranslation(lang, "command-finder-prefix") + " ";
		if (args.length != 1) {
			sender.sendMessage(TextComponent.fromLegacyText(finderPrefix + Translate.getTranslation(lang, "command-finder-usage").replace("%command%", cmdArgs.getLabel())));
			return;
		}
		BungeeCord.getInstance().getScheduler().runAsync(BungeeMain.getPlugin(), new Runnable() {
			public void run() {
				UUID uuid = BattlebitsAPI.getUUIDOf(args[0]);
				if (uuid == null) {
					sender.sendMessage(TextComponent.fromLegacyText(finderPrefix + Translate.getTranslation(language, "player-not-exist")));
					return;
				}
				ProxiedPlayer proxied = BungeeCord.getInstance().getPlayer(uuid);
				if (proxied == null) {
					sender.sendMessage(TextComponent.fromLegacyText(finderPrefix + Translate.getTranslation(language, "player-not-found")));
					return;
				}
				BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
				if (player.getServerGroup().ordinal() >= Group.MANAGER.ordinal()) {
					sender.sendMessage(TextComponent.fromLegacyText(finderPrefix + Translate.getTranslation(language, "command-finder-player-not-allowed")));
					return;
				}
				String tag = Tag.valueOf(player.getServerGroup().toString()).getPrefix(language);
				String format = tag + (ChatColor.stripColor(tag).trim().length() > 0 ? " " : "") + player.getUserName();

				TextComponent playerMessage = new TextComponent(format);
				TextComponent space = new TextComponent(ChatColor.WHITE + " - ");
				TextComponent ip = new TextComponent(ChatColor.BLUE + player.getServerConnected());
				ip.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/connect " + player.getServerConnected()));
				ip.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new TextComponent[] { new TextComponent(Translate.getTranslation(language, "command-finder-hover")) }));
				sender.sendMessage(playerMessage, space, ip);
			}
		});
	}

	@Command(name = "staffsee", aliases = "stafflist", usage = "/<command>", groupToUse = Group.TRIAL, noPermMessageId = "command-staffsee-no-access")
	public void staffsee(CommandArgs cmdArgs) {
		CommandSender sender = cmdArgs.getSender();
		Group originalGroup = Group.DONO;
		Language lang = BattlebitsAPI.getDefaultLanguage();
		if (cmdArgs.isPlayer()) {
			originalGroup = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getServerGroup();
			lang = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getLanguage();
		}
		HashMap<Group, ArrayList<UUID>> groups = new HashMap<>();
		for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
			BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
			if (bP.getServerGroup().ordinal() < Group.BUILDER.ordinal()) {
				continue;
			}
			ArrayList<UUID> players = null;
			if (groups.containsKey(bP.getServerGroup()))
				players = groups.get(bP.getServerGroup());
			else
				players = new ArrayList<>();
			players.add(player.getUniqueId());
			groups.put(bP.getServerGroup(), players);
		}
		for (Group group : Group.values()) {
			if (!groups.containsKey(group))
				continue;
			if (group.ordinal() >= Group.MANAGER.ordinal())
				if (originalGroup.ordinal() < Group.MANAGER.ordinal())
					continue;
			for (UUID uuid : groups.get(group)) {
				BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
				String tag = Tag.valueOf(bP.getServerGroup().toString()).getPrefix(lang);
				String format = tag + (ChatColor.stripColor(tag).trim().length() > 0 ? " " : "") + bP.getUserName();
				TextComponent playerMessage = new TextComponent(format);
				TextComponent space = new TextComponent(ChatColor.WHITE + " - ");
				TextComponent ip = new TextComponent(ChatColor.BLUE + bP.getServerConnected());
				ip.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/connect " + bP.getServerConnected()));
				ip.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new TextComponent[] { new TextComponent(Translate.getTranslation(lang, "command-finder-hover")) }));
				sender.sendMessage(playerMessage, space, ip);
			}
		}
	}
}
