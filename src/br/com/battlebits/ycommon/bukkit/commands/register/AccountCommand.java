package br.com.battlebits.ycommon.bukkit.commands.register;

import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.bukkit.event.account.update.PlayerChangeTagEvent;
import br.com.battlebits.ycommon.bukkit.tag.Tag;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.commands.CommandClass;
import br.com.battlebits.ycommon.common.translate.Translate;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class AccountCommand extends CommandClass {

	@Command(name = "account")
	public void account(CommandArgs args) {

	}

	@Command(name = "league")
	public void league(CommandArgs args) {

	}

	@Command(name = "ranking")
	public void ranking(CommandArgs args) {

	}

	@Command(name = "tag", runAsync = true)
	public void tag(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			Player p = cmdArgs.getPlayer();
			String[] args = cmdArgs.getArgs();
			BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			String prefix = Translate.getTranslation(player.getLanguage(), "command-tag-prefix") + " ";
			if (args.length == 0) {
				int max = player.getTags().size() * 2;
				TextComponent[] message = new TextComponent[max];
				message[0] = new TextComponent(prefix + Translate.getTranslation(player.getLanguage(), "command-tag-available") + " ");
				int i = max - 1;
				for (Tag t : player.getTags()) {
					if (i < max - 1) {
						message[i] = new TextComponent("§7, ");
						i -= 1;
					}
					TextComponent component = new TextComponent(t.getPrefix(player.getLanguage()));
					component.setHoverEvent(new HoverEvent(Action.SHOW_TEXT,
							new TextComponent[] { new TextComponent(Translate.getTranslation(player.getLanguage(), "command-tag-click-select")) }));
					component.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/tag " + t.name()));
					message[i] = component;
					i -= 1;
					component = null;
				}
				p.spigot().sendMessage(message);
				message = null;
			} else {
				Tag tag = null;
				try {
					tag = Tag.valueOf(args[0].toUpperCase());
				} catch (Exception ex) {
					p.sendMessage(Translate.getTranslation(player.getLanguage(), "command-tag-not-found"));
					return;
				}
				if (tag != null) {
					if (player.getTags().contains(tag)) {
						if (player.getTag() != tag) {
							PlayerChangeTagEvent event = new PlayerChangeTagEvent(p, player.getTag(), tag);
							BukkitMain.getPlugin().getServer().getPluginManager().callEvent(event);
							if (!event.isCancelled()) {
								player.setTag(tag);
								p.sendMessage(Translate.getTranslation(player.getLanguage(), "command-tag-selected").replace("%tag%",
										tag.getPrefix(player.getLanguage())));
							}
							event = null;
						} else {
							p.sendMessage(Translate.getTranslation(player.getLanguage(), "command-tag-current"));
						}
					} else {
						p.sendMessage(Translate.getTranslation(player.getLanguage(), "command-tag-no-access"));
					}
					tag = null;
				} else {
					p.sendMessage(Translate.getTranslation(player.getLanguage(), "command-tag-not-found"));
				}
			}
			prefix = null;
			player = null;
			args = null;
			p = null;
		} else {
			cmdArgs.getSender().sendMessage("§4§lERRO §fComando disponivel apenas §c§lin-game");
		}
	}

}
