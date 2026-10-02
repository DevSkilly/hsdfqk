package net.arcana.addons.resourcepack.model;

import java.util.Collections;
import java.util.Map;

import net.arcana.addons.animation.AnimationDefinition;

public final class Model3DDefinition {

	private final String model;
	private final String texture;
	private final double scale;
	private final Map<String, AnimationDefinition> animations;

	public Model3DDefinition(String model, String texture, double scale, Map<String, AnimationDefinition> animations) {
		this.model = model;
		this.texture = texture;
		this.scale = scale;
		this.animations = animations;
	}

	public String getModel() {
		return model;
	}

	public String getTexture() {
		return texture;
	}

	public double getScale() {
		return scale;
	}

	public Map<String, AnimationDefinition> getAnimations() {
		return Collections.unmodifiableMap(animations);
	}

	public AnimationDefinition getAnimation(String id) {
		if (id == null) {
			return null;
		}

		return animations.get(id.toLowerCase());
	}
}