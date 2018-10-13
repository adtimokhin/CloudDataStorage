package ru.adtimokhin.CloudDataStorage.api.local;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adtimokhin.CloudDataStorage.api.basic.FolderService;
import ru.adtimokhin.CloudDataStorage.api.general.LocalService;

import java.io.File;
import java.util.List;

public interface FolderLocalService extends FolderService, LocalService {
    void init();
    @NotNull List<String> getFolders(@Nullable File root);
    void createFolder(@Nullable String folderName,@Nullable File root);
    boolean deleteFolder(@Nullable String folderName,@Nullable File root);
    boolean clearFolderDirectory(@Nullable File root);
    boolean exists(@Nullable String folderName,@Nullable File root);

}
