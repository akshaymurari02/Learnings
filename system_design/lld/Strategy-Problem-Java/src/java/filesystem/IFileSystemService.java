package filesystem;

import java.util.List;
import java.util.Optional;

/**
 * Interface for file system operations.
 */
public interface IFileSystemService {

    // Navigation
    Directory getRoot();
    Directory getCurrentDirectory();
    void changeDirectory(String path);

    // File operations
    File createFile(String path);
    File createFile(String path, String content);
    Optional<File> getFile(String path);
    void writeFile(String path, String content);
    void appendFile(String path, String content);
    String readFile(String path);

    // Directory operations
    Directory createDirectory(String path);
    Directory createDirectories(String path); // mkdir -p
    Optional<Directory> getDirectory(String path);
    List<FileSystemNode> list(String path);

    // Common operations
    boolean exists(String path);
    void delete(String path);
    void deleteRecursive(String path);
    void move(String sourcePath, String destPath);
    void copy(String sourcePath, String destPath);
    void rename(String path, String newName);

    // Search
    List<FileSystemNode> find(String name);
    List<FileSystemNode> findByExtension(String extension);

    // Info
    long getSize(String path);
    String getAbsolutePath(String relativePath);
}
