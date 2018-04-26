package net.webasap.nextbus.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2;
import net.webasap.nextbus.HttpClient;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MetroTransitService {

    final private HttpClient client = new HttpClient();
    final private ObjectMapper mapper = new ObjectMapper();

    private static class Singleton {
        public static MetroTransitService INSTANCE = new MetroTransitService();
    }

    public static MetroTransitService instance() {
        return Singleton.INSTANCE;
    }

    public Optional<List<Route>> getRoutes() {

        try {
            return get(Urls.getRoutes(), new TypeReference<List<Route>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<List<Direction>> getValidDirections(Route route) {

        Preconditions.checkNotNull(route);
        Preconditions.checkNotNull(route.getRoute());

        try {
            return get(Urls.getDirections(route), new TypeReference<List<Direction>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<List<Stop>> getStops(Route route, Direction direction) {

        Preconditions.checkNotNull(route);
        Preconditions.checkNotNull(route.getRoute());
        Preconditions.checkNotNull(direction);

        try {
            return get(Urls.getStops(route, direction), new TypeReference<List<Stop>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<List<Departure>> getDepartures(Route route, Direction direction, Stop stop) {

        Preconditions.checkNotNull(route);
        Preconditions.checkNotNull(direction);
        Preconditions.checkNotNull(stop);

        Preconditions.checkNotNull(route.getRoute());
        Preconditions.checkNotNull(stop.getValue());
        Preconditions.checkArgument(direction != Direction.unknown);

        try {
            return get(Urls.getDepartures(route, direction, stop), new TypeReference<List<Departure>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
    private <T> Optional<T> get(String url, TypeReference<T> type) throws IOException {
        String body = client.get(url);
        return Optional.of(mapper.readValue(body, type));
    }

    private static class Urls {

        // TODO Consider a URL type mapping.  Given URLs have a static
        // return type, so coupling the URL with the return type makes
        // a lot of sense and would help decouple the JSON mapping
        // implementation from the MetroTransitService

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
