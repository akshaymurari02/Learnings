package filesystem;

import java.time.LocalDateTime;

/**
 * Abstract base class for file system nodes (files and directories).
 * Uses Composite pattern - both File and Directory are treated uniformly.
 */
public abstract class FileSystemNode {

    protected String name;
    protected Directory parent;
    protected final LocalDateTime createdAt;
    protected LocalDateTime modifiedAt;
    protected String owner;
    protected FilePermission permission;

    protected FileSystemNode(String name) {
        this(name, null);
    }

    protected FileSystemNode(String name, Directory parent) {
        validateName(name);
        this.name = name;
        this.parent = parent;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
        this.owner = "system";
        this.permission = new FilePermission(true, true, false); // rw-
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (name.contains("/") || name.contains("\\")) {
            throw new IllegalArgumentException("Name cannot contain path separators");
        }
    }

    public abstract long getSize();

    public abstract boolean isDirectory();

    public abstract FileSystemNode copy();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
        updateModifiedTime();
    }

    public Directory getParent() {
        return parent;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    public String getPath() {
        if (parent == null) {
            return "/" + name;
        }
        String parentPath = parent.getPath();
        return parentPath.equals("/") ? "/" + name : parentPath + "/" + name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    protected void updateModifiedTime() {
        this.modifiedAt = LocalDateTime.now();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public FilePermission getPermission() {
        return permission;
    }

    public void setPermission(FilePermission permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return name;
    }
}
