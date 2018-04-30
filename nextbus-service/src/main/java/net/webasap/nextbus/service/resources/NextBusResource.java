package net.webasap.nextbus.service.resources;

import lombok.val;
import net.webasap.nextbus.core.domain.Direction;
import net.webasap.nextbus.core.domain.Route;
import net.webasap.nextbus.core.domain.Stop;
import net.webasap.nextbus.core.services.HttpException;
import net.webasap.nextbus.core.services.MetroTransitService;
import net.webasap.nextbus.core.services.TimeToNextBusService;
import net.webasap.nextbus.service.api.StopsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/nextbus/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NextBusResource {

    final static private Logger LOG = LoggerFactory.getLogger(NextBusResource.class);

    final private TimeToNextBusService busService;
    final private MetroTransitService transitService;

    @Inject
    public NextBusResource(TimeToNextBusService busService, MetroTransitService transitService) {
        this.busService = busService;
        this.transitService = transitService;
    }

    @GET
    @Path("/nextbus/{route}/{stop}/{direction}")
    public Response getNextBus(
            @PathParam("route") String route,
            @PathParam("stop") String stop,
            @PathParam("direction") String direction) {

        LOG.info("Route: {}, Stop: {}, Direction: {}", route, stop, direction);

        val str = this.busService.getTimeToNextBus(route, stop, direction);
        return Response
                .ok()
                .entity(String.format("{ \"status\": 200, \"message\": \"%s\"}", str))
                .build();
    }

    @GET
    @Path("/list")
    public Response listRoutes() {
        try {
            return Response
                    .ok()
                    .entity(this.transitService.getRoutes().get())
                    .build();
        } catch (HttpException e) {
            return null;
        }
    }

    @PUT
    @Path("/list/directions")
    public Response listDirections(Route route) {
        try {
            return Response
                    .ok()
                    .entity(this.transitService.getValidDirections(route).get())
                    .build();
        } catch (HttpException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PUT
    @Path("/list/stops")
    public List<Stop> listStops(StopsRequest request) {
        try {
            Route route = request.getRoute();
            Direction direction = Direction.of(request.getDirection());
            return this.transitService.getStops(route, direction).get();
        } catch (HttpException e) {
            e.printStackTrace();
            return null;
        }
    }
}
