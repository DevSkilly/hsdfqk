package net.arcana.addons.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.arcana.addons.ArcanaAddons;
import net.arcana.addons.item.CustomItem;

public final class ItemCommand implements CommandExecutor, TabCompleter {

	private final ArcanaAddons plugin;

	public ItemCommand(ArcanaAddons plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!sender.hasPermission("arcanaitems.admin")) {

			sender.sendMessage(ChatColor.RED + "No permission.");

			return true;
		}

		if (args.length == 0) {

			sendHelp(sender);

			return true;
		}

		switch (args[0].toLowerCase()) {

		case "reload":
			reload(sender);
			break;

		case "list":
			list(sender);
			break;

		case "give":
			give(sender, args);
			break;

		case "pack", "rp", "resourcepack":

			handlePack(sender, args);

			break;

		default:
			sendHelp(sender);
			break;
		}

		return true;
	}

	private void reload(CommandSender sender) {

		plugin.getItemManager().loadItems();

		sender.sendMessage(ChatColor.GREEN + "ArcanaItems reloaded.");

		sender.sendMessage(ChatColor.GRAY + "Items: " + plugin.getItemManager().getRegistry().size());
	}

	private void list(CommandSender sender) {

		List<String> ids = new ArrayList<>(plugin.getItemManager().getRegistry().getItems().keySet());

		Collections.sort(ids);

		sender.sendMessage(ChatColor.GOLD + "ArcanaItems (" + ids.size() + "):");

		if (ids.isEmpty()) {

			sender.sendMessage(ChatColor.GRAY + "No items loaded.");

			return;
		}

		for (String id : ids) {

			sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.WHITE + id);
		}
	}

	private void give(CommandSender sender, String[] args) {

		if (args.length < 2) {

			sender.sendMessage(ChatColor.RED + "/arcanaitems give <item> [joueur] [quantité]");

			return;
		}

		String itemId = args[1].toLowerCase();

		CustomItem item = plugin.getItemManager().get(itemId);

		if (item == null) {

			sender.sendMessage(ChatColor.RED + "Unknown item: " + itemId);

			return;
		}

		Player target;

		if (args.length >= 3) {

			target = Bukkit.getPlayerExact(args[2]);

			if (target == null) {

				sender.sendMessage(ChatColor.RED + "Player not found.");

				return;
			}

		} else {

			if (!(sender instanceof Player player)) {

				sender.sendMessage(ChatColor.RED + "You must specify a player.");

				return;
			}

			target = player;
		}

		int amount = 1;

		if (args.length >= 4) {

			try {

				amount = Integer.parseInt(args[3]);

			} catch (NumberFormatException exception) {

				sender.sendMessage(ChatColor.RED + "Invalid amount.");

				return;
			}

			if (amount <= 0) {

				sender.sendMessage(ChatColor.RED + "Amount must be greater than 0.");

				return;
			}
		}

		ItemStack itemStack = item.create(amount);

		target.getInventory().addItem(itemStack);

		sender.sendMessage(ChatColor.GREEN + "Gave " + amount + "x " + itemId + " to " + target.getName() + ".");
	}

	private void sendHelp(CommandSender sender) {

		sender.sendMessage(ChatColor.GOLD + "----- ArcanaItems -----");

		sender.sendMessage(ChatColor.YELLOW + "/arcanaitems give <item> [joueur] [quantité]");

		sender.sendMessage(ChatColor.YELLOW + "/arcanaitems list");

		sender.sendMessage(ChatColor.YELLOW + "/arcanaitems reload");

		sender.sendMessage(ChatColor.YELLOW + "/arcanaitems pack build");
	}

	private void handlePack(CommandSender sender, String[] args) {

		if (args.length < 2) {

			sender.sendMessage(ChatColor.RED + "/ai pack <build>");

			return;
		}

		if (!args[1].equalsIgnoreCase("build")) {

			sender.sendMessage(ChatColor.RED + "Unknown pack command.");

			return;
		}

		sender.sendMessage(ChatColor.YELLOW + "Building Resource Pack...");

		try {

			File pack = plugin.getResourcePackGenerator().build();

			String sha1 = plugin.getResourcePackGenerator().generateSha1(pack);

			sender.sendMessage(ChatColor.GREEN + "Resource Pack generated.");

			sender.sendMessage(ChatColor.GRAY + "File: " + pack.getAbsolutePath());

			sender.sendMessage(ChatColor.GRAY + "SHA-1: " + sha1);

		} catch (Exception exception) {

			plugin.getLogger().severe("Unable to build Resource Pack.");

			exception.printStackTrace();

			sender.sendMessage(ChatColor.RED + "Resource Pack generation failed.");
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

		if (args.length == 1) {

			return List.of("give", "list", "reload", "rp");
		}

		if (args.length == 2 && args[0].equalsIgnoreCase("give")) {

			String input = args[1].toLowerCase();

			return plugin.getItemManager().getRegistry().getItems().keySet().stream().filter(id -> id.startsWith(input))
					.sorted().toList();
		}

		if (args.length == 3 && args[0].equalsIgnoreCase("give")) {

			String input = args[2].toLowerCase();

			return Bukkit.getOnlinePlayers().stream().map(Player::getName)
					.filter(name -> name.toLowerCase().startsWith(input)).sorted().toList();
		}

		return Collections.emptyList();
	}
}