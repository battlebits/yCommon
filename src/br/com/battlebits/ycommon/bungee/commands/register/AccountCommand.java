package br.com.battlebits.ycommon.bungee.commands.register;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

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
import br.com.battlebits.ycommon.common.payment.RankType;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.tag.Tag;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import br.com.battlebits.ycommon.common.utils.DateUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class AccountCommand extends CommandClass {

	@Command(name = "account", aliases = { "acc" }, usage = "/<command> [player]")
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
					sender.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW + "Multiplicadores de XP: " + player.getDoubleXpMultiplier()));
					if (player.isDoubleXPActivated()) {
						String tempo = DateUtils.formatDifference(language, (player.getLastActivatedMultiplier() - System.currentTimeMillis()) / 1000);
						sender.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW + "Multiplicador de DoubleXP ativado e durara " + tempo + "."));
					}
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

	@Command(name = "doublexp", usage = "/<command>")
	public void doublexp(CommandArgs cmdArgs) {
		if (!cmdArgs.isPlayer())
			return;
		CommandSender sender = cmdArgs.getSender();
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId());
		Language lang = player.getLanguage();
		String banPrefix = Translate.getTranslation(lang, "command-doublexp-prefix") + " ";
		if (player.isDoubleXPActivated()) {
			sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(lang, "command-doublexp-already-activated")));
			return;
		}
		if (player.getDoubleXpMultiplier() <= 0 && !player.hasGroupPermission(Group.MODPLUS)) {
			sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(lang, "command-doublexp-dont-have")));
			return;
		}
		player.activateDoubleXp();
		sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(lang, "command-doublexp-success")));
		ProxiedPlayer p = BungeeCord.getInstance().getPlayer(player.getUuid());
		if (p != null && p.getServer() != null) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("ActivateDoubleXP");
			p.getServer().sendData(BattlebitsAPI.getBungeeChannel(), out.toByteArray());
		}
	}

	@Command(name = "doublexpadd", groupToUse = Group.DONO, noPermMessageId = "command-doublexpadd-no-access")
	public void torneioadd(CommandArgs cmdArgs) {
		final CommandSender sender = cmdArgs.getSender();
		final String[] args = cmdArgs.getArgs();
		Language lang = BattlebitsAPI.getDefaultLanguage();
		if (cmdArgs.isPlayer()) {
			lang = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getLanguage();
		}
		final Language language = lang;
		final String groupSetPrefix = Translate.getTranslation(lang, "command-doublexpadd-prefix") + " ";
		if (args.length != 2) {
			sender.sendMessage(TextComponent.fromLegacyText(groupSetPrefix + Translate.getTranslation(lang, "command-doublexpadd-usage").replace("%command%", cmdArgs.getLabel())));
			return;
		}
		int doublexp = 0;
		try {
			doublexp = Integer.valueOf(args[1]);
		} catch (Exception e) {
			sender.sendMessage(TextComponent.fromLegacyText(groupSetPrefix + Translate.getTranslation(lang, "command-doublexpadd-usage").replace("%command%", cmdArgs.getLabel())));
			return;
		}
		final int d = doublexp;
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
				player.addDoubleXpMultiplier(d);
				if (!player.isOnline()) {
					BattlebitsAPI.getAccountCommon().saveBattlePlayer(player);
				}
				String message = groupSetPrefix + Translate.getTranslation(language, "command-doublexpadd-add-success");
				message = message.replace("%player%", player.getUserName() + "(" + player.getUuid().toString().replace("-", "") + ")");
				sender.sendMessage(TextComponent.fromLegacyText(message));
			}
		});
	}
}
