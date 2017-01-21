package br.com.battlebits.ycommon.bungee.inventory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.spawl.bungeepackets.inventory.Inventory;
import org.spawl.bungeepackets.inventory.Inventory.ClickHandler;
import org.spawl.bungeepackets.item.ItemStack;
import org.spawl.bungeepackets.item.Material;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.inventory.item.ItemBuilder;
import br.com.battlebits.ycommon.bungee.report.Report;
import br.com.battlebits.ycommon.bungee.report.ReportManager;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReportListInventory {

	private static int itemsPerPage = 36;

	public ReportListInventory(ProxiedPlayer player, int page) {
		List<Report> reports = ReportManager.getReports();
		Iterator<Report> iterator = reports.iterator();
		while (iterator.hasNext()) {
			Report report = iterator.next();
			if (BungeeMain.getPlugin().getProxy().getPlayer(report.getPlayerUniqueId()) == null) {
				iterator.remove();
				continue;
			}
			BattlePlayer reportPlayer = BattlePlayer.getPlayer(report.getPlayerUniqueId());
			if (reportPlayer == null || !reportPlayer.isOnline()) {
				iterator.remove();
				return;
			}
			if (report.isExpired()) {
				iterator.remove();
				continue;
			}
			if (report.getReportLevel() < 1000)
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

		int w = 9;

		for (int i = pageStart; i < pageEnd; i++) {
			Report report = reports.get(i);
			ItemBuilder builder = new ItemBuilder();
			builder.type(Material.SKULL_ITEM);
			builder.durability(3);
			builder.name(ChatColor.RED + report.getPlayerName());
			builder.lore("§%right-click-teleport%§");
			ItemStack skull = builder.build();
			skull.setOwner(report.getPlayerUniqueId());
			menu.setItem(w, skull, new ReportClickHandler(report, menu));
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

	private static class ReportClickHandler implements ClickHandler {

		private Report report;
		private Inventory topInventory;

		public ReportClickHandler(Report report, Inventory topInventory) {
			this.report = report;
			this.topInventory = topInventory;
		}

		@Override
		public void onClick(ProxiedPlayer arg0, int arg1, ItemStack arg2, boolean arg3, boolean arg4) {
			BattlePlayer reportPlayer = BattlePlayer.getPlayer(report.getPlayerUniqueId());
			if (reportPlayer == null || !reportPlayer.isOnline()) {
				new ReportListInventory(arg0, 1);
				return;
			}
			if (arg3) {
				ServerInfo info = BungeeCord.getInstance().getServerInfo(reportPlayer.getServerConnected());
				if (info == null) {
					return;
				}
				topInventory.close(arg0);
				if (!arg0.getServer().getInfo().getName().equals(info.getName()))
					arg0.connect(info);
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("Teleport");
				out.writeUTF(report.getPlayerUniqueId() + "");
				arg0.getServer().sendData(BattlebitsAPI.getBungeeChannel(), out.toByteArray());
				return;
			}
			new ReportInventory(arg0, report);
		}

	}
}
