package ru.adtimokhin.CloudDataStorage.service.remote;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.adtimokhin.CloudDataStorage.api.remote.FileRemoteService;
import ru.adtimokhin.CloudDataStorage.handler.keyboard.KeyboardCommandHandler;
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
import static org.apache.jackrabbit.JcrConstants.NT_FOLDER;

@ApplicationScoped
public class FileRemoteServiceBean implements FileRemoteService {
    private boolean foundNode = false;
    private Node file;
    @Inject
    private AppService appService;

    @Override
    public void printListFiles(@Nullable String directory) {
        if (checkConnection()) return;
        List<String> names = getFiles(directory);
        if (names.size() == 0) {
            System.out.println("---EMPTY---");
            return;
        }
        System.out.println("---");
        for (String fileName : names) {
            if (".DS_Store".equals(fileName) || "._.DS_Store".equals(fileName)) continue;
            System.out.println(fileName);
        }
        System.out.println("---");
    }

    @Override
    public @NotNull List<String> getFiles(@Nullable String directoryName) {
        List<String> names = new ArrayList<>();
        if (directoryName == null || directoryName.isEmpty()) return names;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName))
            return getFiles(getRoot());
        else {
            Node node = findFolder(directoryName, getRoot());
            if (node == null) return names;
            return getFiles(node);
        }
    }

    @Override
    @SneakyThrows
    public @NotNull List<String> getFiles(@Nullable Node root) {
        List<String> fileNames = new ArrayList<>();
        if (root == null) return fileNames;

        NodeIterator ndi = root.getNodes();
        while (ndi.hasNext()) {
            Node node = ndi.nextNode();
            NodeType nodeType = node.getPrimaryNodeType();
            if (nodeType.isNodeType(NT_FILE))
                fileNames.add(node.getName());
        }
        return fileNames;
    }

    @Override
    public boolean exists(@Nullable String fileName, @Nullable String directoryName) {
        if (checkConnection()) return false;
        if (fileName == null || fileName.isEmpty()) return false;
        if (directoryName == null || directoryName.isEmpty()) return false;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName))
            exists(fileName, getRoot());
        else {
            Node node = findFolder(directoryName, getRoot());
            if (node == null) return false;
            return exists(fileName, node);
        }
        return false;
    }

    @Override
    @SneakyThrows
    public boolean exists(@Nullable String fileName, @Nullable Node root) {
        if (root == null) return false;
        if (fileName == null || fileName.isEmpty()) return false;

        NodeIterator ndi = root.getNodes();
        while (ndi.hasNext()) {
            Node node = ndi.nextNode();
            if (fileName.equals(node.getName())) {
                NodeType nodeType = node.getPrimaryNodeType();
                if (nodeType.isNodeType(NT_FILE)) return true;
                return false;
            }

        }
        return false;
    }

    @Override
    public void createFile(@Nullable String fileName, @Nullable String directoryName) {
        if (checkConnection()) return;
        if (directoryName == null) return;
        if (fileName == null || fileName.isEmpty()) return;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName))
            createFile(fileName, getRoot());
        else {
            Node node = findFolder(directoryName, getRoot());
            if (node == null) return;
            createFile(fileName, node);
        }
    }

    @Override
    @SneakyThrows
    public void createFile(@Nullable String fileName, @Nullable Node root) { // метод не рабочий
        if (root == null) return;
        if (fileName == null || fileName.isEmpty()) return;
        Node node = root.addNode(fileName, NT_FILE);
        save();
    }

    @Override
    public boolean deleteFile(@Nullable String fileName, @Nullable String directoryName) {
        if (checkConnection()) return false;
        if (fileName == null || fileName.isEmpty()) return false;
        if (directoryName == null || directoryName.isEmpty()) return false;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName))
            deleteFile(fileName, getRoot());
        else {
            Node node = findFolder(directoryName, getRoot());
            if (node == null) return false;
            return deleteFile(fileName, node);
        }
        return false;
    }

    @Override
    @SneakyThrows
    public boolean deleteFile(@Nullable String fileName, @Nullable Node root) {
        if (root == null) return false;
        if (fileName == null || fileName.isEmpty()) return false;
        NodeIterator ndi = root.getNodes();
        while (ndi.hasNext()) {
            Node node = ndi.nextNode();
            if (node.getName().equals(fileName)) {
                NodeType nodeType = node.getPrimaryNodeType();
                if (nodeType.isNodeType(NT_FILE)) {
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
    public boolean clearFileDirectory(@Nullable String directoryName) {
        if (checkConnection()) return false;
        if (directoryName == null | directoryName.isEmpty()) return false;
        if (KeyboardCommandHandler.ROOT.equals(directoryName) || KeyboardCommandHandler.ROOT_SHRT.equals(directoryName))
            return clearFileDirectory(getRoot());
        else {
            Node node = findFolder(directoryName, getRoot());
            if (node == null) return false;
            return clearFileDirectory(node);
        }
    }

    @Override
    @SneakyThrows
    public boolean clearFileDirectory(@Nullable Node root) {
        if (root == null) return false;
        NodeIterator ndi = root.getNodes();
        while (ndi.hasNext()) {
            Node node = ndi.nextNode();
            NodeType nodeType = node.getPrimaryNodeType();
            if (nodeType.isNodeType(NT_FILE)) node.remove();
        }
        save();
        return true;
    }

    @Override
    @Nullable
    @SneakyThrows
    public Node findFolder(@Nullable String folderName, @Nullable Node root) {
        if (folderName == null || folderName.isEmpty()) return null;
        if (root == null) return null;
        List<Node> nodes = new ArrayList<>();
        NodeIterator ndi = root.getNodes();
        while (ndi.hasNext()) {
            Node node = ndi.nextNode();
                NodeType nodeType = node.getPrimaryNodeType();
                if (nodeType.isNodeType(NT_FOLDER)) {
                    if (node.getName().equals(folderName)) {
                        foundNode = true;
                        file = node;
                        return node;
                    }else nodes.add(node);
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
    public @Nullable byte[] readData(@Nullable String fileName) {
        return new byte[0];
    }

    @Override
    public void writeData(@Nullable File file, @Nullable byte[] data) {

    }


    @Override
    public @Nullable Node getRoot() {
        return appService.getRootNode();
    }

    @Override
    public boolean checkConnection() {
        boolean conn = appService.status();
        if (!conn) {
            System.out.println("Вы не вошли учетную запись.");
        }
        return !conn;
    }

    @Override
    public boolean save() {
        return appService.save();
    }
}
