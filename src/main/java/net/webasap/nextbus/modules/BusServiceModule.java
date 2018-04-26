package net.webasap.nextbus.modules;

import com.google.inject.AbstractModule;
import net.webasap.nextbus.services.TimeToNextBusService;
import net.webasap.nextbus.services.HttpClient;
import net.webasap.nextbus.services.MetroTransitService;
import net.webasap.nextbus.services.impl.MetroTransitServiceImpl;
import net.webasap.nextbus.services.impl.OkHttpClientImpl;

public class BusServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TimeToNextBusService.class);
        bind(HttpClient.class).to(OkHttpClientImpl.class);
        bind(MetroTransitService.class).to(MetroTransitServiceImpl.class);
    }
}
