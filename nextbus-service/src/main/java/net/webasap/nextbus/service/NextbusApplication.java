package net.webasap.nextbus.service;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.webasap.nextbus.core.modules.BusServiceModule;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class NextbusApplication extends Application<NextbusConfiguration> {

    public static void main(final String[] args) throws Exception {
        new NextbusApplication().run(args);
    }

    @Override
    public String getName() {
        return "Nextbus";
    }

    @Override
    public void initialize(final Bootstrap<NextbusConfiguration> bootstrap) {
        bootstrap.addBundle(GuiceBundle.builder()
                .enableAutoConfig(getClass().getPackage().getName())
                .modules(new BusServiceModule())
                .build());
    }

    @Override
    public void run(final NextbusConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
