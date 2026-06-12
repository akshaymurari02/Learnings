package filesystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * File System Service - main service for file system operations.
 * Provides a Unix-like file system interface with support for:
 * - Absolute paths (/home/user/file.txt)
 * - Relative paths (./file.txt, ../other/file.txt)
 * - Path navigation (cd, pwd)
 * - File CRUD operations
 * - Directory CRUD operations
 * - Search capabilities
 */
public class FileSystemService implements IFileSystemService {

    private final Directory root;
    private Directory currentDirectory;

    public FileSystemService() {
        this.root = new Directory("");
        this.root.setParent(null);
        this.currentDirectory = root;
    }

    // ==================== NAVIGATION ====================

    @Override
    public Directory getRoot() {
        return root;
    }

    @Override
    public Directory getCurrentDirectory() {
        return currentDirectory;
    }

    @Override
    public void changeDirectory(String path) {
        Optional<FileSystemNode> node = resolvePath(path);
        if (node.isEmpty()) {
            throw FileSystemException.pathNotFound(path);
        }
        if (!node.get().isDirectory()) {
            throw FileSystemException.notADirectory(path);
        }
        currentDirectory = (Directory) node.get();
    }

    // ==================== FILE OPERATIONS ====================

    @Override
    public File createFile(String path) {
        return createFile(path, "");
    }

    @Override
    public File createFile(String path, String content) {
        PathInfo pathInfo = parsePath(path);
        Directory parentDir = getOrCreateParentDirectory(pathInfo);

        if (parentDir.hasChild(pathInfo.name)) {
            throw FileSystemException.pathAlreadyExists(path);
        }

        File file = new File(pathInfo.name, content);
        parentDir.addChild(file);
        return file;
    }

    @Override
    public Optional<File> getFile(String path) {
        Optional<FileSystemNode> node = resolvePath(path);
        if (node.isPresent() && !node.get().isDirectory()) {
            return Optional.of((File) node.get());
        }
        return Optional.empty();
    }

    @Override
    public void writeFile(String path, String content) {
        Optional<File> file = getFile(path);
        if (file.isEmpty()) {
            createFile(path, content);
        } else {
            file.get().setContent(content);
        }
    }

    @Override
    public void appendFile(String path, String content) {
        Optional<File> file = getFile(path);
        if (file.isEmpty()) {
            throw FileSystemException.pathNotFound(path);
        }
        file.get().append(content);
    }

    @Override
    public String readFile(String path) {
        Optional<File> file = getFile(path);
        if (file.isEmpty()) {
            throw FileSystemException.pathNotFound(path);
        }
        return file.get().getContent();
    }

    // ==================== DIRECTORY OPERATIONS ====================

    @Override
    public Directory createDirectory(String path) {
        PathInfo pathInfo = parsePath(path);
        Directory parentDir = resolveParentDirectory(pathInfo);

        if (parentDir == null) {
            throw FileSystemException.pathNotFound(pathInfo.parentPath);
        }

        if (parentDir.hasChild(pathInfo.name)) {
            throw FileSystemException.pathAlreadyExists(path);
        }

        Directory newDir = new Directory(pathInfo.name);
        parentDir.addChild(newDir);
        return newDir;
    }

    @Override
    public Directory createDirectories(String path) {
        String[] parts = normalizePath(path).split("/");
        Directory current = root;

        for (String part : parts) {
            if (part.isEmpty()) continue;

            Optional<FileSystemNode> child = current.getChild(part);
            if (child.isEmpty()) {
                Directory newDir = new Directory(part);
                current.addChild(newDir);
                current = newDir;
            } else if (child.get().isDirectory()) {
                current = (Directory) child.get();
            } else {
                throw FileSystemException.notADirectory(part);
            }
        }
        return current;
    }

    @Override
    public Optional<Directory> getDirectory(String path) {
        Optional<FileSystemNode> node = resolvePath(path);
        if (node.isPresent() && node.get().isDirectory()) {
            return Optional.of((Directory) node.get());
        }
        return Optional.empty();
    }

    @Override
    public List<FileSystemNode> list(String path) {
        Optional<Directory> dir = getDirectory(path);
        if (dir.isEmpty()) {
            throw FileSystemException.pathNotFound(path);
        }
        return dir.get().getChildren();
    }

    // ==================== COMMON OPERATIONS ====================

    @Override
    public boolean exists(String path) {
        return resolvePath(path).isPresent();
    }

    @Override
    public void delete(String path) {
        Optional<FileSystemNode> node = resolvePath(path);
        if (node.isEmpty()) {
            throw FileSystemException.pathNotFound(path);
        }

        FileSystemNode target = node.get();

        if (target.isDirectory() && !((Directory) target).isEmpty()) {
            throw FileSystemException.directoryNotEmpty(path);
        }

        Directory parent = target.getParent();
        if (parent != null) {
            parent.removeChild(target.getName());
        }
    }

    @Override
    public void deleteRecursive(String path) {
        Optional<FileSystemNode> node = resolvePath(path);
        if (node.isEmpty()) {
            throw FileSystemException.pathNotFound(path);
        }

        FileSystemNode target = node.get();
        Directory parent = target.getParent();

        if (parent != null) {
            parent.removeChild(target.getName());
        }
    }

    @Override
    public void move(String sourcePath, String destPath) {
        Optional<FileSystemNode> sourceNode = resolvePath(sourcePath);
        if (sourceNode.isEmpty()) {
            throw FileSystemException.pathNotFound(sourcePath);
        }

        PathInfo destInfo = parsePath(destPath);
        Directory destParent = resolveParentDirectory(destInfo);

        if (destParent == null) {
            throw FileSystemException.pathNotFound(destInfo.parentPath);
        }

        FileSystemNode source = sourceNode.get();
        Directory sourceParent = source.getParent();

        // Remove from source
        if (sourceParent != null) {
            sourceParent.removeChild(source.getName());
        }

        // Add to destination
        source.setName(destInfo.name);
        destParent.addChild(source);
    }

    @Override
    public void copy(String sourcePath, String destPath) {
        Optional<FileSystemNode> sourceNode = resolvePath(sourcePath);
        if (sourceNode.isEmpty()) {
            throw FileSystemException.pathNotFound(sourcePath);
        }

        PathInfo destInfo = parsePath(destPath);
        Directory destParent = resolveParentDirectory(destInfo);

        if (destParent == null) {
            throw FileSystemException.pathNotFound(destInfo.parentPath);
        }

        FileSystemNode copy = sourceNode.get().copy();
        copy.setName(destInfo.name);
        destParent.addChild(copy);
    }

    @Override
    public void rename(String path, String newName) {
        Optional<FileSystemNode> node = resolvePath(path);
        if (node.isEmpty()) {
            throw FileSystemException.pathNotFound(path);
        }

        Directory parent = node.get().getParent();
        if (parent != null && parent.hasChild(newName)) {
            throw FileSystemException.pathAlreadyExists(newName);
        }

        // Remove with old name, update, add with new name
        if (parent != null) {
            parent.removeChild(node.get().getName());
            node.get().setName(newName);
            parent.addChild(node.get());
        } else {
            node.get().setName(newName);
        }
    }

    // ==================== SEARCH ====================

    @Override
    public List<FileSystemNode> find(String name) {
        List<FileSystemNode> results = new ArrayList<>();
        findHelper(root, name, results);
        return results;
    }

    private void findHelper(Directory dir, String name, List<FileSystemNode> results) {
        for (FileSystemNode child : dir.getChildren()) {
            if (child.getName().contains(name)) {
                results.add(child);
            }
            if (child.isDirectory()) {
                findHelper((Directory) child, name, results);
            }
        }
    }

    @Override
    public List<FileSystemNode> findByExtension(String extension) {
        List<FileSystemNode> results = new ArrayList<>();
        findByExtensionHelper(root, extension, results);
        return results;
    }

    private void findByExtensionHelper(Directory dir, String extension, List<FileSystemNode> results) {
        for (FileSystemNode child : dir.getChildren()) {
            if (!child.isDirectory()) {
                File file = (File) child;
                if (file.getExtension().equals(extension)) {
                    results.add(file);
                }
            } else {
                findByExtensionHelper((Directory) child, extension, results);
            }
        }
    }

    // ==================== INFO ====================

    @Override
    public long getSize(String path) {
        Optional<FileSystemNode> node = resolvePath(path);
        if (node.isEmpty()) {
            throw FileSystemException.pathNotFound(path);
        }
        return node.get().getSize();
    }

    @Override
    public String getAbsolutePath(String relativePath) {
        Optional<FileSystemNode> node = resolvePath(relativePath);
        if (node.isEmpty()) {
            throw FileSystemException.pathNotFound(relativePath);
        }
        return node.get().getPath();
    }

    // ==================== PATH RESOLUTION ====================

    private Optional<FileSystemNode> resolvePath(String path) {
        if (path == null || path.isEmpty()) {
            return Optional.of(currentDirectory);
        }

        String normalizedPath = normalizePath(path);
        String[] parts = normalizedPath.split("/");

        // Determine starting point
        Directory current;
        if (path.startsWith("/")) {
            current = root;
        } else {
            current = currentDirectory;
        }

        FileSystemNode result = current;

        for (String part : parts) {
            if (part.isEmpty() || part.equals(".")) {
                continue;
            }

            if (part.equals("..")) {
                if (result.getParent() != null) {
                    result = result.getParent();
                }
                continue;
            }

            if (!result.isDirectory()) {
                return Optional.empty();
            }

            Optional<FileSystemNode> child = ((Directory) result).getChild(part);
            if (child.isEmpty()) {
                return Optional.empty();
            }
            result = child.get();
        }

        return Optional.of(result);
    }

    private String normalizePath(String path) {
        return path.replace("\\", "/").replaceAll("/+", "/");
    }

    private PathInfo parsePath(String path) {
        String normalized = normalizePath(path);
        int lastSlash = normalized.lastIndexOf('/');

        if (lastSlash == -1) {
            return new PathInfo(".", normalized);
        } else if (lastSlash == 0) {
            return new PathInfo("/", normalized.substring(1));
        } else {
            return new PathInfo(
                normalized.substring(0, lastSlash),
                normalized.substring(lastSlash + 1)
            );
        }
    }

    private Directory resolveParentDirectory(PathInfo pathInfo) {
        Optional<FileSystemNode> parent = resolvePath(pathInfo.parentPath);
        if (parent.isPresent() && parent.get().isDirectory()) {
            return (Directory) parent.get();
        }
        return null;
    }

    private Directory getOrCreateParentDirectory(PathInfo pathInfo) {
        Directory parent = resolveParentDirectory(pathInfo);
        if (parent == null) {
            return createDirectories(pathInfo.parentPath);
        }
        return parent;
    }

    private static class PathInfo {
        final String parentPath;
        final String name;

        PathInfo(String parentPath, String name) {
            this.parentPath = parentPath;
            this.name = name;
        }
    }

    // ==================== UTILITY ====================

    /**
     * Prints the directory tree starting from root (for debugging).
     */
    public void printTree() {
        System.out.println("/");
        printTreeHelper(root, "");
    }

    private void printTreeHelper(Directory dir, String indent) {
        List<FileSystemNode> children = dir.getChildren();
        for (int i = 0; i < children.size(); i++) {
            FileSystemNode child = children.get(i);
            boolean isLast = (i == children.size() - 1);
            String prefix = isLast ? "└── " : "├── ";
            String childIndent = isLast ? "    " : "│   ";

            System.out.println(indent + prefix + child.getName() +
                (child.isDirectory() ? "/" : " (" + child.getSize() + " bytes)"));

            if (child.isDirectory()) {
                printTreeHelper((Directory) child, indent + childIndent);
            }
        }
    }

    /**
     * Returns the current working directory path.
     */
    public String pwd() {
        return currentDirectory == root ? "/" : currentDirectory.getPath();
    }
}
