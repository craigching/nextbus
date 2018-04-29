package net.webasap.nextbus.core.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.webasap.nextbus.core.domain.Departure;
import net.webasap.nextbus.core.domain.Direction;
import net.webasap.nextbus.core.domain.Route;
import net.webasap.nextbus.core.domain.Stop;
import net.webasap.nextbus.core.services.HttpClient;
import net.webasap.nextbus.core.services.HttpException;
import net.webasap.nextbus.core.services.MetroTransitService;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;

/**
 * An implementation of the MetroTransitService API.
 */
public class MetroTransitServiceImpl implements MetroTransitService {

    final private HttpClient client;
    final private ObjectMapper mapper = new ObjectMapper()
            .registerModule(new GuavaModule());

    @Inject
    public MetroTransitServiceImpl(HttpClient client) {
        this.client = client;
    }

    @Override
    public Optional<ImmutableList<Route>> getRoutes() throws HttpException {
        return get(Urls.getRoutes(), new TypeReference<ImmutableList<Route>>() {});
    }

    @Override
    public Optional<ImmutableList<Direction>> getValidDirections(Route route) throws HttpException {

        Preconditions.checkNotNull(route);
        Preconditions.checkNotNull(route.getRoute());

        return get(Urls.getDirections(route), new TypeReference<ImmutableList<Direction>>() {});
    }

    @Override
    public Optional<ImmutableList<Stop>> getStops(Route route, Direction direction) throws HttpException {

        Preconditions.checkNotNull(route);
        Preconditions.checkNotNull(route.getRoute());
        Preconditions.checkNotNull(direction);

        return get(Urls.getStops(route, direction), new TypeReference<ImmutableList<Stop>>() {});
    }

    @Override
    public Optional<ImmutableList<Departure>> getDepartures(Route route, Direction direction, Stop stop) throws HttpException {

        Preconditions.checkNotNull(route);
        Preconditions.checkNotNull(direction);
        Preconditions.checkNotNull(stop);

        Preconditions.checkNotNull(route.getRoute());
        Preconditions.checkNotNull(stop.getValue());
        Preconditions.checkArgument(direction != Direction.unknown);

        return get(Urls.getDepartures(route, direction, stop), new TypeReference<ImmutableList<Departure>>() {});
    }
    private <T> Optional<T> get(String url, TypeReference<T> type) throws HttpException {
        try {
            String body = client.get(url);
            return Optional.of(mapper.readValue(body, type));
        } catch (IOException e) {
            throw new HttpException("There was a problem decoding the response from the service", e);
        }
    }

    private static class Urls {

        // TODO Consider a URL type mapping.  Given URLs have a static
        // return type, so coupling the URL with the return type makes
        // a lot of sense and would help decouple the JSON mapping
        // implementation from the MetroTransitServiceImpl

        final private static String ROUTES_URL = "http://svc.metrotransit.org/NexTrip/Routes";
        // ROUTE
        final private static String DIRECTIONS_URL_FORMAT = "http://svc.metrotransit.org/NexTrip/Directions/%s";
        // ROUTE, DIRECTION
        final private static String STOPS_URL_FORMAT = "http://svc.metrotransit.org/NexTrip/Stops/%s/%s";
        // ROUTE, DIRECTION, STOP
        final private static String TIMEPOINT_DEPARTURES_URL_FORMAT = "http://svc.metrotransit.org/NexTrip/%s/%s/%s";

        public static String getRoutes() {
            return ROUTES_URL;
        }

        public static String getDirections(Route route) {
            return String.format(DIRECTIONS_URL_FORMAT, route.getRoute());
        }

        public static String getStops(Route route, Direction direction) {
            return String.format(
                    STOPS_URL_FORMAT,
                    route.getRoute(),
                    direction.ordinal());
        }

        public static String getDepartures(Route route, Direction direction, Stop stop) {
            return String.format(
                    TIMEPOINT_DEPARTURES_URL_FORMAT,
                    route.getRoute(),
                    direction.ordinal(),
                    stop.getValue());
        }
    }
}
