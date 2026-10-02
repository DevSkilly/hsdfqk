package net.arcana.addons.entity;

import net.arcana.addons.ArcanaAddons;
import net.arcana.addons.entity.animation.EntityAnimationManager;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRemoveEvent;

public final class EntityListener implements Listener {

	private final ArcanaAddons plugin;

	public EntityListener(ArcanaAddons plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDamage(EntityDamageEvent event) {

		if (!(event.getEntity() instanceof LivingEntity living)) {
			return;
		}

		CustomEntityManager manager = plugin.getEntityManager();

		CustomEntity customEntity = manager.get(living);

		if (customEntity == null) {
			return;
		}

		EntityAnimationManager animationManager = plugin.getEntityAnimationManager();

		/*
		 * Ne pas interrompre une animation importante comme ATTACK ou SKILL.
		 */
		String current = customEntity.getCurrentAnimation();

		if (current != null && (current.equalsIgnoreCase("attack") || current.equalsIgnoreCase("skill")
				|| current.equalsIgnoreCase("death"))) {
			return;
		}
		if (animationManager != null) {
			animationManager.play(customEntity, "hurt");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDeath(EntityDeathEvent event) {

		CustomEntityManager manager = plugin.getEntityManager();

		CustomEntity customEntity = manager.get(event.getEntity());

		if (customEntity == null) {
			return;
		}

		String id = customEntity.getDefinition().getId();

		EntityAnimationManager animationManager =
		        plugin.getEntityAnimationManager();

		if (animationManager != null) {
		    animationManager.play(
		            customEntity,
		            "death"
		    );
		}

		/*
		 * On pourra ajouter ici :
		 *
		 * - drops custom - XP - sons - particules - récompenses - boss loot
		 */
	}

	@EventHandler
	public void onRemove(EntityRemoveEvent event) {

		CustomEntityManager manager = plugin.getEntityManager();

		manager.remove(event.getEntity());
	}
}