package net.arcana.addons.animation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.arcana.addons.entity.animation.AnimationDefinition;

public final class AnimationRegistry {

	private final Map<String, AnimationDefinition> animations = new ConcurrentHashMap<>();

	public void register(AnimationDefinition animation) {

		animations.put(animation.getId(), animation);
	}

	public AnimationDefinition get(String id) {

		if (id == null) {
			return null;
		}

		return animations.get(id.toLowerCase());
	}

	public void clear() {
		animations.clear();
	}
}