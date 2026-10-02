package net.arcana.addons.resourcepack;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import net.arcana.addons.ArcanaAddons;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class ModelDataManager {

	private final ArcanaAddons plugin;
	private final File file;

	private final Map<String, Integer> assignments = new HashMap<>();

	private int nextId;

	public ModelDataManager(ArcanaAddons plugin) {
		this.plugin = plugin;

		this.file = new File(plugin.getDataFolder(), "model-data.yml");

		load();
	}

	private void load() {

		if (!file.exists()) {
			nextId = 10000;
			return;
		}

		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		nextId = config.getInt("next-id", 10000);

		ConfigurationSection section = config.getConfigurationSection("assignments");

		if (section == null) {
			return;
		}

		for (String itemId : section.getKeys(false)) {

			assignments.put(itemId, section.getInt(itemId));
		}
	}

	public synchronized int getOrCreate(String itemId) {

		Integer existing = assignments.get(itemId);

		if (existing != null) {
			return existing;
		}

		int modelData = nextId++;

		assignments.put(itemId, modelData);

		save();

		plugin.getLogger().info("Assigned CustomModelData " + modelData + " to " + itemId);

		return modelData;
	}

	public Integer get(String itemId) {
		return assignments.get(itemId);
	}

	private void save() {

		YamlConfiguration config = new YamlConfiguration();

		config.set("next-id", nextId);

		for (Map.Entry<String, Integer> entry : assignments.entrySet()) {

			config.set("assignments." + entry.getKey(), entry.getValue());
		}

		try {
			config.save(file);

		} catch (IOException exception) {

			plugin.getLogger().severe("Unable to save model-data.yml");

			exception.printStackTrace();
		}
	}
}