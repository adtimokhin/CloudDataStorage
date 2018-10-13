package ru.adtimokhin.CloudDataStorage.api.local;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adtimokhin.CloudDataStorage.api.basic.FileService;
import ru.adtimokhin.CloudDataStorage.api.general.LocalService;

import java.io.File;
import java.util.List;

public interface FileLocalService extends FileService, LocalService {
    boolean clearFileDirectory(@Nullable File root);
    @NotNull List<String> getFiles(@Nullable File root);
    boolean exists(@Nullable String fileName, @Nullable File root);
    void createFile(@Nullable String filerName,  @Nullable File root);
    boolean deleteFile(@Nullable String fileName,  @Nullable File root);


}