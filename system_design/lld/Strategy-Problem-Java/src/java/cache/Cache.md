Functional Requirements:

1. The cache should support in-memory caching.
2. The cache should implement both FIFO and LRU eviction policies with capacity.
3. The cache should provide methods for adding, retrieving, and removing items.

Non-Functional Requirements:
1. The cache should be thread-safe to allow concurrent access.
2. The cache should be efficient in terms of time complexity for adding, retrieving, and removing
3. items, ideally O(1) for retrieval and O(1) for adding and removing items.
4. open for extension of various types of storages
5. write through cache ? if yes assuming it is full consistent
6. read through cache or cache aside. if yes then need a DbLoader also which cache itself acts as one point entry to getting data, application as no control of it.
7. serialization and deserialization ?
8. how to invalidate cache ? time based or event based or both ? if time based then need to have a timer thread which will check for expired items and remove them from cache. if event based then need to have a mechanism to listen for events and invalidate cache accordingly.
9. hot data caching or full data caching or partially data caching on caches heavy processing data.
10. require compression policy for caching ?
11. encryption and decryption for caching ? if yes then need to have a mechanism to encrypt and decrypt data before storing and retrieving from cache.

BrianStorm Requirements: 

Cache

   EvictionPolicy
   FIFO
   LRU

   StorageType
   MEMORY
   DISK

So We can actually have

FIFOInMemoryCache
LRUInMemoryCache
FIFODiskCache
LRUDiskCache

or we can make use of bridge pattern and have

or even can use composition design architecture

Cache
- has Storage
- has EvictionPolicy

or bridge

abstraction Cache ->

abstract Cache
- In Memory Caching
- Disk Caching
- Redis or Db Cache

        - has implementor 

            -EvictionPolicy

Classes:

Cache

    +put(key: String, value: Object): void
    +get(key: String): Object
    +remove(key: String): void

EvictionPolicy
    
    +evictionKey(): String
    keyAccessed(key: String): void

InMemoryCache extends Cache

    -storage: Map<String, Object>
    -evictionPolicy: EvictionPolicy

LRUEvictionPolicy implements EvictionPolicy

    -accessOrder: DoubleLinkedList<String>

LinkedListNode

    -key: String
    -prev: LinkedListNode
    -next: LinkedListNode

DoubleLinkedList

    -head: LinkedListNode
    -tail: LinkedListNode
    +addToFront(key: String): void
    +moveToFront(node: LinkedListNode): void
    +removeLast(): String

Relationships:

Cache -> InMemoryCache ->

EvictionPolicy -> LRUEvictionPolicy -> DoubleLinkedList

Implementation Details:

Design Patterns: The bridge pattern is used to separate the abstraction (Cache) from its implementation (EvictionPolicy). This allows for flexibility in changing the eviction policy without modifying the cache implementation.

Thread Safety: The cache implementation should use synchronization mechanisms (e.g., locks) to ensure thread safety when accessing the storage and eviction policy.

Time Complexity: The get method should have O(1) time complexity for retrieval, while the put and remove methods should have O(1).

Error Handling: The cache should handle cases where a key does not exist when trying to retrieve or remove an item, possibly by returning null or throwing an exception.