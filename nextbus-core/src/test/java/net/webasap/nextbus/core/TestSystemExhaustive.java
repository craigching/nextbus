package net.webasap.nextbus;

import com.google.inject.Guice;
import lombok.val;
import net.webasap.nextbus.core.domain.Departure;
import net.webasap.nextbus.core.domain.Direction;
import net.webasap.nextbus.core.domain.Route;
import net.webasap.nextbus.core.domain.Stop;
import net.webasap.nextbus.core.modules.BusServiceModule;
import net.webasap.nextbus.core.services.MetroTransitService;
import net.webasap.nextbus.core.utilities.TimeUtility;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

public class TestSystemExhaustive {

    private static MetroTransitService metroTransitService;

    private List<Route> getAllRoutes() {
        val routesOpt = metroTransitService.getRoutes();
        if (routesOpt.isPresent()) {
            return routesOpt.get();
        }
        throw new RuntimeException("Failed to get all routes.");
    }

    private List<Direction> getDirectionsForRoute(Route route) {
        val dirOpt = metroTransitService.getValidDirections(route);
        if (dirOpt.isPresent()) {
            return dirOpt.get();
        }
        throw new RuntimeException("Failed to get Directions for route " + route.getDescription());
    }

    private List<Stop> getStops(Route route, Direction direction) {
        val stopsOpt = metroTransitService.getStops(route, direction);
        if (stopsOpt.isPresent()) {
            return stopsOpt.get();
        }
        throw new RuntimeException("Failed to get stops for route: " + route + ", and direction: " + direction);
    }

    private List<Departure> getDepartures(Route route, Direction direction, Stop stop) {
        val departuresOpt = metroTransitService.getDepartures(route, direction, stop);
        if (departuresOpt.isPresent()) {
            return departuresOpt.get();
        }
        throw new RuntimeException("Failed to get departures for route: " + route + ", direction: " + direction + ", and stop: " + stop);
    }

    private String getCsvHeader() {
        return "route,direction,stop,message";
    }
    private String getCsvRecord(Route route, Direction direction, Stop stop, Departure departure, ZonedDateTime now) {

        val departureTime = TimeUtility.convertDepartureTime(departure.getDepartureTime());
        val diff = Duration.between(now, departureTime);
        val message = TimeUtility.convertDuration(diff);
        val bldr = new StringBuilder();

        bldr.append("\"").append(route.getDescription()).append("\",");
        bldr.append(direction).append(",");
        bldr.append("\"").append(stop.getText()).append("\",");
        bldr.append(message);

        return bldr.toString();
    }

    @BeforeClass
    public static void setup() {
        // NOTE: You shouldn't use Guice in unit tests.  However, this
        // is intended to be a system level test to check for runtime
        // exceptions and other unforeseen issues
        val injector = Guice.createInjector(new BusServiceModule());
        metroTransitService = injector.getInstance(MetroTransitService.class);
    }

//    @Test
    public void testSystemExhaustiveTest() {

        val now = ZonedDateTime.now();

        try (val out = new PrintWriter("bustimes.csv")) {
            out.println(getCsvHeader());
            val routes = getAllRoutes();
            routes.stream().forEach(route -> {
                val directions = getDirectionsForRoute(route);
                directions.stream().forEach(direction -> {
                    val stops = getStops(route, direction);
                    stops.stream().forEach(stop -> {
                        try {
                            val departures = getDepartures(route, direction, stop);
                            departures.stream().forEach(departure -> {
                                out.println(getCsvRecord(route, direction, stop, departure, now));
                            });
                        } catch (Exception e) {
                            System.out.println("Failed for route: " + route + ", direction: " + direction + ", stop: " + stop);
                        }
                    });
                });
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
