package ru.adtimokhin.CloudDataStorage.service.local;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adtimokhin.CloudDataStorage.api.local.FolderLocalService;
import ru.adtimokhin.CloudDataStorage.handler.keyboard.KeyboardCommandHandler;
import ru.adtimokhin.CloudDataStorage.system.SettingService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class FolderLocalServiceBean implements FolderLocalService {
    @Inject
    private SettingService settingService;

    private Exception error;
    private boolean directoryIsFound =false;
     File folder = null;


    @Override
    public void init() {
        String folder = settingService.getSyncFolder();
        final File file = new File(folder);
        file.mkdirs();
    }

    @Override
    public void printListFolders(@Nullable String directoryName) {
        List<String> names = getFolders(directoryName);
        if (names.size() == 0) {
            System.out.println("---EMPTY---");
            return;
        }
        System.out.println("---");
        for (String fileName : names) {
            System.out.println(fileName);
        }
        System.out.println("---");
    }

    @Override
    public @NotNull List<String> getFolders(@Nullable String directoryName) {
        List<String> names = new ArrayList<>();
        if (directoryName == null || directoryName.isEmpty()) return names;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName)) return getFolders(getRoot());
        else {
            File folder = findDirectory(getRoot(), directoryName);
            if (folder == null) return names;
            return getFolders(folder);
        }
    }

    @Override
    public @NotNull List<String> getFolders(@Nullable File root) {
        String[] directories = root.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory();
            }
        });
        assert directories != null;
        return Arrays.asList(directories);
    }

    @Override
    public boolean exists(@Nullable String folderName, @Nullable String directoryName) {
        if(directoryName == null|| directoryName.isEmpty())return false;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName))return exists(directoryName,getRoot());
        else{
            File folder = findDirectory(getRoot(), directoryName);
            if(folder ==null)return false;
            return exists(directoryName, folder);
        }
    }

    @Override
    public boolean exists(@Nullable String folderName, @Nullable File root) {
        if(folderName == null|| folderName.isEmpty())return false;
        if(root ==null)return false;
        List<String> fileNames = getFolders(root);
        for (String name : fileNames) {
            if(name.equals(folderName))return true;
        }
        return false;
    }

    @Override
    public void createFolder(@Nullable String folderName, @Nullable String directoryName) {
        if (directoryName == null || directoryName.isEmpty()) return;
        if (folderName == null || folderName.isEmpty()) return;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName)) createFolder(folderName, getRoot());
        else {
            File folder = findDirectory(getRoot(), directoryName);
            if (folder == null) return;
            createFolder(folderName, folder);
        }
    }

    @Override
    public void createFolder(@Nullable String folderName, @Nullable File root) {
        if(root == null)return;
        if(folderName ==null || folderName.isEmpty())return;
        File newFile = new File(root +"/"+folderName);
        newFile.mkdirs();
    }

    @Override
    public boolean deleteFolder(@Nullable String folderName, @Nullable String directoryName) {
        if (directoryName == null || directoryName.isEmpty()) return false;
        if (folderName == null || folderName.isEmpty()) return false;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName)) deleteFolder(folderName, getRoot());
        else {
            File folder = findDirectory(getRoot(), directoryName);
            if (folder == null) return false;
            return deleteFolder(folderName, folder);
        }
        return false;
    }

    @Override
    public boolean deleteFolder(@Nullable String folderName, @Nullable File root) {
        if(root ==null)return false;
        if(folderName ==null || folderName.isEmpty())return false;
        File fileToDelete = new File(root, folderName);
        return fileToDelete.delete();
    }

    @Override
    public boolean clearFolderDirectory(@Nullable String directoryName) {
        if(directoryName == null || directoryName.isEmpty())return false;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName))return clearFolderDirectory(getRoot());
        else{
            File directory = findDirectory(getRoot(), directoryName);
            if(directory == null)return false;
            return clearFolderDirectory(directory);
        }
    }

    @Override
    public boolean clearFolderDirectory( @Nullable File root) { // todo добавить проверку папки на пустоту, и в случае наличия в нем элементов, опуститься на уровень нуже, провести проверку и в случае удачи, удалить там все элементы, а потом вернуться и удалить все элементы там
        if(root == null)return false;
        List<String> fileNames = getFolders(root);
        for (String name : fileNames) {
            if(name==  null)return false;
            File file = new File(root,name);
            file.delete();
        }
        if(fileNames.size() !=0) System.out.println("Все папки не удалось удалить. Причина: в них храняться другие файлы.");
        return true;
    }

    @Override
    public File findDirectory(@NotNull File root, @Nullable String directoryName) {
        List<String>directories = getFolders(root);
        if (directories.size() != 0) {
            for (String directory : directories) {
                if (!directoryIsFound)
                    if (directory.equals(directoryName)) {
                        directoryIsFound = true;
                        folder = new File(root, directoryName);
                    }
            }
            if(!directoryIsFound) {
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
    public File getRoot(){return new File(settingService.getSyncFolder());}


}