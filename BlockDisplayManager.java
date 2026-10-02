package net.arcana.addons.block;

import net.arcana.addons.ArcanaAddons;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class BlockDisplayManager {

	private final ArcanaAddons plugin;

	private final Map<String, AnimatedBlock> blocks = new ConcurrentHashMap<>();

	public BlockDisplayManager(ArcanaAddons plugin) {
		this.plugin = plugin;
	}

	public AnimatedBlock spawn(String id, Location location, Material material) {

		if (id == null || location == null) {
			throw new IllegalArgumentException("id/location cannot be null");
		}

		if (material == null || !material.isBlock()) {

			throw new IllegalArgumentException("Material must be a block.");
		}

		remove(id);

		BlockDisplay display = (BlockDisplay) location.getWorld().spawnEntity(location, EntityType.BLOCK_DISPLAY);

		BlockData data = material.createBlockData();

		display.setBlock(data);

		display.setPersistent(true);

		AnimatedBlock animated = new AnimatedBlock(id, location, display);

		blocks.put(id.toLowerCase(), animated);

		return animated;
	}

	public AnimatedBlock get(String id) {

		if (id == null) {
			return null;
		}

		return blocks.get(id.toLowerCase());
	}

	public void remove(String id) {

		if (id == null) {
			return;
		}

		AnimatedBlock block = blocks.remove(id.toLowerCase());

		if (block == null) {
			return;
		}

		if (!block.getDisplay().isDead()) {
			block.getDisplay().remove();
		}
	}

	public void removeAll() {

		for (AnimatedBlock block : blocks.values()) {

			if (!block.getDisplay().isDead()) {
				block.getDisplay().remove();
			}
		}

		blocks.clear();
	}

	/**
	 * Rotation autour de l'axe Y.
	 */
	public void rotateY(String id, float degrees) {

		AnimatedBlock block = get(id);

		if (block == null) {
			return;
		}

		BlockDisplay display = block.getDisplay();

		float radians = (float) Math.toRadians(degrees);

		Transformation transformation = display.getTransformation();

		transformation.getLeftRotation().set(new AxisAngle4f(radians, 0, 1, 0));

		display.setTransformation(transformation);
	}

	/**
	 * Change la taille du bloc.
	 */
	public void scale(String id, float x, float y, float z) {

		AnimatedBlock block = get(id);

		if (block == null) {
			return;
		}

		BlockDisplay display = block.getDisplay();

		Transformation transformation = display.getTransformation();

		transformation.getScale().set(new Vector3f(x, y, z));

		display.setTransformation(transformation);
	}

	public void rotateAnimation(String id, float from, float to, long duration) {

		AnimatedBlock block = get(id);

		if (block == null) {
			return;
		}

		BlockDisplay display = block.getDisplay();

		int steps = Math.max(1, (int) (duration / 50L));

		float step = (to - from) / steps;

		display.setInterpolationDuration(1);

		display.setInterpolationDelay(0);

		animateRotation(display, from, step, steps, 0);
	}

	private void animateRotation(BlockDisplay display, float current, float step, int totalSteps, int stepIndex) {

		if (display.isDead()) {
			return;
		}

		float rotation = current + step;

		Transformation transformation = display.getTransformation();

		transformation.getLeftRotation().set(new AxisAngle4f((float) Math.toRadians(rotation), 0, 1, 0));

		display.setTransformation(transformation);

		if (stepIndex >= totalSteps) {
			return;
		}

		plugin.getServer().getScheduler().runTaskLater(plugin,
				() -> animateRotation(display, rotation, step, totalSteps, stepIndex + 1), 1L);
	}

	/**
	 * Change le bloc affiché.
	 */
	public void setMaterial(String id, Material material) {

		AnimatedBlock block = get(id);

		if (block == null || material == null || !material.isBlock()) {
			return;
		}

		block.getDisplay().setBlock(material.createBlockData());
	}

	public Map<String, AnimatedBlock> getBlocks() {
		return Map.copyOf(blocks);
	}
}