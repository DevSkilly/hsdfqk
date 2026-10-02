package net.arcana.addons.item;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.arcana.addons.resourcepack.ResourcePackDefinition;

public final class ItemDefinition {

	private final String id;
	private final int version;
	private final ResourcePackDefinition resourcePack;
	
	private final String material;

	private final String displayName;
	private final List<String> lore;

	private final int modelData;

	private final double damage;
	private final double attackSpeed;

	private final boolean durabilityEnabled;
	private final int maxDurability;

	private final Map<String, String> abilities;
	private final Map<String, Long> abilityCooldowns;

	public ItemDefinition(String id, int version, String material, String displayName, List<String> lore, int modelData,
			double damage, double attackSpeed, boolean durabilityEnabled, int maxDurability,
			Map<String, String> abilities, Map<String, Long> abilityCooldowns, ResourcePackDefinition resourcePack) {

		this.id = id;
		this.version = version;
		this.material = material;
		this.displayName = displayName;
		this.lore = List.copyOf(lore);
		this.modelData = modelData;
		this.damage = damage;
		this.attackSpeed = attackSpeed;
		this.durabilityEnabled = durabilityEnabled;
		this.maxDurability = maxDurability;
		this.abilities = Map.copyOf(abilities);
		this.abilityCooldowns = Map.copyOf(abilityCooldowns);
		this.resourcePack = resourcePack;
	}

	public String getId() {
		return id;
	}

	public int getVersion() {
		return version;
	}

	public String getMaterial() {
		return material;
	}

	public String getDisplayName() {
		return displayName;
	}

	public List<String> getLore() {
		return Collections.unmodifiableList(lore);
	}

	public int getModelData() {
		return modelData;
	}

	public double getDamage() {
		return damage;
	}

	public double getAttackSpeed() {
		return attackSpeed;
	}
	
	public ResourcePackDefinition getResourcePack() {
		return resourcePack;
	}

	public boolean isDurabilityEnabled() {
		return durabilityEnabled;
	}

	public int getMaxDurability() {
		return maxDurability;
	}

	public String getAbilityId(String trigger) {

		if (trigger == null) {
			return null;
		}

		return abilities.get(trigger.toLowerCase());
	}

	public long getAbilityCooldown(String trigger) {

		if (trigger == null) {
			return 0L;
		}

		return abilityCooldowns.getOrDefault(trigger.toLowerCase(), 0L);
	}

	public Map<String, String> getAbilities() {
		return Collections.unmodifiableMap(abilities);
	}

	public Map<String, Long> getAbilityCooldowns() {
		return Collections.unmodifiableMap(abilityCooldowns);
	}
}