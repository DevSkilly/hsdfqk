package net.arcana.addons.item;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public final class ItemIdentifier {

	private final NamespacedKey itemIdKey;
	private final NamespacedKey itemVersionKey;

	private final NamespacedKey durabilityKey;
	private final NamespacedKey maxDurabilityKey;

	public ItemIdentifier(Plugin plugin) {

		itemIdKey = new NamespacedKey(plugin, "item_id");

		itemVersionKey = new NamespacedKey(plugin, "item_version");

		durabilityKey = new NamespacedKey(plugin, "durability");

		maxDurabilityKey = new NamespacedKey(plugin, "max_durability");
	}

	public NamespacedKey getItemIdKey() {
		return itemIdKey;
	}

	public NamespacedKey getItemVersionKey() {
		return itemVersionKey;
	}

	public NamespacedKey getDurabilityKey() {
		return durabilityKey;
	}

	public NamespacedKey getMaxDurabilityKey() {
		return maxDurabilityKey;
	}
}