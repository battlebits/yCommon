package br.com.battlebits.ycommon.bukkit.commands.register;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.api.admin.AdminMode;
import br.com.battlebits.ycommon.bukkit.api.chat.ChatAPI;
import br.com.battlebits.ycommon.bukkit.api.chat.ChatAPI.ChatState;
import br.com.battlebits.ycommon.bukkit.api.vanish.VanishAPI;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.commands.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;

public class AdminCommand extends CommandClass {

	@Command(name = "admin", groupToUse = Group.TRIAL, noPermMessageId = "command-admin-no-access")
	public void admin(CommandArgs args) {
		if (args.isPlayer()) {
			Player p = args.getPlayer();
			if (AdminMode.getInstance().isAdmin(p)) {
				AdminMode.getInstance().setPlayer(p);
			} else {
				AdminMode.getInstance().setAdmin(p);
			}
		}
	}

	@Command(name = "updatevanish", groupToUse = Group.TRIAL, noPermMessageId = "command-admin-no-access")
	public void updatevanish(CommandArgs args) {
		if (args.isPlayer()) {
			Player p = args.getPlayer();
			VanishAPI.getInstance().updateVanishToPlayer(p);
		}
	}

	@Command(name = "visible", aliases = { "vis", "visivel" }, groupToUse = Group.TRIAL, noPermMessageId = "command-vanish-no-access")
	public void visible(CommandArgs args) {
		if (args.isPlayer()) {
			Player p = args.getPlayer();
			VanishAPI.getInstance().showPlayer(p);
			BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).sendMessage("command-vanish-prefix", "command-vanish-visible-all");
		}
	}

	@Command(name = "invisible", aliases = { "invis", "invisivel" }, groupToUse = Group.TRIAL, noPermMessageId = "command-vanish-no-access")
	public void invisible(CommandArgs args) {
		if (args.isPlayer()) {
			Player p = args.getPlayer();
			BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			Group group = Group.NORMAL;
			if (args.getArgs().length > 0) {
				try {
					group = Group.valueOf(args.getArgs()[0].toUpperCase());
				} catch (Exception e) {
					bP.sendMessage("command-vanish-prefix", "command-vanish-rank-not-exist");
					return;
				}
				if (group.ordinal() >= bP.getServerGroup().ordinal()) {
					bP.sendMessage("command-vanish-prefix", "command-vanish-rank-high");
					return;
				}
			} else
				group = VanishAPI.getInstance().hidePlayer(p);
			if (group.ordinal() < Group.DEV.ordinal()) {
				bP.sendMessage("command-vanish-prefix", "command-vanish-rank-lowest-allowed");
				return;
			}
			VanishAPI.getInstance().setPlayerVanishToGroup(p, group);
			HashMap<String, String> map = new HashMap<>();
			map.put("%invisible%", group.toString());
			bP.sendMessage("command-vanish-prefix", "command-vanish-invisible", map);
		}
	}

	@Command(name = "inventorysee", aliases = { "invsee", "inv" }, groupToUse = Group.TRIAL, noPermMessageId = "command-inventorysee-no-access")
	public void inventorysee(CommandArgs args) {
		if (args.isPlayer()) {
			Player p = args.getPlayer();
			BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			if (args.getArgs().length == 0) {
				HashMap<String, String> map = new HashMap<>();
				map.put("%command%", args.getLabel());
				bP.sendMessage("command-inventorysee-prefix", "command-inventorysee-usage", map);
			} else {
				Player t = Bukkit.getPlayer(args.getArgs()[0]);
				if (t != null) {
					HashMap<String, String> map = new HashMap<>();
					map.put("%player%", t.getName());
					bP.sendMessage("command-inventorysee-prefix", "command-inventorysee-success", map);
					p.openInventory(t.getInventory());
				} else {
					bP.sendMessage("command-inventorysee-prefix", "command-inventorysee-not-found");
				}
			}
		}
	}

	@Command(name = "chat", groupToUse = Group.MOD, noPermMessageId = "command-chat-no-access")
	public void chat(CommandArgs args) {
		if (args.isPlayer()) {
			BattlePlayer bP = BattlebitsAPI.getAccountCommon().getBattlePlayer(args.getPlayer().getUniqueId());
			if (args.getArgs().length == 1) {
				if (args.getArgs()[0].equalsIgnoreCase("on")) {
					if (ChatAPI.getInstance().getChatState() == ChatState.ENABLED) {
						bP.sendMessage("command-chat-prefix", "command-chat-already-enabled");
						return;
					}
					ChatAPI.getInstance().setChatState(ChatState.ENABLED);
					BukkitMain.getPlugin().broadcastMessage("command-chat-prefix", "command-chat-enabled");
				} else if (args.getArgs()[0].equalsIgnoreCase("off")) {
					if (ChatAPI.getInstance().getChatState() == ChatState.YOUTUBER) {
						bP.sendMessage("command-chat-prefix", "command-chat-already-disabled");
						return;
					}
					ChatAPI.getInstance().setChatState(ChatState.YOUTUBER);
					BukkitMain.getPlugin().broadcastMessage("command-chat-prefix", "command-chat-disabled");
				} else {
					bP.sendMessage("command-chat-prefix", "command-chat-usage");
				}
			} else {
				bP.sendMessage("command-chat-prefix", "command-chat-usage");
			}
		}
	}

	@Command(name = "clearchat", aliases = { "limparchat" }, groupToUse = Group.TRIAL, noPermMessageId = "command-chat-no-access")
	public void clearchat(CommandArgs args) {
		if (args.isPlayer()) {
			HashMap<String, String> map = new HashMap<>();
			map.put("%player%", args.getPlayer().getName());
			for (Player p : Bukkit.getOnlinePlayers()) {
				for (int i = 0; i < 100; i++)
					p.sendMessage("");
				BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).sendMessage("command-chat-prefix", "command-chat-success", map);
			}
		}
	}
}
