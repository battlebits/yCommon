package br.com.battlebits.ycommon.bukkit.commands.register;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.bukkit.commands.CommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.CommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.banmanager.constructors.Ban;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import br.com.battlebits.ycommon.common.utils.mojang.UUIDFetcher;

public class BanCommand {

	@Command(name = "ban", usage = "/<command> <player> <reason>", aliases = { "banir" })
	public void ban(CommandArgs cmdArgs) {
		final CommandSender sender = cmdArgs.getSender();
		final String[] args = cmdArgs.getArgs();
		Language lang = BattlebitsAPI.getDefaultLanguage();
		if (cmdArgs.isPlayer()) {
			BukkitPlayer player = getBukkitPlayer(cmdArgs.getPlayer().getUniqueId());
			lang = player.getLanguage();
			if (!player.hasGroupPermission(Group.TRIAL)) {
				cmdArgs.getPlayer().sendMessage(Translate.getTranslation(lang, "ban-prefix") + " " + Translate.getTranslation(lang, "no-permission"));
				return;
			}
		}
		final Language language = lang;
		final String banPrefix = Translate.getTranslation(lang, "ban-prefix");
		if (args.length < 2) {
			sender.sendMessage(banPrefix + " " + Translate.getTranslation(lang, "ban-usage"));
			return;
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				Player target = Bukkit.getPlayer(args[0]);
				UUID uuid = null;
				if (target != null) {
					uuid = target.getUniqueId();
				} else {
					try {
						uuid = UUIDFetcher.getUUIDOf(args[0]);
					} catch (Exception e) {
						sender.sendMessage(banPrefix + " " + Translate.getTranslation(language, "player-not-exist"));
						return;
					}
				}
				BukkitPlayer player = getBukkitPlayer(uuid);
				if (player == null) {
					if (sender instanceof Player) {
						if (getBukkitPlayer(cmdArgs.getPlayer().getUniqueId()).getServerGroup().equals(Group.TRIAL)) {
							sender.sendMessage(banPrefix + " " + Translate.getTranslation(language, "trial-no-prefix"));
							return;
						}
					}
					try {
						player = BukkitMain.getPlugin().getAccountManager().getOfflinePlayer(uuid);
					} catch (Exception e) {
						e.printStackTrace();
						sender.sendMessage(banPrefix + " " + Translate.getTranslation(language, "cant-request-offline"));
						return;
					}
					Ban actualBan = player.getBanHistory().getActualBan();
					if(actualBan != null && !actualBan.isUnbanned() && actualBan.isPermanent()) {
						sender.sendMessage(banPrefix + " " + Translate.getTranslation(language, "already-banned"));
						return;
					}
					if(player.isStaff()) {
						
					}
				}
			}
		}.runTaskAsynchronously(BukkitMain.getPlugin());
	}

	private BukkitPlayer getBukkitPlayer(UUID uuid) {
		return (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
	}

	@Command(name = "tempban")
	public void tempban(CommandArgs args) {

	}

	@Command(name = "unban")
	public void unban(CommandArgs args) {

	}

	@Command(name = "mute")
	public void mute(CommandArgs args) {

	}

	@Command(name = "tempmute")
	public void tempmute(CommandArgs args) {

	}

	@Command(name = "unmute")
	public void unmute(CommandArgs args) {

	}

}
