package br.com.battlebits.ycommon.common.utils.string;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

public class StringLoreUtils {

	public static List<String> formatForLore(String text) {
		return getLore(25, text);
	}

	public static List<String> getLore(int max, String text) {
		List<String> lore = new ArrayList<>();
		text = ChatColor.translateAlternateColorCodes('&', text);
		String[] split = text.split(" ");
		ChatColor color = null;
		text = "";
		for (int i = 0; i < split.length; i++) {
			if (ChatColor.stripColor(text).length() >= max || ChatColor.stripColor(text).endsWith(".") || ChatColor.stripColor(text).endsWith("!")) {
				lore.add(text);
				if (text.endsWith(".") || text.endsWith("!"))
					lore.add("");
				text = (color != null ? color : "") + "";
			}
			String toAdd = split[i];
			if (toAdd.contains("§"))
				for (int a = toAdd.length() - 1; a >= 0; a--) {
					if (toAdd.charAt(a) == '§') {
						if (toAdd.length() > a + 1) {
							ChatColor c = ChatColor.getByChar(toAdd.toLowerCase().charAt(a + 1));
							if (c != null)
								color = c;
						}

						break;
					}
				}
			if (toAdd.contains("\\n")) {
				toAdd = toAdd.substring(0, toAdd.indexOf("\\n"));
				split[i] = split[i].substring(toAdd.length() + 2);
				lore.add(text + (text.length() == 0 ? "" : " ") + toAdd);
				text = (color != null ? color : "") + "";
				i--;
			} else {
				text += (ChatColor.stripColor(text).length() == 0 ? "" : " ") + toAdd;
			}
		}
		lore.add(text);
		return lore;
	}

	public static void main(String[] args) {
		System.out.println(getLore(20, "§AAAAAAAAAAAAAAAA AAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAA ASD"));
	}
}
