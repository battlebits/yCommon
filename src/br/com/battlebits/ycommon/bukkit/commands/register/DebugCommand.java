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

}
