package net.arcana.addons.entity.visual;

import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class VisualTransformUtil {

	private VisualTransformUtil() {
	}

	public static void setRotation(Display display, Quaternionf rotation) {

		if (display == null || rotation == null) {
			return;
		}

		Transformation current = display.getTransformation();

		Transformation updated = new Transformation(
				new Vector3f(current.getTranslation().x, current.getTranslation().y, current.getTranslation().z),
				new Quaternionf(current.getLeftRotation()),
				new Vector3f(current.getScale().x, current.getScale().y, current.getScale().z),
				new Quaternionf(rotation));

		display.setTransformation(updated);
	}

	public static void resetRotation(Display display) {

		if (display == null) {
			return;
		}

		Transformation current = display.getTransformation();

		Transformation updated = new Transformation(
				new Vector3f(current.getTranslation().x, current.getTranslation().y, current.getTranslation().z),
				new Quaternionf(current.getLeftRotation()),
				new Vector3f(current.getScale().x, current.getScale().y, current.getScale().z), new Quaternionf());

		display.setTransformation(updated);
	}
}