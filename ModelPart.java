package net.arcana.addons.entity.model;

import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class ModelPart {

	private final String id;
	private final ItemDisplay display;

	private final Vector3f baseTranslation;
	private final Vector3f baseScale;

	private Quaternionf baseLeftRotation;
	private Quaternionf baseRightRotation;

	public ModelPart(String id, ItemDisplay display, Vector3f translation, Vector3f scale) {
		this.id = id;
		this.display = display;
		this.baseTranslation = new Vector3f(translation);
		this.baseScale = new Vector3f(scale);

		Transformation transformation = display.getTransformation();

		this.baseLeftRotation = new Quaternionf(transformation.getLeftRotation());

		this.baseRightRotation = new Quaternionf(transformation.getRightRotation());
	}

	public String getId() {
		return id;
	}

	public ItemDisplay getDisplay() {
		return display;
	}

	public Vector3f getBaseTranslation() {
		return new Vector3f(baseTranslation);
	}

	public Vector3f getBaseScale() {
		return new Vector3f(baseScale);
	}

	public Quaternionf getBaseLeftRotation() {
		return new Quaternionf(baseLeftRotation);
	}

	public Quaternionf getBaseRightRotation() {
		return new Quaternionf(baseRightRotation);
	}

	public void reset() {

		Transformation transformation = new Transformation(new Vector3f(baseTranslation),
				new Quaternionf(baseLeftRotation), new Vector3f(baseScale), new Quaternionf(baseRightRotation));

		display.setTransformation(transformation);
	}

	public void setTransformation(Vector3f translation, Quaternionf leftRotation, Vector3f scale,
			Quaternionf rightRotation) {

		Transformation transformation = new Transformation(new Vector3f(translation), new Quaternionf(leftRotation),
				new Vector3f(scale), new Quaternionf(rightRotation));

		display.setTransformation(transformation);
	}
}