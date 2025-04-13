package mp2;

import java.util.*;

public class Server extends ObjectPlus {
    private final String name;
    private final User owner;


    public Server(String name, User owner) {
        super();

        if (owner == null) {
            removeFromExtent(this);
            throw new IllegalArgumentException("Owner is null.");
        }

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name is null or empty.");
        }

        this.name = name;
        this.owner = owner;

        new UserOnServer(owner, this);
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
    }


    public List<UserOnServer> getUsersOnServer() {
        List<UserOnServer> users = new ArrayList<>();

        try {
            for (UserOnServer userOnServer : getExtent(UserOnServer.class)) {
                if (userOnServer.getServer().equals(this)) {
                    users.add(userOnServer);
                }
            }
        } catch (ClassNotFoundException e) {
            return null;
        }
        return users;
    }

    @Override
    public String toString() {
        int userCount = 0;

        userCount = getUsersOnServer().size();

        return String.format("(Server: %s, Owner: %s)", this.name, owner.getLogin());
    }
}
