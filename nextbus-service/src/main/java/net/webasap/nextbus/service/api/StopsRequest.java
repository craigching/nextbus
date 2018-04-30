package net.webasap.nextbus.service.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import net.webasap.nextbus.core.domain.Route;

@Value
public class StopsRequest {

    @JsonProperty
    final private Route route;
    @JsonProperty
    final private String direction;

    @JsonCreator
    public StopsRequest(
            @JsonProperty("route") Route route,
            @JsonProperty("direction") String direction
    ) {
        this.route = route;
        this.direction = direction;
    }

}
