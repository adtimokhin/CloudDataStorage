package ru.adtimokhin.CloudDataStorage;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CloudApp {
    public static void main(String[] args) {
//        SeContainerInitializer.newInstance().addPackages(CloudApp.class).initialize()
////                .select(BootstrapServiceBean.class).get().init();
        System.out.println("ddd");
    }
}
