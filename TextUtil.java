package net.arcana.addons.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class TextUtil {

	private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacyAmpersand();

	private TextUtil() {
	}

	public static Component color(String text) {

		if (text == null) {
			return Component.empty();
		}

		return SERIALIZER.deserialize(text);
	}
}