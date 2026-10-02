package net.arcana.addons.entity.visual;

import net.arcana.addons.entity.CustomEntity;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class VisualEntityManager {

	private final Map<UUID, VisualEntity> visuals = new ConcurrentHashMap<>();

	public VisualEntity create(CustomEntity customEntity, VisualModel model, ItemStack item) {

		if (customEntity == null || model == null || item == null || !customEntity.isValid()) {

			return null;
		}

		Location location = customEntity.getEntity().getLocation().clone();

		location.add(model.getOffsetX(), model.getOffsetY(), model.getOffsetZ());

		ItemDisplay display = location.getWorld().spawn(location, ItemDisplay.class);

		display.setItemStack(item);

		display.setBillboard(Display.Billboard.FIXED);

		display.setInterpolationDuration(2);
		display.setInterpolationDelay(0);

		float scale = (float) model.getScale();

		Transformation transformation = display.getTransformation();

		transformation.getScale().set(new Vector3f(scale, scale, scale));

		display.setTransformation(transformation);

		VisualEntity visual = new VisualEntity(customEntity, display, model);

		visuals.put(customEntity.getEntity().getUniqueId(), visual);

		return visual;
	}

	public VisualEntity get(CustomEntity customEntity) {

		if (customEntity == null) {
			return null;
		}

		return visuals.get(customEntity.getEntity().getUniqueId());
	}

	public VisualEntity get(UUID uuid) {

		if (uuid == null) {
			return null;
		}

		return visuals.get(uuid);
	}

	public boolean has(CustomEntity customEntity) {

		if (customEntity == null) {
			return false;
		}

		return visuals.containsKey(customEntity.getEntity().getUniqueId());
	}

	public void remove(CustomEntity customEntity) {

		if (customEntity == null) {
			return;
		}

		remove(customEntity.getEntity().getUniqueId());
	}

	public void remove(UUID uuid) {

		if (uuid == null) {
			return;
		}

		VisualEntity visual = visuals.remove(uuid);

		if (visual == null) {
			return;
		}

		if (visual.getDisplay() != null && visual.getDisplay().isValid()) {

			visual.getDisplay().remove();
		}
	}

	public Map<UUID, VisualEntity> getVisuals() {
		return Map.copyOf(visuals);
	}

	public void clear() {

		for (VisualEntity visual : visuals.values()) {

			if (visual.getDisplay() != null && visual.getDisplay().isValid()) {

				visual.getDisplay().remove();
			}
		}

		visuals.clear();
	}

	public int size() {
		return visuals.size();
	}
}