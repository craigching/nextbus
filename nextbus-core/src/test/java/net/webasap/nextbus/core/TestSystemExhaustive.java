package net.webasap.nextbus.core;

import com.google.inject.Guice;
import lombok.val;
import net.webasap.nextbus.core.domain.Departure;
import net.webasap.nextbus.core.domain.Direction;
import net.webasap.nextbus.core.domain.Route;
import net.webasap.nextbus.core.domain.Stop;
import net.webasap.nextbus.core.modules.BusServiceModule;
import net.webasap.nextbus.core.services.HttpException;
import net.webasap.nextbus.core.services.MetroTransitService;
import net.webasap.nextbus.core.services.TimeToNextBusService;
import net.webasap.nextbus.core.services.impl.MetroTransitServiceImpl;
import net.webasap.nextbus.core.services.impl.OkHttpClientImpl;
import net.webasap.nextbus.core.utilities.RefTime;
import net.webasap.nextbus.core.utilities.TimeUtility;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestSystemExhaustive {

    private static MetroTransitService metroTransitService;

    private List<Route> getAllRoutes() throws HttpException {
        val routesOpt = metroTransitService.getRoutes();
        if (routesOpt.isPresent()) {
            return routesOpt.get();
        }
        throw new RuntimeException("Failed to get all routes.");
    }

    private List<Direction> getDirectionsForRoute(Route route) throws HttpException {
        val dirOpt = metroTransitService.getValidDirections(route);
        if (dirOpt.isPresent()) {
            return dirOpt.get();
        }
        throw new RuntimeException("Failed to get Directions for route " + route.getDescription());
    }

    private List<Stop> getStops(Route route, Direction direction) throws HttpException {
        val stopsOpt = metroTransitService.getStops(route, direction);
        if (stopsOpt.isPresent()) {
            return stopsOpt.get();
        }
        throw new RuntimeException("Failed to get stops for route: " + route + ", and direction: " + direction);
    }

    private List<Departure> getDepartures(Route route, Direction direction, Stop stop) throws HttpException {
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
    public void testSystemExhaustiveTest() throws HttpException {

        val now = ZonedDateTime.now();

        try (val out = new PrintWriter("bustimes.csv")) {
            out.println(getCsvHeader());
            val routes = getAllRoutes();
            routes.stream().forEach(route -> {
                try {
                    val directions = getDirectionsForRoute(route);
                    directions.stream().forEach(direction -> {
                        try {
                            val stops = getStops(route, direction);
                            stops.stream().forEach(stop -> {
                                try {
                                    val departures = getDepartures(route, direction, stop);
                                    departures.stream().forEach(departure -> {
                                        out.println(getCsvRecord(route, direction, stop, departure, now));
                                    });
                                } catch (Exception e) {
                                    System.out.println("Failed for route: " + route + ", direction: " + direction + ", stop: " + stop);
                                    e.printStackTrace(System.out);
                                }
                            });
                        } catch (HttpException e) {
                            System.out.println("HttpException: " + e.getMessage());
                        }
                    });
                } catch (HttpException e) {
                    System.out.println("HttpException: " + e.getMessage());
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

//    @Test
    public void testBadRoute() {

        // I don't know that this combination always fails, but it does
        // seem to happen often.

        val badRoute = "Jackson St - Robert St - 5th Av - Inver Hills";
        val badDirection = "north";
        val badStop = "5th Ave and South St";

        val busService = new TimeToNextBusService(new MetroTransitServiceImpl(new OkHttpClientImpl()), new RefTime());
        val message = busService.getTimeToNextBus(badRoute,badStop, badDirection);
        assertEquals("There was an unexpected error returned from the service, status code: 404", message);
    }
}
