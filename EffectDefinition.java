package net.arcana.addons.effect;

import java.util.List;

public final class EffectDefinition {

	private final String id;
	private final List<ParticleEffect> particles;
	private final SoundEffect sound;

	public EffectDefinition(String id, List<ParticleEffect> particles, SoundEffect sound) {
		this.id = id;
		this.particles = List.copyOf(particles);
		this.sound = sound;
	}

	public String getId() {
		return id;
	}

	public List<ParticleEffect> getParticles() {
		return particles;
	}

	public SoundEffect getSound() {
		return sound;
	}

	public record ParticleEffect(String type, int amount, double radius, double offsetX, double offsetY, double offsetZ,
			double speed) {
	}

	public record SoundEffect(String type, float volume, float pitch) {
	}
}