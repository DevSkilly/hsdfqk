package net.arcana.addons.entity;

import net.arcana.addons.ArcanaAddons;
import net.arcana.addons.entity.animation.EntityAnimationManager;

import org.bukkit.scheduler.BukkitRunnable;

public final class EntityTask extends BukkitRunnable {

	private final ArcanaAddons plugin;

	private final EntityPhaseManager phaseManager;
	private final EntityAIManager aiManager;
	private final EntityCombatManager combatManager;
	private final EntityAnimationManager animationManager;

	public EntityTask(ArcanaAddons plugin) {

		this.plugin = plugin;

		this.phaseManager = new EntityPhaseManager();

		this.aiManager = new EntityAIManager();

		this.combatManager = new EntityCombatManager();

		this.animationManager = plugin.getEntityAnimationManager();
	}

	@Override
	public void run() {

		for (CustomEntity customEntity : plugin.getEntityManager().getSpawnedEntities()) {

			if (customEntity == null) {
				continue;
			}

			if (!customEntity.isValid()) {

				plugin.getEntityManager().remove(customEntity);

				continue;
			}

			/*
			 * PHASES
			 */
			phaseManager.update(customEntity);

			/*
			 * IA
			 */
			aiManager.update(customEntity);

			/*
			 * COMBAT
			 */
			combatManager.update(customEntity);

			/*
			 * ANIMATIONS
			 */
			updateAnimation(customEntity);
		}
	}

	private void updateAnimation(CustomEntity entity) {

		if (animationManager == null) {
			return;
		}

		/*
		 * Si une animation prioritaire est déjà en cours, on la laisse se terminer.
		 */
		if (entity.getCurrentAnimation() != null) {

			animationManager.update(entity);

			return;
		}

		/*
		 * Pour l'instant on utilise IDLE.
		 *
		 * Plus tard l'IA déterminera :
		 *
		 * IDLE WALK ATTACK HURT SKILL DEATH
		 */
		if (!animationManager.isPlaying(entity, "idle")) {

			animationManager.play(entity, "idle");
		}
	}

	public EntityPhaseManager getPhaseManager() {
		return phaseManager;
	}

	public EntityAIManager getAIManager() {
		return aiManager;
	}

	public EntityCombatManager getCombatManager() {
		return combatManager;
	}

	public EntityAnimationManager getAnimationManager() {
		return animationManager;
	}

	public void shutdown() {

		combatManager.clearAll();

		cancel();
	}
}