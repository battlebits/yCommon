package br.com.battlebits.ycommon.bungee.commands.register;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.Command;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.banmanager.constructors.Ban;
import br.com.battlebits.ycommon.common.banmanager.constructors.Mute;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.enums.ServerStaff;
import br.com.battlebits.ycommon.common.payment.enums.RankType;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.tag.Tag;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import br.com.battlebits.ycommon.common.utils.DateUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class AccountCommand extends CommandClass {

	@Command(name = "account", usage = "/<command> [player]")
	public void account(CommandArgs cmdArgs) {
		final CommandSender sender = cmdArgs.getSender();
		final String[] args = cmdArgs.getArgs();
		Language lang = BattlebitsAPI.getDefaultLanguage();
		if (cmdArgs.isPlayer()) {
			lang = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getLanguage();
		}
		final Language language = lang;
		final String banPrefix = Translate.getTranslation(lang, "command-account-prefix") + " ";
		if (args.length > 1) {
			sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(lang, "command-account-usage").replace("%command%", cmdArgs.getLabel())));
			return;
		}
		BungeeCord.getInstance().getScheduler().runAsync(BungeeMain.getPlugin(), new Runnable() {
			public void run() {
				UUID uuid = null;
				if (args.length > 0) {
					uuid = BattlebitsAPI.getUUIDOf(args[0]);
				} else {
					if (cmdArgs.isPlayer()) {
						uuid = cmdArgs.getPlayer().getUniqueId();
					}
				}
				if (uuid == null) {
					sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "player-not-exist")));
					return;
				}
				BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
				if (player == null) {
					try {
						player = BungeeMain.getPlugin().getAccountManager().loadBattlePlayer(uuid);
					} catch (Exception e) {
						e.printStackTrace();
						sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "player-cant-request-offline")));
						return;
					}
					if (player == null) {
						sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "player-never-joined")));
						return;
					}
				}
				sender.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW + "Informacoes sobre o Jogador " + player.getUserName() + "(" + uuid.toString().replace("-", "") + ")"));
				sender.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW.toString() + ChatColor.UNDERLINE + "---------------------------------------------"));
				sender.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW + "Grupo do servidor atual: " + player.getServerGroup().toString()));
				sender.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW + "Liga atual: " + player.getLiga()));
				sender.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW + "XP: " + player.getXp()));
				if (cmdArgs.isPlayer() && cmdArgs.getPlayer().getUniqueId() == uuid) {
					sender.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW + "Money: " + player.getMoney()));
					sender.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW + "Fichas: " + player.getFichas()));
				}
				for (Entry<RankType, Long> entry : player.getRanks().entrySet()) {
					sender.sendMessage(TextComponent.fromLegacyText(Tag.valueOf(entry.getKey().toString()).getPrefix(language) + ChatColor.YELLOW + " expira em " + DateUtils.formatDifference(language, (entry.getValue() - System.currentTimeMillis()) / 1000)));
				}
				for (Entry<ServerStaff, Group> staff : player.getGroups().entrySet()) {
					sender.sendMessage(TextComponent.fromLegacyText(Tag.valueOf(staff.getValue().toString()).getPrefix(language) + ChatColor.YELLOW + " do server " + staff.getKey().toString()));
				}
				Ban ban = player.getBanHistory().getActualBan();
				if (ban != null) {
					if (!ban.isPermanent()) {
						String tempo = DateUtils.getDifferenceFormat(language, ban.getDuration());
						sender.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW + "Foi banido por " + ban.getBannedBy() + " e durara " + tempo + ". Motivo: " + ban.getReason()));
					} else {
						Date date = new Date(ban.getBanTime());
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						sender.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW + "Foi banido permanentemente por " + ban.getBannedBy() + " no dia " + df.format(date) + ". Motivo: " + ban.getReason()));
					}
				}
				Mute mute = player.getBanHistory().getActualMute();
				if (mute != null) {
					if (!mute.isPermanent()) {
						String tempo = DateUtils.getDifferenceFormat(language, mute.getDuration());
						sender.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW + "Foi mutado por " + mute.getMutedBy() + " e durara " + tempo + ". Motivo: " + mute.getReason()));
					} else {
						Date date = new Date(mute.getMuteTime());
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						sender.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW + "Foi mutado permanentemente por " + mute.getMutedBy() + " no dia " + df.format(date) + ". Motivo: " + mute.getReason()));
					}
				}
				sender.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW.toString() + ChatColor.UNDERLINE + "---------------------------------------------"));
			}
		});
	}
}
