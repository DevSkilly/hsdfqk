package net.arcana.addons.ability.impl;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.arcana.addons.ability.Ability;
import net.arcana.addons.ability.AbilityContext;

public final class RubySlashAbility implements Ability {

	@Override
	public String getId() {
		return "ruby_slash";
	}

	@Override
	public void execute(AbilityContext context) {

		Player player = context.getPlayer();

		/*
		 * Effets visuels.
		 */
		player.getWorld().spawnParticle(Particle.DUST_PLUME, player.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 1);

		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.8f);

		/*
		 * Direction du joueur.
		 */
		Vector direction = player.getLocation().getDirection().normalize();

		/*
		 * Entités proches.
		 */
		for (Entity entity : player.getNearbyEntities(4.0, 2.0, 4.0)) {

			if (!(entity instanceof LivingEntity target)) {
				continue;
			}

			if (target.equals(player)) {
				continue;
			}

			/*
			 * Vérification approximative de la direction.
			 */
			Vector toTarget = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();

			double dot = direction.dot(toTarget);

			if (dot < 0.35) {
				continue;
			}

			target.damage(6.0, player);

			Vector knockback = toTarget.multiply(0.6).setY(0.25);

			target.setVelocity(knockback);

			target.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, target.getLocation().add(0, 1, 0), 10, 0.3, 0.3,
					0.3, 0.1);
		}
	}
}