package net.arcana.addons.effect;

import net.arcana.addons.ArcanaAddons;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public final class EffectManager {

	private final ArcanaAddons plugin;

	private final EffectRegistry registry;
	private final EffectLoader loader;

	public EffectManager(ArcanaAddons plugin) {

		this.plugin = plugin;

		this.registry = new EffectRegistry();

		this.loader = new EffectLoader(plugin);
	}

	public void load() {

		loader.load(registry);
	}

	public void shutdown() {
		registry.clear();
	}

	public EffectRegistry getRegistry() {
		return registry;
	}

	public void play(String id, Player player, Location location) {

		EffectDefinition definition = registry.get(id);

		if (definition == null) {

			plugin.getLogger().warning("Unknown effect: " + id);

			return;
		}

		play(definition, player, location);
	}

	public void playAll(List<String> ids, Player player, Location location) {

		if (ids == null || ids.isEmpty()) {
			return;
		}

		for (String id : ids) {

			play(id, player, location);
		}
	}

	private void play(EffectDefinition definition, Player player, Location location) {

		World world = location.getWorld();

		if (world == null) {
			return;
		}

		for (EffectDefinition.ParticleEffect particle : definition.getParticles()) {

			Particle type;

			try {

				type = Particle.valueOf(particle.type());

			} catch (IllegalArgumentException exception) {

				plugin.getLogger().warning("Unknown particle: " + particle.type());

				continue;
			}

			Location target = location.clone();

			if (particle.radius() > 0) {

				target.add(random(particle.radius()), random(particle.radius()), random(particle.radius()));
			}

			world.spawnParticle(type, target, particle.amount(), particle.offsetX(), particle.offsetY(),
					particle.offsetZ(), particle.speed());
		}

		EffectDefinition.SoundEffect sound = definition.getSound();

		if (sound != null) {

			try {

				Sound soundType = Sound.valueOf(sound.type());

				world.playSound(location, soundType, sound.volume(), sound.pitch());

			} catch (IllegalArgumentException exception) {

				plugin.getLogger().warning("Unknown sound: " + sound.type());
			}
		}
	}

	private double random(double radius) {

		return (Math.random() * 2.0 - 1.0) * radius;
	}
}