package net.arcana.addons.block;

import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;

public final class AnimatedBlock {

	private final String id;
	private final Location location;
	private final BlockDisplay display;

	public AnimatedBlock(String id, Location location, BlockDisplay display) {
		this.id = id;
		this.location = location.clone();
		this.display = display;
	}

	public String getId() {
		return id;
	}

	public Location getLocation() {
		return location.clone();
	}

	public BlockDisplay getDisplay() {
		return display;
	}
	
	
}