package net.webasap.nextbus.core.services;

import com.google.common.collect.ImmutableList;
import net.webasap.nextbus.core.domain.Departure;
import net.webasap.nextbus.core.domain.Direction;
import net.webasap.nextbus.core.domain.Route;
import net.webasap.nextbus.core.domain.Stop;

import java.util.Optional;

/**
 * A Java API for interfacing to the Metro Transit REST NexTrip API at
 * http://svc.metrotransit.org
 */
public interface MetroTransitService {

    /** Returns all routes known to Metro Transit */
    public Optional<ImmutableList<Route>> getRoutes();

    /** For a given Route, return the valid directions for that route */
    public Optional<ImmutableList<Direction>> getValidDirections(Route route);

    /** For the given Route and Direction, return the stops on that route */
    public Optional<ImmutableList<Stop>> getStops(Route route, Direction direction);

    /** For the given Route, Direction, and Stop, return all departures */
    public Optional<ImmutableList<Departure>> getDepartures(Route route, Direction direction, Stop stop);
}
