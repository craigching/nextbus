package net.webasap.nextbus.core.service;

import lombok.val;
import net.webasap.nextbus.core.BaseJsonTestSuite;
import net.webasap.nextbus.core.services.HttpClient;
import net.webasap.nextbus.core.services.HttpException;
import net.webasap.nextbus.core.services.TimeToNextBusService;
import net.webasap.nextbus.core.services.impl.MetroTransitServiceImpl;
import net.webasap.nextbus.core.utilities.RefTime;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestTimeToNextBusService extends BaseJsonTestSuite {

    @Test
    public void testHappyPath() throws HttpException {

        // The departure we are targeting is at 9:45 and we want the output to
        // be "5 minutes", so specify our ref time as 9:40.  See departures.json
        val refTimeStr = "2018-04-25T09:40:00-05:00";
        val refTime = ZonedDateTime.parse(refTimeStr);

        val routeText = "METRO Blue Line";
        val directionText = "south";
        val stopText = "Target Field Station Platform 1";

        val routeIdText = "901"; // METRO Blue Line
        val directionIdText = "1"; // SOUTHBOUND
        val stopIdText = "TF12"; // Target Field Station Platform 1

        val routesUrl = "http://svc.metrotransit.org/NexTrip/Routes";
        val directionsUrl = String.format("http://svc.metrotransit.org/NexTrip/Directions/%s", routeIdText);
        val stopsUrl = String.format("http://svc.metrotransit.org/NexTrip/Stops/%s/%s", routeIdText, directionIdText);
        val departuresUrl = String.format("http://svc.metrotransit.org/NexTrip/%s/%s/%s", routeIdText, directionIdText, stopIdText);

        val mockHttpClient = mock(HttpClient.class);
        when(mockHttpClient.get(routesUrl)).thenReturn(ROUTES_JSON);
        when(mockHttpClient.get(directionsUrl)).thenReturn(DIRECTIONS_JSON);
        when(mockHttpClient.get(stopsUrl)).thenReturn(METRO_BLUE_LINE_STOPS_JSON);
        when(mockHttpClient.get(departuresUrl)).thenReturn(DEPARTURES_JSON);

        val mockRefTime = mock(RefTime.class);
        when(mockRefTime.getRefTime()).thenReturn(refTime);

        val busService = new TimeToNextBusService(
                new MetroTransitServiceImpl(mockHttpClient),
                mockRefTime);

        val message = busService.getTimeToNextBus(routeText, stopText, directionText);

        assertEquals("5 minutes", message);
    }

    @Test
    public void testUnknownRoute() throws HttpException {

        val routesUrl = "http://svc.metrotransit.org/NexTrip/Routes";

        val mockHttpClient = mock(HttpClient.class);
        when(mockHttpClient.get(routesUrl)).thenReturn(ROUTES_JSON);

        val busService = new TimeToNextBusService(
                new MetroTransitServiceImpl(mockHttpClient),
                new RefTime());

        val message = busService.getTimeToNextBus("blah", "", "");

        assertEquals("No matches were found for the given route.", message);
    }

    @Test
    public void testMulitpleMatchingRoutes() throws HttpException {


        val routesUrl = "http://svc.metrotransit.org/NexTrip/Routes";

        val mockHttpClient = mock(HttpClient.class);
        when(mockHttpClient.get(routesUrl)).thenReturn(ROUTES_JSON);

        val busService = new TimeToNextBusService(
                new MetroTransitServiceImpl(mockHttpClient),
                new RefTime());

        val message = busService.getTimeToNextBus("METRO", "", "east");

        assertEquals("More than one match was found for the given route.", message);
    }

    @Test
    public void testBadDirection() throws HttpException {
        val routeIdText = "901"; // METRO Blue Line

        val routesUrl = "http://svc.metrotransit.org/NexTrip/Routes";
        val directionsUrl = String.format("http://svc.metrotransit.org/NexTrip/Directions/%s", routeIdText);

        val mockHttpClient = mock(HttpClient.class);
        when(mockHttpClient.get(routesUrl)).thenReturn(ROUTES_JSON);
        when(mockHttpClient.get(directionsUrl)).thenReturn(DIRECTIONS_JSON);

        val busService = new TimeToNextBusService(
                new MetroTransitServiceImpl(mockHttpClient),
                new RefTime());

        val message = busService.getTimeToNextBus("METRO Blue Line", "", "east");

        assertEquals("The provided direction is not valid for the given route.", message);
    }

    @Test
    public void testUnknownStop() throws HttpException {
        val routeIdText = "901"; // METRO Blue Line
        val directionIdText = "1"; // SOUTHBOUND

        val routesUrl = "http://svc.metrotransit.org/NexTrip/Routes";
        val directionsUrl = String.format("http://svc.metrotransit.org/NexTrip/Directions/%s", routeIdText);
        val stopsUrl = String.format("http://svc.metrotransit.org/NexTrip/Stops/%s/%s", routeIdText, directionIdText);

        val mockHttpClient = mock(HttpClient.class);
        when(mockHttpClient.get(routesUrl)).thenReturn(ROUTES_JSON);
        when(mockHttpClient.get(directionsUrl)).thenReturn(DIRECTIONS_JSON);
        when(mockHttpClient.get(stopsUrl)).thenReturn(METRO_BLUE_LINE_STOPS_JSON);

        val busService = new TimeToNextBusService(
                new MetroTransitServiceImpl(mockHttpClient),
                new RefTime());

        val message = busService.getTimeToNextBus("METRO Blue Line", "blah", "south");

        assertEquals("No stop was found for the given stop, route, and direction.", message);
    }

    @Test
    public void testMultipleStops() throws HttpException {
        val routeIdText = "901"; // METRO Blue Line
        val directionIdText = "1"; // SOUTHBOUND

        val routesUrl = "http://svc.metrotransit.org/NexTrip/Routes";
        val directionsUrl = String.format("http://svc.metrotransit.org/NexTrip/Directions/%s", routeIdText);
        val stopsUrl = String.format("http://svc.metrotransit.org/NexTrip/Stops/%s/%s", routeIdText, directionIdText);

        val mockHttpClient = mock(HttpClient.class);
        when(mockHttpClient.get(routesUrl)).thenReturn(ROUTES_JSON);
        when(mockHttpClient.get(directionsUrl)).thenReturn(DIRECTIONS_JSON);
        when(mockHttpClient.get(stopsUrl)).thenReturn(METRO_BLUE_LINE_STOPS_JSON);

        val busService = new TimeToNextBusService(
                new MetroTransitServiceImpl(mockHttpClient),
                new RefTime());

        val message = busService.getTimeToNextBus("METRO Blue Line", "Target Field Station", "south");

        assertEquals("More than one stop was found for the given stop, route, and direction.", message);
    }

    @Test
    public void test404FromHttpClient() throws HttpException {
        val routeIdText = "901"; // METRO Blue Line
        val directionIdText = "1"; // SOUTHBOUND
        val stopIdText = "TF12";

        val routesUrl = "http://svc.metrotransit.org/NexTrip/Routes";
        val directionsUrl = String.format("http://svc.metrotransit.org/NexTrip/Directions/%s", routeIdText);
        val stopsUrl = String.format("http://svc.metrotransit.org/NexTrip/Stops/%s/%s", routeIdText, directionIdText);
        val departuresUrl = String.format("http://svc.metrotransit.org/NexTrip/%s/%s/%s", routeIdText, directionIdText, stopIdText);

        val mockHttpClient = mock(HttpClient.class);
        when(mockHttpClient.get(routesUrl)).thenReturn(ROUTES_JSON);
        when(mockHttpClient.get(directionsUrl)).thenReturn(DIRECTIONS_JSON);
        when(mockHttpClient.get(stopsUrl)).thenReturn(METRO_BLUE_LINE_STOPS_JSON);
        when(mockHttpClient.get(departuresUrl)).thenThrow(new HttpException(404));

        val busService = new TimeToNextBusService(
                new MetroTransitServiceImpl(mockHttpClient),
                new RefTime());

        val message = busService.getTimeToNextBus("METRO Blue Line", "Target Field Station Platform 1", "south");
        assertEquals("There was an unexpected error returned from the service, status code: 404", message);
    }
}
