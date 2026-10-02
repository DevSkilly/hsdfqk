package net.arcana.addons.entity;

import net.arcana.addons.ArcanaAddons;
import net.arcana.addons.entity.visual.VisualModel;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EntityLoader {

	private final ArcanaAddons plugin;
	private final File folder;

	public EntityLoader(ArcanaAddons plugin) {
		this.plugin = plugin;
		this.folder = new File(plugin.getDataFolder(), "entities");
	}

	public Map<String, EntityDefinition> load() {

		Map<String, EntityDefinition> result = new HashMap<>();

		if (!folder.exists() && !folder.mkdirs()) {
			plugin.getLogger().severe("Unable to create entities folder.");
			return result;
		}

		File[] files = folder.listFiles();

		if (files == null) {
			return result;
		}

		for (File file : files) {

			if (!file.isFile() || !file.getName().toLowerCase().endsWith(".yml")) {
				continue;
			}

			try {

				EntityDefinition definition = loadFile(file);

				String id = definition.getId().toLowerCase();

				if (result.containsKey(id)) {

					throw new IllegalArgumentException("Duplicate entity ID: " + id);
				}

				result.put(id, definition);

			} catch (Exception exception) {

				plugin.getLogger().severe("Unable to load " + file.getName() + ": " + exception.getMessage());
			}
		}

		return result;
	}

	private EntityDefinition loadFile(File file) {

		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		String id = config.getString("id");

		if (id == null || id.isBlank()) {
			throw new IllegalArgumentException("Missing id.");
		}

		String baseType = config.getString("base-type", "ZOMBIE");

		String name = config.getString("display.name", "&f" + id);

		double health = config.getDouble("stats.health", 20.0D);

		double speed = config.getDouble("stats.speed", 0.23D);

		double damage = config.getDouble("stats.damage", 2.0D);

		boolean invulnerable = config.getBoolean("flags.invulnerable", false);

		boolean glowing = config.getBoolean("flags.glowing", false);

		boolean silent = config.getBoolean("flags.silent", false);

		boolean gravity = config.getBoolean("flags.gravity", true);

		double detectionRange = config.getDouble("combat.detection-range", 16.0D);

		double attackRange = config.getDouble("combat.attack-range", 3.0D);

		long attackCooldown = parseCooldown(config.getString("combat.attack-cooldown", "1000ms"));

		List<String> effects = config.getStringList("effects");

		String spawnAnimation = config.getString("animations.spawn");

		String deathAnimation = config.getString("animations.death");

		List<EntityPhaseDefinition> phases = loadPhases(config);
		
		VisualModel visualModel = loadVisual(config);

		return new EntityDefinition(id.toLowerCase(), baseType.toUpperCase(), name, health, speed, damage, invulnerable,
				glowing, silent, gravity, detectionRange, attackRange, attackCooldown, effects, spawnAnimation,
				deathAnimation, phases, visualModel);
	}

	private VisualModel loadVisual(YamlConfiguration config) {

		ConfigurationSection section = config.getConfigurationSection("visual");

		if (section == null || !section.getBoolean("enabled", false)) {

			return null;
		}

		String model = section.getString("model");

		if (model == null || model.isBlank()) {

			throw new IllegalArgumentException("Visual model is missing.");
		}

		double scale = section.getDouble("scale", 1.0D);

		ConfigurationSection offset = section.getConfigurationSection("offset");

		double x = 0.0D;
		double y = 0.0D;
		double z = 0.0D;

		if (offset != null) {

			x = offset.getDouble("x", 0.0D);
			y = offset.getDouble("y", 0.0D);
			z = offset.getDouble("z", 0.0D);
		}

		return new VisualModel(model, scale, x, y, z);
	}

	private List<EntityPhaseDefinition> loadPhases(YamlConfiguration config) {

		List<EntityPhaseDefinition> phases = new ArrayList<>();

		ConfigurationSection section = config.getConfigurationSection("phases");

		if (section == null) {
			return phases;
		}

		for (String id : section.getKeys(false)) {

			ConfigurationSection phase = section.getConfigurationSection(id);

			if (phase == null) {
				continue;
			}

			double healthPercent = phase.getDouble("health", 100.0D);

			double damageMultiplier = phase.getDouble("damage-multiplier", 1.0D);

			double speedMultiplier = phase.getDouble("speed-multiplier", 1.0D);

			phases.add(new EntityPhaseDefinition(id, healthPercent, damageMultiplier, speedMultiplier));
		}

		return phases;
	}

	private long parseCooldown(String value) {

		if (value == null || value.isBlank()) {
			return 1000L;
		}

		String input = value.trim().toLowerCase();

		try {

			if (input.endsWith("ms")) {

				return Long.parseLong(input.substring(0, input.length() - 2));
			}

			if (input.endsWith("s")) {

				double seconds = Double.parseDouble(input.substring(0, input.length() - 1));

				return (long) (seconds * 1000.0D);
			}

			if (input.endsWith("m")) {

				double minutes = Double.parseDouble(input.substring(0, input.length() - 1));

				return (long) (minutes * 60000.0D);
			}

			return Long.parseLong(input);

		} catch (NumberFormatException exception) {

			throw new IllegalArgumentException("Invalid cooldown: " + value);
		}
	}
}