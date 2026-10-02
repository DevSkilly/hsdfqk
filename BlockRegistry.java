package net.arcana.addons.block;

import org.bukkit.Location;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BlockRegistry {

	private final Map<String, CustomBlock> blocks = new ConcurrentHashMap<>();

	public void register(CustomBlock block) {

		if (block == null) {
			return;
		}

		blocks.put(key(block.getLocation()), block);
	}

	public CustomBlock get(Location location) {

		if (location == null || location.getWorld() == null) {
			return null;
		}

		return blocks.get(key(location));
	}

	public boolean contains(Location location) {
		return get(location) != null;
	}

	public void remove(Location location) {

		if (location == null || location.getWorld() == null) {
			return;
		}

		blocks.remove(key(location));
	}

	public Map<String, CustomBlock> getBlocks() {
		return Collections.unmodifiableMap(blocks);
	}

	public void clear() {
		blocks.clear();
	}

	private String key(Location location) {

		return location.getWorld().getUID() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":"
				+ location.getBlockZ();
	}
}