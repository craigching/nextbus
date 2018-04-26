package net.webasap.nextbus.core.utilities;

import joptsimple.OptionParser;
import lombok.Getter;
import lombok.val;

import java.io.IOException;
import java.util.Arrays;

@Getter
public class BusServiceCommandParser {

    private String route;
    private String direction;
    private String stop;
    private boolean isList;

    public void parse(String[] args) {
        val parser = new OptionParser();

        val routeString = parser.accepts("route", "A valid route")
                .withRequiredArg()
                .ofType(String.class);

        val stopString = parser.accepts("stop", "A valid stop on the given route")
                .withRequiredArg()
                .ofType(String.class);

        val directionString = parser.accepts("direction", "A valid direction on the given route, one of south, east, west, or north")
                .withRequiredArg()
                .ofType(String.class);

        parser.accepts("list", "List routes, directions, or stops depending on given arguments");

        parser.acceptsAll( Arrays.asList( "h", "?", "help" ), "show help" ).forHelp();

        val opt = parser.parse(args);

        if (opt.has("help") || opt.has("h") || opt.has("?")) {
            System.out.println("You may specify <route> <stop> <direction> or you may use the robust options below.");
            System.out.println();
            try {
                parser.printHelpOn( System.out );
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.route = routeString.value(opt);
        this.direction = directionString.value(opt);
        this.stop = stopString.value(opt);
        this.isList = opt.has("list");
    }
}
