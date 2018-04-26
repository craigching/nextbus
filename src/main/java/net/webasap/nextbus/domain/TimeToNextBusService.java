package net.webasap.nextbus.domain;

import lombok.val;
import net.webasap.nextbus.TimeUtility;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TimeToNextBusService {

    public Route findRouteFromText(String text) throws BusServiceException {

        val routes = MetroTransitService.instance().getRoutes().get();

        val matches = routes.stream()
                .filter(route -> route.getDescription().contains(text))
                .collect(Collectors.toList());

        if (matches.size() == 0) {
            throw new BusServiceException("No matches were found for the given route.");
        } else if (matches.size() > 1) {
            throw new BusServiceException("More than one match was found for the given route.");
        }

        return matches.get(0);
    }

    public Direction validateDirection(Route route, String direction) throws BusServiceException {

        val validDirections = MetroTransitService.instance().getValidDirections(route).get();

        if (!validDirections.contains(Direction.of(direction))) {
            throw new BusServiceException("The provided direction is not valid for the given route.");
        }

        return Direction.of(direction);
    }

    public Stop findStop(Route route, Direction direction, String stopText) throws BusServiceException {

        val stops = MetroTransitService.instance().getStops(route, direction).get();

        val foundStops = stops.stream()
                .filter(stop -> stop.getText().contains(stopText))
                .collect(Collectors.toList());

        if (foundStops.size() == 0) {
            throw new BusServiceException("No stop was found for the given stop, route, and direction.");
        } else if (foundStops.size() > 1) {
            throw new BusServiceException("More than one stop was found for the given stop, route, and direction.");
        }

        return foundStops.get(0);
    }

    public Optional<Departure> findNextDeparture(Route route, Direction direction, Stop stop) {
        val departures = MetroTransitService.instance().getDepartures(route, direction, stop).get();
        // TODO departures could be empty
        // TODO do we know the sort order on the list if there is one?

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

    public String getTimeToNextBus(String route, String stop, String direction) {
        try {
            val foundRoute = findRouteFromText(route);
            val foundDirection = validateDirection(foundRoute, direction);
            val foundStop = findStop(foundRoute, foundDirection, stop);
            val opt = findNextDeparture(foundRoute, foundDirection, foundStop);

            if (opt.isPresent()) {
                val departure = opt.get();
                val departTime = TimeUtility.convertDepartureTime(departure.getDepartureTime());
                val diff = Duration.between(departTime, ZonedDateTime.now());
                // TODO Pretty print diff
                // TODO Need to return the string, let the caller decide what to do with it
                System.out.println(diff);
            } else {
                // TODO Need to return the string, let the caller decide what to do with it
                System.out.println("");
            }

        } catch (BusServiceException e) {

        }

        return "";
    }

    private static class BusServiceException extends Exception {
        public BusServiceException(String message) {
            super(message);
        }
    }
}
