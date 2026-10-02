package net.arcana.addons.durability;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.arcana.addons.ArcanaAddons;
import net.arcana.addons.item.CustomItem;
import net.arcana.addons.item.ItemManager;
import net.arcana.addons.util.PDCUtil;

public final class DurabilityManager {

	private final ArcanaAddons plugin;

	public DurabilityManager(ArcanaAddons plugin) {
		this.plugin = plugin;
	}

	public boolean hasCustomDurability(ItemStack item) {

		if (item == null || item.getType() == Material.AIR) {
			return false;
		}

		ItemManager manager = plugin.getItemManager();

		String id = manager.getItemId(item);

		if (id == null) {
			return false;
		}

		CustomItem customItem = manager.get(id);

		return customItem != null && customItem.getDefinition().isDurabilityEnabled();
	}

	public int getDurability(ItemStack item) {

		if (!hasCustomDurability(item)) {
			return 0;
		}

		ItemMeta meta = item.getItemMeta();

		Integer value = PDCUtil.getInteger(meta.getPersistentDataContainer(),
				plugin.getItemManager().getIdentifier().getDurabilityKey());

		return value == null ? 0 : value;
	}

	public int getMaxDurability(ItemStack item) {

		if (!hasCustomDurability(item)) {
			return 0;
		}

		ItemMeta meta = item.getItemMeta();

		Integer value = PDCUtil.getInteger(meta.getPersistentDataContainer(),
				plugin.getItemManager().getIdentifier().getMaxDurabilityKey());

		return value == null ? 0 : value;
	}

	public boolean damage(ItemStack item, int amount) {

		if (!hasCustomDurability(item) || amount <= 0) {

			return false;
		}

		int current = getDurability(item);

		int result = Math.max(0, current - amount);

		ItemMeta meta = item.getItemMeta();

		PDCUtil.setInteger(meta.getPersistentDataContainer(),
				plugin.getItemManager().getIdentifier().getDurabilityKey(), result);

		item.setItemMeta(meta);

		return result <= 0;
	}

	public void repair(ItemStack item, int amount) {

		if (!hasCustomDurability(item) || amount <= 0) {

			return;
		}

		int current = getDurability(item);

		int max = getMaxDurability(item);

		int result = Math.min(max, current + amount);

		ItemMeta meta = item.getItemMeta();

		PDCUtil.setInteger(meta.getPersistentDataContainer(),
				plugin.getItemManager().getIdentifier().getDurabilityKey(), result);

		item.setItemMeta(meta);
	}
}