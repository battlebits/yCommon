package br.com.battlebits.ycommon.bukkit.commands.register;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.bukkit.commands.CommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.CommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.utils.string.StringURLUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class MessengerComand {

	@Command(name = "tell", aliases = { "msg", "w", "pm", "privatemessage", "whisper" })
	public void tell(CommandArgs args) {
		if (args.isPlayer()) {
			Player p = args.getPlayer();
			BukkitPlayer bp = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			if (args.getArgs().length <= 1) {
				p.sendMessage(Translate.getTranslation(bp.getLanguage(), "tell-help").replace("%command%", args.getLabel().toLowerCase()));
			} else {
				if (bp.getConfiguration().isTellEnabled()) {
					Player t = Bukkit.getPlayer(args.getArgs()[0]);
					if (t != null) {
						if (t.getUniqueId() != p.getUniqueId()) {
							BukkitPlayer bt = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(t.getUniqueId());
							if (bt.getConfiguration().isTellEnabled()) {
								TextComponent[] toPlayer = new TextComponent[args.getArgs().length];
								TextComponent to = new TextComponent(
										Translate.getTranslation(bp.getLanguage(), "tell-me-player").replace("%player%", t.getName()));
								to.setClickEvent(
										new ClickEvent(Action.SUGGEST_COMMAND, "/" + args.getLabel().toLowerCase() + " " + t.getName() + " "));
								to.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
										new TextComponent[] { new TextComponent(Translate.getTranslation(bp.getLanguage(), "tell-hover-another")
												.replace("%player%", t.getName())) }));
								toPlayer[0] = to;
								to = null;
								TextComponent[] toTarget = new TextComponent[args.getArgs().length];
								TextComponent from = new TextComponent(
										Translate.getTranslation(bt.getLanguage(), "tell-player-me").replace("%player%", p.getName()));
								from.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/r "));
								from.setHoverEvent(
										new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new TextComponent[] { new TextComponent(
												Translate.getTranslation(bt.getLanguage(), "tell-hover-reply").replace("%player%", p.getName())) }));
								toTarget[0] = from;
								from = null;
								for (int i = 1; i < args.getArgs().length; i += 1) {
									String msg = args.getArgs()[i];
									msg = " " + msg;
									TextComponent text = new TextComponent(msg);
									List<String> url = StringURLUtils.extractUrls(msg);
									if (url.size() > 0) {
										text.setClickEvent(new ClickEvent(Action.OPEN_URL, url.get(0)));
									}
									toPlayer[i] = text;
									toTarget[i] = text;
									text = null;
									url = null;
									msg = null;
								}
								p.spigot().sendMessage(toPlayer);
								t.spigot().sendMessage(toTarget);
								// TODO: SEND MESSAGE ON BOSSBAR/ACTIONBAR
								// TODO: PLAY SOUND
								bt.setLastTellUUID(p.getUniqueId());
								toTarget = null;
								toPlayer = null;
							} else {
								p.sendMessage(Translate.getTranslation(bp.getLanguage(), "tell-player-disabled"));
							}
							bt = null;
						} else {
							p.sendMessage(Translate.getTranslation(bp.getLanguage(), "tell-send-to-me"));
						}
					} else {
						p.sendMessage(Translate.getTranslation(bp.getLanguage(), "tell-not-found"));
					}
					t = null;
				} else {
					p.sendMessage(Translate.getTranslation(bp.getLanguage(), "tell-disabled"));
				}
			}
			bp = null;
			p = null;
		}
	}

	@Command(name = "reply", aliases = { "r", "responder" })
	public void reply(CommandArgs args) {
		if (args.isPlayer()) {
			Player p = args.getPlayer();
			BukkitPlayer bp = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			if (args.getArgs().length == 0) {
				p.sendMessage(Translate.getTranslation(bp.getLanguage(), "reply-use"));
			} else {
				if (bp.hasLastTell()) {
					Player t = Bukkit.getPlayer(bp.getLastTellUUID());
					if (t != null) {
						BukkitPlayer bt = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(t.getUniqueId());
						if (bt.getConfiguration().isTellEnabled()) {
							TextComponent[] toPlayer = new TextComponent[args.getArgs().length + 1];
							TextComponent to = new TextComponent(
									Translate.getTranslation(bp.getLanguage(), "tell-me-player").replace("%player%", t.getName()));
							to.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/tell " + t.getName() + " "));
							to.setHoverEvent(
									new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new TextComponent[] { new TextComponent(
											Translate.getTranslation(bp.getLanguage(), "tell-hover-another").replace("%player%", t.getName())) }));
							toPlayer[0] = to;
							to = null;
							TextComponent[] toTarget = new TextComponent[args.getArgs().length + 1];
							TextComponent from = new TextComponent(
									Translate.getTranslation(bt.getLanguage(), "tell-player-me").replace("%player%", p.getName()));
							from.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/r "));
							from.setHoverEvent(
									new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new TextComponent[] { new TextComponent(
											Translate.getTranslation(bt.getLanguage(), "tell-hover-reply").replace("%player%", p.getName())) }));
							toTarget[0] = from;
							from = null;
							for (int i = 0; i < args.getArgs().length; i += 1) {
								String msg = args.getArgs()[i];
								msg = " " + msg;
								TextComponent text = new TextComponent(msg);
								List<String> url = StringURLUtils.extractUrls(msg);
								if (url.size() > 0) {
									text.setClickEvent(new ClickEvent(Action.OPEN_URL, url.get(0)));
								}
								toPlayer[i + 1] = text;
								toTarget[i + 1] = text;
								text = null;
								url = null;
								msg = null;
							}
							p.spigot().sendMessage(toPlayer);
							t.spigot().sendMessage(toTarget);
							// TODO: SEND MESSAGE ON BOSSBAR/ACTIONBAR
							// TODO: PLAY SOUND
							bt.setLastTellUUID(p.getUniqueId());
							toTarget = null;
							toPlayer = null;
						} else {
							p.sendMessage(Translate.getTranslation(bp.getLanguage(), "reply-tell-off"));
						}
						bt = null;
					} else {
						p.sendMessage(Translate.getTranslation(bp.getLanguage(), "reply-offline"));
					}
					t = null;
				} else {
					p.sendMessage(Translate.getTranslation(bp.getLanguage(), "reply-none"));
				}
			}
			bp = null;
			p = null;
		}
	}

}
