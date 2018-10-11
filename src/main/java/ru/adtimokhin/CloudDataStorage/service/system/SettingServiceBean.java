package ru.adtimokhin.CloudDataStorage.service.system;

import org.jetbrains.annotations.NotNull;
import ru.adtimokhin.CloudDataStorage.system.SettingService;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;
import java.util.Properties;
@ApplicationScoped
public class SettingServiceBean implements SettingService {
    InputStream reader;
    Properties properties;
    @Override
    public void init() {
        try {
            reader = new FileInputStream(new File("../Cloud/src/main/resources/resources.properties"));
            properties = new Properties();
            properties.load(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public @NotNull Boolean getJcrActive() {
        return Boolean.valueOf(properties.getProperty("jcr.active"));
    }

    @Override
    public @NotNull String getJcrUrl() {
        return properties.getProperty("jcr.url");
    }

    @Override
    public @NotNull String getJcrLogin() {
        return properties.getProperty("jcr.login");
    }

    @Override
    public @NotNull String getJcrPassword() {
        return properties.getProperty("jcr.password");
    }

    @Override
    public @NotNull Boolean getSyncActive() {
        return Boolean.valueOf(properties.getProperty("sync.active"));
    }

    @Override
    public @NotNull Boolean getSyncSoap() {
        return Boolean.valueOf(properties.getProperty("sync.soap"));
    }

    @Override
    public @NotNull String getSyncEndpoint() {
        return properties.getProperty("sync.endpoint");
    }

    @Override
    public @NotNull Integer getSyncTimeout() {
        return Integer.valueOf(properties.getProperty("sync.timeout"));
    }

    @Override
    public @NotNull String getSyncFolder() {
        return properties.getProperty("sync.folder");
    }
}
