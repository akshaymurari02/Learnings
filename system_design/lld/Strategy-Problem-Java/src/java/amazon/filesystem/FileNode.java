package amazon.filesystem;

public class FileNode extends FileSystemNode
{

    public volatile String content = "";

    FileNode(String name) {
        super(name);
    }

    public String read()
    {
        return content;
    }

    public void write(String content)
    {
        this.content = content;
        this.updatedTime = System.currentTimeMillis();
    }
}
