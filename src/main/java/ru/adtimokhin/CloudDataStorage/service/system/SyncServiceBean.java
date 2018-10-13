package ru.adtimokhin.CloudDataStorage.service.system;

import ru.adtimokhin.CloudDataStorage.system.SyncService;

public class SyncServiceBean implements SyncService{
    @Override
    public boolean status() {
        return false;
    }

    @Override
    public void sync() {

    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public boolean stop() {
        return false;
    }
}
