package net.webasap.nextbus.core.utilities;

import lombok.val;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.regex.Pattern;

public class TimeUtility {

    private static final String DEPARTURE_TIME_REGEX = "\\/Date\\((.*)(-.*)\\)\\/";
    private static final Pattern DEPARTURE_TIME_PATTERN = Pattern.compile(DEPARTURE_TIME_REGEX);

    public static ZonedDateTime convertDepartureTime(String value) {

        val matcher = DEPARTURE_TIME_PATTERN.matcher(value);
        val found = matcher.find();

        if (!found) {
            throw new IllegalArgumentException("The departure time value of " + value + " is not a valid departure time.");
        }

        val ts = matcher.group(1);
        val tz = matcher.group(2);
        val millis = Long.valueOf(ts).longValue();
        val inst = Instant.ofEpochMilli(millis);

        return ZonedDateTime.ofInstant(inst, ZoneId.of(tz));
    }

    public static String convertDuration(Duration duration) {
        long millis = duration.toMillis();
        val hours = millis / 3_600_000;
        millis -= hours * 3_600_000;
        val minutes = millis / 60_000;
        millis -= minutes * 60_000;
        val seconds = millis / 1000;

        val bldr = new StringBuilder();

        if (hours > 0) {
            bldr.append(hours).append(" hour");
            if (hours > 1) {
                bldr.append("s ");
            }
        } else {
            if (minutes > 0) {
                bldr.append(minutes).append(" minute");
                if (minutes > 1) {
                    bldr.append("s ");
                }
            } else {
                bldr.append(seconds).append(" second");
                if (seconds > 1) {
                    bldr.append("s");
                }
            }
        }

        return bldr.toString().trim();
    }
}
