package ru.adtimokhin.CloudDataStorage.service.local;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adtimokhin.CloudDataStorage.api.local.FileLocalService;
import ru.adtimokhin.CloudDataStorage.system.SettingService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class FileLocalServiceBean implements FileLocalService {
    // Todo: реализовать все методы. Все есть в 3-ем уроке
    @Inject
    private SettingService settingService;

    @Override
    public @NotNull List<String> getListNameFiles() {
        File root = getRoot();
        String[] directories = root.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isFile();
            }
        });
        assert directories != null;
        return Arrays.asList(directories);
    }

    @Nullable
    @Override
    public byte[] readData(@Nullable String fileName) {
        if (fileName == null || fileName.isEmpty()) return null;
        if (exists(fileName)) {
            final File root = getRoot();
            File file = new File(root, fileName);
            Path path = Paths.get(file.toURI());
            try {
                return Files.readAllBytes(path);
            } catch (IOException e) {
                System.out.println("failed to read data from file: "+ file.getName());
            }
        }
        return null;
    }

    @Override
    public boolean exists(@Nullable String fileName) {
        if (fileName == null || fileName.isEmpty()) return false;
        final File root = getRoot();
        File file = new File(root, fileName);
        return file.exists();
    }

    @Override
    public void remove(@Nullable String fileName) {
        if (fileName == null || fileName.isEmpty()) return;
        final File root = getRoot();
        File fileToDelete = new File(root, fileName);
        fileToDelete.delete();

    }

    @Override
    public void createRootFile(@Nullable String fileName, String inputData) {
        if (fileName == null || fileName.isEmpty()) return;
        final File root = getRoot();
        String data = (inputData == null || inputData.isEmpty()) ? " " : inputData;// возможность создавать файлы
        // без полезной нагрузки в них
        File newFile = new File(root +"/"+ fileName);
        writeData(newFile, toByteArray(data));
        newFile.mkdirs();


    }

    public byte[] toByteArray(String data) {
        char[] chars = data.toCharArray();
        if (chars.length == 0) return null;
        byte[] bytes = new byte[chars.length];
        int i = 0;
        for (char c : chars) {
            bytes[i] = (byte) c;
            i++;
        }
        return bytes;
    }

    public void writeData(@Nullable File file, @Nullable byte[] data) {
        if (file == null || data == null) return;
        Path path = Paths.get(file.toURI());
        try {
            Files.write(path, data);
        } catch (IOException e) {
            System.out.println("Failed to write data into a file " + file.getName());
        }
    }

    @Override
    public void getRootFiles() {
        List<String> names = getListNameFiles();
        if(names.size() ==0){ System.out.println("---EMPTY---"); return;}
        System.out.println("---");
        for (String fileName :names) {
            System.out.println(fileName);
        }
        System.out.println("---");
    }




    @NotNull
    private File getRoot() {
        return new File(settingService.getSyncFolder());
    }
}
