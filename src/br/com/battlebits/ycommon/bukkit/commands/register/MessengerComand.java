package br.com.battlebits.ycommon.bukkit.commands.register;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.bukkit.commands.CommandClass;
import br.com.battlebits.ycommon.bukkit.commands.CommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.CommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.friends.block.Blocked;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.utils.string.StringURLUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class MessengerComand extends CommandClass {

	@Command(name = "tell", aliases = { "msg", "w", "pm", "privatemessage", "whisper" })
	public void tell(CommandArgs args) {
		if (args.isPlayer()) {
			Player p = args.getPlayer();
			BukkitPlayer bp = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			String prefix = Translate.getTranslation(bp.getLanguage(), "command-tell-prefix");
			if (args.getArgs().length <= 1) {
				p.sendMessage(
						prefix + Translate.getTranslation(bp.getLanguage(), "command-tell-help").replace("%command%", args.getLabel().toLowerCase()));
			} else {
				if (bp.getConfiguration().isTellEnabled()) {
					Player t = Bukkit.getPlayer(args.getArgs()[0]);
					if (t != null) {
						if (t.getUniqueId() != p.getUniqueId()) {
							BukkitPlayer bt = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(t.getUniqueId());
							if (!bt.getConfiguration().isIgnoreAll()) {
								if (!bt.getBlockedPlayers().containsKey(p.getUniqueId())) {
									if (bt.getConfiguration().isTellEnabled()) {
										TextComponent[] toPlayer = new TextComponent[args.getArgs().length];
										TextComponent to = new TextComponent(Translate.getTranslation(bp.getLanguage(), "command-tell-me-player")
												.replace("%player%", t.getName()));
										to.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND,
												"/" + args.getLabel().toLowerCase() + " " + t.getName() + " "));
										to.setHoverEvent(
												new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
														new TextComponent[] { new TextComponent(
																Translate.getTranslation(bp.getLanguage(), "command-tell-hover-another")
																		.replace("%player%", t.getName())) }));
										toPlayer[0] = to;
										to = null;
										TextComponent[] toTarget = new TextComponent[args.getArgs().length];
										TextComponent from = new TextComponent(Translate.getTranslation(bt.getLanguage(), "command-tell-player-me")
												.replace("%player%", p.getName()));
										from.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/r "));
										from.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
												new TextComponent[] { new TextComponent(Translate.getTranslation(bt.getLanguage(), "tell-hover-reply")
														.replace("%player%", p.getName())) }));
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
										// TODO: SEND MESSAGE ON
										// BOSSBAR/ACTIONBAR
										// TODO: PLAY SOUND
										bt.setLastTellUUID(p.getUniqueId());
										toTarget = null;
										toPlayer = null;
									} else {
										p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-tell-player-disabled"));
									}
								} else {
									p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-tell-ignore-you"));
								}
							} else {
								p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-tell-ignore-all"));
							}
							bt = null;
						} else {
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-tell-send-to-me"));
						}
					} else {
						p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-tell-not-found"));
					}
					t = null;
				} else {
					p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-tell-disabled"));
				}
			}
			prefix = null;
			bp = null;
			p = null;
		}
	}

	@Command(name = "reply", aliases = { "r", "responder" })
	public void reply(CommandArgs args) {
		if (args.isPlayer()) {
			Player p = args.getPlayer();
			BukkitPlayer bp = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			String prefix = Translate.getTranslation(bp.getLanguage(), "command-tell-prefix");
			if (args.getArgs().length == 0) {
				p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-reply-use"));
			} else {
				if (bp.hasLastTell()) {
					Player t = Bukkit.getPlayer(bp.getLastTellUUID());
					if (t != null) {
						BukkitPlayer bt = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(t.getUniqueId());
						if (!bt.getConfiguration().isIgnoreAll()) {
							if (!bt.getBlockedPlayers().containsKey(p.getUniqueId())) {
								if (bt.getConfiguration().isTellEnabled()) {
									TextComponent[] toPlayer = new TextComponent[args.getArgs().length + 1];
									TextComponent to = new TextComponent(
											Translate.getTranslation(bp.getLanguage(), "command-tell-me-player").replace("%player%", t.getName()));
									to.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/tell " + t.getName() + " "));
									to.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
											new TextComponent[] { new TextComponent(Translate.getTranslation(bp.getLanguage(), "tell-hover-another")
													.replace("%player%", t.getName())) }));
									toPlayer[0] = to;
									to = null;
									TextComponent[] toTarget = new TextComponent[args.getArgs().length + 1];
									TextComponent from = new TextComponent(
											Translate.getTranslation(bt.getLanguage(), "command-tell-player-me").replace("%player%", p.getName()));
									from.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/r "));
									from.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
											new TextComponent[] { new TextComponent(Translate.getTranslation(bt.getLanguage(), "tell-hover-reply")
													.replace("%player%", p.getName())) }));
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
									p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-reply-tell-off"));
								}
							} else {
								p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-tell-ignore-you"));
							}
						} else {
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-tell-ignore-all"));
						}
						bt = null;
					} else {
						p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-reply-offline"));
					}
					t = null;
				} else {
					p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-reply-none"));
				}
			}
			prefix = null;
			bp = null;
			p = null;
		}
	}

	@Command(name = "ignore", aliases = { "ignorar", "bloquear", "block" })
	public void ignore(CommandArgs args) {
		if (args.isPlayer()) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Player p = args.getPlayer();
					BukkitPlayer bp = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
					String prefix = Translate.getTranslation(bp.getLanguage(), "command-ignore-prefix");
					if (args.getArgs().length == 0) {
						p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-ignore-invalid"));
					} else {
						UUID id = null;
						Player t = Bukkit.getPlayerExact(args.getArgs()[0]);
						if (t != null) {
							id = t.getUniqueId();
						}
						t = null;
						if (id == null) {
							try {
								id = BattlebitsAPI.getUUIDOf(args.getArgs()[0]);
							} catch (Exception e) {
								p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-ignore-player-not-found"));
								return;
							}
						}
						if (id != null) {
							if (id != p.getUniqueId()) {
								if (!bp.getBlockedPlayers().containsKey(id)) {
									bp.getBlockedPlayers().put(id, new Blocked());
									// TODO: BLOCKED?
									p.sendMessage(prefix
											+ Translate.getTranslation(bp.getLanguage(), "command-ignore-you-ignore").replace("%player%", args.getArgs()[0]));
								} else {
									bp.getBlockedPlayers().remove(id);
									p.sendMessage(prefix
											+ Translate.getTranslation(bp.getLanguage(), "command-ignore-not-ignore").replace("%player%", args.getArgs()[0]));
								}
							} else {
								p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-ignore-cant-you"));
							}
						} else {
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-ignore-player-not-found"));
						}
						id = null;
					}
					bp = null;
					p = null;
				}
			}.runTaskAsynchronously(BukkitMain.getPlugin());
		}
	}

}