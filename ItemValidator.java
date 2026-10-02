package net.arcana.addons.item;

import java.util.regex.Pattern;

public final class ItemValidator {

    private static final Pattern ID_PATTERN =
            Pattern.compile(
                    "^[a-z0-9][a-z0-9_-]{0,63}$"
            );

    private ItemValidator() {
    }

    public static boolean isValidId(
            String id
    ) {

        if (id == null) {
            return false;
        }

        return ID_PATTERN.matcher(id).matches();
    }
}