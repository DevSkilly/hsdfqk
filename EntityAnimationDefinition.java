package net.arcana.addons.entity.animation;

public final class EntityAnimationDefinition {

	private final String id;
	private final int duration;
	private final boolean loop;
	private final double speed;
	private final float yaw;
	private final float pitch;
	private final float roll;

	public EntityAnimationDefinition(String id, int duration, boolean loop, double speed, float yaw, float pitch,
			float roll) {
		this.id = id;
		this.duration = Math.max(1, duration);
		this.loop = loop;
		this.speed = Math.max(0.01D, speed);
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
	}

	public String getId() {
		return id;
	}

	public int getDuration() {
		return duration;
	}

	public boolean isLoop() {
		return loop;
	}

	public double getSpeed() {
		return speed;
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public float getRoll() {
		return roll;
	}
}