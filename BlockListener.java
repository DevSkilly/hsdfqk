package net.arcana.addons.block;

import net.arcana.addons.ArcanaAddons;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public final class BlockListener implements Listener {

	private final ArcanaAddons plugin;

	public BlockListener(ArcanaAddons plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlace(BlockPlaceEvent event) {

		ItemStack item = event.getItemInHand();

		String itemId = plugin.getItemManager().getItemId(item);

		if (itemId == null) {
			return;
		}

		BlockDefinition definition = plugin.getBlockManager().getDefinition(itemId);

		if (definition == null) {
			return;
		}

		plugin.getBlockManager().place(definition, event.getBlock().getLocation());

		Player player = event.getPlayer();

		// Les effets seront branchés ici.
		plugin.getEffectManager().playAll(definition.getPlacedEffects(), player, event.getBlock().getLocation());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBreak(BlockBreakEvent event) {

		Block block = event.getBlock();

		CustomBlock customBlock = plugin.getBlockManager().getRegistry().get(block.getLocation());

		if (customBlock == null) {
			return;
		}

		event.setDropItems(false);

		BlockDefinition definition = plugin.getBlockManager().getDefinition(customBlock.getId());

		if (definition == null) {
			return;
		}

		if (definition.isDropEnabled()) {

			ItemStack drop = plugin.getBlockManager().createItem(definition.getDropItem(), definition.getDropAmount());

			if (drop != null) {

				block.getWorld().dropItemNaturally(block.getLocation(), drop);
			}
		}

		plugin.getEffectManager().playAll(definition.getBrokenEffects(), event.getPlayer(), block.getLocation());

		plugin.getBlockManager().remove(block.getLocation());
	}
}