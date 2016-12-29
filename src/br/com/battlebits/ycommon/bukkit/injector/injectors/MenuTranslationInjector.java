package br.com.battlebits.ycommon.bukkit.injector.injectors;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.battlebits.ycommon.bukkit.injector.PacketListener;
import br.com.battlebits.ycommon.bukkit.injector.PacketListenerAPI;
import br.com.battlebits.ycommon.bukkit.utils.PlayerUtils;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import br.com.battlebits.ycommon.common.utils.string.StringLoreUtils;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_7_R4.PacketPlayOutSetSlot;
import net.minecraft.server.v1_7_R4.PacketPlayOutWindowItems;

public class MenuTranslationInjector {

	private Pattern finder = Pattern.compile("§%(([^)]+)%§)");
	private PacketListener injectorListener;

	public MenuTranslationInjector() {
		injectorListener = new PacketListener() {
			@Override
			public void onPacketSend(PacketObject pacote) {
				if (pacote.getPlayer() == null)
					return;
				if (pacote.getPlayer().getUniqueId() == null)
					return;
				if (BattlePlayer.getPlayer(pacote.getPlayer().getUniqueId()) == null)
					return;
				Language lang = BattlePlayer.getLanguage(pacote.getPlayer().getUniqueId());
				Packet packet = pacote.getPacket();
				if (packet instanceof PacketPlayOutChat) {
					PacketPlayOutChat chat = (PacketPlayOutChat) packet;
					try {
						Field field = chat.getClass().getDeclaredField("a");
						field.setAccessible(true);
						IChatBaseComponent component = (IChatBaseComponent) field.get(chat);
						if (component != null) {
							String message = ChatSerializer.a(component);
							Matcher matcher = finder.matcher(message);
							while (matcher.find()) {
								message = message.replace(matcher.group(), Translate.getTranslation(lang, matcher.group(2)));
							}
							field.set(chat, ChatSerializer.a(message));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (packet instanceof PacketPlayOutWindowItems) {
					PacketPlayOutWindowItems items = (PacketPlayOutWindowItems) packet;
					ArrayList<ItemStack> array = new ArrayList<>();
					for (ItemStack item : items.b) {
						if (item == null) {
							array.add(item);
							continue;
						}
						ItemStack iS = CraftItemStack.copyNMSStack(item, item.count);
						ItemMeta meta = CraftItemStack.getItemMeta(iS);
						if (meta != null) {
							if (meta.hasDisplayName()) {
								String message = meta.getDisplayName();
								Matcher matcher = finder.matcher(message);
								while (matcher.find()) {
									message = message.replace(matcher.group(), Translate.getTranslation(lang, matcher.group(2)));
								}
								meta.setDisplayName(message);
							}
							if (meta.hasLore()) {
								List<String> newlore = new ArrayList<>();
								for (String message : meta.getLore()) {
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
									message = null;
								}
								meta.setLore(newlore);
								newlore = null;
							}
							CraftItemStack.setItemMeta(iS, meta);
							meta = null;
						}
						array.add(iS);
						iS = null;
					}
					pacote.setPacket(new PacketPlayOutWindowItems(items.a, array));
					array.clear();
					array = null;
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
								if (meta.hasDisplayName()) {
									String message = meta.getDisplayName();
									Matcher matcher = finder.matcher(message);
									while (matcher.find()) {
										message = message.replace(matcher.group(), Translate.getTranslation(lang, matcher.group(2)));
									}
									meta.setDisplayName(message);
								}
								if (meta.hasLore()) {
									List<String> newlore = new ArrayList<>();
									for (String message : meta.getLore()) {
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
										message = null;
									}
									meta.setLore(newlore);
									newlore = null;
								}
								CraftItemStack.setItemMeta(iS, meta);
								meta = null;
							}
							c.set(setSlot, iS);
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
							String message = name;
							Matcher matcher = finder.matcher(message);
							while (matcher.find()) {
								message = message.replace(matcher.group(), Translate.getTranslation(lang, matcher.group(2)));
							}
							if (PlayerUtils.isPlayerOn18(pacote.getPlayer()))
								c.set(openWindow, message.substring(0, message.length() > 32 ? 32 : message.length()));
							else
								c.set(openWindow, message);
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

		};
	}

	public void inject() {
		PacketListenerAPI.addListener(injectorListener);
		BattlebitsAPI.getLogger().info("MenuTranslationInjector injetado com sucesso!");
	}

	public void end() {
		PacketListenerAPI.removeListener(injectorListener);
	}

}
