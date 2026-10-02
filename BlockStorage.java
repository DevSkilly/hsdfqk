package net.arcana.addons.block;

import net.arcana.addons.ArcanaAddons;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public final class BlockStorage {

	private final ArcanaAddons plugin;
	private final BlockRegistry registry;

	private final File file;

	public BlockStorage(ArcanaAddons plugin, BlockRegistry registry) {

		this.plugin = plugin;
		this.registry = registry;

		this.file = new File(plugin.getDataFolder(), "placed-blocks.yml");
	}

	public void save() {

		YamlConfiguration config = new YamlConfiguration();

		for (CustomBlock block : registry.getBlocks().values()) {

			Location location = block.getLocation();

			if (location.getWorld() == null) {
				continue;
			}

			String key = location.getWorld().getUID().toString() + "_" + location.getBlockX() + "_"
					+ location.getBlockY() + "_" + location.getBlockZ();

			String path = "blocks." + key;

			config.set(path + ".world", location.getWorld().getUID().toString());

			config.set(path + ".x", location.getBlockX());

			config.set(path + ".y", location.getBlockY());

			config.set(path + ".z", location.getBlockZ());

			config.set(path + ".id", block.getId());

			config.set(path + ".version", block.getVersion());
		}

		try {

			config.save(file);

		} catch (IOException exception) {

			plugin.getLogger().severe("Unable to save placed blocks.");

			exception.printStackTrace();
		}
	}

	public void load(BlockDefinitionRegistry definitions) {

		if (!file.exists()) {
			return;
		}

		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		ConfigurationSection section = config.getConfigurationSection("blocks");

		if (section == null) {
			return;
		}

		for (String key : section.getKeys(false)) {

			String worldUuid = section.getString(key + ".world");

			String id = section.getString(key + ".id");

			if (worldUuid == null || id == null) {

				continue;
			}

			World world = Bukkit.getWorld(UUID.fromString(worldUuid));

			if (world == null) {
				continue;
			}

			BlockDefinition definition = definitions.get(id);

			if (definition == null) {

				plugin.getLogger().warning("Unknown custom block: " + id);

				continue;
			}

			int x = section.getInt(key + ".x");

			int y = section.getInt(key + ".y");

			int z = section.getInt(key + ".z");

			Location location = new Location(world, x, y, z);

			registry.register(new CustomBlock(definition.getId(), definition.getVersion(), location));
		}
	}
}