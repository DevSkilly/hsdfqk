package net.arcana.addons.entity.animation;

import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import net.arcana.addons.animation.AnimationDefinition;
import net.arcana.addons.entity.model.ModelInstance;
import net.arcana.addons.entity.model.ModelPart;

public final class AnimationPlayer {

    private final AnimationDefinition definition;

    private double tick;

    public AnimationPlayer(AnimationDefinition definition) {

        if (definition == null) {
            throw new IllegalArgumentException(
                    "Animation definition cannot be null."
            );
        }

        this.definition = definition;
        this.tick = 0.0D;
    }

    public boolean update(ModelInstance model) {

        if (model == null) {
            return false;
        }

        AnimationFrame current = getCurrentFrame();
        AnimationFrame next = getNextFrame();

        if (current != null) {

            if (next != null) {
                applyInterpolatedFrame(model, current, next);
            } else {
                applyFrame(model, current);
            }
        }

        tick += definition.getSpeed();

        if (tick >= definition.getDuration()) {

            if (definition.isLoop()) {

                tick %= definition.getDuration();

                return false;
            }

            return true;
        }

        return false;
    }

    /**
     * Retourne la frame active.
     */
    private AnimationFrame getCurrentFrame() {

        AnimationFrame previous = null;

        for (AnimationFrame frame : definition.getFrames()) {

            if (frame.getTick() <= tick) {

                previous = frame;

            } else {

                break;
            }
        }

        return previous;
    }

    /**
     * Retourne la prochaine frame.
     */
    private AnimationFrame getNextFrame() {

        for (AnimationFrame frame : definition.getFrames()) {

            if (frame.getTick() > tick) {

                return frame;
            }
        }

        return null;
    }

    /**
     * Applique directement une frame.
     */
    private void applyFrame(
            ModelInstance model,
            AnimationFrame frame
    ) {

        Vector3f translation = frame.getTranslation();
        Vector3f scale = frame.getScale();
        Quaternionf rotation = frame.getRotation();

        for (ModelPart part : model.getParts().values()) {

            if (part == null) {
                continue;
            }

            ItemDisplay display = part.getDisplay();

            if (display == null || display.isDead()) {
                continue;
            }

            Transformation transformation =
                    new Transformation(
                            new Vector3f(translation),
                            new Quaternionf(),
                            new Vector3f(scale),
                            new Quaternionf(rotation)
                    );

            display.setTransformation(transformation);
        }
    }

    /**
     * Interpolation linéaire entre deux frames.
     */
    private void applyInterpolatedFrame(
            ModelInstance model,
            AnimationFrame current,
            AnimationFrame next
    ) {

        int currentTick = current.getTick();
        int nextTick = next.getTick();

        if (nextTick <= currentTick) {
            applyFrame(model, current);
            return;
        }

        double progress =
                (tick - currentTick)
                        / (double) (nextTick - currentTick);

        progress = Math.max(
                0.0D,
                Math.min(1.0D, progress)
        );

        Vector3f translation =
                interpolateVector(
                        current.getTranslation(),
                        next.getTranslation(),
                        progress
                );

        Vector3f scale =
                interpolateVector(
                        current.getScale(),
                        next.getScale(),
                        progress
                );

        Quaternionf rotation =
                interpolateRotation(
                        current.getRotation(),
                        next.getRotation(),
                        progress
                );

        for (ModelPart part : model.getParts().values()) {

            if (part == null) {
                continue;
            }

            ItemDisplay display = part.getDisplay();

            if (display == null || display.isDead()) {
                continue;
            }

            Transformation transformation =
                    new Transformation(
                            new Vector3f(translation),
                            new Quaternionf(),
                            new Vector3f(scale),
                            new Quaternionf(rotation)
                    );

            display.setTransformation(transformation);
        }
    }

    private Vector3f interpolateVector(
            Vector3f from,
            Vector3f to,
            double progress
    ) {

        return new Vector3f(
                (float) (
                        from.x
                                + (to.x - from.x)
                                * progress
                ),

                (float) (
                        from.y
                                + (to.y - from.y)
                                * progress
                ),

                (float) (
                        from.z
                                + (to.z - from.z)
                                * progress
                )
        );
    }

    private Quaternionf interpolateRotation(
            Quaternionf from,
            Quaternionf to,
            double progress
    ) {

        return new Quaternionf(from)
                .slerp(
                        new Quaternionf(to),
                        (float) progress
                );
    }

    public String getId() {

        return definition.getId();
    }

    public double getTick() {

        return tick;
    }

    public AnimationDefinition getDefinition() {

        return definition;
    }

    public void reset() {

        tick = 0.0D;
    }
}