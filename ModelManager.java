package net.arcana.addons.entity.model;

import net.arcana.addons.ArcanaAddons;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.joml.Vector3f;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ModelManager {

	private final ArcanaAddons plugin;

	private final Map<String, ModelDefinition> definitions = new ConcurrentHashMap<>();

	public ModelManager(ArcanaAddons plugin) {
		this.plugin = plugin;
	}

	public void register(ModelDefinition definition) {

		if (definition == null) {
			return;
		}

		definitions.put(definition.getId(), definition);
	}

	public ModelDefinition get(String id) {

		if (id == null) {
			return null;
		}

		return definitions.get(id.toLowerCase());
	}

	public ModelInstance spawn(String modelId, Location location) {

		ModelDefinition definition = get(modelId);

		if (definition == null) {
			return null;
		}

		ModelInstance instance = new ModelInstance(definition);

		for (ModelPartDefinition part : definition.getParts().values()) {

			Location partLocation = location.clone().add(part.getOffset().x, part.getOffset().y, part.getOffset().z);

			ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(partLocation, EntityType.ITEM_DISPLAY);

			ItemStack item = createModelItem(part.getModel());

			display.setItemStack(item);

			display.setBillboard(Display.Billboard.FIXED);

			display.setInterpolationDuration(2);
			display.setInterpolationDelay(0);

			display.setViewRange(64.0F);

			Vector3f scale = part.getScale();

			display.setTransformation(new org.bukkit.util.Transformation(new Vector3f(), new org.joml.Quaternionf(),
					scale, new org.joml.Quaternionf()));

			ModelPart modelPart = new ModelPart(part.getId(), display, part.getOffset(), scale);

			instance.addPart(modelPart);
		}

		return instance;
	}

	private ItemStack createModelItem(String modelId) {

		ItemStack item = new ItemStack(Material.PAPER);

		ItemMeta meta = item.getItemMeta();

		if (meta != null) {

			/*
			 * Le modèle 3D sera fourni par le ResourcePack via CustomModelData.
			 */
			meta.setCustomModelData(Math.abs(modelId.hashCode()));

			item.setItemMeta(meta);
		}

		return item;
	}

	public void clear() {
		definitions.clear();
	}
}