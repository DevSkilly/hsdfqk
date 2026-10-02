package net.arcana.addons.animation;

public final class AnimationFrame {

	private final int modelData;
	private final long duration;
	private final String effect;

	public AnimationFrame(int modelData, long duration, String effect) {

		if (modelData < 0) {
			throw new IllegalArgumentException("ModelData cannot be negative.");
		}

		if (duration < 1) {
			throw new IllegalArgumentException("Animation frame duration must be >= 1.");
		}

		this.modelData = modelData;
		this.duration = duration;
		this.effect = effect;
	}

	public int modelData() {
		return modelData;
	}

	public long duration() {
		return duration;
	}

	public String effect() {
		return effect;
	}

	public int getModelData() {
		return modelData;
	}

	public long getDuration() {
		return duration;
	}

	public String getEffect() {
		return effect;
	}
}