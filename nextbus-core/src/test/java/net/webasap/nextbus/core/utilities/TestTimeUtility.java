package net.webasap.nextbus.core.utilities;

import lombok.val;
import org.junit.Test;

import java.time.Duration;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

public class TestTimeUtility {

    @Test
    public void testConvertValidDepartureTime() {
        String dateStr = "/Date(1524678900000-0500)/";
        val zdt = TimeUtility.convertDepartureTime(dateStr);

        assertEquals(zdt.toString(), "2018-04-25T12:55-05:00");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDepartureTime() {
        TimeUtility.convertDepartureTime("blah");
    }

    @Test
    public void textConvertDuration() {

        val refTime = ZonedDateTime.parse("2018-04-25T23:00:00-05:00");

        String [][] dateStrs = {
                { "2018-04-26T01:00:00-05:00", "2 hours"},
                { "2018-04-26T00:00:00-05:00", "1 hour"},
                { "2018-04-25T23:30:00-05:00", "30 minutes"},
                { "2018-04-25T23:01:00-05:00", "1 minute"},
                { "2018-04-25T23:00:30-05:00", "30 seconds"},
                { "2018-04-25T23:00:01-05:00", "1 second"}
        };

        for (String[] elem : dateStrs) {
            val zdt1 = ZonedDateTime.parse(elem[0]);
            val dur = Duration.between(refTime, zdt1);
            val str = TimeUtility.convertDuration(dur);

            assertEquals(str, elem[1]);
        }
    }
}
