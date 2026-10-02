package net.arcana.addons.util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TimeUtil {

    private static final Pattern PATTERN =
            Pattern.compile(
                    "^([0-9]+(?:\\.[0-9]+)?)\\s*(ms|s|m|h)$",
                    Pattern.CASE_INSENSITIVE
            );

    private TimeUtil() {
    }

    public static long parseMillis(
            String input
    ) {

        if (input == null) {
            return 0L;
        }

        Matcher matcher =
                PATTERN.matcher(
                        input.trim()
                                .toLowerCase(
                                        Locale.ROOT
                                )
                );

        if (!matcher.matches()) {

            throw new IllegalArgumentException(
                    "Invalid duration: "
                            + input
                            + ". Use ms, s, m or h."
            );
        }

        double value =
                Double.parseDouble(
                        matcher.group(1)
                );

        String unit =
                matcher.group(2);

        double multiplier;

        switch (unit) {

            case "ms":
                multiplier = 1D;
                break;

            case "s":
                multiplier = 1000D;
                break;

            case "m":
                multiplier = 60_000D;
                break;

            case "h":
                multiplier = 3_600_000D;
                break;

            default:
                throw new IllegalArgumentException(
                        "Unknown duration unit."
                );
        }

        double result =
                value * multiplier;

        if (result > Long.MAX_VALUE) {

            throw new IllegalArgumentException(
                    "Duration is too large."
            );
        }

        return (long) result;
    }
}