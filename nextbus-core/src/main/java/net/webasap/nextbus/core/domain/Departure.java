package net.webasap.nextbus.core.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

/**
 * A data class representing a departure for the Metro Transit API
 */
@Value
public class Departure {

    @JsonProperty("Actual")
    final private String actual;
    @JsonProperty("BlockNumber")
    final private String blockNumber;
    @JsonProperty("DepartureText")
    final private String departureText;
    @JsonProperty("DepartureTime")
    final private String departureTime;
    @JsonProperty("Description")
    final private String description;
    @JsonProperty("Gate")
    final private String gate;
    @JsonProperty("Route")
    final private String route;
    @JsonProperty("RouteDirection")
    final private String routeDirection;
    @JsonProperty("Terminal")
    final private String terminal;
    @JsonProperty("VehicleHeading")
    final private String vehicleHeading;
    @JsonProperty("VehicleLatitude")
    final private String vehicleLatitude;
    @JsonProperty("VehicleLongitude")
    final private String vehicleLongitude;

    @JsonCreator
    public Departure(
        @JsonProperty("Actual") String actual,
        @JsonProperty("BlockNumber") String blockNumber,
        @JsonProperty("DepartureText") String departureText,
        @JsonProperty("DepartureTime") String departureTime,
        @JsonProperty("Description") String description,
        @JsonProperty("Gate") String gate,
        @JsonProperty("Route") String route,
        @JsonProperty("RouteDirection") String routeDirection,
        @JsonProperty("Terminal") String terminal,
        @JsonProperty("VehicleHeading") String vehicleHeading,
        @JsonProperty("VehicleLatitude") String vehicleLatitude,
        @JsonProperty("VehicleLongitude") String vehicleLongitude
        ) {
        this.actual = actual;
        this.blockNumber = blockNumber;
        this.departureText = departureText;
        this.departureTime = departureTime;
        this.description = description;
        this.gate = gate;
        this.route = route;
        this.routeDirection = routeDirection;
        this.terminal = terminal;
        this.vehicleHeading = vehicleHeading;
        this.vehicleLatitude = vehicleLatitude;
        this.vehicleLongitude = vehicleLongitude;
    }
}
