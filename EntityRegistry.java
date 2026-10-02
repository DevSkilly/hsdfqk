package net.arcana.addons.entity;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class EntityRegistry {

	private final Map<String, EntityDefinition> definitions = new ConcurrentHashMap<>();

	public void register(EntityDefinition definition) {

		if (definition == null) {
			return;
		}

		definitions.put(definition.getId().toLowerCase(), definition);
	}

	public EntityDefinition get(String id) {

		if (id == null) {
			return null;
		}

		return definitions.get(id.toLowerCase());
	}

	public boolean contains(String id) {

		return id != null && definitions.containsKey(id.toLowerCase());
	}

	public Map<String, EntityDefinition> getDefinitions() {
		return Collections.unmodifiableMap(definitions);
	}

	public void clear() {
		definitions.clear();
	}
}