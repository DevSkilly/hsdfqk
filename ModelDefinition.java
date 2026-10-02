package net.arcana.addons.entity.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ModelDefinition {

    private final String id;

    private final Map<String, ModelPartDefinition> parts =
            new HashMap<>();

    public ModelDefinition(String id) {
        this.id = id.toLowerCase();
    }

    public String getId() {
        return id;
    }

    public void addPart(
            ModelPartDefinition part
    ) {
        if (part == null) {
            return;
        }

        parts.put(
                part.getId().toLowerCase(),
                part
        );
    }

    public ModelPartDefinition getPart(
            String id
    ) {

        if (id == null) {
            return null;
        }

        return parts.get(
                id.toLowerCase()
        );
    }

    public Map<String, ModelPartDefinition> getParts() {
        return Collections.unmodifiableMap(parts);
    }
}