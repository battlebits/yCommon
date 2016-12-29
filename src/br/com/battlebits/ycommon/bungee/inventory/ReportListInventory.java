package br.com.battlebits.ycommon.bungee.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.spawl.bungeepackets.inventory.Inventory;
import org.spawl.bungeepackets.inventory.Inventory.ClickHandler;
import org.spawl.bungeepackets.item.ItemStack;
import org.spawl.bungeepackets.item.Material;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.inventory.item.ItemBuilder;
import br.com.battlebits.ycommon.bungee.report.Report;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReportListInventory {

	private static int itemsPerPage = 36;

	public ReportListInventory(ProxiedPlayer player, int page) {
		List<Report> reports = new ArrayList<>(BungeeMain.getPlugin().getReportManager().getReports().values());
		Iterator<Report> iterator = reports.iterator();
		while (iterator.hasNext()) {
			if (BungeeMain.getPlugin().getProxy().getPlayer(iterator.next().getPlayerUniqueId()) == null)
				iterator.remove();
		}
		Collections.sort(reports, new Comparator<Report>() {
			@Override
			public int compare(Report o1, Report o2) {
				if (o1.getLastReportTime() > o2.getLastReportTime())
					return 1;
				else if (o1.getLastReportTime() == o2.getLastReportTime())
					return 0;
				return -1;
			}
		});

		Inventory menu = new Inventory("§%reports%§", 54);

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
					new ReportListInventory(player, page - 1);
				}
			});
		}
		if (Math.ceil(reports.size() / itemsPerPage) + 1 > page) {
			menu.setItem(8, new ItemBuilder().type(Material.INK_SACK).durability(10).name("§%page-next-page%§").lore(Arrays.asList("§%page-next-click-here%§")).build(), new ClickHandler() {
				@Override
				public void onClick(ProxiedPlayer arg0, int arg1, ItemStack arg2, boolean arg3, boolean arg4) {
					new ReportListInventory(player, page + 1);
				}
			});
		} else {
			menu.setItem(8, new ItemBuilder().type(Material.INK_SACK).durability(8).name("§%page-next-dont-have%§").build());
		}

		// REPORT LIST

		int w = 19;

		for (int i = pageStart; i < pageEnd; i++) {
			Report report = reports.get(i);
			menu.setItem(w, new ItemStack(Material.SKULL).setData(3).setOwner(report.getPlayerUniqueId()).setTitle(report.getPlayerName()), new ReportClickHandler(report));
			if (w % 9 == 7) {
				w += 3;
				continue;
			}
			w += 1;
		}
		if (reports.size() == 0) {
			menu.setItem(31, new ItemBuilder().type(Material.PAINTING).name("§c§lOps!").lore(Arrays.asList("§%error%§")).build());
		}

		menu.open(player);
	}

	private static class ReportClickHandler implements ClickHandler {

		public ReportClickHandler(Report report) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onClick(ProxiedPlayer arg0, int arg1, ItemStack arg2, boolean arg3, boolean arg4) {
			// TODO Auto-generated method stub

		}

	}
}
