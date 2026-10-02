package net.arcana.addons.entity.visual;

import java.util.List;

public final class VisualAnimation {

	private final String id;
	private final boolean loop;
	private final List<VisualAnimationFrame> frames;

	public VisualAnimation(String id, boolean loop, List<VisualAnimationFrame> frames) {
		this.id = id;
		this.loop = loop;
		this.frames = List.copyOf(frames);
	}

	public String getId() {
		return id;
	}

	public boolean isLoop() {
		return loop;
	}

	public List<VisualAnimationFrame> getFrames() {
		return frames;
	}
}