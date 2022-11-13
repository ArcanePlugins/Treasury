package me.lokka30.treasury.api.common.service;

import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceRegistryTest {

    static class MyService {

        int i = -1;

        String get() {
            return i == 0 ? "foo" : "bar";
        }

    }

    @BeforeEach
    void before() {
        MyService service = new MyService();
        service.i++;
        ServiceRegistry.INSTANCE.registerService(MyService.class,
                service,
                service.get(),
                ServicePriority.NORMAL
        );
    }

    @Test
    void testOneProvider() {
        Set<Service<MyService>> services = ServiceRegistry.INSTANCE.allServicesFor(MyService.class);
        System.out.println(services);
        Assertions.assertEquals(1, services.size());
    }

    @Test
    void testTwoProviders() {
        Set<Service<MyService>> services = ServiceRegistry.INSTANCE.allServicesFor(MyService.class);
        System.out.println(services);
        Assertions.assertEquals(2, services.size());
    }

}
