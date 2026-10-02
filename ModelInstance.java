package net.arcana.addons.entity.model;

import org.bukkit.entity.ItemDisplay;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ModelInstance {

	private final ModelDefinition definition;

	private final Map<String, ModelPart> parts = new ConcurrentHashMap<>();

	public ModelInstance(ModelDefinition definition) {
		this.definition = definition;
	}

	public ModelDefinition getDefinition() {
		return definition;
	}

	public void addPart(ModelPart part) {

		if (part == null) {
			return;
		}

		parts.put(part.getId().toLowerCase(), part);
	}

	public ModelPart getPart(String id) {

		if (id == null) {
			return null;
		}

		return parts.get(id.toLowerCase());
	}

	public Map<String, ModelPart> getParts() {
		return Collections.unmodifiableMap(parts);
	}

	public void remove() {

		for (ModelPart part : parts.values()) {

			ItemDisplay display = part.getDisplay();

			if (display != null && !display.isDead()) {

				display.remove();
			}
		}

		parts.clear();
	}

	public void reset() {

		for (ModelPart part : parts.values()) {
			part.reset();
		}
	}
}