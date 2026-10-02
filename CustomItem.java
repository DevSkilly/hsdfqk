package net.arcana.addons.item;

import org.bukkit.inventory.ItemStack;

public final class CustomItem {

	private final ItemManager manager;
	private final ItemDefinition definition;

	CustomItem(ItemManager manager, ItemDefinition definition) {

		this.manager = manager;
		this.definition = definition;
	}

	public String getId() {
		return definition.getId();
	}

	public ItemDefinition getDefinition() {
		return definition;
	}

	public ItemStack create() {
		return create(1);
	}

	public ItemStack create(int amount) {

		if (amount <= 0) {

			throw new IllegalArgumentException("Amount must be greater than 0.");
		}

		return manager.getBuilder().build(this, amount);
	}

	public boolean is(ItemStack itemStack) {

		String id = manager.getItemId(itemStack);

		return getId().equals(id);
	}
}