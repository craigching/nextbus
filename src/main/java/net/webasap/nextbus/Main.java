package net.webasap.nextbus;

import com.google.inject.Guice;
import lombok.val;
import net.webasap.nextbus.domain.Route;
import net.webasap.nextbus.modules.BusServiceModule;
import net.webasap.nextbus.services.TimeToNextBusService;
import net.webasap.nextbus.utilities.BusServiceCommandParser;

import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {

        val injector = Guice.createInjector(new BusServiceModule());
        val nextBusService = injector.getInstance(TimeToNextBusService.class);
        val robustCommand = Stream.of(args).anyMatch(arg -> arg.startsWith("-"));

        if (args.length > 2 && !robustCommand) {
            // Support the commad line as specified, assuming:
            // arg[0] == route
            // arg[1] == stop
            // arg[2] == direction

            val route = args[0];
            val stop = args[1];
            val direction = args[2];

            // Show the wait duration until the next bus arrives at the stop
            System.out.println(nextBusService.getTimeToNextBus(route, stop, direction));

        } else if (robustCommand) {

            val parser = new BusServiceCommandParser();
            parser.parse(args);

            val route = parser.getRoute();
            val direction = parser.getDirection();
            val stop = parser.getStop();

            if (parser.isList()) {
                if (route != null && direction != null && stop != null) {
                    // List departures for route, direction and stop
                    System.out.println(nextBusService.listDepartures(route, direction, stop));
                } else if (route != null && direction != null) {
                    // List stops for route and direction
                    System.out.println(nextBusService.listStops(route, direction));
                } else if (route != null) {
                    // List directions for route
                    System.out.println(nextBusService.listDirections(route));
                } else {
                    // List routes
                    System.out.println(nextBusService.listRoutes());
                }
            } else {
                // Show the wait duration until the next bus arrives at the stop
                System.out.println(nextBusService.getTimeToNextBus(route, stop, direction));
            }
        } else {
            // Show usage
            String[] helpArgs = { "-help" };
            new BusServiceCommandParser().parse(helpArgs);
        }
    }
}
