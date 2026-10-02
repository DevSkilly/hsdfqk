package net.arcana.addons.ability;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.arcana.addons.ArcanaAddons;
import net.arcana.addons.item.CustomItem;

public final class AbilityManager {

	private final ArcanaAddons plugin;

	private final Map<String, Ability> abilities = new ConcurrentHashMap<>();

	private final CooldownManager cooldownManager = new CooldownManager();

	public AbilityManager(ArcanaAddons plugin) {
		this.plugin = plugin;
	}

	public void register(Ability ability) {

		if (ability == null) {
			throw new IllegalArgumentException("Ability cannot be null.");
		}

		String id = ability.getId();

		if (id == null || id.isBlank()) {
			throw new IllegalArgumentException("Ability ID cannot be empty.");
		}

		String normalized = id.toLowerCase();

		if (abilities.putIfAbsent(normalized, ability) != null) {

			throw new IllegalStateException("Ability already registered: " + id);
		}
	}

	public Ability get(String id) {

		if (id == null) {
			return null;
		}

		return abilities.get(id.toLowerCase());
	}

	public boolean exists(String id) {

		return id != null && abilities.containsKey(id.toLowerCase());
	}

	public boolean execute(CustomItem customItem, String trigger, AbilityContext context) {

		if (customItem == null || trigger == null || context == null) {

			return false;
		}

		String normalizedTrigger = trigger.toLowerCase();

		String abilityId = customItem.getDefinition().getAbilityId(normalizedTrigger);

		if (abilityId == null || abilityId.isBlank()) {

			return false;
		}

		Ability ability = get(abilityId);

		if (ability == null) {

			plugin.getLogger()
					.warning("Unknown ability '" + abilityId + "' used by item '" + customItem.getId() + "'.");

			return false;
		}

		if (cooldownManager.isOnCooldown(context.getPlayer().getUniqueId(), abilityId)) {

			long remaining = cooldownManager.getRemaining(context.getPlayer().getUniqueId(), abilityId);

			context.getPlayer().sendActionBar("§cCooldown : " + formatCooldown(remaining));

			return false;
		}

		/*
		 * On annule le clic uniquement si une ability existe réellement.
		 */
		if (context.getEvent() != null) {
			context.getEvent().setCancelled(true);
		}

		/*
		 * Exécution.
		 */
		ability.execute(context);

		/*
		 * Cooldown.
		 */
		long cooldown = customItem.getDefinition().getAbilityCooldown(normalizedTrigger);

		if (cooldown > 0) {

			cooldownManager.setCooldown(context.getPlayer().getUniqueId(), abilityId, cooldown);
		}

		return true;
	}

	private String formatCooldown(long milliseconds) {

		if (milliseconds < 1000) {
			return milliseconds + "ms";
		}

		return String.format("%.1fs", milliseconds / 1000.0);
	}

	public CooldownManager getCooldownManager() {
		return cooldownManager;
	}

	public int size() {
		return abilities.size();
	}

	public void unregister(String id) {

		if (id != null) {
			abilities.remove(id.toLowerCase());
		}
	}

	public void clear() {
		abilities.clear();
		cooldownManager.clear();
	}

	public void shutdown() {
		clear();
	}
}