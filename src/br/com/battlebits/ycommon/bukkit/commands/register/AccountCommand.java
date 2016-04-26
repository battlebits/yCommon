package br.com.battlebits.ycommon.bukkit.commands.register;

import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.commands.CommandClass;
import net.md_5.bungee.api.chat.TextComponent;

public class AccountCommand extends CommandClass {

	@Command(name = "account")
	public void account(CommandArgs args) {

	}

	@Command(name = "league")
	public void league(CommandArgs args) {

	}

	@Command(name = "ranking")
	public void ranking(CommandArgs args) {

	}

	@Command(name = "tag")
	public void tag(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			Player p = cmdArgs.getPlayer();
			// String[] args = cmdArgs.getArgs();
			BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			TextComponent[] message = new TextComponent[(player.getTags().size() * 2) + 1];
			p.spigot().sendMessage(message);
		} else {
			cmdArgs.getSender().sendMessage("§4§lERRO §fComando disponivel apenas §c§lin-game");
		}
	}

}
