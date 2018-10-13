package ru.adtimokhin.CloudDataStorage.api.basic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface FolderService {
    @NotNull List<String> getFolders(@Nullable String directoryName);
    void printListFolders(@Nullable String directoryName);
    void createFolder(@Nullable String folderName,@Nullable String directoryName);
    boolean deleteFolder(@Nullable String folderName,@Nullable String directoryName);
    boolean clearFolderDirectory(@Nullable String directoryName);
    boolean exists(@Nullable String folderName,@Nullable String directoryName);
}
