package ru.adtimokhin.CloudDataStorage.service.remote;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adtimokhin.CloudDataStorage.api.remote.FolderRemoteService;
import ru.adtimokhin.CloudDataStorage.system.AppService;
import ru.adtimokhin.CloudDataStorage.system.SettingService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;
import java.util.ArrayList;
import java.util.List;

import static org.apache.jackrabbit.JcrConstants.NT_FOLDER;

@ApplicationScoped
public class FolderRemoteServiceBean implements FolderRemoteService {
    @Inject
    private SettingService settingService;
    @Inject
    private AppService appService;


    @Override
    public void init() {
    }

    @Override
    public @NotNull List<String> getListFolderName() {
        List<String> folderNames = new ArrayList<>();
        if(!appService.status()){
            System.out.println("Соединение с удаленным хранилищем не установленно");
            return folderNames;
        }
        if (appService.getRootNode() == null) return folderNames;
        Node root = appService.getRootNode();
        try {
            NodeIterator ndi = root.getNodes();
            while (ndi.hasNext()) {
                Node node = ndi.nextNode();
                NodeType nodeType = node.getPrimaryNodeType();
                 if(nodeType.isNodeType(NT_FOLDER)) // todo: разобраться, почему папки не подходят под NT_FOLDER
                    folderNames.add(node.getName());
            }
            return folderNames;
        } catch (RepositoryException e) {
            System.out.println("root folder is empty");
            return folderNames;
        }

    }

    @Override
    public void getListNamesRoot() {
        List<String> folderNames = getListFolderName();
        if (folderNames.size() == 0) {
            System.out.println("---EMPTY---");
            return;
        }
        System.out.println("---");
        for (String name : folderNames) {
            System.out.println(name);
        }
        System.out.println("---");
    }

    @Override
    public void createFolder(@Nullable String folderName) {

    }

    @Override
    public boolean deleteFolder(@Nullable String folderName) {
        return true;
    }

    @Override
    public boolean clearRoot() {
        return false;
    }

    @Override
    public boolean exists(@Nullable String folderName) {
        List<String> names = getListFolderName();
        for (int i = 0; i < names.size(); i++) {
            if(names.get(i).equals(folderName)) return true;
        }
        return false;
    }
}
