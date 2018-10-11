package ru.adtimokhin.CloudDataStorage.service.local;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adtimokhin.CloudDataStorage.api.local.FolderLocalService;
import ru.adtimokhin.CloudDataStorage.system.SettingService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class FolderLocalServiceBean implements FolderLocalService {
    @Inject
    private SettingService settingService;

    private Exception error;



    @Override
    public void init() {
        String folder = settingService.getSyncFolder();
        final File file = new File(folder);
        file.mkdirs();
    }

    @Override
    public @NotNull List<String> getListFolderName() {
        final File root = getRoot();
        final String[] folders = root.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir,name).isDirectory();//метод пройдет по каждому элементу корневой папки и если
                // найдет, то тогда запишет его имя в folders
            }
        });
        return Arrays.asList(folders);
    }

    @Override
    public void getListNamesRoot() {
        List<String> names = getListFolderName();
        if(names.size() ==0){ System.out.println("---EMPTY---"); return;}
        System.out.println("---");
        for (String folderName :names) {
            System.out.println(folderName);
        }
        System.out.println("---");
    }

    @Override
    public void createFolder(@Nullable String folderName) {
        final File root = getRoot();
        if(folderName ==null|| folderName.isEmpty())return;
        final File newFile = new File(root+"/"+folderName);
        newFile.mkdirs();//если путь для файла не будет обнаружен, то он будет создан

    }

    @Override
    public boolean deleteFolder(@Nullable String folderName) {
        final File root = getRoot();
        if(folderName ==null|| folderName.isEmpty())return false;
        final File fileToDelete = new File(root,folderName);
        return fileToDelete.delete();
    }

    @Override
    public boolean clearRoot() {
        final File root = getRoot();
        List <String> directories = getListFolderName();
        if(directories.size() == 0)return false;//todo: добавить в это условие файловую директорию
        for (String directory : directories) {
            new File(root,directory).delete();
        }
        //todo: добавить удаление файлов в FileLocalServiceBean и применить его здесь, чтобы и файлы тоже удалались.
        return true;
    }

    @Override
    public boolean exists(@Nullable String folderName) {
        for (String name : getListFolderName()) {
            if(name.equals(folderName))return true;
        }return false;
    }

    @NotNull
    private File getRoot(){return new File(settingService.getSyncFolder());}


}