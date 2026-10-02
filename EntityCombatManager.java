package net.arcana.addons.entity;

import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class EntityCombatManager {

	private final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();

	public void update(CustomEntity customEntity) {

		if (customEntity == null || !customEntity.isValid()) {
			return;
		}

		LivingEntity entity = customEntity.getEntity();

		EntityDefinition definition = customEntity.getDefinition();

		Player target = findTarget(entity, definition.getAttackRange());

		if (target == null) {
			return;
		}

		attack(customEntity, target);
	}

	private Player findTarget(LivingEntity entity, double range) {

		Player closest = null;
		double closestDistance = range;

		for (Player player : entity.getWorld().getPlayers()) {

			if (!player.isOnline() || player.isDead() || player.getGameMode() == GameMode.SPECTATOR) {
				continue;
			}

			double distance = entity.getLocation().distance(player.getLocation());

			if (distance <= closestDistance) {

				closest = player;
				closestDistance = distance;
			}
		}

		return closest;
	}

	public void attack(CustomEntity customEntity, Player player) {

		if (customEntity == null || player == null || !customEntity.isValid() || player.isDead()) {
			return;
		}

		LivingEntity entity = customEntity.getEntity();

		EntityDefinition definition = customEntity.getDefinition();

		double distance = entity.getLocation().distance(player.getLocation());

		if (distance > definition.getAttackRange()) {
			return;
		}

		UUID uuid = entity.getUniqueId();

		long now = System.currentTimeMillis();

		long lastAttack = cooldowns.getOrDefault(uuid, 0L);

		if (now - lastAttack < definition.getAttackCooldown()) {
			return;
		}

		cooldowns.put(uuid, now);

		double damage = definition.getDamage();

		EntityPhaseDefinition phase = getCurrentPhase(customEntity);

		if (phase != null) {

			damage *= phase.getDamageMultiplier();
		}

		player.damage(damage, entity);
	}

	private EntityPhaseDefinition getCurrentPhase(CustomEntity customEntity) {

		String phaseId = customEntity.getCurrentPhase();

		if (phaseId == null) {
			return null;
		}

		for (EntityPhaseDefinition phase : customEntity.getDefinition().getPhases()) {

			if (phase.getId().equalsIgnoreCase(phaseId)) {

				return phase;
			}
		}

		return null;
	}

	public void clear(UUID uuid) {

		if (uuid != null) {
			cooldowns.remove(uuid);
		}
	}

	public void clearAll() {
		cooldowns.clear();
	}
}