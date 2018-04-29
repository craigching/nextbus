package net.webasap.nextbus.service.resources;

import net.webasap.nextbus.core.services.TimeToNextBusService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/nextbus/v1")
@Produces(MediaType.TEXT_PLAIN)
public class NextBusResource {

    final private TimeToNextBusService busService;

    @Inject
    public NextBusResource(TimeToNextBusService busService) {
        this.busService = busService;
    }

    @GET
    @Path("/nextbus/{route}/{stop}/{direction}")
    public String getNextBus(
            @PathParam("route") String route,
            @PathParam("stop") String stop,
            @PathParam("direction") String direction) {
        return this.busService.getTimeToNextBus(route, stop, direction);
    }
}
