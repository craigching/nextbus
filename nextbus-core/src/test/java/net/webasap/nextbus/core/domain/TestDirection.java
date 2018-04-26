package net.webasap.nextbus.core.domain;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestDirection {

    @Test
    public void testDirectionOfText() {
        assertTrue(Direction.of("south") == Direction.south);
        assertTrue(Direction.of("east") == Direction.east);
        assertTrue(Direction.of("west") == Direction.west);
        assertTrue(Direction.of("north") == Direction.north);
        assertTrue(Direction.of("unknown") == Direction.unknown);
        assertTrue(Direction.of("blah") == Direction.unknown);
    }

    @Test
    public void testDirectionOfTextValue() {
        assertTrue(Direction.of("southbound", String.valueOf(Direction.south.ordinal())) == Direction.south);
        assertTrue(Direction.of("eastbound", String.valueOf(Direction.east.ordinal())) == Direction.east);
        assertTrue(Direction.of("westbound", String.valueOf(Direction.west.ordinal())) == Direction.west);
        assertTrue(Direction.of("northbound", String.valueOf(Direction.north.ordinal())) == Direction.north);
        assertTrue(Direction.of("blah", "1") == Direction.unknown);

        assertTrue(Direction.of("southbound".toUpperCase(), String.valueOf(Direction.south.ordinal())) == Direction.south);
        assertTrue(Direction.of("eastbound".toUpperCase(), String.valueOf(Direction.east.ordinal())) == Direction.east);
        assertTrue(Direction.of("westbound".toUpperCase(), String.valueOf(Direction.west.ordinal())) == Direction.west);
        assertTrue(Direction.of("northbound".toUpperCase(), String.valueOf(Direction.north.ordinal())) == Direction.north);
        assertTrue(Direction.of("blah".toUpperCase(), "1") == Direction.unknown);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDirectionOfTextValueIllegalArgumentSouth() {
        Direction.of("southbound", "blah");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDirectionOfTextValueIllegalArgumentEast() {
        Direction.of("eastbound", "blah");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDirectionOfTextValueIllegalArgumentWest() {
        Direction.of("westbound", "blah");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDirectionOfTextValueIllegalArgumentNorth() {
        Direction.of("northbound", "blah");
    }
}
