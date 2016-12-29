package br.com.battlebits.ycommon.bungee.listeners;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spawl.bungeepackets.connection.BungeeConnection;
import org.spawl.bungeepackets.event.PacketEvent;
import org.spawl.bungeepackets.item.ItemStack;
import org.spawl.bungeepackets.nbt.NBTTagCompound;
import org.spawl.bungeepackets.nbt.NBTTagList;
import org.spawl.bungeepackets.packet.server.OutOpenWindow;
import org.spawl.bungeepackets.packet.server.OutSetSlot;
import org.spawl.bungeepackets.packet.server.OutWindowItems;

import br.com.battlebits.ycommon.bungee.event.UpdateEvent;
import br.com.battlebits.ycommon.bungee.event.UpdateEvent.UpdateType;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.clans.Clan;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.tag.Tag;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import br.com.battlebits.ycommon.common.utils.string.StringLoreUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {

	@EventHandler
	public void onUpdate(UpdateEvent event) {
		if (event.getType() != UpdateType.SECOND)
			return;
		Iterator<BattlePlayer> players = BattlebitsAPI.getAccountCommon().getPlayers().iterator();
		while (players.hasNext()) {
			BattlePlayer player = players.next();
			if (BungeeCord.getInstance().getPlayer(player.getUuid()) == null) {
				if (player.isCacheExpired()) {
					players.remove();
					BattlebitsAPI.debug("REMOVENDO BATTLEPLAYER " + player.getUserName() + " DO CACHE");
				}
			}
		}

		Iterator<Clan> clans = BattlebitsAPI.getClanCommon().getClans().iterator();
		while (clans.hasNext()) {
			Clan clan = clans.next();
			boolean offline = true;
			for (UUID uuid : clan.getParticipants()) {
				if (BungeeCord.getInstance().getPlayer(uuid) != null) {
					offline = false;
					break;
				}
			}
			if (offline) {
				if (clan.isCacheExpired()) {
					BattlebitsAPI.getClanCommon().saveClan(clan);
					clans.remove();
					BattlebitsAPI.debug("REMOVENDO CLAN " + clan.getClanName() + " DO CACHE");
				}
			}
		}
	}

	@EventHandler
	public void onChat(ChatEvent event) {
		if (event.isCommand())
			return;
		if (event.isCancelled())
			return;
		if (!(event.getSender() instanceof ProxiedPlayer))
			return;
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(((ProxiedPlayer) event.getSender()).getUniqueId());
		if (player.getConfiguration().isStaffChatEnabled()) {
			sendStaffMessage(player, event.getMessage());
			event.setCancelled(true);
		} else if (player.getConfiguration().isClanChatEnabled() && player.getClan() != null) {
			sendClanMessage(player, event.getMessage());
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = (byte) -128)
	public void onConnect(ServerConnectedEvent event) {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(event.getPlayer().getUniqueId());
		player.connect(event.getServer().getInfo().getName());
	}

	public static void sendStaffMessage(BattlePlayer bP, String message) {
		for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
			BattlePlayer onlineBp = BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
			if (!onlineBp.hasGroupPermission(Group.YOUTUBERPLUS))
				continue;
			String tag = Tag.valueOf(bP.getServerGroup().toString()).getPrefix(onlineBp.getLanguage());
			String format = tag + (ChatColor.stripColor(tag).trim().length() > 0 ? " " : "") + bP.getUserName() + ChatColor.WHITE + ": ";

			player.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW + "" + ChatColor.BOLD + "[STAFF] " + format + message));
		}
	}

	public static void sendClanMessage(BattlePlayer bP, String message) {
		for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
			BattlePlayer onlineBp = BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
			if (onlineBp == null)
				continue;
			Clan clan = bP.getClan();
			if (!clan.isParticipant(onlineBp))
				continue;

			String tag = "";
			if (clan.isOwner(bP)) {
				tag = ChatColor.WHITE + " - " + ChatColor.DARK_RED + "" + Translate.getTranslation(onlineBp.getLanguage(), "owner");
			} else if (clan.isAdministrator(bP)) {
				tag = ChatColor.WHITE + " - " + ChatColor.RED + "" + Translate.getTranslation(onlineBp.getLanguage(), "admin");
			}
			String format = ChatColor.DARK_RED + "[CLAN" + tag.toUpperCase() + ChatColor.DARK_RED + "] " + ChatColor.GRAY + bP.getUserName() + ChatColor.WHITE + ": ";

			player.sendMessage(TextComponent.fromLegacyText(format + message));
		}

	}

	private Pattern finder = Pattern.compile("§%(([^)]+)%§)");

	@EventHandler
	public void onPacket(PacketEvent event) {
		if (event.getSender() instanceof BungeeConnection && event.getReciever() instanceof ProxiedPlayer) {
			Language lang = BattlePlayer.getLanguage(event.getPlayer().getUniqueId());
			if (event.getPacket() instanceof OutWindowItems) {
				OutWindowItems packet = (OutWindowItems) event.getPacket();
				for (ItemStack item : packet.items) {
					if (item != null) {
						try {
							Field field = ItemStack.class.getField("tag");
							NBTTagCompound compound = (NBTTagCompound) field.get(item);
							NBTTagCompound metadata = compound.getCompound("display");
							if (metadata == null)
								return;
							if (compound.getCompound("display").getString("display-name") == null)
								return;
							if (metadata.getString("display-name") != null) {
								String message = metadata.getString("display-name");
								Matcher matcher = finder.matcher(message);
								while (matcher.find()) {
									message = message.replace(matcher.group(), Translate.getTranslation(lang, matcher.group(2)));
								}
								item.setTitle(message);
							}
							if (metadata.getList("lore") != null) {
								List<String> newlore = new ArrayList<>();
								NBTTagList list = metadata.getList("lore");
								for (int i = 0; i < list.size(); i++) {
									String message = list.getString(i);
									Matcher matcher = finder.matcher(message);
									while (matcher.find()) {
										message = message.replace(matcher.group(), Translate.getTranslation(lang, matcher.group(2)));
									}
									if (message.contains("\n")) {
										for (String s : message.split("\n"))
											newlore.addAll(StringLoreUtils.formatForLore(s));
									} else {
										newlore.addAll(StringLoreUtils.formatForLore(message));
									}
								}
								item.setLore(newlore);
							}
						} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			} else if (event.getPacket() instanceof OutOpenWindow) {
				OutOpenWindow packet = (OutOpenWindow) event.getPacket();
				String name = packet.title;
				if (name != null) {
					String message = name;
					Matcher matcher = finder.matcher(message);
					while (matcher.find()) {
						message = message.replace(matcher.group(), Translate.getTranslation(lang, matcher.group(2)));
					}
					if (event.getPlayer().getPendingConnection().getVersion() < 47)
						packet.title = message.substring(0, message.length() > 32 ? 32 : message.length());
					else
						packet.title = message;
				}
			} else if (event.getPacket() instanceof OutSetSlot) {
				OutSetSlot packet = (OutSetSlot) event.getPacket();
				ItemStack item = packet.item;
				if (item != null) {
					try {
						Field field = ItemStack.class.getField("tag");
						NBTTagCompound compound = (NBTTagCompound) field.get(item);
						NBTTagCompound metadata = compound.getCompound("display");
						if (metadata == null)
							return;
						if (compound.getCompound("display").getString("display-name") == null)
							return;
						if (metadata.getString("display-name") != null) {
							String message = metadata.getString("display-name");
							Matcher matcher = finder.matcher(message);
							while (matcher.find()) {
								message = message.replace(matcher.group(), Translate.getTranslation(lang, matcher.group(2)));
							}
							item.setTitle(message);
						}
						if (metadata.getList("lore") != null) {
							List<String> newlore = new ArrayList<>();
							NBTTagList list = metadata.getList("lore");
							for (int i = 0; i < list.size(); i++) {
								String message = list.getString(i);
								Matcher matcher = finder.matcher(message);
								while (matcher.find()) {
									message = message.replace(matcher.group(), Translate.getTranslation(lang, matcher.group(2)));
								}
								if (message.contains("\n")) {
									for (String s : message.split("\n"))
										newlore.addAll(StringLoreUtils.formatForLore(s));
								} else {
									newlore.addAll(StringLoreUtils.formatForLore(message));
								}
							}
							item.setLore(newlore);
						}
					} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
