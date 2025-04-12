package mp2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Channel extends ObjectPlus {
    private String name;
    private Server server;

//    part
    private final List<Message> messages;

    public Channel(String name, Server server) throws ServerAppException {
        super();

        if (name == null || name.isEmpty()) {
            removeFromExtent(this);
            throw new ServerAppException("Name is null or empty.");
        }

        if (server == null) {
            removeFromExtent(this);
            throw new ServerAppException("Server is null.");
        }

        this.name = name;
        this.messages = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    @Override
    public String toString() {
        return String.format("%s (%d messages)", this.name, this.messages.size());
    }

}

