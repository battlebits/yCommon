package br.com.battlebits.ycommon.bukkit.commands.register;

import java.io.IOException;

import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;

public class DebugCommand extends CommandClass {

	@Command(name = "reloadbukkittranslation", usage = "/<command>", aliases = { "rlbukkittranslations", "rlbukkit" }, groupToUse = Group.DONO, noPermMessageId = "command-no-access")
	public void reloadbungeetranslation(CommandArgs cmdArgs) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					BukkitMain.getPlugin().loadTranslations();
					cmdArgs.getSender().sendMessage("Traducoes recarregadas");
				} catch (IOException e) {
					e.printStackTrace();
					cmdArgs.getSender().sendMessage("Falha ao recarregar traduções");
				}
			}
		}.runTaskAsynchronously(BukkitMain.getPlugin());
	}

	@Command(name = "raminfo", usage = "/<command>", groupToUse = Group.DONO, noPermMessageId = "command-no-access")
	public void raminfo(CommandArgs cmdArgs) {
		double total = Runtime.getRuntime().maxMemory();
		double free = Runtime.getRuntime().freeMemory();
		double used = total - free;

		double divisor = 1024 * 1024 * 1024;
		double usedPercentage = (used / total) * 100;

		cmdArgs.getSender().sendMessage((total / divisor) + "GB de memoria RAM Maxima");

		cmdArgs.getSender().sendMessage((used / divisor) + "GB de memoria RAM Usada");
		cmdArgs.getSender().sendMessage((free / divisor) + "GB de memoria RAM Livre");
		cmdArgs.getSender().sendMessage(usedPercentage + "% da memoria RAM");
	}
}
