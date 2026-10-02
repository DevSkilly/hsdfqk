package net.arcana.addons.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.arcana.addons.resourcepack.ResourcePackDefinition;
import net.arcana.addons.util.PDCUtil;
import net.arcana.addons.util.TextUtil;

public final class ItemBuilder {

	private final ItemManager manager;

	public ItemBuilder(ItemManager manager) {
		this.manager = manager;
	}

	public ItemStack build(CustomItem customItem, int amount) {

		ItemDefinition definition = customItem.getDefinition();

		Material material = Material.matchMaterial(definition.getMaterial());

		if (material == null || material == Material.AIR) {
			throw new IllegalStateException("Invalid material for item " + definition.getId());
		}

		int maxStackSize = material.getMaxStackSize();

		int finalAmount = Math.min(Math.max(amount, 1), maxStackSize);

		ItemStack item = new ItemStack(material, finalAmount);

		ItemMeta meta = item.getItemMeta();

		if (meta == null) {
			throw new IllegalStateException("Unable to create ItemMeta.");
		}

		/*
		 * Display name
		 */
		if (definition.getDisplayName() != null) {

			meta.displayName(TextUtil.color(definition.getDisplayName()));
		}

		/*
		 * Custom Model Data
		 */
		ResourcePackDefinition resource = definition.getResourcePack();

		if (resource != null) {
			
			int modelData = manager.getModelDataManager().getOrCreate(definition.getId());
			
			meta.setCustomModelData(modelData);

		} else if (definition.getModelData() >= 0) {

			meta.setCustomModelData(definition.getModelData());
		}

		/*
		 * Lore
		 */
		if (!definition.getLore().isEmpty()) {

			List<net.kyori.adventure.text.Component> lore = new ArrayList<>();

			for (String line : definition.getLore()) {

				lore.add(TextUtil.color(line));
			}

			meta.lore(lore);
		}

		/*
		 * Arcana Item ID
		 */
		PDCUtil.setString(meta.getPersistentDataContainer(), manager.getIdentifier().getItemIdKey(),
				definition.getId());

		/*
		 * Item version
		 */
		PDCUtil.setInteger(meta.getPersistentDataContainer(), manager.getIdentifier().getItemVersionKey(),
				definition.getVersion());

		/*
		 * Durability
		 */
		if (definition.isDurabilityEnabled()) {

			PDCUtil.setInteger(meta.getPersistentDataContainer(), manager.getIdentifier().getDurabilityKey(),
					definition.getMaxDurability());

			PDCUtil.setInteger(meta.getPersistentDataContainer(), manager.getIdentifier().getMaxDurabilityKey(),
					definition.getMaxDurability());
		}

		item.setItemMeta(meta);

		return item;
	}
}