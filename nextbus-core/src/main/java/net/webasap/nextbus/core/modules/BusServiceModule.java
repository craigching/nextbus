package net.webasap.nextbus.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import net.webasap.nextbus.core.services.TimeToNextBusService;
import net.webasap.nextbus.core.services.HttpClient;
import net.webasap.nextbus.core.services.MetroTransitService;
import net.webasap.nextbus.core.services.impl.MetroTransitServiceImpl;
import net.webasap.nextbus.core.services.impl.OkHttpClientImpl;
import net.webasap.nextbus.core.utilities.RefTime;

/**
 * Guice module for the nextbus project
 */
public class BusServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RefTime.class);
        bind(TimeToNextBusService.class)
                .in(Singleton.class);
        bind(HttpClient.class)
                .to(OkHttpClientImpl.class)
                .in(Singleton.class);
        bind(MetroTransitService.class)
                .to(MetroTransitServiceImpl.class)
                .in(Singleton.class);
    }
}
