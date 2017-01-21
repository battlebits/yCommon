package br.com.battlebits.ycommon.bungee.inventory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.spawl.bungeepackets.inventory.Inventory;
import org.spawl.bungeepackets.inventory.Inventory.ClickHandler;
import org.spawl.bungeepackets.item.ItemStack;
import org.spawl.bungeepackets.item.Material;

import br.com.battlebits.ycommon.bungee.inventory.item.ItemBuilder;
import br.com.battlebits.ycommon.bungee.report.Report;
import br.com.battlebits.ycommon.bungee.report.Report.ReportInformation;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.translate.Translate;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReportInformationListInventory {
	private static int itemsPerPage = 36;

	public ReportInformationListInventory(ProxiedPlayer player, Report report, Inventory topInventory, int page) {
		List<ReportInformation> reports = new ArrayList<>(report.getPlayersReason().values());
		Collections.sort(reports, new Comparator<ReportInformation>() {
			@Override
			public int compare(ReportInformation o1, ReportInformation o2) {
				if (o1.getReportTime() > o2.getReportTime())
					return 1;
				else if (o1.getReportTime() == o2.getReportTime())
					return 0;
				return -1;
			}
		});

		Inventory menu = new Inventory("§%reports-rinfo%§", 54);

		if (topInventory != null) {
			menu.setItem(4, new ItemBuilder().type(Material.BED).name("§%back%§").build(), new ClickHandler() {

				@Override
				public void onClick(ProxiedPlayer arg0, int arg1, ItemStack arg2, boolean arg3, boolean arg4) {
					topInventory.open(arg0);
				}
			});
		}

		// PAGINAÇÃO
		int pageStart = 0;
		int pageEnd = itemsPerPage;
		if (page > 1) {
			pageStart = ((page - 1) * itemsPerPage);
			pageEnd = (page * itemsPerPage);
		}
		if (pageEnd > reports.size()) {
			pageEnd = reports.size();
		}
		if (page == 1) {
			menu.setItem(0, new ItemBuilder().type(Material.INK_SACK).durability(8).name("§%page-last-dont-have%§").build());
		} else {
			menu.setItem(0, new ItemBuilder().type(Material.INK_SACK).durability(10).name("§%page-last-page%§").lore(Arrays.asList("§%page-last-click-here%§")).build(), new ClickHandler() {
				@Override
				public void onClick(ProxiedPlayer arg0, int arg1, ItemStack arg2, boolean arg3, boolean arg4) {
					new ReportInformationListInventory(player, report, topInventory, page - 1);
				}
			});
		}
		if (Math.ceil(reports.size() / itemsPerPage) + 1 > page) {
			menu.setItem(8, new ItemBuilder().type(Material.INK_SACK).durability(10).name("§%page-next-page%§").lore(Arrays.asList("§%page-next-click-here%§")).build(), new ClickHandler() {
				@Override
				public void onClick(ProxiedPlayer arg0, int arg1, ItemStack arg2, boolean arg3, boolean arg4) {
					new ReportInformationListInventory(player, report, topInventory, page + 1);
				}
			});
		} else {
			menu.setItem(8, new ItemBuilder().type(Material.INK_SACK).durability(8).name("§%page-next-dont-have%§").build());
		}

		// REPORT LIST

		int w = 9;

		for (int i = pageStart; i < pageEnd; i++) {
			ReportInformation reportInfo = reports.get(i);
			String lore = Translate.getTranslation(BattlePlayer.getLanguage(player.getUniqueId()), "playerInformation-lore");
			Date date = new Date(reportInfo.getReportTime());
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			lore = lore.replace("%reason%", reportInfo.getReason());
			lore = lore.replace("%reportPoints%", reportInfo.getReportLevel() + "");
			lore = lore.replace("%date%", df.format(date));
			menu.setItem(w, new ItemStack(Material.SKULL_ITEM).setData(3).setOwner(reportInfo.getPlayerName()).setTitle(ChatColor.RED + reportInfo.getPlayerName()).setLore(lore));
			if (w % 9 == 7) {
				w += 3;
				continue;
			}
			w += 1;
		}
		if (reports.size() == 0) {
			menu.setItem(31, new ItemBuilder().type(Material.PAINTING).name("§c§lOps!").lore(Arrays.asList("§%error%§")).build());
		}

		ItemStack nullItem = new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build();
		for (int i = 0; i < 9; i++) {
			if (menu.getItem(i) == null)
				menu.setItem(i, nullItem);
		}
		menu.open(player);
	}

}
