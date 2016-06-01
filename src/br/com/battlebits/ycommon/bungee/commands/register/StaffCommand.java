package br.com.battlebits.ycommon.bungee.commands.register;

import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.Command;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import net.md_5.bungee.api.chat.TextComponent;

public class StaffCommand extends CommandClass {

	@Command(name = "staffchat", aliases = { "sc" }, groupToUse = Group.YOUTUBERPLUS, noPermMessageId = "command-staffchat-no-access")
	public void staffchat(CommandArgs args) {
		if (!args.isPlayer()) {
			args.getSender().sendMessage(TextComponent.fromLegacyText("COMANDO APENAS PARA PLAYERS"));
			return;
		}
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(args.getPlayer().getUniqueId());
		boolean active = player.getConfiguration().isStaffChatEnabled();
		player.getConfiguration().setStaffChatEnabled(!active);
		args.getSender().sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(player.getLanguage(), "command-staffchat-" + (active ? "disabled" : "enabled"))));
	}

}
