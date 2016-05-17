package br.com.battlebits.ycommon.bukkit.commands.register;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.networking.packets.CPacketCommandRun;

public class IW4Command extends CommandClass {

	@Command(name = "iw4commandforbungee", aliases = { "unban", "unmute", "givevip", "ban" })
	public void account(CommandArgs args) {
		if (args.isPlayer())
			return;
		String command = args.getLabel();
		for (String arg : args.getArgs()) {
			command = command + " " + arg;
		}
		BukkitMain.getPlugin().getClient().sendPacket(new CPacketCommandRun(command));
	}
}
