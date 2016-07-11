package br.com.battlebits.ycommon.bukkit.commands.register;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.bukkit.utils.FakePlayerUtils;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;

public class YoutubeCommand extends CommandClass {

	@Command(name = "fake", groupToUse = Group.YOUTUBER, noPermMessageId = "command-fake-no-access", runAsync = true)
	public void fake(CommandArgs args) {
		if (!args.isPlayer()) {
			args.getSender().sendMessage("COMANDO PARA PLAYERS");
			return;
		}
		Player p = args.getPlayer();
		BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
		if (!bP.getServerGroup().toString().contains("YOUTUBER") && !bP.hasGroupPermission(Group.MODPLUS)) {
			p.sendMessage(Translate.getTranslation(bP.getLanguage(), "command-fake-no-access"));
			return;
		}
		String fakePrefix = Translate.getTranslation(bP.getLanguage(), "command-fake-prefix") + " ";
		if (args.getArgs().length != 1) {
			p.sendMessage(fakePrefix + Translate.getTranslation(bP.getLanguage(), "command-fake-usage"));
			return;
		}
		String playerName = args.getArgs()[0];

		if (playerName.equalsIgnoreCase(bP.getUserName())) {
			fakeremove(args);
			return;
		}

		if (!FakePlayerUtils.validateName(playerName)) {
			p.sendMessage(fakePrefix + Translate.getTranslation(bP.getLanguage(), "command-fake-invalid"));
			return;
		}
		if (BattlebitsAPI.getUUIDOf(playerName) != null) {
			p.sendMessage(fakePrefix + Translate.getTranslation(bP.getLanguage(), "command-fake-player-exists"));
			return;
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				BukkitMain.getPlugin().getTagManager().removePlayerTag(p);
				FakePlayerUtils.removePlayerSkin(p, false);
				FakePlayerUtils.changePlayerName(p, playerName, true);
				bP.setTag(bP.getTag());
				bP.setFakeName(playerName);
				p.sendMessage(fakePrefix + Translate.getTranslation(bP.getLanguage(), "command-fake-changed-success"));
			}
		}.runTask(BukkitMain.getPlugin());
	}

	@Command(name = "fakeremove", aliases = { "removefake", "removerfake" }, groupToUse = Group.YOUTUBER, noPermMessageId = "command-fakeremove-no-access")
	public void fakeremove(CommandArgs args) {
		if (!args.isPlayer()) {
			args.getSender().sendMessage("COMANDO PARA PLAYERS");
			return;
		}
		Player p = args.getPlayer();
		BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
		if (!bP.getServerGroup().toString().contains("YOUTUBER") && !bP.hasGroupPermission(Group.MODPLUS)) {
			p.sendMessage(Translate.getTranslation(bP.getLanguage(), "command-fakeremove-no-access").replace("%command%", args.getLabel()));
			return;
		}
		bP.setFakeName("");

		BukkitMain.getPlugin().getTagManager().removePlayerTag(p);
		FakePlayerUtils.changePlayerSkin(p, bP.getUserName(), bP.getUuid(), false);
		FakePlayerUtils.changePlayerName(p, bP.getUserName(), true);
		bP.setTag(bP.getTag());
		p.sendMessage(Translate.getTranslation(bP.getLanguage(), "command-fakeremove-prefix") + " " + Translate.getTranslation(bP.getLanguage(), "command-fakeremove-changed-success"));
	}

	@Command(name = "changeskin", groupToUse = Group.ULTIMATE, noPermMessageId = "command-changeskin-no-access", runAsync = true)
	public void changeskin(CommandArgs args) {
		if (!args.isPlayer()) {
			args.getSender().sendMessage("COMANDO PARA PLAYERS");
			return;
		}
		Player p = args.getPlayer();
		BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
		String fakePrefix = Translate.getTranslation(bP.getLanguage(), "command-changeskin-prefix") + " ";
		if (args.getArgs().length != 1) {
			p.sendMessage(fakePrefix + Translate.getTranslation(bP.getLanguage(), "command-changeskin-usage"));
			return;
		}
		String playerName = args.getArgs()[0];
		if (!FakePlayerUtils.validateName(playerName)) {
			p.sendMessage(fakePrefix + Translate.getTranslation(bP.getLanguage(), "command-changeskin-invalid"));
			return;
		}
		UUID uuid = BattlebitsAPI.getUUIDOf(playerName);

		if (uuid == null) {
			p.sendMessage(fakePrefix + Translate.getTranslation(bP.getLanguage(), "command-changeskin-player-not-exists"));
			return;
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				FakePlayerUtils.changePlayerSkin(p, playerName, uuid);
				p.sendMessage(fakePrefix + Translate.getTranslation(bP.getLanguage(), "command-changeskin-changed-success"));
			}
		}.runTask(BukkitMain.getPlugin());
	}

}
