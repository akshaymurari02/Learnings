package amazon.filesystem;

import java.util.HashMap;
import java.util.Map;

public abstract class FileSystemNode
{
    String name;
    long createdTime;
    long updatedTime;
    String extension;

    Map<String, FileSystemNode> children;

    FileSystemNode(String name)
    {
        this.name = name;
        this.extension = setExtension();
        this.children = new HashMap<>();
        createdTime = System.currentTimeMillis();
        updatedTime = System.currentTimeMillis();
    }

    private String setExtension() {
        if(name.contains("."))
        {
            return name.substring(name.lastIndexOf("."));
        }
        return "";
    }

     public String getName() {
        return name;
    }

}
