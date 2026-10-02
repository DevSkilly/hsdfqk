package net.arcana.addons.entity.visual;

import net.arcana.addons.entity.animation.EntityAnimationManager;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class VisualEntityTask extends BukkitRunnable {

	private final VisualEntityManager manager;
	private final EntityAnimationManager animationManager;

	public VisualEntityTask(VisualEntityManager manager, EntityAnimationManager animationManager) {
		this.manager = manager;
		this.animationManager = animationManager;
	}

	@Override
	public void run() {

		for (VisualEntity visual : manager.getVisuals().values()) {

			if (!visual.isValid() || !visual.getCustomEntity().isValid()) {

				manager.remove(visual.getCustomEntity());

				continue;
			}

			/*
			 * Animation
			 */
			animationManager.update(visual.getCustomEntity());

			/*
			 * Si aucune animation n'est active, on suit simplement l'entité.
			 */
			if (visual.getCustomEntity().getCurrentAnimation() == null) {

				Location location = visual.getCustomEntity().getLocation();

				if (location == null) {
					continue;
				}

				Vector offset = visual.getModel().getOffset();

				Location visualLocation = location.clone().add(offset);

				visualLocation.setYaw(location.getYaw());

				visualLocation.setPitch(location.getPitch());

				visual.getDisplay().teleport(visualLocation);
			}
		}
	}
}