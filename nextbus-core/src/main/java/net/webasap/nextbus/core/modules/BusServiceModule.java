package net.webasap.nextbus.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import net.webasap.nextbus.core.services.TimeToNextBusService;
import net.webasap.nextbus.core.services.HttpClient;
import net.webasap.nextbus.core.services.MetroTransitService;
import net.webasap.nextbus.core.services.impl.MetroTransitServiceImpl;
import net.webasap.nextbus.core.services.impl.OkHttpClientImpl;

public class BusServiceModule extends AbstractModule {
    @Override
    protected void configure() {
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
