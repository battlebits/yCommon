package br.com.battlebits.ycommon.bukkit.commands.register;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.commands.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;

public class ModeratingCommands extends CommandClass {

	private DecimalFormat locationFormater = new DecimalFormat("######.##");

	@SuppressWarnings("deprecation")
	@Command(name = "gamemode", aliases = { "gm" }, groupToUse = Group.ADMIN, noPermMessageId = "command-gamemode-no-access", runAsync = true)
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
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-changed-you").replace("%gamemode%",
									Translate.getTranslation(bp.getLanguage(), "name-gamemode-" + gm.name().toLowerCase())));
						} else {
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-already-you"));
						}
					} else {
						Player t = Bukkit.getPlayer(args[1]);
						if (t != null) {
							if (t.getGameMode() != gm) {
								t.setGameMode(gm);
								p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-changed-other").replace(
										"%gamemode%", Translate.getTranslation(bp.getLanguage(), "name-gamemode-" + gm.name().toLowerCase())));
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

	@Command(name = "tp", aliases = { "teleport", "teleportar" }, groupToUse = Group.TRIAL, noPermMessageId = "command-teleport-no-access", runAsync = false)
	public void tp(CommandArgs cmdArgs) {
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
					p.sendMessage(prefix
							+ Translate.getTranslation(bp.getLanguage(), "command-teleport-teleported-to-player").replace("%player%", t.getName()));
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
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-teleport-teleported-other-player")
									.replace("%player%", player.getName()).replace("%target%", target.getName()));
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
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-teleport-to-location")
									.replace("%x%", locationFormater.format(loc.getX())).replace("%y%", locationFormater.format(loc.getY()))
									.replace("%z%", locationFormater.format(loc.getZ())));
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
								p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-teleport-to-location-other")
										.replace("%x%", locationFormater.format(loc.getX())).replace("%y%", locationFormater.format(loc.getY()))
										.replace("%z%", locationFormater.format(loc.getZ())).replace("%target%", target.getName()));
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

	@SuppressWarnings("deprecation")
	@Command(name = "tpall", aliases = { "teleportall" }, groupToUse = Group.MOD, noPermMessageId = "command-teleportall-no-access", runAsync = false)
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
						on.sendMessage(prefix
								+ Translate.getTranslation(bp.getLanguage(), "command-teleportall-teleported").replace("%target%", p.getName()));
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
							on.sendMessage(prefix
									+ Translate.getTranslation(bp.getLanguage(), "command-teleportall-teleported").replace("%target%", t.getName()));
							i++;
						}
						on = null;
					}
					p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-teleportall-success-other")
							.replace("%players%", i + "").replace("%target%", t.getName()));
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
	
	public void tphere(CommandArgs cmdArgs){
		
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

}
