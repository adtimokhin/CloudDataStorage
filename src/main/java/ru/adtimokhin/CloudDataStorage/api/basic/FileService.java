package ru.adtimokhin.CloudDataStorage.api.basic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public interface FileService {
    @NotNull List<String> getListNameFiles();

    @Nullable
    byte[] readData(@Nullable String fileName);

    boolean exists(@Nullable String fileName);

    void remove(@Nullable String fileName);

    void createRootFile(@Nullable String fileName, String inputData);

    void writeData(@Nullable File file, @Nullable byte[] data);

    void getRootFiles();
}
