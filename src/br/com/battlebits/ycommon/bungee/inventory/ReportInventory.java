package br.com.battlebits.ycommon.bungee.inventory;

import org.spawl.bungeepackets.inventory.Inventory;
import org.spawl.bungeepackets.inventory.Inventory.ClickHandler;
import org.spawl.bungeepackets.item.ItemStack;
import org.spawl.bungeepackets.item.Material;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import br.com.battlebits.ycommon.bungee.inventory.ConfirmInventory.ConfirmHandler;
import br.com.battlebits.ycommon.bungee.inventory.item.ItemBuilder;
import br.com.battlebits.ycommon.bungee.report.Report;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReportInventory {

	public ReportInventory(ProxiedPlayer player, Report report) {
		Language lang = BattlePlayer.getLanguage(player.getUniqueId());
		Inventory menu = new Inventory(Translate.getTranslation(lang, "report-inv").replace("%reported%", report.getPlayerName()), 54);

		menu.setItem(0, new ItemBuilder().type(Material.BED).name("§%back%§").build(), new ClickHandler() {

			@Override
			public void onClick(ProxiedPlayer arg0, int arg1, ItemStack arg2, boolean arg3, boolean arg4) {
				new ReportListInventory(arg0, 1);
			}
		});
		BattlePlayer reportPlayer = BattlePlayer.getPlayer(report.getPlayerUniqueId());
		ItemBuilder builder = new ItemBuilder().type(Material.SKULL_ITEM);
		builder.name(Translate.getTranslation(lang, "report-info").replace("%reported%", report.getPlayerName()));
		String lore = Translate.getTranslation(lang, "report-info-lore");
		lore = lore.replace("%reported%", report.getPlayerName());
		lore = lore.replace("%server%", reportPlayer.getServerConnected());
		lore = lore.replace("%rank%", reportPlayer.getTag().getPrefix(lang));
		builder.lore(lore);
		ItemStack skull = builder.build();
		skull.setData(3);
		skull.setOwner(report.getPlayerUniqueId());
		menu.setItem(22, skull, new ClickHandler() {

			@Override
			public void onClick(ProxiedPlayer arg0, int arg1, ItemStack arg2, boolean arg3, boolean arg4) {
				new AccountInventory();
			}
		});

		menu.setItem(38, new ItemBuilder().type(Material.COMPASS).name("§%teleport%§").build(), new ClickHandler() {

			@Override
			public void onClick(ProxiedPlayer arg0, int arg1, ItemStack arg2, boolean arg3, boolean arg4) {
				if (reportPlayer == null || !reportPlayer.isOnline()) {
					new ReportListInventory(arg0, 1);
					return;
				}
				ServerInfo info = BungeeCord.getInstance().getServerInfo(reportPlayer.getServerConnected());
				if (info == null) {
					new ReportListInventory(arg0, 1);
					return;
				}
				menu.close(arg0);
				if (!arg0.getServer().getInfo().getName().equals(info.getName()))
					arg0.connect(info);
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("Teleport");
				out.writeUTF(report.getPlayerUniqueId() + "");
				arg0.getServer().sendData(BattlebitsAPI.getBungeeChannel(), out.toByteArray());
			}
		});
		String listLore = Translate.getTranslation(lang, "report-list-lore");
		listLore = listLore.replace("%number%", report.getPlayersReason().size() + "");
		listLore = listLore.replace("%lastReason%", report.getLastReport().getReason());
		listLore = listLore.replace("%lastPlayer%", report.getLastReport().getPlayerName());

		menu.setItem(40, new ItemBuilder().type(Material.BOOK_AND_QUILL).name("§%report-list%§").lore(listLore).build(), new ClickHandler() {

			@Override
			public void onClick(ProxiedPlayer arg0, int arg1, ItemStack arg2, boolean arg3, boolean arg4) {
				new ReportInformationListInventory(player, report, menu, 1);
			}
		});

		menu.setItem(42, new ItemBuilder().type(Material.REDSTONE_BLOCK).name("§%reject-report%§").build(), new ClickHandler() {

			@Override
			public void onClick(ProxiedPlayer arg0, int arg1, ItemStack arg2, boolean arg3, boolean arg4) {
				new ConfirmInventory(player, "§%remove-report%§", new ConfirmHandler() {

					@Override
					public void onCofirm(boolean confirmed) {
						if (confirmed) {
							if (reportPlayer == null || !reportPlayer.isOnline()) {
								new ReportListInventory(arg0, 1);
								return;
							}
							report.expire();
							new ReportListInventory(arg0, 1);
						} else {
							menu.open(arg0);
						}
					}
				}, menu);
			}
		});

		ItemStack nullItem = new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build();
		for (int i = 0; i < 9; i++) {
			if (menu.getItem(i) == null)
				menu.setItem(i, nullItem);
		}
		menu.open(player);
	}

}
