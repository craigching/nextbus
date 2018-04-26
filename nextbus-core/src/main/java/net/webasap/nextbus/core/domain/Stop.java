package net.webasap.nextbus.core.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Stop {

    @JsonProperty("Text")
    final String text;
    @JsonProperty("Value")
    final String value;

    @JsonCreator
    public Stop(
            @JsonProperty("Text") String text,
            @JsonProperty("Value") String value) {
        this.text = text;
        this.value = value;
    }
}
