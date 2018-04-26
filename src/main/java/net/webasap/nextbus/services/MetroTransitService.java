package net.webasap.nextbus.services;

import com.google.common.collect.ImmutableList;
import net.webasap.nextbus.domain.Departure;
import net.webasap.nextbus.domain.Direction;
import net.webasap.nextbus.domain.Route;
import net.webasap.nextbus.domain.Stop;

import java.util.Optional;

public interface MetroTransitService {
    public Optional<ImmutableList<Route>> getRoutes();

    public Optional<ImmutableList<Direction>> getValidDirections(Route route);

    public Optional<ImmutableList<Stop>> getStops(Route route, Direction direction);

    public Optional<ImmutableList<Departure>> getDepartures(Route route, Direction direction, Stop stop);
}
