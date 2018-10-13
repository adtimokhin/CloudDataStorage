package ru.adtimokhin.CloudDataStorage.service.local;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adtimokhin.CloudDataStorage.api.local.FileLocalService;
import ru.adtimokhin.CloudDataStorage.api.local.FolderLocalService;
import ru.adtimokhin.CloudDataStorage.handler.keyboard.KeyboardCommandHandler;
import ru.adtimokhin.CloudDataStorage.system.SettingService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class FileLocalServiceBean implements FileLocalService {
    private boolean directoryIsFound = false;
    private File folder;
    @Inject
    private SettingService settingService;
    @Inject
    private FolderLocalService folderLocalService;

    @Override
    public void printListFiles(@Nullable String directory) {
        List<String> names = getFiles(directory);
        if (names.size() == 0) {
            System.out.println("---EMPTY---");
            return;
        }
        System.out.println("---");
        for (String fileName : names) {
            if(".DS_Store".equals(fileName))continue;
            System.out.println(fileName);
        }
        System.out.println("---");
    }

    @Override
    public @NotNull List<String> getFiles(@Nullable String directoryName) {
        List<String> names = new ArrayList<>();
        if (directoryName == null || directoryName.isEmpty()) return names;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName)) return getFiles(getRoot());
        else {
            File folder = findDirectory(getRoot(), directoryName);
            if (folder == null) return names;
            return getFiles(folder);
        }
    }

    @Override
    public @NotNull List<String> getFiles(@Nullable File root) {
        String[] directories = root.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isFile();
            }
        });
        assert directories != null;
        return Arrays.asList(directories);
    }

    @Override
    public boolean exists(@Nullable String fileName, @Nullable String directoryName) {
        if (fileName == null || fileName.isEmpty()) return false;
        if (directoryName == null || directoryName.isEmpty()) return false;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName)) return exists(fileName, getRoot());
        else {
            File folder = findDirectory(getRoot(), directoryName);
            if (folder == null) return false;
            return exists(fileName, folder);
        }
    }

    @Override
    public boolean exists(@Nullable String fileName, @Nullable File root) {
        if (fileName == null || fileName.isEmpty()) return false;
        if (root == null) return false;
        List<String> fileNames = getFiles(root);
        for (String name : fileNames) {
            if (name.equals(fileName)) return true;
        }
        return false;
    }

    @Override
    public void createFile(@Nullable String fileName, @Nullable String directoryName) {
        if (directoryName == null || directoryName.isEmpty()) return;
        if (fileName == null || fileName.isEmpty()) return;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName)) createFile(fileName, getRoot());
        else {
            File folder = findDirectory(getRoot(), directoryName);
            if (folder == null) return;
            createFile(fileName, folder);
        }
    }

    @Override
    public void createFile(@Nullable String filerName, @Nullable File root) {
        if (root == null) return;
        if (filerName == null || filerName.isEmpty()) return;
        String data = "empty";
        File newFile = new File(root + "/" + filerName);
        writeData(newFile, toByteArray(data));
        newFile.mkdirs();
    }

    @Override
    @SneakyThrows
    public void writeData(@Nullable File file, @Nullable byte[] data) {
        if (file == null || data == null) return;
        Path path = Paths.get(file.toURI());
        Files.write(path, data);

    }

    private byte[] toByteArray(String data) {
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

    @Override
    public boolean deleteFile(@Nullable String fileName, @Nullable String directoryName) {
        if (directoryName == null || directoryName.isEmpty()) return false;
        if (fileName == null || fileName.isEmpty()) return false;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName)) deleteFile(fileName, getRoot());
        else {
            File folder = findDirectory(getRoot(), directoryName);
            if (folder == null) return false;
            return deleteFile(fileName, folder);
        }
        return false;
    }

    @Override
    public boolean deleteFile(@Nullable String fileName, @Nullable File root) {
        if (root == null) return false;
        if (fileName == null || fileName.isEmpty()) return false;
        File fileToDelete = new File(root, fileName);
        return fileToDelete.delete();
    }

    @Override
    public boolean clearFileDirectory(@Nullable String directoryName) {
        if (directoryName == null || directoryName.isEmpty()) return false;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName)) return clearFileDirectory(getRoot());
        else {
            File directory = findDirectory(getRoot(), directoryName);
            if (directory == null) return false;
            return clearFileDirectory(directory);
        }
    }

    @Override
    public boolean clearFileDirectory(@Nullable File root) {
        List<String> fileNames = getFiles(root);
        for (String name : fileNames) {
            if (name == null) return false;
            File file = new File(root, name);
            file.delete();
        }
        return true;
    }

    @Override
    @Nullable
    public File findDirectory(File root, @Nullable String directoryName) {
        List<String> directories = folderLocalService.getFolders(root);
        if (directories.size() != 0) {
            for (String directory : directories) {
                if (!directoryIsFound)
                    if (directory.equals(directoryName)) {
                        directoryIsFound = true;
                        folder = new File(root, directoryName);
                    }
            }
            if (!directoryIsFound) {
                for (String directory : directories) {
                    findDirectory(new File(root, directory), directoryName);
                }
            }
        }
        if (root.getName().equals(getRoot().getName()) && !directoryIsFound) {
            directoryIsFound = false;
            return null;
        } else if (root.getName().equals(getRoot().getName()) && directoryIsFound) {
            directoryIsFound = false;
            return folder;
        }
        return null;
    }

    @Override
    @NotNull
    public File getRoot() {
        return new File(settingService.getSyncFolder());
    }
    // todo реализовтаь потом

    @Override
    public @Nullable byte[] readData(@Nullable String fileName) {
        return new byte[0];
    }

    //        return null;
    //
    //    @Override
    //    public void getFiles(@Nullable String name) {
    //        List<String> names = getFiles();
    //        if(names.size() ==0){ System.out.println("---EMPTY---"); return;}
    //        System.out.println("---");
    //        for (String fileName :names) {
    //            System.out.println(fileName);
    //        }
    //        System.out.println("---");
    //    }
    //
    //    @Override
    //    public @NotNull List<String> getListNameFilesNamedDirectory(@Nullable File root) {
    //        root = getRoot();
    //        String[] directories = root.list(new FilenameFilter() {
    //            @Override
    //            public boolean accept(File dir, String name) {
    //                return new File(dir, name).isFile();
    //            }
    //        });
    //        assert directories != null;
    //        return Arrays.asList(directories);
    //    }
    //
    //    @Override
    //    public @NotNull List<String> getFiles() {
    //        File root = getRoot();
    //        String[] directories = root.list(new FilenameFilter() {
    //            @Override
    //            public boolean accept(File dir, String name) {
    //                return new File(dir, name).isFile();
    //            }
    //        });
    //        assert directories != null;
    //        return Arrays.asList(directories);
    //    }
    //
    //
    //
    //    @Override
    //    public boolean exists(@Nullable String fileName) {
    //        if (fileName == null || fileName.isEmpty()) return false;
    //        final File root = getRoot();
    //        File file = new File(root, fileName);
    //        return file.exists();
    //    }
    //
    //    @Override
    //    public void remove(@Nullable String fileName) {
    //        if (fileName == null || fileName.isEmpty()) return;
    //        final File root = getRoot();
    //        File fileToDelete = new File(root, fileName);
    //        fileToDelete.delete();
    //
    //    }
    //    // clear directory block completed
    //    @Override
    //    public boolean clearFileDirectory(@Nullable String name) {
    //        if(name == null|| name.isEmpty())return false;
    //        if("root".equals(name)|| "rt".equals(name))return clearFileRoot();
    //        else{
    //            List<String> names = folderLocalService.getListFolderName();
    //            for (String folderName : names) {
    //                if(name.equals(folderName))return clearFileNamedDirectory(new File(getRoot(),folderName));
    //            }
    //        }
    //        return false;
    //    }
    //
    //    @Override
    //    public boolean clearFileRoot() {
    //        File root = getRoot();
    //        List<String> fileNames = getFiles();
    //        for (String name : fileNames) {
    //            if(name == null)return false;
    //            File file = new File(root,name);
    //            file.delete();
    //        }return true;
    //    }
    //
    //    @Override
    //    public boolean clearFileNamedDirectory(@Nullable File root) {
    //        List<String> fileNames = getListNameFilesNamedDirectory(root);
    //        for (String fileName : fileNames) {
    //            File file = new File(root,fileName);
    //            file.delete();
    //        }
    //        return true;
    //    }
    //
    //    @Override
    //    public void createRootFile(@Nullable String fileName) {
    //        if (fileName == null || fileName.isEmpty()) return;
    //        final File root = getRoot();
    //        File newFile = new File(root +"/"+ fileName);
    ////        writeData(newFile, toByteArray(data));
    //        newFile.mkdirs();
    //
    //
    //    }
    //
    //    @Override
    //    public void createFileNamedDirectory(@Nullable String fileName, @Nullable File root) {
    //
    //    }
    //
    //    public byte[] toByteArray(String data) {
    //        char[] chars = data.toCharArray();
    //        if (chars.length == 0) return null;
    //        byte[] bytes = new byte[chars.length];
    //        int i = 0;
    //        for (char c : chars) {
    //            bytes[i] = (byte) c;
    //            i++;
    //        }
    //        return bytes;
    //    }
    //
    //    public void writeData(@Nullable File file, @Nullable byte[] data) {
    //        if (file == null || data == null) return;
    //        Path path = Paths.get(file.toURI());
    //        try {
    //            Files.write(path, data);
    //        } catch (IOException e) {
    //            System.out.println("Failed to write data into a file " + file.getName());
    //        }
    //    }
    //
    //
    //    @Override
    //    public void createFile(@Nullable String fileName) {
    //
    //    }
    //    @Nullable
    //    @Override
    //    public byte[] readData(@Nullable String fileName) {
    //        if (fileName == null || fileName.isEmpty()) return null;
    //        if (exists(fileName)) {
    //            final File root = getRoot();
    //            File file = new File(root, fileName);
    //            Path path = Paths.get(file.toURI());
    //            try {
    //                return Files.readAllBytes(path);
    //            } catch (IOException e) {
    //                System.out.println("failed to read data from file: "+ file.getName());
    //            }
    //        }
    //    }

}
