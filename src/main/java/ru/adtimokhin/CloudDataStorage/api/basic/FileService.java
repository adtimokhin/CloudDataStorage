package ru.adtimokhin.CloudDataStorage.api.basic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.jcr.Node;
import java.io.File;
import java.util.List;

public interface FileService {
    @NotNull List<String> getFiles(@Nullable String directoryName);
    void printListFiles(@Nullable String directory);
    boolean clearFileDirectory(@Nullable String directoryName);
    void createFile(@Nullable String fileName, @Nullable String directoryName);
    boolean deleteFile(@Nullable String fileName, @Nullable String directoryName);
    @Nullable byte[] readData(@Nullable String fileName);
    void writeData(@Nullable File file, @Nullable byte[] data);
    boolean exists(@Nullable String fileName, @Nullable String directoryName);
}
