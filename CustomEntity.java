package net.arcana.addons.entity;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import net.arcana.addons.entity.model.ModelInstance;

public final class CustomEntity {

	private final LivingEntity entity;
	private final EntityDefinition definition;
	private String currentAnimation;
	private int animationTick;
	private net.arcana.addons.entity.model.ModelInstance modelInstance;

	private String currentPhase;

	public CustomEntity(LivingEntity entity, EntityDefinition definition) {
		this.entity = entity;
		this.definition = definition;
	}

	public LivingEntity getEntity() {
		return entity;
	}

	public EntityDefinition getDefinition() {
		return definition;
	}

	public String getCurrentPhase() {
		return currentPhase;
	}

	public String getCurrentAnimation() {
		return currentAnimation;
	}

	public void setCurrentAnimation(String animation) {
		this.currentAnimation = animation;
		this.animationTick = 0;
	}

	public ModelInstance getModelInstance() {
		return modelInstance;
	}

	public void setModelInstance(ModelInstance modelInstance) {
		this.modelInstance = modelInstance;
	}
	
	public void removeModel() {

	    if (modelInstance != null) {

	        modelInstance.remove();
	        modelInstance = null;
	    }
	}

	public int getAnimationTick() {
		return animationTick;
	}

	public void setAnimationTick(int animationTick) {
		this.animationTick = animationTick;
	}

	public void incrementAnimationTick() {
		this.animationTick++;
	}

	public void clearAnimation() {
		this.currentAnimation = null;
		this.animationTick = 0;
	}

	public void setCurrentPhase(String currentPhase) {
		this.currentPhase = currentPhase;
	}

	public boolean isValid() {

		return entity != null && entity.isValid() && !entity.isDead();
	}

	public Location getLocation() {

		if (entity == null) {
			return null;
		}

		return entity.getLocation();
	}
}