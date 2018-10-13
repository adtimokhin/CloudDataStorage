package ru.adtimokhin.CloudDataStorage.service.system;

import lombok.SneakyThrows;
import ru.adtimokhin.CloudDataStorage.api.annotations.Loggable;
import ru.adtimokhin.CloudDataStorage.event.keyboard.KeyboardInitEvent;
import ru.adtimokhin.CloudDataStorage.service.local.FolderLocalServiceBean;
import ru.adtimokhin.CloudDataStorage.system.AppService;
import ru.adtimokhin.CloudDataStorage.system.SettingService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@ApplicationScoped
public class BootstrapServiceBean {
    @Inject
    private Event<KeyboardInitEvent> keyboardInitEventEvent;

    @Inject
    private AppService appService;
    @Inject
    private SettingService settingService;
    @Inject
    private FolderLocalServiceBean folderLocalServiceBean;


    @Loggable
    @SneakyThrows
    public void init() {
        settingService.init();
        appService.init();
        folderLocalServiceBean.init();
        keyboardInitEventEvent.fire(new KeyboardInitEvent());

    }
}
