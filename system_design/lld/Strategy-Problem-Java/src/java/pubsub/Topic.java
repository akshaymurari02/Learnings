package pubsub;

/**
 * Represents a topic in the pub-sub system.
 */
public class Topic {

    private final String name;
    private final String description;

    public Topic(String name) {
        this(name, "");
    }

    public Topic(String name, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic name cannot be null or empty");
        }
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Topic topic = (Topic) obj;
        return name.equals(topic.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
