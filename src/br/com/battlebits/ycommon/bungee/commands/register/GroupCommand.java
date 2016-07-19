package br.com.battlebits.ycommon.bungee.commands.register;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.Command;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.Completer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.enums.ServerType;
import br.com.battlebits.ycommon.common.payment.RankType;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.tag.Tag;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import br.com.battlebits.ycommon.common.utils.DateUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class GroupCommand extends CommandClass {

	@Command(name = "groupset", usage = "/<command> <player> <group>", groupToUse = Group.MANAGER, aliases = { "setargrupo" }, noPermMessageId = "command-groupset-no-access")
	public void groupset(CommandArgs cmdArgs) {
		final CommandSender sender = cmdArgs.getSender();
		final String[] args = cmdArgs.getArgs();
		Language lang = BattlebitsAPI.getDefaultLanguage();
		if (cmdArgs.isPlayer()) {
			lang = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getLanguage();
		}
		final Language language = lang;
		final String groupSetPrefix = Translate.getTranslation(lang, "command-groupset-prefix") + " ";
		if (args.length != 2) {
			sender.sendMessage(TextComponent.fromLegacyText(groupSetPrefix + Translate.getTranslation(lang, "command-groupset-usage")));
			return;
		}
		Group grupo = null;
		try {
			grupo = Group.valueOf(args[1].toUpperCase());
		} catch (Exception e) {
			sender.sendMessage(TextComponent.fromLegacyText(groupSetPrefix + Translate.getTranslation(lang, "command-groupset-group-not-exist")));
			return;
		}
		final Group group = grupo;
		boolean owner = false;
		ServerType type = ServerType.NONE;
		if (cmdArgs.isPlayer()) {
			BattlePlayer battleSender = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId());
			type = battleSender.getServerConnectedType();
			if (battleSender.getServerGroup() == Group.DONO)
				owner = true;
		} else {
			owner = true;
			type = ServerType.NETWORK;
		}
		if (group.ordinal() <= Group.YOUTUBER.ordinal() && group != Group.NORMAL) {
			sender.sendMessage(TextComponent.fromLegacyText(groupSetPrefix + Translate.getTranslation(lang, "command-groupset-group-temporary")));
			return;
		}

		if (!owner)
			if (group.ordinal() > Group.MODPLUS.ordinal()) {
				sender.sendMessage(TextComponent.fromLegacyText(groupSetPrefix + Translate.getTranslation(lang, "command-groupset-group-not-owner")));
				return;
			}
		final ServerType typep = type;
		BungeeCord.getInstance().getScheduler().runAsync(BungeeMain.getPlugin(), new Runnable() {
			public void run() {
				UUID uuid = BattlebitsAPI.getUUIDOf(args[0]);
				if (uuid == null) {
					sender.sendMessage(TextComponent.fromLegacyText(groupSetPrefix + Translate.getTranslation(language, "player-not-exist")));
					return;
				}
				BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
				if (player == null) {
					try {
						player = BungeeMain.getPlugin().getAccountManager().loadBattlePlayer(uuid);
					} catch (Exception e) {
						e.printStackTrace();
						sender.sendMessage(TextComponent.fromLegacyText(groupSetPrefix + Translate.getTranslation(language, "cant-request-offline")));
						return;
					}
					if (player == null) {
						sender.sendMessage(TextComponent.fromLegacyText(groupSetPrefix + Translate.getTranslation(language, "player-never-joined")));
						return;
					}
				}
				ServerType serverType = typep;
				if (group.ordinal() >= Group.MODPLUS.ordinal())
					serverType = ServerType.NETWORK;
				if (group == Group.NORMAL && player.getServerGroup().ordinal() >= Group.MODPLUS.ordinal())
					serverType = ServerType.NETWORK;
				Group actualGroup = player.getGroups().containsKey(serverType.getStaffType()) ? player.getGroups().get(serverType.getStaffType()) : Group.NORMAL;
				if (actualGroup == group) {
					sender.sendMessage(TextComponent.fromLegacyText(groupSetPrefix + Translate.getTranslation(language, "command-groupset-player-already-group")));
					return;
				}

				if (group == Group.NORMAL) {
					if (serverType != ServerType.NETWORK)
						if (player.getGroups().containsKey(ServerType.NETWORK.getStaffType()) && cmdArgs.isPlayer() && BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).isOnline())
							serverType = ServerType.NETWORK;
					player.getGroups().remove(serverType.getStaffType());
				} else {
					player.getGroups().put(serverType.getStaffType(), group);
				}
				if (!player.isOnline()) {
					BattlebitsAPI.getAccountCommon().saveBattlePlayer(player);
				}
				ProxiedPlayer pPlayer = BungeeMain.getPlugin().getProxy().getPlayer(player.getUuid());
				if (pPlayer != null) {
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("Groupset");
					out.writeUTF(group.toString());
					out.writeUTF(serverType.toString());
					if (pPlayer.getServer() != null)
						pPlayer.getServer().sendData(BattlebitsAPI.getBungeeChannel(), out.toByteArray());
				}
				String message = groupSetPrefix + Translate.getTranslation(language, "command-groupset-change-group");
				message = message.replace("%player%", player.getUserName() + "(" + player.getUuid().toString().replace("-", "") + ")");
				message = message.replace("%group%", group.name());
				message = message.replace("%serverType%", serverType != null ? serverType.toString() : "null");
				sender.sendMessage(TextComponent.fromLegacyText(message));

			}
		});
	}

	@Command(name = "givevip", usage = "/<command> <player> <tempo> <group>", groupToUse = Group.MODPLUS, aliases = { "darvip" }, noPermMessageId = "command-givevip-no-access")
	public void givevip(CommandArgs cmdArgs) {
		final CommandSender sender = cmdArgs.getSender();
		final String[] args = cmdArgs.getArgs();
		Language lang = BattlebitsAPI.getDefaultLanguage();
		if (cmdArgs.isPlayer()) {
			lang = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getLanguage();
		}
		final Language language = lang;
		final String giveVipPrefix = Translate.getTranslation(lang, "command-givevip-prefix") + " ";
		if (args.length != 3) {
			sender.sendMessage(TextComponent.fromLegacyText(giveVipPrefix + Translate.getTranslation(lang, "command-givevip-usage").replace("%command%", cmdArgs.getLabel())));
			return;
		}
		BungeeCord.getInstance().getScheduler().runAsync(BungeeMain.getPlugin(), new Runnable() {
			public void run() {
				UUID uuid = BattlebitsAPI.getUUIDOf(args[0]);
				if (uuid == null) {
					sender.sendMessage(TextComponent.fromLegacyText(giveVipPrefix + Translate.getTranslation(language, "player-not-exist")));
					return;
				}
				BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
				if (player == null) {
					try {
						player = BungeeMain.getPlugin().getAccountManager().loadBattlePlayer(uuid);
					} catch (Exception e) {
						e.printStackTrace();
						sender.sendMessage(TextComponent.fromLegacyText(giveVipPrefix + Translate.getTranslation(language, "cant-request-offline")));
						return;
					}
					if (player == null) {
						sender.sendMessage(TextComponent.fromLegacyText(giveVipPrefix + Translate.getTranslation(language, "player-never-joined")));
						return;
					}
				}

				long expiresCheck;
				try {
					expiresCheck = DateUtils.parseDateDiff(args[1], true);
				} catch (Exception e1) {
					sender.sendMessage(TextComponent.fromLegacyText(giveVipPrefix + Translate.getTranslation(language, "invalid-format")));
					return;
				}
				expiresCheck = expiresCheck - System.currentTimeMillis();
				RankType rank = null;
				try {
					rank = RankType.valueOf(args[2].toUpperCase());
				} catch (Exception e) {
					sender.sendMessage(TextComponent.fromLegacyText(giveVipPrefix + Translate.getTranslation(language, "command-givevip-rank-not-exist")));
					return;
				}
				long newAdd = System.currentTimeMillis();
				if (player.getRanks().containsKey(rank)) {
					newAdd = player.getRanks().get(rank);
				}
				newAdd = newAdd + expiresCheck;
				player.getRanks().put(rank, newAdd);
				player.setTag(Tag.valueOf(rank.toString()));
				if (!player.isOnline()) {
					BattlebitsAPI.getAccountCommon().saveBattlePlayer(player);
				}
				ProxiedPlayer pPlayer = BungeeMain.getPlugin().getProxy().getPlayer(player.getUuid());
				if (pPlayer != null) {
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("Givevip");
					out.writeUTF(rank.name());
					out.writeLong(expiresCheck);
					if (pPlayer.getServer() != null)
						pPlayer.getServer().sendData(BattlebitsAPI.getBungeeChannel(), out.toByteArray());
				}
				String message = giveVipPrefix + Translate.getTranslation(language, "command-givevip-added");
				message = message.replace("%player%", player.getUserName() + "(" + player.getUuid().toString().replace("-", "") + ")");
				message = message.replace("%rank%", rank.name());
				message = message.replace("%duration%", DateUtils.formatDifference(language, expiresCheck / 1000));
				sender.sendMessage(TextComponent.fromLegacyText(message));

			}
		});
	}

	@Command(name = "removevip", usage = "/<command> <player> <group>", groupToUse = Group.MANAGER, aliases = { "removervip" }, noPermMessageId = "command-removevip-no-access")
	public void removevip(CommandArgs cmdArgs) {
		final CommandSender sender = cmdArgs.getSender();
		final String[] args = cmdArgs.getArgs();
		Language lang = BattlebitsAPI.getDefaultLanguage();
		if (cmdArgs.isPlayer()) {
			lang = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getLanguage();
		}
		final Language language = lang;
		final String giveVipPrefix = Translate.getTranslation(lang, "command-removevip-prefix") + " ";
		if (args.length != 2) {
			sender.sendMessage(TextComponent.fromLegacyText(giveVipPrefix + Translate.getTranslation(lang, "command-removevip-usage").replace("%command%", cmdArgs.getLabel())));
			return;
		}
		BungeeCord.getInstance().getScheduler().runAsync(BungeeMain.getPlugin(), new Runnable() {
			public void run() {
				UUID uuid = BattlebitsAPI.getUUIDOf(args[0]);
				if (uuid == null) {
					sender.sendMessage(TextComponent.fromLegacyText(giveVipPrefix + Translate.getTranslation(language, "player-not-exist")));
					return;
				}
				BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
				if (player == null) {
					try {
						player = BungeeMain.getPlugin().getAccountManager().loadBattlePlayer(uuid);
					} catch (Exception e) {
						e.printStackTrace();
						sender.sendMessage(TextComponent.fromLegacyText(giveVipPrefix + Translate.getTranslation(language, "cant-request-offline")));
						return;
					}
					if (player == null) {
						sender.sendMessage(TextComponent.fromLegacyText(giveVipPrefix + Translate.getTranslation(language, "player-never-joined")));
						return;
					}
				}

				RankType rank = null;
				try {
					rank = RankType.valueOf(args[1].toUpperCase());
				} catch (Exception e) {
					sender.sendMessage(TextComponent.fromLegacyText(giveVipPrefix + Translate.getTranslation(language, "command-removevip-rank-not-exist")));
					return;
				}
				player.getRanks().remove(rank);
				if (!player.isOnline()) {
					BattlebitsAPI.getAccountCommon().saveBattlePlayer(player);
				}
				ProxiedPlayer pPlayer = BungeeMain.getPlugin().getProxy().getPlayer(player.getUuid());
				if (pPlayer != null) {
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("Removevip");
					out.writeUTF(rank.name());
					if (pPlayer.getServer() != null)
						pPlayer.getServer().sendData(BattlebitsAPI.getBungeeChannel(), out.toByteArray());
				}
				String message = giveVipPrefix + Translate.getTranslation(language, "command-removevip-removed");
				message = message.replace("%player%", player.getUserName() + "(" + player.getUuid().toString().replace("-", "") + ")");
				message = message.replace("%rank%", rank.name());
				sender.sendMessage(TextComponent.fromLegacyText(message));

			}
		});
	}

	@Completer(name = "groupset", aliases = { "setargrupo", "removevip", "removervip" })
	public List<String> groupsetCompleter(CommandArgs args) {
		if (args.isPlayer()) {
			if (args.getArgs().length == 1) {
				ArrayList<String> players = new ArrayList<>();
				for (ProxiedPlayer p : BungeeMain.getPlugin().getProxy().getPlayers()) {
					if (p.getName().toLowerCase().startsWith(args.getArgs()[0].toLowerCase())) {
						players.add(p.getName());
					}
				}
				return players;
			} else if (args.getArgs().length == 2) {
				ArrayList<String> grupos = new ArrayList<>();
				for (Group group : Group.values()) {
					if (group.toString().toLowerCase().startsWith(args.getArgs()[1].toLowerCase())) {
						grupos.add(group.toString());
					}
				}
				return grupos;
			}
		}
		return new ArrayList<>();
	}

	@Completer(name = "givevip", aliases = { "darvip" })
	public List<String> givevipCompleter(CommandArgs args) {
		if (args.isPlayer()) {
			if (args.getArgs().length == 1) {
				ArrayList<String> players = new ArrayList<>();
				for (ProxiedPlayer p : BungeeMain.getPlugin().getProxy().getPlayers()) {
					if (p.getName().toLowerCase().startsWith(args.getArgs()[0].toLowerCase())) {
						players.add(p.getName());
					}
				}
				return players;
			} else if (args.getArgs().length == 3) {
				ArrayList<String> grupos = new ArrayList<>();
				for (Group group : Group.values()) {
					if (group.toString().toLowerCase().startsWith(args.getArgs()[2].toLowerCase())) {
						grupos.add(group.toString());
					}
				}
				return grupos;
			}
		}
		return new ArrayList<>();
	}

}
