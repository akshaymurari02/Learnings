package filesystem;

/**
 * Represents a file in the file system.
 * Contains content and has a specific size based on content length.
 */
public class File extends FileSystemNode {

    private String content;
    private String extension;

    public File(String name) {
        super(name);
        this.content = "";
        this.extension = extractExtension(name);
    }

    public File(String name, Directory parent) {
        super(name, parent);
        this.content = "";
        this.extension = extractExtension(name);
    }

    public File(String name, String content) {
        super(name);
        this.content = content != null ? content : "";
        this.extension = extractExtension(name);
    }

    private String extractExtension(String name) {
        int lastDot = name.lastIndexOf('.');
        return lastDot > 0 ? name.substring(lastDot + 1) : "";
    }

    @Override
    public long getSize() {
        return content.length();
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public FileSystemNode copy() {
        File copy = new File(this.name, this.content);
        copy.setOwner(this.owner);
        copy.setPermission(new FilePermission(
            this.permission.isReadable(),
            this.permission.isWritable(),
            this.permission.isExecutable()
        ));
        return copy;
    }

    public String getContent() {
        if (!permission.isReadable()) {
            throw new SecurityException("No read permission for file: " + name);
        }
        return content;
    }

    public void setContent(String content) {
        if (!permission.isWritable()) {
            throw new SecurityException("No write permission for file: " + name);
        }
        this.content = content != null ? content : "";
        updateModifiedTime();
    }

    public void append(String text) {
        if (!permission.isWritable()) {
            throw new SecurityException("No write permission for file: " + name);
        }
        this.content += text;
        updateModifiedTime();
    }

    public String getExtension() {
        return extension;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        this.extension = extractExtension(name);
    }
}
