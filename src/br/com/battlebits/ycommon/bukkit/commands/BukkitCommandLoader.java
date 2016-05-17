package br.com.battlebits.ycommon.bukkit.commands;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.utils.ClassGetter;

public class BukkitCommandLoader {

	private BukkitCommandFramework framework;

	public BukkitCommandLoader(BukkitCommandFramework framework) {
		this.framework = framework;
	}

	public int loadCommandsFromPackage(String packageName) {
		int i = 0;
		for (Class<?> commandClass : ClassGetter.getClassesForPackage(BukkitMain.getPlugin().getClass(), packageName)) {
			if (CommandClass.class.isAssignableFrom(commandClass)) {
				try {
					CommandClass commands = (CommandClass) commandClass.newInstance();
					framework.registerCommands(commands);
				} catch (Exception e) {
					e.printStackTrace();
					BattlebitsAPI.getLogger().warning("Erro ao carregar comandos da classe " + commandClass.getSimpleName() + "!");
				}
				i++;
			}
		}
		return i;
	}

}
