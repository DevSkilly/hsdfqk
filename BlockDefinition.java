package net.arcana.addons.block;

import java.util.Collections;
import java.util.List;

public final class BlockDefinition {

	private final String id;
	private final int version;

	private final String material;

	private final String model;
	private final String texture;

	private final double hardness;

	private final boolean dropEnabled;
	private final String dropItem;
	private final int dropAmount;

	private final List<String> placedEffects;
	private final List<String> brokenEffects;

	private final String idleAnimation;

	public BlockDefinition(String id, int version, String material, String model, String texture, double hardness,
			boolean dropEnabled, String dropItem, int dropAmount, List<String> placedEffects,
			List<String> brokenEffects, String idleAnimation) {

		this.id = id;
		this.version = version;
		this.material = material;
		this.model = model;
		this.texture = texture;
		this.hardness = hardness;
		this.dropEnabled = dropEnabled;
		this.dropItem = dropItem;
		this.dropAmount = dropAmount;

		this.placedEffects = placedEffects == null ? Collections.emptyList() : List.copyOf(placedEffects);

		this.brokenEffects = brokenEffects == null ? Collections.emptyList() : List.copyOf(brokenEffects);

		this.idleAnimation = idleAnimation;
	}

	public String getId() {
		return id;
	}

	public int getVersion() {
		return version;
	}

	public String getMaterial() {
		return material;
	}

	public String getModel() {
		return model;
	}

	public String getTexture() {
		return texture;
	}

	public double getHardness() {
		return hardness;
	}

	public boolean isDropEnabled() {
		return dropEnabled;
	}

	public String getDropItem() {
		return dropItem;
	}

	public int getDropAmount() {
		return dropAmount;
	}

	public List<String> getPlacedEffects() {
		return placedEffects;
	}

	public List<String> getBrokenEffects() {
		return brokenEffects;
	}

	public String getIdleAnimation() {
		return idleAnimation;
	}
}