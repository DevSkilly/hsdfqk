package net.arcana.addons.block;

import net.arcana.addons.ArcanaAddons;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public final class BlockLoader {

	private final ArcanaAddons plugin;

	private final File folder;

	public BlockLoader(ArcanaAddons plugin) {

		this.plugin = plugin;

		this.folder = new File(plugin.getDataFolder(), "blocks");
	}

	public void load(BlockDefinitionRegistry registry) {

		if (!folder.exists() && !folder.mkdirs()) {

			plugin.getLogger().severe("Unable to create blocks directory.");

			return;
		}

		File[] files = folder.listFiles();

		if (files == null) {
			return;
		}

		for (File file : files) {

			if (!file.isFile() || !file.getName().toLowerCase().endsWith(".yml")) {

				continue;
			}

			try {

				BlockDefinition definition = loadFile(file);

				if (registry.contains(definition.getId())) {

					throw new IllegalStateException("Duplicate block ID: " + definition.getId());
				}

				registry.register(definition);

			} catch (Exception exception) {

				plugin.getLogger().severe("Unable to load " + file.getName() + ": " + exception.getMessage());
			}
		}

		plugin.getLogger().info("Loaded " + registry.getDefinitions().size() + " block definitions.");
	}

	private BlockDefinition loadFile(File file) {

		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		String id = config.getString("id");

		if (id == null || id.isBlank()) {

			throw new IllegalArgumentException("Missing block ID.");
		}

		String material = config.getString("material");

		if (material == null || Material.matchMaterial(material) == null) {

			throw new IllegalArgumentException("Invalid material.");
		}

		ConfigurationSection resource = config.getConfigurationSection("resource");

		if (resource == null) {

			throw new IllegalArgumentException("Missing resource section.");
		}

		String model = resource.getString("model");

		String texture = resource.getString("texture");

		if (model == null || model.isBlank()) {

			throw new IllegalArgumentException("Missing resource model.");
		}

		if (texture == null || texture.isBlank()) {

			throw new IllegalArgumentException("Missing resource texture.");
		}

		double hardness = config.getDouble("block.hardness", 1.0D);

		boolean dropEnabled = config.getBoolean("block.drop.enabled", true);

		String dropItem = config.getString("block.drop.item");

		int dropAmount = config.getInt("block.drop.amount", 1);

		List<String> placedEffects = config.getStringList("effects.placed");

		List<String> brokenEffects = config.getStringList("effects.broken");

		String idleAnimation = config.getString("animation.idle");

		return new BlockDefinition(id.toLowerCase(), config.getInt("version", 1), material.toUpperCase(),
				model.toLowerCase(), texture, hardness, dropEnabled, dropItem == null ? id : dropItem.toLowerCase(),
				Math.max(dropAmount, 1), placedEffects, brokenEffects, idleAnimation);
	}
}