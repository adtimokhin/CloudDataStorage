package ru.adtimokhin.CloudDataStorage.handler.keyboard;

import ru.adtimokhin.CloudDataStorage.event.keyboard.KeyboardCommandEvent;
import ru.adtimokhin.CloudDataStorage.event.keyboard.KeyboardInitEvent;
import ru.adtimokhin.CloudDataStorage.service.system.BootstrapServiceBean;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class KeyboardInitHandler {
    @Inject
    private Event<KeyboardCommandEvent> keyboardCommandEvent;

    public void observe(@Observes final KeyboardInitEvent event){
        System.out.println();
        System.out.println("WELCOME TO COMMAND LINE INTERFACE");
        System.out.println("FOR LIST OF COMMANDS ENTER COMMAND \"help\" or \"hlp\"");
        keyboardCommandEvent.fire(new KeyboardCommandEvent());
    }
}
