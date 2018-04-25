package net.webasap.nextbus.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Route {

    @JsonProperty("Description")
    final String description;
    @JsonProperty("ProviderID")
    final String providerId;
    @JsonProperty("Route")
    final String route;

    @JsonCreator
    public Route(
            @JsonProperty("Description") String description,
            @JsonProperty("ProviderID") String providerId,
            @JsonProperty("Route") String route) {
        this.description = description;
        this.providerId = providerId;
        this.route = route;
    }
}
