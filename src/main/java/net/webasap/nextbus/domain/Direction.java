package net.webasap.nextbus.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import lombok.val;

public enum Direction {

    unknown, south, east, west, north;

    @JsonCreator
    public static Direction of(
            @JsonProperty("Text") String text,
            @JsonProperty("Value") String value) {

        // TODO This could throw an exception if value is not an integer
        val iValue = Integer.valueOf(value);

        switch(text.toLowerCase()) {
            // TODO Bare Strings here
            case "southbound":
                Preconditions.checkArgument(iValue == south.ordinal());
                return south;
            case "eastbound":
                Preconditions.checkArgument(iValue == east.ordinal());
                return east;
            case "westbound":
                Preconditions.checkArgument(iValue == west.ordinal());
                return west;
            case "northbound":
                Preconditions.checkArgument(iValue == north.ordinal());
                return north;
        }
        return unknown;
    }

    public static Direction of(String text) {
        try {
            return Direction.valueOf(text);
        } catch (IllegalArgumentException e) {
        }
        return unknown;
    }
}
