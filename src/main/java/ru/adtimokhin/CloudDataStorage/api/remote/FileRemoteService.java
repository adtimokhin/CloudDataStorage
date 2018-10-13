package ru.adtimokhin.CloudDataStorage.api.remote;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adtimokhin.CloudDataStorage.api.basic.FileService;
import ru.adtimokhin.CloudDataStorage.api.general.RemoteService;

import javax.jcr.Node;
import java.util.List;

public interface FileRemoteService extends FileService, RemoteService {
    boolean clearFileDirectory(@Nullable Node root);
    @NotNull List<String> getFiles(@Nullable Node root);
    boolean exists(@Nullable String fileName, @Nullable Node root);
    void createFile(@Nullable String fileName,  @Nullable Node root);
    boolean deleteFile(@Nullable String fileName,  @Nullable Node root);
}