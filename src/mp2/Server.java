package mp2;

import java.util.*;

public class Server extends ObjectPlus {
    private final String name;
    private final User owner;

    List<Channel> channels = new ArrayList<>();

    Map<User, Role> userRolesQualif = new TreeMap<>();

    List<UserOnServer> usersOnServers = new ArrayList<>();


    public Server(String name, User owner) throws ServerAppException {
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
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
    }

    public List<Channel> getChannels() {
        return Collections.unmodifiableList(channels);
    }

    public void addChannel(Channel channel) throws ServerAppException {
        if (channel == null) {
            throw new IllegalArgumentException("Channel is null");
        }

        if (channel.getName() == null || channel.getName().isEmpty()) {
            throw new IllegalArgumentException("Channel's name is null or " +
                    "empty.");
        }

        for (Channel c : channels) {
            if (c.equals(channel)) {
                throw new ServerAppException("This channel already exists.");
            }
        }

        channels.add(channel);
    }

    public void removeChannel(Channel channel) throws ServerAppException {
        if (channel == null) {
            throw new NullPointerException("Channel is null.");
        }

        Channel foundChannel = null;
        for (Channel c : channels) {
            if (c.equals(channel)) {
                foundChannel = c;
            }
        }

        if (foundChannel == null) {
            throw new ServerAppException(String.format("Channel %s not found " +
                    "on server %s.", channel.getName(), this.name));
        }

        channels.remove(foundChannel);
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

        return String.format("%s (Owner: %s) - %d channels, %d members", name,
                owner.getLogin(), channels.size(), userCount);
    }

    public List<UserOnServer> getUsersOnServers() {
        return Collections.unmodifiableList(usersOnServers);
    }

    public void addRoleQualif(User user, Role role) {
        if (user == null) {
            throw new NullPointerException("User is null.");
        }

        if (role == null) {
            throw new IllegalArgumentException("Role is null.");
        }

        if (!userRolesQualif.containsKey(user)) {
            userRolesQualif.put(user, role);
        }
    }

    public Role getUserRoleQualif(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null.");
        }

        if (!userRolesQualif.containsKey(user)) {
            throw new NullPointerException("Unable to find role for user");
        }

        return userRolesQualif.get(user);
    }
}
