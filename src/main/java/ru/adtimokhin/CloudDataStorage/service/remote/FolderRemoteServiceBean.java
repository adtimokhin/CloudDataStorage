package ru.adtimokhin.CloudDataStorage.service.remote;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adtimokhin.CloudDataStorage.api.remote.FolderRemoteService;
import ru.adtimokhin.CloudDataStorage.handler.keyboard.KeyboardCommandHandler;
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

import static org.apache.jackrabbit.JcrConstants.MIX_REFERENCEABLE;
import static org.apache.jackrabbit.JcrConstants.NT_FOLDER;

@ApplicationScoped
public class FolderRemoteServiceBean implements FolderRemoteService {
    @Inject
    private SettingService settingService;
    @Inject
    private AppService appService;
    private boolean foundNode = false;
    private Node file;

    @Override
    public void printListFolders(@Nullable String directoryName) {
        if(checkConnection()) return;
        List<String> names = getFolders(directoryName);
        if (names.size() == 0) {
            System.out.println("---EMPTY---");
            return;
        }
        System.out.println("---");
        for (String name : names) {
            System.out.println(name);
        }
        System.out.println("---");
    }

    @Override
    public @NotNull List<String> getFolders(@Nullable String directoryName) {
        List<String> names = new ArrayList<>();
        if (directoryName == null || directoryName.isEmpty()) return names;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName))
            return getFolders(getRoot());
        else {
            Node node = findFolder(directoryName, getRoot());
            if (node == null) return names;
            return getFolders(node);
        }
    }

    private NodeType nodeType;
    private Node node;
    @Override
    @SneakyThrows
    public @NotNull List<String> getFolders(@Nullable Node root) { // метод не рабочий
        List<String> names = new ArrayList<>();
        if (root == null) return names;
        NodeIterator ndi = root.getNodes();
        while (ndi.hasNext()) {
             node = ndi.nextNode();
             nodeType = node.getPrimaryNodeType();
            if (nodeType.isNodeType(NT_FOLDER)) names.add(node.getName());
        }
        return names;
    }

    @Override
    public boolean exists(@Nullable String folderName, @Nullable String directoryName) {
        if(checkConnection()) return false;
        if (folderName == null || folderName.isEmpty()) return false;
        if (directoryName == null || directoryName.isEmpty()) return false;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName))
            return exists(folderName, getRoot());
        else {
            Node node = findFolder(directoryName, getRoot());
            if (node == null) return false;
            return exists(folderName, node);
        }
    }

    @Override
    @SneakyThrows
    public boolean exists(@Nullable String folderName, @Nullable Node root) {
        if (folderName == null || folderName.isEmpty()) return false;
        if (root == null) return false;
        NodeIterator ndi = root.getNodes();
        while (ndi.hasNext()) {
            Node node = ndi.nextNode();
            if (folderName.equals(node.getName())) {
                NodeType nodeType = node.getPrimaryNodeType();
                if (nodeType.isNodeType(NT_FOLDER)) return true;
                return false;
            }
        }
        return false;
    }

    @Override
    public void createFolder(@Nullable String folderName, @Nullable String directoryName) {
        if(checkConnection()) return;
        if (folderName == null || folderName.isEmpty()) return;
        if (directoryName == null || directoryName.isEmpty()) return;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName))
            createFolder(folderName, getRoot());
        else {
            Node node = findFolder(directoryName, getRoot());
            if (node == null) return;
            createFolder(folderName, node);
        }
    }

    @Override
    @SneakyThrows
    public void createFolder(@Nullable String folderName, @Nullable Node root) { // метод не рабочий
        if (folderName == null || folderName.isEmpty()) return;
        if (root == null) return;
        Node node = root.addNode(folderName, NT_FOLDER);
        save();
    }

    @Override
    public boolean deleteFolder(@Nullable String folderName, @Nullable String directoryName) {
        if(checkConnection()) return false;
        if (folderName == null || folderName.isEmpty()) return false;
        if (directoryName == null || directoryName.isEmpty()) return false;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName))
            return deleteFolder(folderName, getRoot());
        else {
            Node node = findFolder(directoryName, getRoot());
            if (node == null) return false;
            return deleteFolder(folderName, node);
        }
    }

    @Override
    @SneakyThrows
    public boolean deleteFolder(@Nullable String folderName, @Nullable Node root) {
        if (folderName == null || folderName.isEmpty()) return false;
        if (root == null) return false;
        NodeIterator ndi = root.getNodes();
        while (ndi.hasNext()) {
            Node node = ndi.nextNode();
            if (folderName.equals(node.getName())) {
                NodeType nodeType = node.getPrimaryNodeType();
                if (nodeType.isNodeType(NT_FOLDER)) {
                    node.remove();
                    return true;
                }
                return false;
            }
        }
        save();
        return false;
    }

    @Override
    public boolean clearFolderDirectory(@Nullable String directoryName) {
        if(checkConnection()) return false;
        if (directoryName == null || directoryName.isEmpty()) return false;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName))
            return clearFolderDirectory(getRoot());
        else {
            Node node = findFolder(directoryName, getRoot());
            if (node == null) return false;
            return clearFolderDirectory(node);
        }
    }

    @Override
    @SneakyThrows
    public boolean clearFolderDirectory(@Nullable Node root) {
        if (root == null) return false;
        NodeIterator ndi = root.getNodes();
        while (ndi.hasNext()) {
            Node node = ndi.nextNode();
            NodeType nodeType = node.getPrimaryNodeType();
            if (nodeType.isNodeType(NT_FOLDER)) node.remove();
        }
        save();
        return true;
    }

    @Override
    @Nullable
    @SneakyThrows
    public Node findFolder(@Nullable String folderName, Node root) {
        if (root == null) return null;
        List<Node> nodes = new ArrayList<>();
        NodeIterator ndi = root.getNodes();
        while (ndi.hasNext()) {
            Node node = ndi.nextNode();
            if (node.getName().equals(folderName)) {
                NodeType nodeType = node.getPrimaryNodeType();
                if (nodeType.isNodeType(NT_FOLDER)) {
                    foundNode = true;
                    file = node;
                    return node;
                } else nodes.add(node);
            }
        }
        if (!foundNode) {
            for (Node node : nodes) {
                findFolder(folderName, node);
            }
        }

        if (root == getRoot() && foundNode) return file;
        else if (root == getRoot()) return null;

        return null;

    }

    @Override
    public @Nullable Node getRoot() {
        return appService.getRootNode();
    }

    @Override
    public boolean checkConnection() {
        boolean conn = appService.status();
        if(!conn){
            System.out.println("Вы не вошли учетную запись.");
        }
        return !conn;
    }
    @Override
    public boolean save() {
        return appService.save();
    }
}
