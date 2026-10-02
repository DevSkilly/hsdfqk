package net.arcana.addons.resourcepack.sound;

public final class CustomSoundDefinition {

	private final String id;
	private final String file;
	private final float volume;
	private final float pitch;

	public CustomSoundDefinition(String id, String file, float volume, float pitch) {
		this.id = id;
		this.file = file;
		this.volume = volume;
		this.pitch = pitch;
	}

	public String getId() {
		return id;
	}

	public String getFile() {
		return file;
	}

	public float getVolume() {
		return volume;
	}

	public float getPitch() {
		return pitch;
	}
}