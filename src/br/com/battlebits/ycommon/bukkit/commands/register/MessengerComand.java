package br.com.battlebits.ycommon.bukkit.commands.register;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.commands.CommandClass;
import br.com.battlebits.ycommon.common.friends.block.Blocked;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAddBlockedPlayer;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveBlockedPlayer;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.utils.string.StringURLUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class MessengerComand extends CommandClass {

	@Command(name = "tell", aliases = { "msg", "w", "pm", "privatemessage", "whisper" }, runAsync = true, groupToUse = Group.NORMAL, description = "Mensagens privadas")
	public void tell(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			Player p = cmdArgs.getPlayer();
			BukkitPlayer bp = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			String[] args = cmdArgs.getArgs();
			String prefix = Translate.getTranslation(bp.getLanguage(), "command-tell-prefix") + " ";
			if (args.length <= 1) {
				p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-tell-usage").replace("%command%", cmdArgs.getLabel().toLowerCase()));
			} else {
				if (bp.getConfiguration().isTellEnabled()) {
					Player t = Bukkit.getPlayer(args[0]);
					if (t != null) {
						if (t.getUniqueId() != p.getUniqueId()) {
							BukkitPlayer bt = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(t.getUniqueId());
							if (!bt.getConfiguration().isIgnoreAll()) {
								if (!bt.getBlockedPlayers().containsKey(p.getUniqueId())) {
									if (bt.getConfiguration().isTellEnabled()) {
										TextComponent[] toPlayer = new TextComponent[args.length];
										TextComponent to = new TextComponent(Translate.getTranslation(bp.getLanguage(), "command-tell-me-player").replace("%player%", t.getName()));
										to.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/" + cmdArgs.getLabel().toLowerCase() + " " + t.getName() + " "));
										to.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new TextComponent[] { new TextComponent(Translate.getTranslation(bp.getLanguage(), "command-tell-hover-another").replace("%player%", t.getName())) }));
										toPlayer[0] = to;
										to = null;
										TextComponent[] toTarget = new TextComponent[args.length];
										TextComponent from = new TextComponent(Translate.getTranslation(bt.getLanguage(), "command-tell-player-me").replace("%player%", p.getName()));
										from.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/r "));
										from.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new TextComponent[] { new TextComponent(Translate.getTranslation(bt.getLanguage(), "command-tell-hover-reply").replace("%player%", p.getName())) }));
										toTarget[0] = from;
										from = null;
										for (int i = 1; i < args.length; i += 1) {
											String msg = args[i];
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
						p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "player-not-found"));
					}
					t = null;
				} else {
					p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-tell-disabled"));
				}
			}
			prefix = null;
			bp = null;
			args = null;
			p = null;
		} else {
			cmdArgs.getSender().sendMessage("§4§lERRO §fComando disponivel apenas §c§lin-game");
		}
	}

	@Command(name = "reply", aliases = { "r", "responder" }, description = "Mensagens privadas", groupToUse = Group.NORMAL, runAsync = true)
	public void reply(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			Player p = cmdArgs.getPlayer();
			String[] args = cmdArgs.getArgs();
			BukkitPlayer bp = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			String prefix = Translate.getTranslation(bp.getLanguage(), "command-tell-prefix") + " ";
			if (args.length == 0) {
				p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-reply-usage"));
			} else {
				if (bp.hasLastTell()) {
					Player t = Bukkit.getPlayer(bp.getLastTellUUID());
					if (t != null) {
						BukkitPlayer bt = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(t.getUniqueId());
						if (!bt.getConfiguration().isIgnoreAll()) {
							if (!bt.getBlockedPlayers().containsKey(p.getUniqueId())) {
								if (bt.getConfiguration().isTellEnabled()) {
									TextComponent[] toPlayer = new TextComponent[args.length + 1];
									TextComponent to = new TextComponent(Translate.getTranslation(bp.getLanguage(), "command-tell-me-player").replace("%player%", t.getName()));
									to.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/tell " + t.getName() + " "));
									to.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new TextComponent[] { new TextComponent(Translate.getTranslation(bp.getLanguage(), "command-tell-hover-another").replace("%player%", t.getName())) }));
									toPlayer[0] = to;
									to = null;
									TextComponent[] toTarget = new TextComponent[args.length + 1];
									TextComponent from = new TextComponent(Translate.getTranslation(bt.getLanguage(), "command-tell-player-me").replace("%player%", p.getName()));
									from.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/r "));
									from.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new TextComponent[] { new TextComponent(Translate.getTranslation(bt.getLanguage(), "command-tell-hover-reply").replace("%player%", p.getName())) }));
									toTarget[0] = from;
									from = null;
									for (int i = 0; i < args.length; i += 1) {
										String msg = args[i];
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
			args = null;
			p = null;
		} else {
			cmdArgs.getSender().sendMessage("§4§lERRO §fComando disponivel apenas §c§lin-game");
		}
	}

	@Command(name = "ignore", aliases = { "ignorar", "bloquear", "block" }, runAsync = true)
	public void ignore(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			Player p = cmdArgs.getPlayer();
			String[] args = cmdArgs.getArgs();
			BukkitPlayer bp = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			String prefix = Translate.getTranslation(bp.getLanguage(), "command-block-prefix") + " ";
			if (args.length == 0) {
				p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-block-usage"));
			} else {
				UUID id = BattlebitsAPI.getUUIDOf(args[0]);
				if (id != null) {
					if (id != p.getUniqueId()) {
						if (!bp.getBlockedPlayers().containsKey(id)) {
							try {
								Blocked block = new Blocked(id);
								bp.getBlockedPlayers().put(id, block);
								BukkitMain.getPlugin().getClient().sendPacket(new CPacketAddBlockedPlayer(p.getUniqueId(), block));
								p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-block-blocked").replace("%player%", cmdArgs.getArgs()[0]));
								block = null;
							} catch (Exception e) {
								p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "error-try-again-please"));
							}
						} else {
							try {
								bp.getBlockedPlayers().remove(id);
								BukkitMain.getPlugin().getClient().sendPacket(new CPacketRemoveBlockedPlayer(p.getUniqueId(), id));
								p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-block-unblocked").replace("%player%", cmdArgs.getArgs()[0]));
							} catch (Exception e) {
								p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "error-try-again-please"));
							}
						}
					} else {
						p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-block-cant-you"));
					}
				} else {
					p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "player-not-exist"));
				}
				id = null;
			}
			prefix = null;
			args = null;
			bp = null;
			p = null;
		} else {
			cmdArgs.getSender().sendMessage("§4§lERRO §fComando disponivel apenas §c§lin-game");
		}
	}

}