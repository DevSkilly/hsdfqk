package net.arcana.addons.entity;

import net.arcana.addons.ArcanaAddons;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CustomEntityManager {

	private final ArcanaAddons plugin;
	private final EntityLoader loader;

	private final Map<UUID, CustomEntity> spawned = new ConcurrentHashMap<>();

	private volatile Map<String, EntityDefinition> definitions = new ConcurrentHashMap<>();

	public CustomEntityManager(ArcanaAddons plugin) {
		this.plugin = plugin;
		this.loader = new EntityLoader(plugin);
	}

	public void load() {

		Map<String, EntityDefinition> loaded = loader.load();

		if (loaded == null) {
			loaded = new ConcurrentHashMap<>();
		}

		definitions = new ConcurrentHashMap<>(loaded);

		plugin.getLogger().info("Loaded " + definitions.size() + " custom entity definitions.");
	}

	public EntityDefinition getDefinition(String id) {

		if (id == null || id.isBlank()) {
			return null;
		}

		return definitions.get(id.toLowerCase());
	}

	public boolean hasDefinition(String id) {

		return id != null && definitions.containsKey(id.toLowerCase());
	}

	public CustomEntity get(UUID uuid) {

		if (uuid == null) {
			return null;
		}

		return spawned.get(uuid);
	}

	public CustomEntity get(LivingEntity entity) {

		if (entity == null) {
			return null;
		}

		return get(entity.getUniqueId());
	}

	public void register(CustomEntity customEntity) {

		if (customEntity == null || customEntity.getEntity() == null) {
			return;
		}

		spawned.put(customEntity.getEntity().getUniqueId(), customEntity);
	}

	public boolean isCustomEntity(LivingEntity entity) {

		return entity != null && spawned.containsKey(entity.getUniqueId());
	}

	public String getId(LivingEntity entity) {

		CustomEntity customEntity = get(entity);

		if (customEntity == null || customEntity.getDefinition() == null) {
			return null;
		}

		return customEntity.getDefinition().getId();
	}

	public Collection<CustomEntity> getSpawnedEntities() {

		return Collections.unmodifiableCollection(spawned.values());
	}

	public Collection<CustomEntity> getAll() {

		return getSpawnedEntities();
	}

	public Map<String, EntityDefinition> getDefinitions() {

		return Collections.unmodifiableMap(definitions);
	}

	public void remove(UUID uuid) {

	    if (uuid == null) {
	        return;
	    }

	    spawned.remove(uuid);
	}

	public void remove(Entity entity) {

	    if (entity == null) {
	        return;
	    }

	    spawned.remove(entity.getUniqueId());
	}

	public void remove(LivingEntity entity) {

	    if (entity == null) {
	        return;
	    }

	    spawned.remove(entity.getUniqueId());
	}

	public void remove(CustomEntity customEntity) {

	    if (customEntity == null) {
	        return;
	    }

	    Entity entity = customEntity.getEntity();

	    if (entity != null) {
	        remove(entity);
	    }
	}
	
	public int size() {
		return spawned.size();
	}

	public void clear() {
		spawned.clear();
	}

	public void shutdown() {

		spawned.clear();
		definitions = new ConcurrentHashMap<>();
	}

}