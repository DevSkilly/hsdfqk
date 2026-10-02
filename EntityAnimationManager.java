package net.arcana.addons.entity.animation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.arcana.addons.entity.CustomEntity;

public final class EntityAnimationManager {

	private final Map<String, AnimationDefinition> animations = new ConcurrentHashMap<>();

	private final Map<CustomEntity, AnimationPlayer> players = new ConcurrentHashMap<>();

	public void register(AnimationDefinition definition) {

		if (definition == null) {
			return;
		}

		animations.put(definition.getId().toLowerCase(), definition);
	}

	public AnimationDefinition get(String id) {

		if (id == null || id.isBlank()) {
			return null;
		}

		return animations.get(id.toLowerCase());
	}

	public boolean has(String id) {

		return get(id) != null;
	}

	public void play(CustomEntity entity, String animation) {

		if (entity == null || animation == null || animation.isBlank()) {

			return;
		}

		AnimationDefinition definition = get(animation);

		if (definition == null) {
			return;
		}

		AnimationPlayer current = players.get(entity);

		/*
		 * Ne redémarre pas une animation si elle est déjà en cours.
		 */
		if (current != null && current.getId().equalsIgnoreCase(definition.getId())) {

			return;
		}

		players.put(entity, new AnimationPlayer(definition));

		entity.setCurrentAnimation(definition.getId());
	}

	public void update(CustomEntity entity) {

		if (entity == null) {
			return;
		}

		AnimationPlayer player = players.get(entity);

		if (player == null) {
			return;
		}

		if (!entity.isValid()) {

			players.remove(entity);

			return;
		}

		boolean finished = player.update(entity.getModelInstance());

		if (!finished) {
			return;
		}

		players.remove(entity);

		entity.setCurrentAnimation(null);
	}

	public boolean isPlaying(CustomEntity entity, String animation) {

		if (entity == null || animation == null) {

			return false;
		}

		AnimationPlayer player = players.get(entity);

		return player != null && player.getId().equalsIgnoreCase(animation);
	}

	public AnimationPlayer getPlayer(CustomEntity entity) {

		return players.get(entity);
	}

	public void stop(CustomEntity entity) {

		if (entity == null) {
			return;
		}

		players.remove(entity);

		entity.setCurrentAnimation(null);
	}

	public void stopAll() {

		for (CustomEntity entity : players.keySet()) {

			if (entity != null) {

				entity.setCurrentAnimation(null);
			}
		}

		players.clear();
	}

	public void clear() {

		stopAll();

		animations.clear();
	}

	public int getAnimationCount() {

		return animations.size();
	}

	public int getPlayingCount() {

		return players.size();
	}
}