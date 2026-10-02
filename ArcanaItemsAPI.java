package net.arcana.addons.api;

import org.bukkit.inventory.ItemStack;

import net.arcana.addons.ArcanaAddons;
import net.arcana.addons.item.CustomItem;

public final class ArcanaItemsAPI {

	private ArcanaItemsAPI() {
	}

	public static CustomItem getItem(String id) {

		return ArcanaAddons.getInstance().getItemManager().get(id);
	}

	public static ItemStack createItem(String id) {

		CustomItem item = getItem(id);

		if (item == null) {
			return null;
		}

		return item.create();
	}

	public static ItemStack createItem(String id, int amount) {

		CustomItem item = getItem(id);

		if (item == null) {
			return null;
		}

		return item.create(amount);
	}

	public static boolean isCustomItem(ItemStack itemStack) {

		if (itemStack == null) {
			return false;
		}

		return ArcanaAddons.getInstance().getItemManager().getItemId(itemStack) != null;
	}

	public static String getItemId(ItemStack itemStack) {

		if (itemStack == null) {
			return null;
		}

		return ArcanaAddons.getInstance().getItemManager().getItemId(itemStack);
	}
}