package ru.adtimokhin.CloudDataStorage.service.system;

import ru.adtimokhin.CloudDataStorage.system.TimerService;

public class TimerServiceBean implements TimerService {
    @Override
    public boolean getActive() {
        return false;
    }

    @Override
    public void setActive(boolean active) {

    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public boolean stop() {
        return false;
    }

    @Override
    public void restart() {

    }
}
