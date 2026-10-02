package net.arcana.addons.entity;

public final class EntityPhaseDefinition {

    private final String id;
    private final double healthPercent;
    private final double damageMultiplier;
    private final double speedMultiplier;

    public EntityPhaseDefinition(
            String id,
            double healthPercent,
            double damageMultiplier,
            double speedMultiplier
    ) {
        this.id = id;
        this.healthPercent = healthPercent;
        this.damageMultiplier = damageMultiplier;
        this.speedMultiplier = speedMultiplier;
    }

    public String getId() {
        return id;
    }

    public double getHealthPercent() {
        return healthPercent;
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }
}