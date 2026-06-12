package filesystem;

/**
 * Custom exception for file system operations.
 */
public class FileSystemException extends RuntimeException {

    public FileSystemException(String message) {
        super(message);
    }

    public FileSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public static FileSystemException pathNotFound(String path) {
        return new FileSystemException("Path not found: " + path);
    }

    public static FileSystemException pathAlreadyExists(String path) {
        return new FileSystemException("Path already exists: " + path);
    }

    public static FileSystemException notAFile(String path) {
        return new FileSystemException("Not a file: " + path);
    }

    public static FileSystemException notADirectory(String path) {
        return new FileSystemException("Not a directory: " + path);
    }

    public static FileSystemException invalidPath(String path) {
        return new FileSystemException("Invalid path: " + path);
    }

    public static FileSystemException directoryNotEmpty(String path) {
        return new FileSystemException("Directory not empty: " + path);
    }
}
