package net.webasap.nextbus;

import com.sun.javaws.exceptions.InvalidArgumentException;
import lombok.val;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class TestTimeUtility {

    @Test
    public void testConvertValidDepartureTime() {
        String dateStr = "/Date(1524678900000-0500)/";
        val zdt = TimeUtility.convertDepartureTime(dateStr);

        assertTrue(zdt.toString().equals("2018-04-25T12:55-05:00"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDepartureTime() {
        TimeUtility.convertDepartureTime("blah");
    }
}
