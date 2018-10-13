package ru.adtimokhin.CloudDataStorage.api.remote;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adtimokhin.CloudDataStorage.api.basic.FolderService;
import ru.adtimokhin.CloudDataStorage.api.general.RemoteService;

import javax.jcr.Node;
import java.util.List;

public interface FolderRemoteService extends FolderService, RemoteService {
    @NotNull List<String> getFolders(@Nullable Node root);
    void createFolder(@Nullable String folderName,@Nullable Node root);
    boolean deleteFolder(@Nullable String folderName,@Nullable Node root);
    boolean clearFolderDirectory(@Nullable Node root);
    boolean exists(@Nullable String folderName,@Nullable Node root);
}
