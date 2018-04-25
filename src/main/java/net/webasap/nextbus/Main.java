package net.webasap.nextbus;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import net.webasap.nextbus.domain.Direction;
import net.webasap.nextbus.domain.MetroTransitService;
import net.webasap.nextbus.domain.Route;
import net.webasap.nextbus.domain.Stop;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        // TODO Command line argument parsing
        // TODO Usage

        val route = args[0];
        val stop = args[1];
        val direction = args[2];

        List<Route> routes = MetroTransitService.instance().getRoutes().get();

        List<Route> matches = routes.stream().filter(route1 -> {
            // TODO Do we want to compare ignoring case?
            if (route1.getDescription().contains(route)) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        if (matches.size() == 1) {
            System.out.println("Exactly one match found");

            Route foundRoute = matches.get(0);
            List<Direction> validDirections = MetroTransitService.instance().getValidDirections(foundRoute).get();

            validDirections.forEach(System.out::println);

            if (validDirections.contains(Direction.of(direction))) {
                List<Stop> stops = MetroTransitService.instance().getStops(foundRoute, Direction.of(direction)).get();
                List<Stop> foundStops = stops.stream().filter(stop1 -> {
                    if (stop1.getText().contains(stop)) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());

                if (foundStops.size() == 1) {
                    System.out.println("Exactly one stop found.");
                    System.out.println(foundStops.get(0).getText());
                    System.out.println(foundStops.get(0).getValue());
                } else if (foundStops.size() > 1) {
                    System.out.println("More than one matching stop found.");
                } else {
                    System.out.println("No matching stops found.");
                }

            } else {
                System.out.println("Invalid direction, either the input direction isn't valid, or the input direction is not valid for the route given.");
            }



        } else if (matches.size() > 1) {
            System.out.println("More than one match found");
        } else {
            System.out.println("No matches for route found");
        }

//        MetroTransitService.instance()
//                .getRoutes()
//                .ifPresent(routes -> {
//                    routes.forEach(r -> System.out.println(r.getDescription()));
//                });
    }
}
