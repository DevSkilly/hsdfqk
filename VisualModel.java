package net.arcana.addons.entity.visual;

import org.bukkit.util.Vector;

public final class VisualModel {

	private final String model;
	private final double scale;
	private final double offsetX;
	private final double offsetY;
	private final double offsetZ;

	public VisualModel(String model, double scale, double offsetX, double offsetY, double offsetZ) {
		this.model = model;
		this.scale = scale;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
	}

	public String getModel() {
		return model;
	}

	public double getScale() {
		return scale;
	}

	public double getOffsetX() {
		return offsetX;
	}

	public double getOffsetY() {
		return offsetY;
	}

	public double getOffsetZ() {
		return offsetZ;
	}

	/**
	 * Offset du modèle par rapport à l'entité.
	 */
	public Vector getOffset() {
		return new Vector(offsetX, offsetY, offsetZ);
	}
}