package ru.adtimokhin.CloudDataStorage.system;

import org.jetbrains.annotations.NotNull;

public interface SettingService {
    void init();

    // Jcr getters
    @NotNull
    Boolean getJcrActive();

    @NotNull
    String getJcrUrl();

    @NotNull
    String getJcrLogin();

    @NotNull
    String getJcrPassword();

    //Sync getters
    @NotNull
    Boolean getSyncActive();

    @NotNull
    Boolean getSyncSoap();

    @NotNull
    String getSyncEndpoint();

    @NotNull
    Integer getSyncTimeout();

    @NotNull
    String getSyncFolder();

}
