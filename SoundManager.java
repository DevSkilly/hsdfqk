package net.arcana.addons.resourcepack.sound;

import net.arcana.addons.ArcanaAddons;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SoundManager {

	private final ArcanaAddons plugin;

	private final Map<String, CustomSoundDefinition> sounds = new ConcurrentHashMap<>();

	public SoundManager(ArcanaAddons plugin) {
		this.plugin = plugin;
	}

	public void register(CustomSoundDefinition sound) {
		if (sound == null) {
			return;
		}

		sounds.put(sound.getId().toLowerCase(), sound);
	}

	public CustomSoundDefinition get(String id) {
		if (id == null) {
			return null;
		}

		return sounds.get(id.toLowerCase());
	}

	public void play(Player player, String id) {
		if (player == null) {
			return;
		}

		CustomSoundDefinition sound = get(id);

		if (sound == null) {
			plugin.getLogger().warning("Unknown custom sound: " + id);
			return;
		}

		player.playSound(player.getLocation(), "arcana:" + sound.getId(), SoundCategory.PLAYERS, sound.getVolume(),
				sound.getPitch());
	}

	public void play(Location location, String id) {
		if (location == null || location.getWorld() == null) {
			return;
		}

		CustomSoundDefinition sound = get(id);

		if (sound == null) {
			return;
		}

		location.getWorld().playSound(location, "arcana:" + sound.getId(), SoundCategory.PLAYERS, sound.getVolume(),
				sound.getPitch());
	}

	public void clear() {
		sounds.clear();
	}
}