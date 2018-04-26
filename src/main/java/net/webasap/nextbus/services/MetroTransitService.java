package net.webasap.nextbus.services;

import net.webasap.nextbus.domain.Departure;
import net.webasap.nextbus.domain.Direction;
import net.webasap.nextbus.domain.Route;
import net.webasap.nextbus.domain.Stop;

import java.util.List;
import java.util.Optional;

public interface MetroTransitService {
    public Optional<List<Route>> getRoutes();

    public Optional<List<Direction>> getValidDirections(Route route);

    public Optional<List<Stop>> getStops(Route route, Direction direction);

    public Optional<List<Departure>> getDepartures(Route route, Direction direction, Stop stop);
}
