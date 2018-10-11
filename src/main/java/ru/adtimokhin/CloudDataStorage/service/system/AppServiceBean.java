package ru.adtimokhin.CloudDataStorage.service.system;

import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;
import org.jetbrains.annotations.Nullable;
import ru.adtimokhin.CloudDataStorage.system.AppService;
import ru.adtimokhin.CloudDataStorage.system.SettingService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jcr.*;

@ApplicationScoped
public class AppServiceBean implements AppService {
    @Inject
    private SettingService settingService;

    private Repository repository = null;
    private Session session = null;
    private Exception error = null;

    @Override
    public void init() {
        if (settingService.getJcrActive()) login();
    }

    @Override
    public void shutdown() {
        System.exit(0);
    }

    @Override
    public boolean login() {
        if (status()) return false;
        try {
            repository = new URLRemoteRepository(settingService.getJcrUrl());
            String jcrLogin = settingService.getJcrLogin();
            String jcrPassword = settingService.getJcrPassword();
            char[] password = jcrPassword.toCharArray();
            session = repository.login(new SimpleCredentials(jcrLogin, password));
            return true;
        } catch (Exception e) {
            error = e;
            return false;
        } // https://geekbrains.ru/posts/gamedev_books_part1

    }

    @Override
    public boolean logout() {
        try {
//            session.logout();
            session = null;// не уверен, что лучше оставить
            return true;
        } catch (Exception e) {
            error = e;
            return false;
        }

    }

    @Override
    public boolean status() {
        if(repository == null)return false;
        if(session == null)return false;
        return true;
    }

    @Override
    public boolean save() {
        try {
            session.save();
            return true;
        } catch (Exception e) {
            error = e;
            return false;
        }
    }

    @Override
    public @Nullable Exception error() {
        return error;
    }

    @Override
    public @Nullable Repository repository() {
        return repository;
    }

    @Override
    public @Nullable Session session() {
        return session;
    }



    @Override
    public @Nullable Node getRootNode() {

        if(!status())return null;
        try {
            return session.getRootNode();
        } catch (RepositoryException e) {
            error =e;
        }
        return null;
    }
}
