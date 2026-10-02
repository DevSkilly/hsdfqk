package net.arcana.addons.entity.visual;

import net.arcana.addons.entity.CustomEntity;
import org.bukkit.entity.ItemDisplay;

public final class VisualEntity {

    private final CustomEntity customEntity;
    private final ItemDisplay display;
    private final VisualModel model;

    public VisualEntity(
            CustomEntity customEntity,
            ItemDisplay display,
            VisualModel model
    ) {
        this.customEntity = customEntity;
        this.display = display;
        this.model = model;
    }

    public CustomEntity getCustomEntity() {
        return customEntity;
    }

    public ItemDisplay getDisplay() {
        return display;
    }

    public VisualModel getModel() {
        return model;
    }

    public boolean isValid() {
        return display != null
                && display.isValid()
                && !display.isDead();
    }
}