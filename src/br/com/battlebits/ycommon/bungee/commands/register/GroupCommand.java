package br.com.battlebits.ycommon.bungee.commands.register;

import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.Command;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.commands.CommandClass;
import br.com.battlebits.ycommon.common.enums.ServerType;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class GroupCommand extends CommandClass {

	@Command(name = "groupset", usage = "/<command> <player> <group>", groupToUse = Group.MANAGER, noPermMessageId = "command-groupset-no-access")
	public void groupset(CommandArgs cmdArgs) {
		final CommandSender sender = cmdArgs.getSender();
		if (!cmdArgs.isPlayer()) {
			sender.sendMessage(TextComponent.fromLegacyText("§4§lERRO §fComando disponivel apenas §c§lin-game"));
			return;
		}
		final String[] args = cmdArgs.getArgs();
		Language lang = BattlebitsAPI.getDefaultLanguage();
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
		BattlePlayer battleSender = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId());
		if (battleSender.getServerGroup() != Group.DONO)
			if (group.ordinal() > Group.STREAMER.ordinal()) {
				sender.sendMessage(TextComponent.fromLegacyText(groupSetPrefix + Translate.getTranslation(lang, "command-groupset-group-not-owner")));
				return;
			}
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
				ServerType serverType = battleSender.getServerConnectedType();
				if (group.ordinal() > Group.STREAMER.ordinal())
					serverType = ServerType.NETWORK;
				Group actualGroup = player.getGroups().containsKey(serverType) ? player.getGroups().get(serverType) : Group.NORMAL;
				if (actualGroup == group) {
					sender.sendMessage(TextComponent.fromLegacyText(groupSetPrefix + Translate.getTranslation(language, "command-groupset-player-already-group")));
					return;
				}

				if (group == Group.NORMAL) {
					if (player.getGroups().containsKey(ServerType.NETWORK) && battleSender.getServerGroup() == Group.DONO)
						serverType = ServerType.NETWORK;
					player.getGroups().remove(serverType);
				} else {
					player.getGroups().put(serverType, group);
				}
				if (!player.isOnline()) {
					BattlebitsAPI.getAccountCommon().saveBattlePlayer(player);
				}
				String message = groupSetPrefix + Translate.getTranslation(language, "command-groupset-change-group");
				message = message.replace("%player%", player.getUserName() + "(" + player.getUuid().toString().replace("-", "") + ")");
				message = message.replace("%group%", group.name());
				message = message.replace("%serverType%", serverType != null ? serverType.toString() : "null");
				sender.sendMessage(TextComponent.fromLegacyText(message));

			}
		});
	}

}
