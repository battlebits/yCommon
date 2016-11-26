package br.com.battlebits.ycommon.bukkit.api.input;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.battlebits.ycommon.bukkit.api.input.anvil.AnvilInputGui;
import br.com.battlebits.ycommon.bukkit.api.input.anvil.AnvilInputHandler;
import br.com.battlebits.ycommon.bukkit.api.input.sign.SignInputGui;
import br.com.battlebits.ycommon.bukkit.api.input.sign.SignInputHandler;
import br.com.battlebits.ycommon.common.utils.string.StringLoreUtils;

public class InputGui {

	private SignInputGui signGui;
	private SignInputHandler signHandler;
	private ItemStack anvilStack;
	private AnvilInputHandler anvilHandler;

	public InputGui(final InputHandler handler, String description) {
		this.signGui = new SignInputGui(description);
		this.anvilStack = new ItemStack(Material.PAPER, 1);
		ItemMeta anvilMeta = this.anvilStack.getItemMeta();
		anvilMeta.setDisplayName("");
		anvilMeta.setLore(StringLoreUtils.getLore(25, description));
		this.anvilStack.setItemMeta(anvilMeta);
		this.signHandler = new SignInputHandler() {
			@Override
			public void onDone(Player p, String[] lines) {
				handler.onDone(p, lines[0]);
			}
		};
		this.anvilHandler = new AnvilInputHandler() {
			@Override
			public void onDone(Player p, String name) {
				handler.onDone(p, name);
			}

			@Override
			public void onClose(Player p) {
				handler.onClose(p);
			}
		};
	}

	public AnvilInputGui getAnvil(Player p) {
		return new AnvilInputGui(p, anvilStack, anvilHandler);
	}

	public SignInputGui getSignGui() {
		return signGui;
	}

	public SignInputHandler getSignHandler() {
		return signHandler;
	}

	public void open(Player p) {
		InputManager.open(p, this);
	}

}
