package net.webasap.nextbus.core.service.impl;

import lombok.val;
import net.webasap.nextbus.core.BaseJsonTestSuite;
import net.webasap.nextbus.core.domain.Direction;
import net.webasap.nextbus.core.domain.Route;
import net.webasap.nextbus.core.services.HttpClient;
import net.webasap.nextbus.core.services.impl.MetroTransitServiceImpl;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static net.webasap.nextbus.core.JsonLoader.loadJsonFile;
import static net.webasap.nextbus.core.domain.Direction.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestMetroTransitServiceImpl extends BaseJsonTestSuite {

    @Test
    public void testGetRoutes() {

        val colors = Arrays.asList("Blue", "Green", "Red");

        val client = mock(HttpClient.class);
        when(client.get("http://svc.metrotransit.org/NexTrip/Routes")).thenReturn(ROUTES_JSON);

        val transitService = new MetroTransitServiceImpl(client);
        val routes = transitService.getRoutes().get();

        colors.forEach(color -> {
            val descr = String.format("METRO %s Line", color);
            assertTrue(routes.stream()
                    .anyMatch(route -> route.getDescription().equals(descr)));
        });
    }

    @Test
    public void testGetDirections() {

        val routeName = "MyTestRoute";
        val url = String.format("http://svc.metrotransit.org/NexTrip/Directions/%s", routeName);

        val client = mock(HttpClient.class);
        when(client.get(url)).thenReturn(DIRECTIONS_JSON);

        val route = mock(Route.class);
        when(route.getRoute()).thenReturn(routeName);

        val transitService = new MetroTransitServiceImpl(client);
        val directions = transitService.getValidDirections(route).get();

        assertEquals(directions.size(), 2);
        assertTrue(directions.stream().anyMatch(direction -> direction == north));
        assertTrue(directions.stream().anyMatch(direction -> direction == south));
    }

    @Test
    public void testGetStops() {

        val wantedStops = Arrays.asList(
                "TGBF",
                "TGBD",
                "6625",
                "4SHE",
                "7SMA",
                "11MA"
        );

        val routeName = "MyTestRoute";
        val direction = north;
        val url = String.format("http://svc.metrotransit.org/NexTrip/Stops/%s/%s", routeName, direction.ordinal());

        val client = mock(HttpClient.class);
        when(client.get(url)).thenReturn(STOPS_JSON);

        val route = mock(Route.class);
        when(route.getRoute()).thenReturn(routeName);

        val mockDirection = mock(Direction.class);
        when(mockDirection.ordinal()).thenReturn(direction.ordinal());

        val transitService = new MetroTransitServiceImpl(client);
        val stops = transitService.getStops(route, direction).get();

        assertEquals(stops.size(), 6);

        wantedStops.forEach(wantedStop -> {
            assertTrue(stops.stream()
                    .anyMatch(st -> st.getValue().equals(wantedStop)));
        });

    }
}
