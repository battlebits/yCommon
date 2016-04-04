package br.com.battlebits.ycommon.bukkit.commands;

import java.util.HashMap;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.utils.ClassGetter;

public class CommandLoader {
	private HashMap<String, Object> commands = new HashMap<String, Object>();
	private CommandFramework framework;

	public CommandLoader(CommandFramework framework) {
		this.framework = framework;
		initializeAllCommandsInPackage("br.com.battlebits.ycommon.bukkit.commands.register");
	}

	public void initializeAllCommandsInPackage(String packageName) {
		int i = 0;
		for (Class<?> commandClass : ClassGetter.getClassesForPackage(BukkitMain.getPlugin().getClass(), packageName)) {
			if (CommandClass.class.isAssignableFrom(commandClass)) {
				try {
					Object abilityListener = commandClass.newInstance();
					commands.put(commandClass.getSimpleName(), abilityListener);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("Erro ao carregar classe de comando " + commandClass.getSimpleName());
				}
				i++;
			}
		}
		BattlebitsAPI.getLogger().info(i + " classes de comando registradas!");
	}

	public void registerAbilityListeners() {
		for (Object commandClass : commands.values()) {
			framework.registerCommands(commandClass);
		}
	}
}
