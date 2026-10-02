package net.arcana.addons.entity.visual;

public record VisualAnimationFrame(int modelData, long duration) {

	public VisualAnimationFrame {
		if (duration < 1) {
			duration = 1;
		}
	}
}