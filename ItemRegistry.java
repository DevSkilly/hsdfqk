package net.arcana.addons.item;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ItemRegistry {

	private volatile Map<String, CustomItem> items = new ConcurrentHashMap<>();

	public CustomItem get(String id) {

		if (id == null) {
			return null;
		}

		return items.get(id);
	}

	public boolean contains(String id) {

		return id != null && items.containsKey(id);
	}

	public int size() {
		return items.size();
	}

	public Map<String, CustomItem> getItems() {

		return Collections.unmodifiableMap(new HashMap<>(items));
	}

	public synchronized void replaceAll(Map<String, CustomItem> newItems) {

		if (newItems == null) {
			throw new IllegalArgumentException("newItems cannot be null.");
		}

		Map<String, CustomItem> replacement = new ConcurrentHashMap<>(newItems);

		this.items = replacement;
	}

	public void clear() {
		items = new ConcurrentHashMap<>();
	}
}