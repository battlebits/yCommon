package br.com.battlebits.ycommon.bukkit.commands.register;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class ModeratingCommands extends CommandClass {

	private DecimalFormat locationFormater = new DecimalFormat("######.##");

	@SuppressWarnings("deprecation")
	@Command(name = "gamemode", aliases = { "gm" }, groupToUse = Group.MANAGER, noPermMessageId = "command-gamemode-no-access", runAsync = true)
	public void gamemode(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			Player p = cmdArgs.getPlayer();
			String[] args = cmdArgs.getArgs();
			BattlePlayer bp = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			String prefix = Translate.getTranslation(bp.getLanguage(), "command-gamemode-prefix") + " ";
			if (args.length == 0) {
				p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-usage"));
			} else {
				GameMode gm = null;
				try {
					gm = GameMode.valueOf(args[0].toUpperCase());
				} catch (Exception e) {
					try {
						gm = GameMode.getByValue(Integer.parseInt(args[0]));
					} catch (Exception ex) {
					}
				}
				if (gm != null) {
					if (args.length == 1) {
						if (p.getGameMode() != gm) {
							p.setGameMode(gm);
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-changed-you").replace("%gamemode%", Translate.getTranslation(bp.getLanguage(), "name-gamemode-" + gm.name().toLowerCase())));
						} else {
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-already-you"));
						}
					} else {
						Player t = Bukkit.getPlayer(args[1]);
						if (t != null) {
							if (t.getGameMode() != gm) {
								t.setGameMode(gm);
								p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-changed-other").replace("%gamemode%", Translate.getTranslation(bp.getLanguage(), "name-gamemode-" + gm.name().toLowerCase())));
							} else {
								p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-already-other"));
							}
						} else {
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "player-not-found"));
						}
						t = null;
					}
				} else {
					p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-unknown"));
				}
				gm = null;
			}
			prefix = null;
			bp = null;
			args = null;
			p = null;
		} else {
			cmdArgs.getSender().sendMessage("§4§lERRO §fComando disponivel apenas §c§lin-game");
		}
		// TODO: ALERT STAFFS
	}

	@Command(name = "teleport", aliases = { "tp", "teleportar" }, groupToUse = Group.TRIAL, noPermMessageId = "command-teleport-no-access", runAsync = false)
	public void teleport(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			Player p = cmdArgs.getPlayer();
			String[] args = cmdArgs.getArgs();
			BattlePlayer bp = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			String prefix = Translate.getTranslation(bp.getLanguage(), "command-teleport-prefix") + " ";
			if (args.length == 0) {
				p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-teleport-usage"));
			} else if (args.length == 1 || !bp.hasGroupPermission(Group.MOD)) {
				Player t = Bukkit.getPlayer(args[0]);
				if (t != null) {
					p.teleport(t.getLocation());
					p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-teleport-teleported-to-player").replace("%player%", t.getName()));
					t = null;
				} else {
					p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "player-not-found"));
				}
			} else if (bp.hasGroupPermission(Group.MOD)) {
				if (args.length == 2) {
					Player player = Bukkit.getPlayer(args[0]);
					if (player != null) {
						Player target = Bukkit.getPlayer(args[1]);
						if (target != null) {
							player.teleport(target);
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-teleport-teleported-other-player").replace("%player%", player.getName()).replace("%target%", target.getName()));
							target = null;
						} else {
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "player-not-found"));
						}
						player = null;
					} else {
						p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "player-not-found"));
					}
				} else if (args.length >= 3) {
					if (args.length == 3) {
						Location loc = getLocationBased(p.getLocation(), args[0], args[1], args[2]);
						if (loc != null) {
							p.teleport(loc);
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-teleport-to-location").replace("%x%", locationFormater.format(loc.getX())).replace("%y%", locationFormater.format(loc.getY())).replace("%z%", locationFormater.format(loc.getZ())));
							loc = null;
						} else {
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-teleport-invalid-location"));
						}
					} else {
						Player target = Bukkit.getPlayer(args[0]);
						if (target != null) {
							Location loc = getLocationBased(target.getLocation(), args[1], args[2], args[3]);
							if (loc != null) {
								target.teleport(loc);
								p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-teleport-to-location-other").replace("%x%", locationFormater.format(loc.getX())).replace("%y%", locationFormater.format(loc.getY())).replace("%z%", locationFormater.format(loc.getZ())).replace("%target%", target.getName()));
								loc = null;
								target = null;
							} else {
								p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-teleport-invalid-location"));
							}
						} else {
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "player-not-found"));
						}
					}
				}
			}
			prefix = null;
			bp = null;
			args = null;
			p = null;
		} else {
			cmdArgs.getSender().sendMessage("§4§lERRO §fComando disponivel apenas §c§lin-game");
		}
		// TODO: ALERT STAFFS
	}

	@Command(name = "teleportall", aliases = { "tpall" }, groupToUse = Group.STREAMER, noPermMessageId = "command-teleportall-no-access", runAsync = false)
	public void tpall(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			Player p = cmdArgs.getPlayer();
			String[] args = cmdArgs.getArgs();
			BattlePlayer bp = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			String prefix = Translate.getTranslation(bp.getLanguage(), "command-teleportall-prefix") + " ";
			if (args.length == 0) {
				int i = 0;
				for (Player on : Bukkit.getOnlinePlayers()) {
					if (on != null && on.isOnline() && on.getUniqueId() != p.getUniqueId()) {
						on.teleport(p.getLocation());
						on.setFallDistance(0.0F);
						on.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-teleportall-teleported").replace("%target%", p.getName()));
						i++;
					}
					on = null;
				}
				p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-teleportall-success").replace("%players%", i + ""));
			} else if (args.length == 1) {
				Player t = Bukkit.getPlayer(args[0]);
				if (t != null) {
					int i = 0;
					for (Player on : Bukkit.getOnlinePlayers()) {
						if (on != null && on.isOnline() && on.getUniqueId() != t.getUniqueId() && on.getUniqueId() != p.getUniqueId()) {
							on.teleport(t.getLocation());
							on.setFallDistance(0.0F);
							on.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-teleportall-teleported").replace("%target%", t.getName()));
							i++;
						}
						on = null;
					}
					p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-teleportall-success-other").replace("%players%", i + "").replace("%target%", t.getName()));
					t = null;
				} else {
					p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "player-not-found"));
				}
			}
			prefix = null;
			bp = null;
			args = null;
			p = null;
		} else {
			cmdArgs.getSender().sendMessage("§4§lERRO §fComando disponivel apenas §c§lin-game");
		}
		// TODO: ALERT STAFFS
	}

	public void tphere(CommandArgs cmdArgs) {

	}

	private Location getLocationBased(Location loc, String argX, String argY, String argZ) {
		double x = 0;
		double y = 0;
		double z = 0;
		if (!argX.startsWith("~")) {
			try {
				x = Integer.parseInt(argX);
			} catch (Exception e) {
				return null;
			}
		} else {
			x = loc.getX();
			try {
				x += Integer.parseInt(argX.substring(1, argX.length()));
			} catch (Exception e) {
			}
		}
		if (!argY.startsWith("~")) {
			try {
				y = Integer.parseInt(argY);
			} catch (Exception e) {
				return null;
			}
		} else {
			y = loc.getY();
			try {
				y += Integer.parseInt(argY.substring(1, argY.length()));
			} catch (Exception e) {
			}
		}
		if (!argZ.startsWith("~")) {
			try {
				z = Integer.parseInt(argZ);
			} catch (Exception e) {
				return null;
			}
		} else {
			z = loc.getZ();
			try {
				z += Integer.parseInt(argZ.substring(1, argZ.length()));
			} catch (Exception e) {
			}
		}
		Location l = loc.clone();
		l.setX(x);
		l.setY(y);
		l.setZ(z);
		loc = null;
		return l;
	}

	@Command(name = "kick", aliases = { "kickar" }, groupToUse = Group.TRIAL, noPermMessageId = "command-kick-no-access", runAsync = false)
	public void kick(CommandArgs cmdArgs) {
		CommandSender sender = cmdArgs.getSender();
		String[] args = cmdArgs.getArgs();
		Language language = BattlebitsAPI.getDefaultLanguage();
		if (cmdArgs.isPlayer())
			language = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getLanguage();
		String prefix = Translate.getTranslation(language, "command-kick-prefix") + " ";

		if (args.length < 1) {
			sender.sendMessage(prefix + Translate.getTranslation(language, "command-kick-usage").replace("%command%", cmdArgs.getLabel()));
			return;
		}

		Player target = BukkitMain.getPlugin().getServer().getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(prefix + Translate.getTranslation(language, "player-not-found"));
			return;
		}

		boolean hasReason = false;
		StringBuilder builder = new StringBuilder();
		if (args.length > 1) {
			hasReason = true;
			for (int i = 1; i < args.length; i++) {
				String espaco = " ";
				if (i >= args.length - 1)
					espaco = "";
				builder.append(args[i] + espaco);
			}
		}

		for (Player p : Bukkit.getOnlinePlayers()) {
			BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			if (!player.isStaff())
				continue;
			String staffMessage = prefix + Translate.getTranslation(player.getLanguage(), "command-kick-message-" + (hasReason ? "reason" : "no-reason"));

			staffMessage = staffMessage.replace("%player%", target.getName());
			staffMessage = staffMessage.replace("%kickedBy%", sender.getName());
			staffMessage = staffMessage.replace("%reason%", builder.toString());
			p.sendMessage(staffMessage);
		}
		target.kickPlayer(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(target.getUniqueId()).getLanguage(), "command-kick-message-target-" + (hasReason ? "reason" : "no-reason")));
	}

	// whitelist

}
