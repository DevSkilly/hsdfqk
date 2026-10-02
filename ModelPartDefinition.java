package net.arcana.addons.entity.model;

import org.joml.Vector3f;

public final class ModelPartDefinition {

	private final String id;
	private final String model;

	private final Vector3f offset;
	private final Vector3f scale;

	public ModelPartDefinition(String id, String model, Vector3f offset, Vector3f scale) {

		this.id = id.toLowerCase();
		this.model = model;

		this.offset = new Vector3f(offset);
		this.scale = new Vector3f(scale);
	}

	public String getId() {
		return id;
	}

	public String getModel() {
		return model;
	}

	public Vector3f getOffset() {
		return new Vector3f(offset);
	}

	public Vector3f getScale() {
		return new Vector3f(scale);
	}
}