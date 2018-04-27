package net.webasap.nextbus.core.services;

import com.google.common.collect.ImmutableList;
import lombok.val;
import net.webasap.nextbus.core.domain.Departure;
import net.webasap.nextbus.core.domain.Direction;
import net.webasap.nextbus.core.domain.Route;
import net.webasap.nextbus.core.domain.Stop;
import net.webasap.nextbus.core.utilities.TimeUtility;

import javax.inject.Inject;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {@code TimeToNextBusService} is a high-level class that provides the user
 * interface for the nextbus command-line interface.  The spirit of the
 * implementation is that the main success path is straight-forward and the
 * {@code BusServiceException} breaks out when there are errors and reports
 * the problem with a user-friendly message.
 *
 * Uses the low-level {@link MetroTransitService} API providing a more user-
 * friendly interface.
 */
public class TimeToNextBusService {

    final private MetroTransitService metroTransitService;

    @Inject
    public TimeToNextBusService(MetroTransitService metroTransitService) {
        this.metroTransitService = metroTransitService;
    }

    /**
     * Given an input text string, finds the Route that contains the text as a
     * substring.
     * @param text Text representing a substring of a Route's description
     * @return The best matching route if it exists
     * @throws BusServiceException Thrown if no Route or no best Route is found
     */
    public Route findRouteFromText(String text) throws BusServiceException {

        val routes = this.metroTransitService.getRoutes().get();

        val matches = routes.stream()
                .filter(route -> route.getDescription().contains(text))
                .collect(ImmutableList.toImmutableList());

        if (matches.size() == 0) {
            throw new BusServiceException("No matches were found for the given route.");
        } else if (matches.size() > 1) {
            throw new BusServiceException("More than one match was found for the given route.");
        }

        return matches.get(0);
    }

    /**
     * Given a Route and an input text string representing a direction,
     * ensures that direction exists for the route and returns a valid
     * {@link Direction} object.
     * @param route A Route object that represents the intended route
     * @param direction A String that represents a potentially valid direction for the route
     * @return The {@link Direction} object if valid
     * @throws BusServiceException Thrown if the given direction is not valid for the Route
     */
    public Direction validateDirection(Route route, String direction) throws BusServiceException {

        val validDirections = this.metroTransitService.getValidDirections(route).get();

        if (!validDirections.contains(Direction.of(direction))) {
            throw new BusServiceException("The provided direction is not valid for the given route.");
        }

        return Direction.of(direction);
    }

    /**
     * Given a Route, a Direction, and a String representation of a Stop, finds
     * the Stop that contains the text as a substring
     * @param route Route that represents the desired route
     * @param direction A valid direction on the Route
     * @param stopText Text representing a substring of a Stop's Text value
     * @return The best matching Stop if it exists
     * @throws BusServiceException Thrown if no Stop or no best Stop is found
     */
    public Stop findStop(Route route, Direction direction, String stopText) throws BusServiceException {

        val stops = this.metroTransitService.getStops(route, direction).get();

        val foundStops = stops.stream()
                .filter(stop -> stop.getText().contains(stopText))
                .collect(ImmutableList.toImmutableList());

        if (foundStops.size() == 0) {
            throw new BusServiceException("No stop was found for the given stop, route, and direction.");
        } else if (foundStops.size() > 1) {
            throw new BusServiceException("More than one stop was found for the given stop, route, and direction.");
        }

        return foundStops.get(0);
    }

    /**
     * Given a Route, a Direction, and a Stop, find the next Departure if it
     * exists
     * @param route Route that represents the desired route
     * @param direction Direction that represents the desired direction
     * @param stop Stop that represents the desired stop
     * @return The next Departure if one exists, none otherwise
     */
    public Optional<Departure> findNextDeparture(Route route, Direction direction, Stop stop) {
        val departures = this.metroTransitService.getDepartures(route, direction, stop).get();

        // Convert each departure time using TimeUtility to a ZonedDateTime,
        // collect those in a map, find the minimum key and return that
        // departure
        Map<ZonedDateTime, Departure> m = departures.stream()
                .collect(Collectors
                        .toMap( // key == ZonedDateTime
                                d -> TimeUtility.convertDepartureTime(d.getDepartureTime()),
                                // value == Optional<Departure>
                                d -> d));
        val minDate = m.keySet().stream()
                .min(Comparator.comparing(zdt -> zdt));

        if (minDate.isPresent()) {
            return Optional.of(m.get(minDate.get()));
        }

        return Optional.empty();
    }

    /**
     * Given String representations of a desired route, direction, and stop,
     * finds the wait time until the next bus arrives.
     * @param route The desired route
     * @param stop The desired stop
     * @param direction The desired direction
     * @return The wait time if it exists, a blank string if not
     */
    public String getTimeToNextBus(String route, String stop, String direction) {

        String message = "";

        try {
            val foundRoute = findRouteFromText(route);
            val foundDirection = validateDirection(foundRoute, direction);
            val foundStop = findStop(foundRoute, foundDirection, stop);
            val opt = findNextDeparture(foundRoute, foundDirection, foundStop);

            if (opt.isPresent()) {
                val departure = opt.get();
                val departTime = TimeUtility.convertDepartureTime(departure.getDepartureTime());
                val diff = Duration.between(ZonedDateTime.now(), departTime);

                message = TimeUtility.convertDuration(diff);
            }

        } catch (BusServiceException e) {
            message = e.getMessage();
        }

        return message;
    }

    /**
     * Returns a list of all possible routes
     * @return A newline delimited String of all route's by their description
     */
    public String listRoutes() {
        val routes = this.metroTransitService.getRoutes().get();
        val bldr = new StringBuilder();
        for (val route : routes) {
            bldr.append(route.getDescription()).append(System.getProperty("line.separator"));
        }
        return bldr.toString();
    }

    /**
     * Returns a list of all possible directions defined for the route
     * @param routeText A route as a substring of a Route's description
     * @return A newline delimited String of all directions for the route
     */
    public String listDirections(String routeText) {
        String message = "";
        try {
            val route = findRouteFromText(routeText);
            val directions = this.metroTransitService.getValidDirections(route).get();
            val bldr = new StringBuilder();
            for (val direction : directions) {
                bldr.append(direction).append(System.getProperty("line.separator"));
            }
            message = bldr.toString();
        } catch (BusServiceException e) {
            message = e.getMessage();
        }

        return message;
    }

    /**
     * Returns a list of all possible stops defined for the route and direction
     * @param routeText A route as a substring of a Route's description
     * @param directionText A text string direction, "south", "east", "west",
     *                     or "north"
     * @return A newline delimited String of all stops for the given route and
     * direction
     */
    public String listStops(String routeText, String directionText) {
        String message = "";
        try {
            val route = findRouteFromText(routeText);
            val direction = validateDirection(route, directionText);
            val stops = this.metroTransitService.getStops(route, direction).get();
            val bldr = new StringBuilder();
            for (val stop : stops) {
                bldr.append(stop.getText()).append(System.getProperty("line.separator"));
            }
            message = bldr.toString();
        } catch (BusServiceException e) {
            message = e.getMessage();
        }

        return message;

    }

    /**
     * Returns a list of all possible departures defined for the route,
     * direction, and stop
     * @param routeText A route as a substring of a Route's description
     * @param directionText A text string direction, "south", "east", "west",
     *                      or "north"
     * @param stopText A stop as a subscript of a Stop's Text value
     * @return A newline delimited String of all departures for the given
     * route, direction, and stop
     */
    public String listDepartures(String routeText, String directionText, String stopText) {
        String message = "";
        try {
            val route = findRouteFromText(routeText);
            val direction = validateDirection(route, directionText);
            val stop = findStop(route, direction, stopText);
            val departures = this.metroTransitService.getDepartures(route, direction, stop).get();
            val bldr = new StringBuilder();
            for (val departure : departures) {
                val departTime = TimeUtility.convertDepartureTime(departure.getDepartureTime());
                val diff = Duration.between(ZonedDateTime.now(), departTime);
                val t = TimeUtility.convertDuration(diff);
                bldr.append(t).append(System.getProperty("line.separator"));
            }
            message = bldr.toString();
        } catch (BusServiceException e) {
            message = e.getMessage();
        }

        return message;
    }

    private static class BusServiceException extends Exception {
        public BusServiceException(String message) {
            super(message);
        }
    }
}
