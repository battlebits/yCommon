package br.com.battlebits.ycommon.bukkit.commands;

import java.util.HashMap;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.utils.ClassGetter;

public class CommandLoader {
	private HashMap<String, Object> abilities = new HashMap<String, Object>();
	private CommandFramework framework;
	
	public CommandLoader(CommandFramework framework) {
		this.framework = framework;
		initializeAllAbilitiesInPackage("me.flame.HungerGames.Kits");
	}

	public void initializeAllAbilitiesInPackage(String packageName) {
		int i = 0;
		for (Class<?> abilityClass : ClassGetter.getClassesForPackage(BukkitMain.getPlugin(), packageName)) {
			try {
				Object abilityListener = abilityClass.newInstance();
				abilities.put(abilityClass.getSimpleName(), abilityListener);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.print("Erro ao carregar a habilidade " + abilityClass.getSimpleName());
			}
			i++;
		}
		BattlebitsAPI.getLogger().info(i + " habilidades carregadas!");
	}

	public void registerAbilityListeners() {
		for (Object commandClass : abilities.values()) {
			framework.registerCommands(commandClass);
		}
	}
}
