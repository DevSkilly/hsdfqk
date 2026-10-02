package net.arcana.addons.entity;

import java.util.List;

public final class EntitySkillDefinition {

	private final String id;
	private final String type;
	private final long cooldown;
	private final double range;
	private final double damage;
	private final List<String> effects;

	public EntitySkillDefinition(String id, String type, long cooldown, double range, double damage,
			List<String> effects) {

		this.id = id;
		this.type = type;
		this.cooldown = cooldown;
		this.range = range;
		this.damage = damage;
		this.effects = List.copyOf(effects);
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public long getCooldown() {
		return cooldown;
	}

	public double getRange() {
		return range;
	}

	public double getDamage() {
		return damage;
	}

	public List<String> getEffects() {
		return effects;
	}
}