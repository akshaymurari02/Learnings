FileSystem

FR:

1. File management system handles file system operations such as creating, deleting, moving, and copying files and directories. 
2. It also provides functionality for reading and writing data to files.
3. using delimiter / to separate directories and files in the path.
4. should store metadata for each file and directory, such as name, size, creation date.


Classes:

1. FileSystemManager
2. FileSystemNode 
3. DirectoryNode
4. FileNode

Relationships:
1. FileSystemManager is the main class that manages the file system operations and maintains the root directory.
2. FileSystemNode is an abstract class that represents a node in the file system, which
3. can be either a DirectoryNode or a FileNode.
4. DirectoryNode extends FileSystemNode and represents a directory in the file system, which can
5. contain other FileSystemNodes (both DirectoryNodes and FileNodes).
6. FileNode extends FileSystemNode and represents a file in the file system, which contains
7. data and metadata such as name, size, and creation date.
8. FileSystemManager provides methods for creating, deleting, moving, and copying files and directories, as well as reading and writing data to files. It also maintains the root directory of the file system.
9. FileSystemNode provides common properties and methods for both DirectoryNode and FileNode, such as name, size, creation date, and methods for getting and setting these properties.
10. DirectoryNode provides methods for managing its child nodes, such as adding, removing, and retrieving child nodes.
11. FileNode provides methods for reading and writing data to the file, as well as getting
12. and setting its metadata properties.
13. Overall, the FileSystemManager class serves as the main interface for interacting with the file system, while the FileSystemNode, DirectoryNode, and FileNode classes represent the structure and functionality of the file system itself.

