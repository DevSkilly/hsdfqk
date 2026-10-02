package net.arcana.addons.block;

import org.bukkit.Location;

public final class CustomBlock {

	private final String id;
	private final int version;
	private final Location location;

	public CustomBlock(String id, int version, Location location) {
		this.id = id;
		this.version = version;
		this.location = location.clone();
	}

	public String getId() {
		return id;
	}

	public int getVersion() {
		return version;
	}

	public Location getLocation() {
		return location.clone();
	}
}