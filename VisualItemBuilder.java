package net.arcana.addons.entity.visual;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class VisualItemBuilder {

	private VisualItemBuilder() {
	}

	public static ItemStack create(Material material, int modelData) {

		ItemStack item = new ItemStack(material);

		ItemMeta meta = item.getItemMeta();

		if (meta == null) {
			return item;
		}

		meta.setCustomModelData(modelData);

		item.setItemMeta(meta);

		return item;
	}
}