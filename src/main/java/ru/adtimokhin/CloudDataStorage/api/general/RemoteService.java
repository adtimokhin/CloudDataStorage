package ru.adtimokhin.CloudDataStorage.api.general;

import org.jetbrains.annotations.Nullable;

import javax.jcr.Node;

public interface RemoteService {
    @Nullable Node getRoot();
    Node findFolder(@Nullable String folderName,@Nullable Node root);
    boolean checkConnection();
    boolean save();
}
