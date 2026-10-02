package net.arcana.addons.item;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.arcana.addons.ArcanaAddons;
import net.arcana.addons.ability.AbilityContext;

public final class ItemListener implements Listener {

	private final ArcanaAddons plugin;

	public ItemListener(ArcanaAddons plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent event) {

		ItemStack item = event.getItem();

		if (item == null || item.getType() == Material.AIR) {

			return;
		}

		ItemManager manager = plugin.getItemManager();

		String itemId = manager.getItemId(item);

		if (itemId == null) {
			return;
		}

		CustomItem customItem = manager.get(itemId);

		if (customItem == null) {
			return;
		}

		String trigger;

		Action action = event.getAction();

		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {

			trigger = event.getPlayer().isSneaking() ? "shift-right-click" : "right-click";
			
		} else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {

			trigger = event.getPlayer().isSneaking() ? "shift-left-click" : "left-click";

		} else {

			return;
		}

		AbilityContext context = new AbilityContext(event.getPlayer(), item, customItem, event, trigger);

		plugin.getAbilityManager().execute(customItem, trigger, context);
	}
}