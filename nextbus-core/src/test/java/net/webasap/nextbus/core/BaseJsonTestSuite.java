package net.webasap.nextbus.core;

import org.junit.BeforeClass;

import java.io.IOException;

import static net.webasap.nextbus.core.JsonLoader.loadJsonFile;

public class BaseJsonTestSuite {

    protected static String ROUTES_JSON;
    protected static String DIRECTIONS_JSON;
    protected static String STOPS_JSON;
    protected static String METRO_BLUE_LINE_STOPS_JSON;
    protected static String DEPARTURES_JSON;

    @BeforeClass
    public static void setup() throws IOException {
        ROUTES_JSON = loadJsonFile("routes.json");
        DIRECTIONS_JSON = loadJsonFile("directions.json");
        STOPS_JSON = loadJsonFile("stops.json");
        METRO_BLUE_LINE_STOPS_JSON = loadJsonFile("metro_blue_line_stops.json");
        DEPARTURES_JSON = loadJsonFile("departures.json");
    }
}
