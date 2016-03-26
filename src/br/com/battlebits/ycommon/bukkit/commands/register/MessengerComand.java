package br.com.battlebits.ycommon.bukkit.commands.register;

import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.bukkit.commands.CommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.CommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;

public class MessengerComand {

	@Command(name = "tell")
	public void tell(CommandArgs args) {
		if (args.isPlayer()) {
			BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(args.getPlayer().getUniqueId());
			if (args.getArgs().length == 0) {
				args.getPlayer().sendMessage("Seu tell está " + player.getConfiguration().isTellEnabled());
			} else {
				String what = args.getArgs()[0];
				if(what.equalsIgnoreCase("off")){
					player.getConfiguration().setTellEnabled(false);
				} else {
					player.getConfiguration().setTellEnabled(true);
				}
			}
		}
	}

}
