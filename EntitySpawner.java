package net.arcana.addons.entity;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import net.arcana.addons.ArcanaAddons;
import net.arcana.addons.entity.visual.VisualItemBuilder;
import net.arcana.addons.entity.visual.VisualModel;

public final class EntitySpawner {

	private final ArcanaAddons plugin;

	public EntitySpawner(ArcanaAddons plugin) {
		this.plugin = plugin;
	}

	public CustomEntity spawn(String id, Location location) {

		EntityDefinition definition = plugin.getEntityManager().getDefinition(id);

		if (definition == null) {

			throw new IllegalArgumentException("Unknown custom entity: " + id);
		}

		EntityType type;

		try {

			type = EntityType.valueOf(definition.getBaseType().toUpperCase());

		} catch (IllegalArgumentException exception) {

			throw new IllegalArgumentException("Invalid EntityType: " + definition.getBaseType());
		}

		if (!type.isAlive()) {

			throw new IllegalArgumentException("EntityType is not living: " + type);
		}

		LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, type);

		entity.setCustomName(ChatColor.translateAlternateColorCodes('&', definition.getName()));

		entity.setCustomNameVisible(true);

		entity.setInvulnerable(definition.isInvulnerable());

		entity.setGlowing(definition.isGlowing());

		entity.setSilent(definition.isSilent());

		entity.setGravity(definition.hasGravity());

		if (entity.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {

			entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(definition.getHealth());

			entity.setHealth(definition.getHealth());
		}

		if (entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null) {

			entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(definition.getSpeed());
		}

		CustomEntity customEntity = new CustomEntity(entity, definition);

		plugin.getEntityManager().register(customEntity);

		VisualModel visualModel = definition.getVisualModel();

		if (visualModel != null) {

			ItemStack visualItem = VisualItemBuilder.create(Material.DIAMOND_SWORD, 1001);

			plugin.getVisualEntityManager().create(customEntity, visualModel, visualItem);
		}
		return customEntity;
	}
}