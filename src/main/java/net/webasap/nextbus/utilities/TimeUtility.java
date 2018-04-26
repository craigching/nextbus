package net.webasap.nextbus.utilities;

import com.google.common.base.Preconditions;
import lombok.val;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.regex.Matcher;
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
}
