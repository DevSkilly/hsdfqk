package net.arcana.addons.effect;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class EffectRegistry {

    private final Map<String, EffectDefinition> effects =
            new ConcurrentHashMap<>();

    public void register(EffectDefinition definition) {

        if (definition == null) {
            return;
        }

        effects.put(
                definition.getId(),
                definition
        );
    }

    public EffectDefinition get(String id) {

        if (id == null) {
            return null;
        }

        return effects.get(id.toLowerCase());
    }

    public boolean contains(String id) {

        return id != null
                && effects.containsKey(id.toLowerCase());
    }

    public Map<String, EffectDefinition> getEffects() {
        return Collections.unmodifiableMap(effects);
    }

    public void clear() {
        effects.clear();
    }
}