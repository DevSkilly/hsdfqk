package net.arcana.addons.entity;

import java.util.List;

import net.arcana.addons.entity.visual.VisualModel;

public final class EntityDefinition {

	private final String id;
	private final String baseType;
	private final String name;

	private final double health;
	private final double speed;
	private final double damage;

	private final boolean invulnerable;
	private final boolean glowing;
	private final boolean silent;
	private final boolean gravity;

	private final double detectionRange;
	private final double attackRange;
	private final long attackCooldown;

	private final List<String> effects;

	private final String spawnAnimation;
	private final String deathAnimation;

	private final VisualModel visualModel;

	private final List<EntityPhaseDefinition> phases;

	/*
	 * Constructeur utilisé actuellement par ton EntityLoader. On garde cette
	 * signature pour ne pas casser ton loader.
	 */
	public EntityDefinition(String id, String baseType, String name, double health, double speed, boolean invulnerable,
			boolean glowing, boolean silent, boolean gravity, List<String> effects, String spawnAnimation,
			String deathAnimation,VisualModel visualModel) {

		this(id, baseType, name, health, speed, 0.0D, invulnerable, glowing, silent, gravity, 16.0D, 3.0D, 1000L,
				effects, spawnAnimation, deathAnimation, List.of(), null);
	}

	/*
	 * Constructeur complet.
	 */
	public EntityDefinition(String id, String baseType, String name, double health, double speed, double damage,
			boolean invulnerable, boolean glowing, boolean silent, boolean gravity, double detectionRange,
			double attackRange, long attackCooldown, List<String> effects, String spawnAnimation, String deathAnimation,
			List<EntityPhaseDefinition> phases,VisualModel visualModel) {

		this.id = id;
		this.baseType = baseType;
		this.name = name;

		this.health = health;
		this.speed = speed;
		this.damage = damage;

		this.invulnerable = invulnerable;
		this.glowing = glowing;
		this.silent = silent;
		this.gravity = gravity;

		this.detectionRange = detectionRange;
		this.attackRange = attackRange;
		this.attackCooldown = attackCooldown;

		this.effects = effects == null ? List.of() : List.copyOf(effects);

		this.spawnAnimation = spawnAnimation;
		this.deathAnimation = deathAnimation;
		this.visualModel = visualModel;
		
		this.phases = phases == null ? List.of() : List.copyOf(phases);
	}

	public String getId() {
		return id;
	}

	public String getBaseType() {
		return baseType;
	}

	public String getName() {
		return name;
	}

	public double getHealth() {
		return health;
	}

	public double getSpeed() {
		return speed;
	}

	public double getDamage() {
		return damage;
	}
	
	public VisualModel getVisualModel() {
	    return visualModel;
	}

	public boolean isInvulnerable() {
		return invulnerable;
	}

	public boolean isGlowing() {
		return glowing;
	}
	

	public boolean isSilent() {
		return silent;
	}

	public boolean hasGravity() {
		return gravity;
	}

	public double getDetectionRange() {
		return detectionRange;
	}

	public double getAttackRange() {
		return attackRange;
	}

	public long getAttackCooldown() {
		return attackCooldown;
	}

	public List<String> getEffects() {
		return effects;
	}

	public String getSpawnAnimation() {
		return spawnAnimation;
	}

	public String getDeathAnimation() {
		return deathAnimation;
	}

	public List<EntityPhaseDefinition> getPhases() {
		return phases;
	}
}