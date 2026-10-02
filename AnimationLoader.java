package net.arcana.addons.animation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import net.arcana.addons.ArcanaAddons;
import net.arcana.addons.entity.animation.AnimationFrame;

public final class AnimationLoader {

	private final ArcanaAddons plugin;
	private final File folder;

	public AnimationLoader(ArcanaAddons plugin) {

		this.plugin = plugin;

		this.folder = new File(plugin.getDataFolder(), "animations");
	}

	public void load(AnimationRegistry registry) {

		if (!folder.exists() && !folder.mkdirs()) {

			plugin.getLogger().severe("Unable to create animations directory.");

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

				AnimationDefinition definition = loadFile(file);

				registry.register(definition);

			} catch (Exception exception) {

				plugin.getLogger().severe("Unable to load animation " + file.getName() + ": " + exception.getMessage());
			}
		}
	}

	private AnimationDefinition loadFile(File file) {

		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		String id = config.getString("id");

		if (id == null || id.isBlank()) {

			throw new IllegalArgumentException("Missing animation ID.");
		}

		boolean loop = config.getBoolean("loop", true);

		List<AnimationFrame> frames = new ArrayList<>();

		ConfigurationSection section = config.getConfigurationSection("frames");

		if (section == null) {

			throw new IllegalArgumentException("Animation has no frames.");
		}

		for (String key : section.getKeys(false)) {

			ConfigurationSection frame = section.getConfigurationSection(key);

			if (frame == null) {
				continue;
			}

			int modelData = frame.getInt("model-data", -1);

			long duration = Math.max(frame.getLong("duration", 50L), 1L);

			String effect = frame.getString("effect");

			frames.add(new AnimationFrame(modelData, duration, effect));
		}

		if (frames.isEmpty()) {

			throw new IllegalArgumentException("Animation has no valid frames.");
		}

		return new AnimationDefinition(id.toLowerCase(), loop, frames);
	}
}