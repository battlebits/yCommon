package br.com.battlebits.ycommon.bungee.commands.register;

import java.util.ArrayList;
import java.util.List;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.Completer;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TabCompleter extends CommandClass {

	@Completer(name = "account", aliases = { "ban", "banir", "tempban", "tempbanir", "mute", "mutar", "tempmute", "tempmutar", "unban", "desbanir", "unmute", "desmutar", "report", "screenshare", "ss", "finder", "" })
	public List<String> player(CommandArgs args) {
		if (args.isPlayer()) {
			if (args.getArgs().length == 1) {
				ArrayList<String> players = new ArrayList<>();
				for (ProxiedPlayer p : BungeeMain.getPlugin().getProxy().getPlayers()) {
					if (p.getName().toLowerCase().startsWith(args.getArgs()[0].toLowerCase())) {
						players.add(p.getName());
					}
				}
				return players;
			}
		}
		return new ArrayList<>();
	}
}
