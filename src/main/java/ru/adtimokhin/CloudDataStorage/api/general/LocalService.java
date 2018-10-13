package ru.adtimokhin.CloudDataStorage.api.general;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public interface LocalService {
    @NotNull File getRoot();
    File findDirectory(File root, @Nullable String directoryName);
}
