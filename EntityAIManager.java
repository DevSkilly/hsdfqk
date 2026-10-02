package net.arcana.addons.entity;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public final class EntityAIManager {

	public void update(CustomEntity customEntity) {

		if (customEntity == null || !customEntity.isValid()) {
			return;
		}

		LivingEntity entity = customEntity.getEntity();

		EntityDefinition definition = customEntity.getDefinition();

		Player target = findTarget(entity, definition.getDetectionRange());

		if (target == null) {
			return;
		}

		double distance = entity.getLocation().distance(target.getLocation());

		if (distance > definition.getAttackRange()) {

			moveToTarget(entity, target);

			return;
		}
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

	private void moveToTarget(LivingEntity entity, Player target) {

		Location targetLocation = target.getLocation();

		Location current = entity.getLocation();

		double dx = targetLocation.getX() - current.getX();

		double dz = targetLocation.getZ() - current.getZ();

		double length = Math.sqrt(dx * dx + dz * dz);

		if (length <= 0.001D) {
			return;
		}

		double speed = 0.10D;

		double x = dx / length * speed;

		double z = dz / length * speed;

		entity.setVelocity(entity.getVelocity().setX(x).setZ(z));

		/*
		 * Oriente le mob vers sa cible.
		 */
		Location look = entity.getLocation();

		look.setDirection(targetLocation.toVector().subtract(look.toVector()));

		entity.teleport(new Location(entity.getWorld(), entity.getX(), entity.getY(), entity.getZ(), look.getYaw(),
				look.getPitch()));
	}
}