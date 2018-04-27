package net.webasap.nextbus.core.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

/**
 * A data class representing a route for the Metro Transit API
 */
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
