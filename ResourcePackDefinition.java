package net.arcana.addons.resourcepack;

public final class ResourcePackDefinition {

    private final String model;
    private final String texture;

    public ResourcePackDefinition(
            String model,
            String texture
    ) {
        this.model = model;
        this.texture = texture;
    }

    public String getModel() {
        return model;
    }

    public String getTexture() {
        return texture;
    }

}