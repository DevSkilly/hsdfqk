package net.arcana.addons.entity;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

public final class EntityPhaseManager {

    public void update(CustomEntity customEntity) {

        if (customEntity == null || !customEntity.isValid()) {
            return;
        }

        LivingEntity entity = customEntity.getEntity();
        EntityDefinition definition = customEntity.getDefinition();

        if (definition.getPhases().isEmpty()) {
            return;
        }

        double maxHealth = getMaxHealth(entity, definition);

        if (maxHealth <= 0.0D) {
            return;
        }

        double healthPercent =
                (entity.getHealth() / maxHealth) * 100.0D;

        EntityPhaseDefinition activePhase = null;

        /*
         * Les phases sont sélectionnées en fonction
         * du pourcentage de vie.
         */
        for (EntityPhaseDefinition phase : definition.getPhases()) {

            if (healthPercent <= phase.getHealthPercent()) {

                if (activePhase == null
                        || phase.getHealthPercent()
                        > activePhase.getHealthPercent()) {

                    activePhase = phase;
                }
            }
        }

        if (activePhase == null) {
            return;
        }

        /*
         * La phase est déjà active.
         */
        if (activePhase.getId().equalsIgnoreCase(
                customEntity.getCurrentPhase()
        )) {
            return;
        }

        /*
         * Changement de phase.
         */
        customEntity.setCurrentPhase(
                activePhase.getId()
        );

        applyPhase(
                entity,
                definition,
                activePhase
        );
    }

    private double getMaxHealth(
            LivingEntity entity,
            EntityDefinition definition
    ) {

        if (entity.getAttribute(
                Attribute.GENERIC_MAX_HEALTH
        ) != null) {

            return entity.getAttribute(
                    Attribute.GENERIC_MAX_HEALTH
            ).getValue();
        }

        return definition.getHealth();
    }

    private void applyPhase(
            LivingEntity entity,
            EntityDefinition definition,
            EntityPhaseDefinition phase
    ) {

        if (entity.getAttribute(
                Attribute.GENERIC_MOVEMENT_SPEED
        ) != null) {

            entity.getAttribute(
                    Attribute.GENERIC_MOVEMENT_SPEED
            ).setBaseValue(
                    definition.getSpeed()
                            * phase.getSpeedMultiplier()
            );
        }
    }
}