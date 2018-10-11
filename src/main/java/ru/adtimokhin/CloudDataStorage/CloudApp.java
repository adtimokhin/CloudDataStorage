package ru.adtimokhin.CloudDataStorage;

import ru.adtimokhin.CloudDataStorage.service.system.BootstrapServiceBean;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.se.SeContainerInitializer;

@ApplicationScoped
public class CloudApp {
    public static void main(String[] args) {
        SeContainerInitializer.newInstance().addPackages(CloudApp.class).initialize()
             .select(BootstrapServiceBean.class).get().init();
    }
}
