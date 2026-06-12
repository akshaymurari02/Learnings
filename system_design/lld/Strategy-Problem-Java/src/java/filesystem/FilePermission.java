package filesystem;

/**
 * Represents file permissions (read, write, execute).
 */
public class FilePermission {

    private boolean readable;
    private boolean writable;
    private boolean executable;

    public FilePermission(boolean readable, boolean writable, boolean executable) {
        this.readable = readable;
        this.writable = writable;
        this.executable = executable;
    }

    public static FilePermission readOnly() {
        return new FilePermission(true, false, false);
    }

    public static FilePermission readWrite() {
        return new FilePermission(true, true, false);
    }

    public static FilePermission fullAccess() {
        return new FilePermission(true, true, true);
    }

    public boolean isReadable() {
        return readable;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    public boolean isWritable() {
        return writable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    public boolean isExecutable() {
        return executable;
    }

    public void setExecutable(boolean executable) {
        this.executable = executable;
    }

    @Override
    public String toString() {
        return (readable ? "r" : "-") +
               (writable ? "w" : "-") +
               (executable ? "x" : "-");
    }
}
