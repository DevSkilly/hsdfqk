package net.arcana.addons.effect;

import net.arcana.addons.ArcanaAddons;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class EffectLoader {

	private final ArcanaAddons plugin;
	private final File folder;

	public EffectLoader(ArcanaAddons plugin) {

		this.plugin = plugin;

		this.folder = new File(plugin.getDataFolder(), "effects");
	}

	public void load(EffectRegistry registry) {

		if (!folder.exists() && !folder.mkdirs()) {

			plugin.getLogger().severe("Unable to create effects directory.");

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

				EffectDefinition definition = loadFile(file);

				if (registry.contains(definition.getId())) {

					throw new IllegalStateException("Duplicate effect ID: " + definition.getId());
				}

				registry.register(definition);

			} catch (Exception exception) {

				plugin.getLogger().severe("Unable to load effect " + file.getName() + ": " + exception.getMessage());
			}
		}

		plugin.getLogger().info("Loaded " + registry.getEffects().size() + " effects.");
	}

	private EffectDefinition loadFile(File file) {

		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		String id = config.getString("id");

		if (id == null || id.isBlank()) {

			throw new IllegalArgumentException("Missing effect ID.");
		}

		List<EffectDefinition.ParticleEffect> particles = new ArrayList<>();

		for (int i = 0;; i++) {

			String path = "particles." + i;

			if (!config.contains(path)) {
				break;
			}

			ConfigurationSection section = config.getConfigurationSection(path);

			if (section == null) {
				continue;
			}

			String type = section.getString("type");

			if (type == null || type.isBlank()) {

				throw new IllegalArgumentException("Missing particle type at " + path);
			}

			particles.add(new EffectDefinition.ParticleEffect(type.toUpperCase(),
					Math.max(section.getInt("amount", 1), 1), section.getDouble("radius", 0.0),
					section.getDouble("offset-x", 0.0), section.getDouble("offset-y", 0.0),
					section.getDouble("offset-z", 0.0), section.getDouble("speed", 0.0)));
		}

		EffectDefinition.SoundEffect sound = null;

		ConfigurationSection soundSection = config.getConfigurationSection("sound");

		if (soundSection != null) {

			String type = soundSection.getString("type");

			if (type != null && !type.isBlank()) {

				sound = new EffectDefinition.SoundEffect(type.toUpperCase(),
						(float) soundSection.getDouble("volume", 1.0), (float) soundSection.getDouble("pitch", 1.0));
			}
		}

		return new EffectDefinition(id.toLowerCase(), particles, sound);
	}
}