package net.arcana.addons.animation;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import net.arcana.addons.ArcanaAddons;
import net.arcana.addons.entity.animation.AnimationFrame;

public final class AnimationManager {

	private final ArcanaAddons plugin;
	private final AnimationRegistry registry;

	private final Map<UUID, BukkitTask> running = new ConcurrentHashMap<>();

	public AnimationManager(ArcanaAddons plugin) {
		this.plugin = plugin;
		this.registry = new AnimationRegistry();
	}

	public AnimationRegistry getRegistry() {
		return registry;
	}

	/**
	 * Lance une animation sur un ItemStack.
	 */
	public void playItem(Player player, ItemStack item, String animationId) {

		if (player == null || item == null) {
			return;
		}

		AnimationDefinition animation = registry.get(animationId);

		if (animation == null) {
			plugin.getLogger().warning("Unknown animation: " + animationId);
			return;
		}

		stop(player);

		playFrame(player, item, animation, 0);
	}

	private void playFrame(Player player, ItemStack item, AnimationDefinition animation, int frameIndex) {

		if (!player.isOnline()) {
			stop(player);
			return;
		}

		if (frameIndex >= animation.getFrames().size()) {

			if (!animation.isLoop()) {
				stop(player);
				return;
			}

			frameIndex = 0;
		}

		AnimationFrame frame = animation.getFrames().get(frameIndex);

		applyFrame(item, frame);

		int nextFrame = frameIndex + 1;

		long delayTicks = Math.max(1L, (long) Math.ceil(frame.duration() / 50.0));

		final int finalFrameIndex = nextFrame;

		BukkitTask task = plugin.getServer().getScheduler().runTaskLater(plugin,
				() -> playFrame(player, item, animation, finalFrameIndex), delayTicks);

		running.put(player.getUniqueId(), task);
	}

	private void applyFrame(ItemStack item, AnimationFrame frame) {

		if (frame.modelData() < 0) {
			return;
		}

		ItemMeta meta = item.getItemMeta();

		if (meta == null) {
			return;
		}

		meta.setCustomModelData(frame.modelData());

		item.setItemMeta(meta);
	}

	public void stop(Player player) {

		if (player == null) {
			return;
		}

		BukkitTask task = running.remove(player.getUniqueId());

		if (task != null) {
			task.cancel();
		}
	}

	public boolean isPlaying(Player player) {

		return player != null && running.containsKey(player.getUniqueId());
	}

	public void shutdown() {

		for (BukkitTask task : running.values()) {

			task.cancel();
		}

		running.clear();
		registry.clear();
	}
}