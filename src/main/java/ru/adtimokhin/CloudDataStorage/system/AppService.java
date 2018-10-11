package ru.adtimokhin.CloudDataStorage.system;

import org.jetbrains.annotations.Nullable;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;

public interface AppService {
    void init();

    void shutdown();

    boolean login();

    boolean logout();

    boolean status();

    boolean save();

    @Nullable
    Exception error();

    @Nullable
    Repository repository();

    @Nullable Session session();

    @Nullable Node getRootNode();
}
