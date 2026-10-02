package net.arcana.addons.ability;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CooldownManager {

	private final Map<UUID, Map<String, Long>> cooldowns = new ConcurrentHashMap<>();

	public boolean isOnCooldown(UUID uuid, String abilityId) {

		return getRemaining(uuid, abilityId) > 0;
	}

	public long getRemaining(UUID uuid, String abilityId) {

		Map<String, Long> playerCooldowns = cooldowns.get(uuid);

		if (playerCooldowns == null) {
			return 0L;
		}

		Long expiration = playerCooldowns.get(abilityId.toLowerCase());

		if (expiration == null) {
			return 0L;
		}

		long remaining = expiration - System.currentTimeMillis();

		if (remaining <= 0) {

			playerCooldowns.remove(abilityId.toLowerCase());

			if (playerCooldowns.isEmpty()) {
				cooldowns.remove(uuid);
			}

			return 0L;
		}

		return remaining;
	}

	public void setCooldown(UUID uuid, String abilityId, long duration) {

		if (duration <= 0) {
			return;
		}

		cooldowns.computeIfAbsent(uuid, ignored -> new ConcurrentHashMap<>()).put(abilityId.toLowerCase(),
				System.currentTimeMillis() + duration);
	}

	public void remove(UUID uuid, String abilityId) {

		Map<String, Long> playerCooldowns = cooldowns.get(uuid);

		if (playerCooldowns == null) {
			return;
		}

		playerCooldowns.remove(abilityId.toLowerCase());

		if (playerCooldowns.isEmpty()) {
			cooldowns.remove(uuid);
		}
	}

	public void clearPlayer(UUID uuid) {
		cooldowns.remove(uuid);
	}

	public void clear() {
		cooldowns.clear();
	}
}