package net.arcana.addons.item;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.arcana.addons.ArcanaAddons;
import net.arcana.addons.resourcepack.ModelDataManager;
import net.arcana.addons.resourcepack.ResourcePackDefinition;
import net.arcana.addons.util.PDCUtil;
import net.arcana.addons.util.TimeUtil;

public final class ItemManager {

	private final ArcanaAddons plugin;

	private final ItemRegistry registry;
	private final ItemIdentifier identifier;
	private final ItemBuilder builder;
	private final ModelDataManager modelDataManager;
	
	private final File itemsFolder;

	public ItemManager(ArcanaAddons plugin) {

		this.plugin = plugin;

		this.registry = new ItemRegistry();
		this.identifier = new ItemIdentifier(plugin);
		this.builder = new ItemBuilder(this);
		this.modelDataManager = new ModelDataManager(plugin);
		
		this.itemsFolder = new File(plugin.getDataFolder(), "items");
	}

	/**
	 * Charge tous les items custom.
	 */
	public void loadItems() {

		if (!itemsFolder.exists() && !itemsFolder.mkdirs()) {

			plugin.getLogger().severe("Cannot create items folder.");

			return;
		}

		/*
		 * IMPORTANT :
		 *
		 * On charge d'abord dans un nouveau registre. Si quelque chose casse, l'ancien
		 * registre reste intact.
		 */
		Map<String, CustomItem> newItems = new HashMap<>();

		loadDirectory(itemsFolder, newItems);

		registry.replaceAll(newItems);

		plugin.getLogger().info("Loaded " + registry.size() + " custom items.");
	}

	/**
	 * Parcourt récursivement le dossier items.
	 */
	private void loadDirectory(File directory, Map<String, CustomItem> destination) {

		File[] files = directory.listFiles();

		if (files == null) {
			return;
		}

		for (File file : files) {

			/*
			 * Sous-dossier.
			 */
			if (file.isDirectory()) {

				loadDirectory(file, destination);

				continue;
			}

			/*
			 * Seulement les fichiers YAML.
			 */
			if (!file.getName().toLowerCase().endsWith(".yml")) {

				continue;
			}

			try {

				CustomItem item = loadFile(file);

				if (item == null) {
					continue;
				}

				/*
				 * Empêche les IDs dupliqués.
				 */
				if (destination.containsKey(item.getId())) {

					throw new IllegalStateException("Duplicate item ID: " + item.getId());
				}

				destination.put(item.getId(), item);

			} catch (Exception exception) {

				plugin.getLogger().severe("Unable to load " + file.getName() + ": " + exception.getMessage());
			}
		}
	}

	/**
	 * Charge un fichier YAML.
	 */
	private CustomItem loadFile(File file) {

		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		/*
		 * ========================= ID =========================
		 */

		String id = config.getString("id");

		if (!ItemValidator.isValidId(id)) {

			throw new IllegalArgumentException("Invalid ID.");
		}

		/*
		 * ========================= MATERIAL =========================
		 */

		String materialName = config.getString("material");

		if (materialName == null) {

			throw new IllegalArgumentException("Material is missing.");
		}

		Material material = Material.matchMaterial(materialName);

		if (material == null || material == Material.AIR) {

			throw new IllegalArgumentException("Invalid material: " + materialName);
		}

		/*
		 * ========================= VERSION =========================
		 */

		int version = config.getInt("version", 1);

		if (version <= 0) {

			throw new IllegalArgumentException("Version must be greater than 0.");
		}

		/*
		 * ========================= DISPLAY =========================
		 */

		String name = config.getString("display.name", "&f" + id);

		List<String> lore = config.getStringList("display.lore");

		/*
		 * ========================= MODEL DATA =========================
		 */

		ConfigurationSection resource = config.getConfigurationSection("resource");

		int modelData = -1;

		if (resource != null) {
			modelData = resource.getInt("model-data", -1);
		}
		/*
		 * ========================= STATS =========================
		 */

		double damage = config.getDouble("stats.damage", 0D);

		double attackSpeed = config.getDouble("stats.attack-speed", 0D);

		if (damage < 0) {

			throw new IllegalArgumentException("Damage cannot be negative.");
		}

		if (attackSpeed < 0) {

			throw new IllegalArgumentException("Attack speed cannot be negative.");
		}

		/*
		 * ========================= DURABILITY =========================
		 */

		boolean durabilityEnabled = config.getBoolean("durability.enabled", false);

		int maxDurability = config.getInt("durability.max", 0);

		if (durabilityEnabled && maxDurability <= 0) {

			throw new IllegalArgumentException("Durability max must be > 0.");
		}

		/*
		 * ========================= ABILITIES + RP =========================
		 *
		 * On récupère maintenant :
		 *
		 * abilities : trigger -> ability ID
		 *
		 * cooldowns : trigger -> cooldown
		 */

		AbilityData abilityData = parseAbilities(config);
		ResourcePackDefinition resourcePackData = parseResourcePack(config);

		/*
		 * ========================= ITEM DEFINITION =========================
		 */

		ItemDefinition definition = new ItemDefinition(id, version, material.name(), name, lore, modelData, damage,
				attackSpeed, durabilityEnabled, maxDurability, abilityData.abilities, abilityData.cooldowns,
				resourcePackData);

		return new CustomItem(this, definition);
	}

	/**
	 * Parse les abilities du fichier YAML.
	 *
	 * Exemple :
	 *
	 * abilities:
	 *
	 * right-click: id: ruby_slash cooldown: 5s
	 *
	 * shift-right-click: id: ruby_burst cooldown: 15s
	 */
	private AbilityData parseAbilities(YamlConfiguration config) {

		Map<String, String> abilities = new HashMap<>();

		Map<String, Long> cooldowns = new HashMap<>();

		ConfigurationSection section = config.getConfigurationSection("abilities");

		/*
		 * Aucun ability configuré.
		 */
		if (section == null) {

			return new AbilityData(abilities, cooldowns);
		}

		for (String trigger : section.getKeys(false)) {

			ConfigurationSection ability = section.getConfigurationSection(trigger);

			/*
			 * Protection contre une mauvaise structure YAML.
			 */
			if (ability == null) {

				plugin.getLogger().warning("Invalid ability section '" + trigger + "'.");

				continue;
			}

			/*
			 * ========================= ABILITY ID =========================
			 */

			String abilityId = ability.getString("id");

			if (abilityId == null || abilityId.isBlank()) {

				throw new IllegalArgumentException("Ability ID is missing " + "for trigger: " + trigger);
			}

			/*
			 * ========================= COOLDOWN =========================
			 */

			String cooldown = ability.getString("cooldown", "0ms");

			long cooldownMillis;

			try {

				cooldownMillis = TimeUtil.parseMillis(cooldown);

			} catch (IllegalArgumentException exception) {

				throw new IllegalArgumentException(
						"Invalid cooldown '" + cooldown + "' for ability '" + abilityId + "'.", exception);
			}

			/*
			 * ========================= NORMALISATION =========================
			 */

			String normalizedTrigger = trigger.toLowerCase();

			String normalizedAbilityId = abilityId.toLowerCase();

			/*
			 * ========================= ENREGISTREMENT =========================
			 */

			abilities.put(normalizedTrigger, normalizedAbilityId);

			cooldowns.put(normalizedTrigger, cooldownMillis);
		}

		return new AbilityData(abilities, cooldowns);
	}

	private ResourcePackDefinition parseResourcePack(YamlConfiguration config) {

		ConfigurationSection section = config.getConfigurationSection("resource");

		if (section == null) {
			return null;
		}

		String model = section.getString("model");

		String texture = section.getString("texture");


		if (model == null || model.isBlank()) {
			throw new IllegalArgumentException("Resource model is missing.");
		}

		if (texture == null || texture.isBlank()) {
			throw new IllegalArgumentException("Resource texture is missing.");
		}


		validateResourceName(model, "model");
		validateResourceName(texture, "texture");

		return new ResourcePackDefinition(model.toLowerCase(), texture);
	}

	private void validateResourceName(String value, String type) {

		if (!value.matches("[a-z0-9_./-]+")) {

			throw new IllegalArgumentException("Invalid resource " + type + ": " + value);
		}
	}

	/**
	 * Récupère un item par son ID.
	 */
	public CustomItem get(String id) {

		if (id == null) {
			return null;
		}

		return registry.get(id.toLowerCase());
	}

	/**
	 * Récupère l'ID d'un ItemStack.
	 */
	public String getItemId(ItemStack itemStack) {

		if (itemStack == null || itemStack.getType() == Material.AIR) {

			return null;
		}

		ItemMeta meta = itemStack.getItemMeta();

		if (meta == null) {
			return null;
		}

		String id = PDCUtil.getString(meta.getPersistentDataContainer(), identifier.getItemIdKey());

		/*
		 * L'item possède peut-être un PDC, mais l'ID peut être inconnu du registre.
		 */
		if (id == null || !registry.contains(id)) {

			return null;
		}

		return id;
	}

	public ItemRegistry getRegistry() {
		return registry;
	}

	public ItemIdentifier getIdentifier() {
		return identifier;
	}

	public ItemBuilder getBuilder() {
		return builder;
	}
	
	public ModelDataManager getModelDataManager() {
		return modelDataManager;
	}

	/**
	 * Arrêt propre du manager.
	 */
	public void shutdown() {
		registry.clear();
	}

	/**
	 * Contient les données des abilities d'un item.
	 */
	private static final class AbilityData {

		private final Map<String, String> abilities;

		private final Map<String, Long> cooldowns;

		private AbilityData(Map<String, String> abilities, Map<String, Long> cooldowns) {

			this.abilities = abilities;

			this.cooldowns = cooldowns;
		}
	}
}