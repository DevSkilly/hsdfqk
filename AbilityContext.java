package net.arcana.addons.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.arcana.addons.item.CustomItem;

public final class AbilityContext {

    private final Player player;
    private final ItemStack item;
    private final CustomItem customItem;
    private final PlayerInteractEvent event;
    private final String trigger;

    public AbilityContext(
            Player player,
            ItemStack item,
            CustomItem customItem,
            PlayerInteractEvent event,
            String trigger
    ) {

        this.player = player;
        this.item = item;
        this.customItem = customItem;
        this.event = event;
        this.trigger = trigger;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItem() {
        return item;
    }

    public CustomItem getCustomItem() {
        return customItem;
    }

    public PlayerInteractEvent getEvent() {
        return event;
    }

    public String getTrigger() {
        return trigger;
    }
}