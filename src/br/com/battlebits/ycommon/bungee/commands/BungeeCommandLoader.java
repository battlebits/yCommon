package br.com.battlebits.ycommon.bungee.commands;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.utils.ClassGetter;

public class BungeeCommandLoader {

	private BungeeCommandFramework framework;

	public BungeeCommandLoader(BungeeCommandFramework framework) {
		this.framework = framework;
	}

	public int loadCommandsFromPackage(String packageName) {
		int i = 0;
		for (Class<?> commandClass : ClassGetter.getClassesForPackage(BungeeMain.getPlugin().getClass(), packageName)) {
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
