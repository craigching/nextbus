package net.webasap.nextbus.core.services;

import com.google.common.collect.ImmutableList;
import net.webasap.nextbus.core.domain.Departure;
import net.webasap.nextbus.core.domain.Direction;
import net.webasap.nextbus.core.domain.Route;
import net.webasap.nextbus.core.domain.Stop;

import java.util.Optional;

public interface MetroTransitService {
    public Optional<ImmutableList<Route>> getRoutes();

    public Optional<ImmutableList<Direction>> getValidDirections(Route route);

    public Optional<ImmutableList<Stop>> getStops(Route route, Direction direction);

    public Optional<ImmutableList<Departure>> getDepartures(Route route, Direction direction, Stop stop);
}
