package net.arcana.addons.resourcepack;

import org.bukkit.entity.Player;

import net.arcana.addons.ArcanaAddons;

import java.io.File;
import java.nio.file.Files;
import java.security.MessageDigest;

public final class ResourcePackManager {

	private final ArcanaAddons plugin;

	private final File resourcePackFile;

	private byte[] hash;

	public ResourcePackManager(ArcanaAddons plugin) {

		this.plugin = plugin;

		this.resourcePackFile = new File(plugin.getDataFolder(), "resourcepack/generated/ArcanaItems.zip");
	}

	public boolean exists() {
		return resourcePackFile.isFile();
	}

	public byte[] getHash() {

		if (!exists()) {
			return null;
		}

		if (hash == null) {
			hash = calculateHash();
		}

		return hash.clone();
	}

	private byte[] calculateHash() {

		try {

			byte[] data = Files.readAllBytes(resourcePackFile.toPath());

			MessageDigest digest = MessageDigest.getInstance("SHA-1");

			return digest.digest(data);

		} catch (Exception exception) {

			plugin.getLogger().severe("Unable to calculate resource pack hash.");

			return null;
		}
	}

	public void invalidateHash() {
		hash = null;
	}

	public void send(Player player, String url) {

		if (!exists()) {
			return;
		}

		byte[] currentHash = getHash();

		if (currentHash == null) {
			return;
		}

		player.setResourcePack(url, currentHash, "ArcanaItems ResourcePack", false);
	}
}