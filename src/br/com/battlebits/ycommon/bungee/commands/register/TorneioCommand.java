package br.com.battlebits.ycommon.bungee.commands.register;

import java.util.UUID;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

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
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TorneioCommand extends CommandClass {

	@Command(name = "torneioadd", aliases = { "tadd" }, groupToUse = Group.DONO, noPermMessageId = "command-torneio-no-access")
	public void torneioadd(CommandArgs cmdArgs) {
		final CommandSender sender = cmdArgs.getSender();
		final String[] args = cmdArgs.getArgs();
		Language lang = BattlebitsAPI.getDefaultLanguage();
		if (cmdArgs.isPlayer()) {
			lang = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getLanguage();
		}
		final Language language = lang;
		final String groupSetPrefix = Translate.getTranslation(lang, "command-torneio-prefix") + " ";
		if (args.length != 1) {
			sender.sendMessage(TextComponent.fromLegacyText(groupSetPrefix + Translate.getTranslation(lang, "command-torneio-remove-usage")));
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
				player.setTorneio(BattlebitsAPI.getDefaultTorneio());
				player.setTag(Tag.TORNEIO);
				if (!player.isOnline()) {
					BattlebitsAPI.getAccountCommon().saveBattlePlayer(player);
				}
				ProxiedPlayer pPlayer = BungeeMain.getPlugin().getProxy().getPlayer(player.getUuid());
				if (pPlayer != null) {
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("TorneioAdd");
					if (pPlayer.getServer() != null)
						pPlayer.getServer().sendData(BattlebitsAPI.getBungeeChannel(), out.toByteArray());
				}
				String message = groupSetPrefix + Translate.getTranslation(language, "command-torneio-add-success");
				message = message.replace("%player%", player.getUserName() + "(" + player.getUuid().toString().replace("-", "") + ")");
				sender.sendMessage(TextComponent.fromLegacyText(message));
			}
		});
	}

	@Command(name = "torneioremove", aliases = { "tremove" }, groupToUse = Group.DONO, noPermMessageId = "command-torneio-no-access")
	public void torneioremove(CommandArgs cmdArgs) {
		final CommandSender sender = cmdArgs.getSender();
		final String[] args = cmdArgs.getArgs();
		Language lang = BattlebitsAPI.getDefaultLanguage();
		if (cmdArgs.isPlayer()) {
			lang = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getLanguage();
		}
		final Language language = lang;
		final String groupSetPrefix = Translate.getTranslation(lang, "command-torneio-prefix") + " ";
		if (args.length != 1) {
			sender.sendMessage(TextComponent.fromLegacyText(groupSetPrefix + Translate.getTranslation(lang, "command-torneio-remove-usage")));
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
				player.setTorneio(null);
				if (!player.isOnline()) {
					BattlebitsAPI.getAccountCommon().saveBattlePlayer(player);
				}
				ProxiedPlayer pPlayer = BungeeMain.getPlugin().getProxy().getPlayer(player.getUuid());
				if (pPlayer != null) {
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("TorneioRemove");
					if (pPlayer.getServer() != null)
						pPlayer.getServer().sendData(BattlebitsAPI.getBungeeChannel(), out.toByteArray());
				}
				String message = groupSetPrefix + Translate.getTranslation(language, "command-torneio-remove-success");
				message = message.replace("%player%", player.getUserName() + "(" + player.getUuid().toString().replace("-", "") + ")");
				sender.sendMessage(TextComponent.fromLegacyText(message));
			}
		});
	}
}
