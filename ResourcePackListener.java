package net.arcana.addons.resourcepack;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.arcana.addons.ArcanaAddons;

public final class ResourcePackListener implements Listener {

	private final ArcanaAddons plugin;

	public ResourcePackListener(ArcanaAddons plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {

		if (!plugin.getConfig().getBoolean("resource-pack.enabled", true)) {
			return;
		}

		if (!plugin.getConfig().getBoolean("resource-pack.send-on-join", true)) {
			return;
		}

		String url = plugin.getConfig().getString("resource-pack.url");

		if (url == null || url.isBlank()) {

			plugin.getLogger().warning("Resource Pack URL is not configured.");

			return;
		}

		String sha1 = plugin.getConfig().getString("resource-pack.sha1", "");

		if (sha1.isBlank()) {

			event.getPlayer().setResourcePack(url);

			return;
		}

		event.getPlayer().setResourcePack(url, hexToBytes(sha1));
	}

	private byte[] hexToBytes(String value) {

		int length = value.length();

		byte[] result = new byte[length / 2];

		for (int i = 0; i < length; i += 2) {

			result[i / 2] = (byte) (Character.digit(value.charAt(i), 16) << 4
					| Character.digit(value.charAt(i + 1), 16));
		}

		return result;
	}
}