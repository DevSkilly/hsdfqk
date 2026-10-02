package net.arcana.addons.util;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class PDCUtil {

	private PDCUtil() {
	}

	public static void setString(PersistentDataContainer container, NamespacedKey key, String value) {

		container.set(key, PersistentDataType.STRING, value);
	}

	public static String getString(PersistentDataContainer container, NamespacedKey key) {

		return container.get(key, PersistentDataType.STRING);
	}

	public static void setInteger(PersistentDataContainer container, NamespacedKey key, int value) {

		container.set(key, PersistentDataType.INTEGER, value);
	}

	public static Integer getInteger(PersistentDataContainer container, NamespacedKey key) {

		return container.get(key, PersistentDataType.INTEGER);
	}
}