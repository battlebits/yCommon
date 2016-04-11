package br.com.battlebits.ycommon.bukkit.injector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_7_R4.PacketPlayOutSetSlot;
import net.minecraft.server.v1_7_R4.PacketPlayOutWindowItems;

public class WindowInjector {

	private static Pattern translateFinder = Pattern.compile("%translateId:\\s*([a-zA-Z0-9_-]+)\\s*%");

	public static void inject(BukkitMain main) {
		PacketListenerAPI.addListener(new PacketListener() {

			@Override
			public void onPacketSend(PacketObject pacote) {
				Packet packet = pacote.getPacket();
				if (packet instanceof PacketPlayOutWindowItems) {
					PacketPlayOutWindowItems items = (PacketPlayOutWindowItems) packet;
					Language lang = BattlebitsAPI.getAccountCommon().getBattlePlayer(pacote.getPlayer().getUniqueId()).getLanguage();
					ArrayList<ItemStack> array = new ArrayList<>();
					for (ItemStack item : items.b) {
						if (item == null) {
							array.add(item);
							continue;
						}
						ItemStack iS = CraftItemStack.copyNMSStack(item, item.count);
						ItemMeta meta = CraftItemStack.getItemMeta(iS);
						if (meta != null) {
							if (meta.hasDisplayName() && meta.getDisplayName().contains("%translateId:")) {
								String name = meta.getDisplayName();
								Matcher matcher = translateFinder.matcher(name);
								while (matcher.find()) {
									name = name.replace("%translateId:" + matcher.group(1) + "%", Translate.getTranslation(lang, matcher.group(1)));
								}
								matcher = null;
								meta.setDisplayName(name);
								name = null;
							}
							if (meta.hasLore()) {
								String newlore = "";
								for (String name : meta.getLore()) {
									if (!newlore.isEmpty()) {
										newlore += "\\n";
									}
									if (name.contains("%translateId:")) {
										Matcher matcher = translateFinder.matcher(name);
										while (matcher.find()) {
											name = name.replace("%translateId:" + matcher.group(1) + "%",
													Translate.getTranslation(lang, matcher.group(1)));
										}
										matcher = null;
									}
									newlore += name;
									name = null;
								}
								meta.setLore(formatForLore(newlore));
								newlore = null;
							}
							CraftItemStack.setItemMeta(iS, meta);
						}
						array.add(iS);
						meta = null;
						iS = null;
					}
					pacote.setPacket(new PacketPlayOutWindowItems(items.a, array));
					array.clear();
					array = null;
					lang = null;
					items = null;
				} else if (packet instanceof PacketPlayOutSetSlot) {
					PacketPlayOutSetSlot setSlot = (PacketPlayOutSetSlot) packet;
					try {
						Field c = setSlot.getClass().getDeclaredField("c");
						c.setAccessible(true);
						ItemStack item = (ItemStack) c.get(setSlot);
						if (item != null) {
							ItemStack iS = CraftItemStack.copyNMSStack(item, item.count);
							ItemMeta meta = CraftItemStack.getItemMeta(iS);
							if (meta != null) {
								Language lang = BattlebitsAPI.getAccountCommon().getBattlePlayer(pacote.getPlayer().getUniqueId()).getLanguage();
								if (meta.hasDisplayName() && meta.getDisplayName().contains("%translateId:")) {
									String name = meta.getDisplayName();
									Matcher matcher = translateFinder.matcher(name);
									while (matcher.find()) {
										name = name.replace("%translateId:" + matcher.group(1) + "%",
												Translate.getTranslation(lang, matcher.group(1)));
									}
									matcher = null;
									meta.setDisplayName(name);
									name = null;
								}
								if (meta.hasLore()) {
									String newlore = "";
									for (String name : meta.getLore()) {
										if (!newlore.isEmpty()) {
											newlore += "\\n";
										}
										if (name.contains("%translateId:")) {
											Matcher matcher = translateFinder.matcher(name);
											while (matcher.find()) {
												name = name.replace("%translateId:" + matcher.group(1) + "%",
														Translate.getTranslation(lang, matcher.group(1)));
											}
											matcher = null;
										}
										newlore += name;
										name = null;
									}
									meta.setLore(formatForLore(newlore));
									newlore = null;
								}
								CraftItemStack.setItemMeta(iS, meta);
								lang = null;
							}
							c.set(setSlot, iS);
							meta = null;
							iS = null;
						}
						item = null;
						c = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					setSlot = null;
				} else if (packet instanceof PacketPlayOutOpenWindow) {
					PacketPlayOutOpenWindow openWindow = (PacketPlayOutOpenWindow) packet;
					try {
						Field c = openWindow.getClass().getDeclaredField("c");
						c.setAccessible(true);
						String name = (String) c.get(openWindow);
						if (name != null) {
							Matcher matcher = translateFinder.matcher(name);
							while (matcher.find()) {
								name = name.replace("%translateId:" + matcher.group(1) + "%",
										Translate.getTranslation(
												BattlebitsAPI.getAccountCommon().getBattlePlayer(pacote.getPlayer().getUniqueId()).getLanguage(),
												matcher.group(1)));
							}
							matcher = null;
							c.set(openWindow, name);
						}
						name = null;
						c = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					openWindow = null;
				}
			}

			@Override
			public void onPacketReceive(PacketObject pacote) {

			}

		});
		BattlebitsAPI.getLogger().info("PacketListener de WindowInjector carregada com sucesso!");
	}

	private static ArrayList<String> formatForLore(String string) {
		String[] split = string.split(" ");
		string = "";
		ArrayList<String> newString = new ArrayList<String>();
		for (int i = 0; i < split.length; i++) {
			if (ChatColor.stripColor(string).length() > 25 || ChatColor.stripColor(string).endsWith(".")
					|| ChatColor.stripColor(string).endsWith("!")) {
				newString.add("§7" + string);
				if (string.endsWith(".") || string.endsWith("!"))
					newString.add("");
				string = "";
			}
			String toAdd = split[i];
			if (toAdd.contains("\\n")) {
				toAdd = toAdd.substring(0, toAdd.indexOf("\\n"));
				split[i] = split[i].substring(toAdd.length() + 2);
				newString.add("§7" + string + (string.length() == 0 ? "" : " ") + toAdd);
				string = "";
				i--;
			} else {
				string += (string.length() == 0 ? "" : " ") + toAdd;
			}
		}
		newString.add("§7" + string);
		return newString;
	}
}
