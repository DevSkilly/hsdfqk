package net.arcana.addons.animation;

import java.util.Collections;
import java.util.List;

import net.arcana.addons.entity.animation.AnimationFrame;

public final class AnimationDefinition {

	private final String id;
	private final int duration;
	private final boolean loop;
	private final double speed;
	private final List<AnimationFrame> frames;

	public AnimationDefinition(String id, boolean loop, List<AnimationFrame> frames) {

		this(id, calculateDuration(frames), loop, 1.0D, frames);
	}

	public AnimationDefinition(String id, int duration, boolean loop, double speed) {

		this(id, duration, loop, speed, Collections.emptyList());
	}

	public AnimationDefinition(String id, int duration, boolean loop, double speed, List<AnimationFrame> frames) {

		if (id == null || id.isBlank()) {
			throw new IllegalArgumentException("Animation ID cannot be empty.");
		}

		if (duration <= 0) {
			throw new IllegalArgumentException("Animation duration must be > 0.");
		}

		if (speed <= 0.0D) {
			throw new IllegalArgumentException("Animation speed must be > 0.");
		}

		if (frames == null) {
			throw new IllegalArgumentException("Animation frames cannot be null.");
		}

		this.id = id.toLowerCase();
		this.duration = duration;
		this.loop = loop;
		this.speed = speed;
		this.frames = List.copyOf(frames);
	}

	private static int calculateDuration(List<AnimationFrame> frames) {

		if (frames == null || frames.isEmpty()) {
			return 1;
		}

		long total = 0L;

		for (AnimationFrame frame : frames) {

			if (frame == null) {
				continue;
			}

			total += frame.getDuration();

			if (total >= Integer.MAX_VALUE) {
				return Integer.MAX_VALUE;
			}
		}

		return Math.max(1, (int) total);
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

	public List<AnimationFrame> getFrames() {
		return Collections.unmodifiableList(frames);
	}

	/*
	 * Compatibilité avec différents loaders/systèmes.
	 */

	public String id() {
		return id;
	}

	public int duration() {
		return duration;
	}

	public boolean loop() {
		return loop;
	}

	public double speed() {
		return speed;
	}

	public List<AnimationFrame> frames() {
		return Collections.unmodifiableList(frames);
	}

	public boolean isEmpty() {
		return frames.isEmpty();
	}

	public int getFrameCount() {
		return frames.size();
	}
}