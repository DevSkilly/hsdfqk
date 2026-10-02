package net.arcana.addons;

import org.bukkit.NamespacedKey;

public final class ArcanaKeys {

	private final NamespacedKey itemIdKey;
	private final NamespacedKey itemVersionKey;
	private final NamespacedKey customEntityKey;

	public ArcanaKeys(ArcanaAddons plugin) {

		itemIdKey = new NamespacedKey(plugin, "item_id");

		itemVersionKey = new NamespacedKey(plugin, "item_version");

		customEntityKey = new NamespacedKey(plugin, "custom_entity");
	}

	public NamespacedKey getItemIdKey() {
		return itemIdKey;
	}

	public NamespacedKey getItemVersionKey() {
		return itemVersionKey;
	}

	public NamespacedKey getCustomEntityKey() {
		return customEntityKey;
	}
}