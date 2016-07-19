package br.com.battlebits.ycommon.bukkit.commands.register;

import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;

public class ClanCommand extends CommandClass {

	 /*
	  * clan create|criar <clanName> <abbreviation>
	  * clan join|entrar <clanName>
	  * clan invite|convidar <player|uuid>
	  * clan removeinvite|removerconvite <player|uuid>
	  * clan promote|promover <player|uuid>
	  * clan demote|rebaixar <player|uuid> 
	  * clan kick|expulsar|remover|remove|ban|banir <player|uuid> 
	  * clan info|informacao <clanName>
	  * clan changeabb|mudarsigla <abbreviation>
	  * clan disband|deletar
	  * 
	  * clan chat
	 * 
	 */
	
	@Command(name = "clan")
	public void clan(CommandArgs args) {
		
	}

}
