package filesystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents a directory in the file system.
 * Contains children nodes (files and subdirectories).
 */
public class Directory extends FileSystemNode {

    private final Map<String, FileSystemNode> children;

    public Directory(String name) {
        super(name);
        this.children = new HashMap<>();
    }

    public Directory(String name, Directory parent) {
        super(name, parent);
        this.children = new HashMap<>();
    }

    @Override
    public long getSize() {
        return children.values().stream()
            .mapToLong(FileSystemNode::getSize)
            .sum();
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public FileSystemNode copy() {
        Directory copy = new Directory(this.name);
        copy.setOwner(this.owner);
        copy.setPermission(new FilePermission(
            this.permission.isReadable(),
            this.permission.isWritable(),
            this.permission.isExecutable()
        ));
        // Deep copy children
        for (FileSystemNode child : children.values()) {
            FileSystemNode childCopy = child.copy();
            copy.addChild(childCopy);
        }
        return copy;
    }

    public void addChild(FileSystemNode node) {
        if (!permission.isWritable()) {
            throw new SecurityException("No write permission for directory: " + name);
        }
        if (children.containsKey(node.getName())) {
            throw new IllegalArgumentException("A node with name '" + node.getName() + "' already exists");
        }
        children.put(node.getName(), node);
        node.setParent(this);
        updateModifiedTime();
    }

    public boolean removeChild(String name) {
        if (!permission.isWritable()) {
            throw new SecurityException("No write permission for directory: " + this.name);
        }
        FileSystemNode removed = children.remove(name);
        if (removed != null) {
            removed.setParent(null);
            updateModifiedTime();
            return true;
        }
        return false;
    }

    public Optional<FileSystemNode> getChild(String name) {
        if (!permission.isReadable()) {
            throw new SecurityException("No read permission for directory: " + this.name);
        }
        return Optional.ofNullable(children.get(name));
    }

    public List<FileSystemNode> getChildren() {
        if (!permission.isReadable()) {
            throw new SecurityException("No read permission for directory: " + name);
        }
        return new ArrayList<>(children.values());
    }

    public List<FileSystemNode> listFiles() {
        return getChildren().stream()
            .filter(node -> !node.isDirectory())
            .collect(Collectors.toList());
    }

    public List<FileSystemNode> listDirectories() {
        return getChildren().stream()
            .filter(FileSystemNode::isDirectory)
            .collect(Collectors.toList());
    }

    public boolean hasChild(String name) {
        return children.containsKey(name);
    }

    public int getChildCount() {
        return children.size();
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }

    /**
     * Recursively lists all nodes in this directory and subdirectories.
     */
    public List<FileSystemNode> listRecursive() {
        List<FileSystemNode> result = new ArrayList<>();
        listRecursiveHelper(this, result);
        return result;
    }

    private void listRecursiveHelper(Directory dir, List<FileSystemNode> result) {
        for (FileSystemNode child : dir.getChildren()) {
            result.add(child);
            if (child.isDirectory()) {
                listRecursiveHelper((Directory) child, result);
            }
        }
    }
}
