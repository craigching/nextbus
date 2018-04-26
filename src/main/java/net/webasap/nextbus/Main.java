package net.webasap.nextbus;

import com.google.inject.Guice;
import lombok.val;
import net.webasap.nextbus.modules.BusServiceModule;
import net.webasap.nextbus.services.TimeToNextBusService;

public class Main {

    public static void main(String[] args) {
        // TODO Command line argument parsing
        // TODO Usage

//        val route = args[0];
//        val stop = args[1];
//        val direction = args[2];
//

        val injector = Guice.createInjector(new BusServiceModule());
        val nextBusService = injector.getInstance(TimeToNextBusService.class);

        nextBusService.getTimeToNextBus("METRO Blue Line", "Target Field Station Platform 1", "south");

    }
}
