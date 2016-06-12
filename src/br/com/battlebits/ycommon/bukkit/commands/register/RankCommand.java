package br.com.battlebits.ycommon.bukkit.commands.register;

import java.util.HashMap;

import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.enums.Liga;

public class RankCommand extends CommandClass {

	@Command(name = "ranking", aliases = { "league", "liga", "rank" })
	public void ranking(CommandArgs args) {
		if (args.isPlayer()) {
			Player p = args.getPlayer();
			BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			player.sendMessage("league-prefix", "league-information");
			for (int i = Liga.values().length; i > 0; i--) {
				Liga rank = Liga.values()[i - 1];
				p.sendMessage(rank.getSymbol() + " " + rank.toString());
			}

			HashMap<String, String> replaces = new HashMap<>();
			replaces.put("%league%", player.getLiga().toString());
			replaces.put("%symbol%", player.getLiga().getSymbol());
			replaces.put("%xp%", player.getXp() + "");

			player.sendMessage("league-league-actual", replaces);
			player.sendMessage("league-xp-actual", replaces);

			if (player.getLiga().ordinal() + 1 < Liga.values().length) {

				Liga rank = Liga.values()[player.getLiga().ordinal() + 1];
				replaces.put("%nextLeague%", rank.toString());
				replaces.put("%nextSymbol%", rank.getSymbol());
				replaces.put("%missingXp%", (player.getLiga().getMaxXp() - player.getXp() + 1) + "");

				player.sendMessage("league-next-league", replaces);
				player.sendMessage("league-next-xp-league", replaces);
			} else {
				player.sendMessage("league-biggest");
			}

		}
	}
}
