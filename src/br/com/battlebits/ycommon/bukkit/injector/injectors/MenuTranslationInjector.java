package br.com.battlebits.ycommon.bukkit.injector.injectors;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import br.com.battlebits.ycommon.bukkit.injector.PacketListener;
import br.com.battlebits.ycommon.bukkit.injector.PacketListenerAPI;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_7_R4.PacketPlayOutSetSlot;
import net.minecraft.server.v1_7_R4.PacketPlayOutWindowItems;

public class MenuTranslationInjector {

	private Pattern translateFinder = Pattern.compile("%msg:\\s*([a-zA-Z0-9_-]+)\\s*%");
	private Pattern finder = Pattern.compile("§%(([^)]+)%§)");
	private Cache<String, Cache<Language, String>> translations = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Cache<Language, String>>() {
		@Override
		public Cache<Language, String> load(String original) throws Exception {
			return getLanguageForCache(original);
		}
	});
	private PacketListener injectorListener;

	public MenuTranslationInjector() {
		injectorListener = new PacketListener() {
			@Override
			public void onPacketSend(PacketObject pacote) {
				if (pacote.getPlayer() == null)
					return;
				if (pacote.getPlayer().getUniqueId() == null)
					return;
				if (BattlebitsAPI.getAccountCommon().getBattlePlayer(pacote.getPlayer().getUniqueId()) == null)
					return;
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
								message = message.replace(matcher.group(), Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(pacote.getPlayer().getUniqueId()).getLanguage(), matcher.group(2)));
							}
							field.set(chat, ChatSerializer.a(message));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (packet instanceof PacketPlayOutWindowItems) {
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
							if (meta.hasDisplayName() && meta.getDisplayName().contains("%msg:")) {
								meta.setDisplayName(getTranslation(meta.getDisplayName(), lang));
							}
							if (meta.hasLore()) {
								String newlore = "";
								for (String name : meta.getLore()) {
									if (!newlore.isEmpty()) {
										newlore += "\\n";
									}
									if (name.contains("%msg:")) {
										name = getTranslation(name, lang);
									}
									newlore += name;
									name = null;
								}
								meta.setLore(formatForLore(newlore));
								newlore = null;
							}
							CraftItemStack.setItemMeta(iS, meta);
							lang = null;
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
								Language lang = BattlebitsAPI.getAccountCommon().getBattlePlayer(pacote.getPlayer().getUniqueId()).getLanguage();
								if (meta.hasDisplayName() && meta.getDisplayName().contains("%msg:")) {
									meta.setDisplayName(getTranslation(meta.getDisplayName(), lang));
								}
								if (meta.hasLore()) {
									String newlore = "";
									for (String name : meta.getLore()) {
										if (!newlore.isEmpty()) {
											newlore += "\\n";
										}
										if (name.contains("%msg:")) {
											name = getTranslation(name, lang);
										}
										newlore += name;
										name = null;
									}
									meta.setLore(formatForLore(newlore));
									newlore = null;
								}
								CraftItemStack.setItemMeta(iS, meta);
								lang = null;
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
						if (name != null && name.contains("%msg:")) {
							c.set(openWindow, getTranslation(name, BattlebitsAPI.getAccountCommon().getBattlePlayer(pacote.getPlayer().getUniqueId()).getLanguage()));
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

	private ArrayList<String> formatForLore(String string) {
		String[] split = string.split(" ");
		string = "";
		ArrayList<String> newString = new ArrayList<String>();
		for (int i = 0; i < split.length; i++) {
			if (ChatColor.stripColor(string).length() > 25 || ChatColor.stripColor(string).endsWith(".") || ChatColor.stripColor(string).endsWith("!")) {
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

	private String getTranslation(String original, Language lang) {
		try {
			return translations.get(original, new Callable<Cache<Language, String>>() {
				@Override
				public Cache<Language, String> call() throws Exception {
					return getLanguageForCache(original);
				}
			}).get(lang, new Callable<String>() {
				@Override
				public String call() throws Exception {
					return getTranslationForCache(original, lang);
				}
			});
		} catch (Exception e) {
			return "";
		}
	}

	private Cache<Language, String> getLanguageForCache(String original) {
		return CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<Language, String>() {
			@Override
			public String load(Language lang) throws Exception {
				return getTranslationForCache(original, lang);
			}
		});
	}

	private String getTranslationForCache(String original, Language lang) {
		try {
			String message = original;
			Matcher matcher = translateFinder.matcher(message);
			while (matcher.find()) {
				message = message.replace("%msg:" + matcher.group(1) + "%", Translate.getTranslation(lang, matcher.group(1)));
			}
			matcher = null;
			lang = null;
			return message;
		} catch (Exception e) {
			return "";
		}
	}

}
