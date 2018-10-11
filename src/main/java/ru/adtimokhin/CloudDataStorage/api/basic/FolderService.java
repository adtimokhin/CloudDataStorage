package ru.adtimokhin.CloudDataStorage.api.basic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface FolderService {
    @NotNull List<String> getListFolderName();
    void getListNamesRoot();
    void createFolder(@Nullable String folderName);
    boolean deleteFolder(@Nullable String folderName);
    boolean clearRoot();
    boolean exists(@Nullable String folderName);

}
