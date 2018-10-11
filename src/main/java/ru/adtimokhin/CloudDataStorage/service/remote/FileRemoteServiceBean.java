package ru.adtimokhin.CloudDataStorage.service.remote;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adtimokhin.CloudDataStorage.api.remote.FileRemoteService;
import ru.adtimokhin.CloudDataStorage.system.AppService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.apache.jackrabbit.JcrConstants.NT_FILE;

@ApplicationScoped
public class FileRemoteServiceBean implements FileRemoteService {
    // Todo: реализовать все методы. Все есть в 3-ем уроке
    @Inject
    private AppService appService;
    @Override
    public @NotNull List<String> getListNameFiles() {
        List<String> fileNames = new ArrayList<>();
        if(!appService.status()){
            System.out.println("Соединение с удаленным хранилищем не установленно");
            return fileNames;
        }
       Node root = appService.getRootNode();
       if (root == null)return fileNames;
       try{
           NodeIterator ndi = root.getNodes();
           while (ndi.hasNext()) {
               Node node = ndi.nextNode();
               NodeType nodeType = node.getPrimaryNodeType();
               //todo: проверить, почему не работает
               if (nodeType.isNodeType(NT_FILE))
                   fileNames.add(node.getName());
           }

       } catch (RepositoryException e) {
           System.out.println("root folder is empty");

       }return fileNames;
    }

    @Nullable
    @Override
    public byte[] readData(@Nullable String fileName) {
        return new byte[0];
    }

    @Override
    public boolean exists(@Nullable String fileName) {
       List<String> names = getListNameFiles();
        for (int i = 0; i < names.size(); i++) {
            if(names.get(i).equals(fileName)) return true;
        }
        return false;
    }

    @Override
    public void remove(@Nullable String fileName) {

    }

    @Override
    public void createRootFile(@Nullable String fileName, String inputData) {

    }

    @Override
    public void writeData(@Nullable File file, @Nullable byte[] data) {

    }

    @Override
    public void getRootFiles() {
        List<String> fileNames = getListNameFiles();
        if(fileNames.size() == 0){
            System.out.println("---EMPTY---");
            return;
        }
        System.out.println("---");
        for (String fileName : fileNames) {
            if(!fileName.equals(".DS_Store") && !fileName.equals("._.DS_Store"))
            System.out.println(fileName);
        }
        System.out.println("---");
    }
}
