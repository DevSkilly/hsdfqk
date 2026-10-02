package net.arcana.addons.block;

import net.arcana.addons.ArcanaAddons;
import net.arcana.addons.item.CustomItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public final class BlockManager {

	private final ArcanaAddons plugin;

	private final BlockRegistry registry;
	private final BlockDefinitionRegistry definitions;
	private final BlockStorage storage;
	private final BlockLoader loader;

	public BlockManager(ArcanaAddons plugin) {

		this.plugin = plugin;

		this.registry = new BlockRegistry();

		this.definitions = new BlockDefinitionRegistry();

		this.storage = new BlockStorage(plugin, registry);

		this.loader = new BlockLoader(plugin);
	}

	public void load() {

		loader.load(definitions);

		storage.load(definitions);

		plugin.getLogger().info("Custom blocks loaded: " + registry.getBlocks().size());
	}

	public void shutdown() {

		storage.save();

		registry.clear();
		definitions.clear();
	}

	public BlockRegistry getRegistry() {
		return registry;
	}

	public BlockDefinitionRegistry getDefinitions() {
		return definitions;
	}

	public BlockDefinition getDefinition(String id) {

		return definitions.get(id);
	}

	public CustomBlock place(BlockDefinition definition, Location location) {

		if (definition == null || location == null) {

			return null;
		}

		Block block = location.getBlock();

		Material material = Material.matchMaterial(definition.getMaterial());

		if (material == null) {
			return null;
		}

		block.setType(material, false);

		CustomBlock customBlock = new CustomBlock(definition.getId(), definition.getVersion(), location);

		registry.register(customBlock);

		storage.save();

		return customBlock;
	}
	

	public void remove(Location location) {

		registry.remove(location);

		storage.save();
	}

	public ItemStack createItem(String id, int amount) {

		BlockDefinition definition = getDefinition(id);

		if (definition == null) {
			return null;
		}

		CustomItem item = plugin.getItemManager().get(definition.getDropItem());

		if (item == null) {
			return null;
		}

		return plugin.getItemManager().getBuilder().build(item, amount);
	}
}