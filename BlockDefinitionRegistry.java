package net.arcana.addons.block;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BlockDefinitionRegistry {

	private final Map<String, BlockDefinition> definitions = new ConcurrentHashMap<>();

	public void register(BlockDefinition definition) {

		if (definition == null) {
			return;
		}

		definitions.put(definition.getId(), definition);
	}

	public BlockDefinition get(String id) {

		if (id == null) {
			return null;
		}

		return definitions.get(id);
	}

	public boolean contains(String id) {

		return id != null && definitions.containsKey(id);
	}

	public Map<String, BlockDefinition> getDefinitions() {
		return Collections.unmodifiableMap(definitions);
	}

	public void clear() {
		definitions.clear();
	}
}