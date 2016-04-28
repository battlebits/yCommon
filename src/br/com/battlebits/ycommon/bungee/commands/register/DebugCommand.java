package br.com.battlebits.ycommon.bungee.commands.register;

import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.Command;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.commands.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import net.md_5.bungee.api.chat.TextComponent;

public class DebugCommand extends CommandClass {

	@Command(name = "debug", usage = "/<command>", groupToUse = Group.DONO, noPermMessageId = "command-no-access")
	public void debug(CommandArgs cmdArgs) {
		cmdArgs.getSender().sendMessage(TextComponent.fromLegacyText(BattlebitsAPI.getAccountCommon().getPlayers().size() + " players no cache"));
	}
}
