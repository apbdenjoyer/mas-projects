package mp2;

import java.time.LocalDate;

public class UserOnServer extends ObjectPlus {
    private final User user;
    private final Server server;
    private final LocalDate joinDate;

    public UserOnServer(User user, Server server) {
        super();

        this.user = user;
        this.server = server;
        this.joinDate = LocalDate.now();

        if (user == null) {
            removeFromExtent(this);
            throw new IllegalArgumentException("User cannot be null.");
        }

        if (server == null) {
            removeFromExtent(this);
            throw new IllegalArgumentException("Server cannot be null.");
        }



        try {
            for (UserOnServer userOnServer : getExtent(UserOnServer.class)) {
                if (userOnServer != this && userOnServer.getUser().equals(user) && userOnServer.getServer().equals(server)) {
                    throw new IllegalArgumentException("User is already on this server.");
                }
            }
        } catch (ClassNotFoundException ignored) {
//            no need to handle, since it'd mean no users
        }

        this.addLink("user", "is member of", user);
        this.addLink("server", "has a member", server);
    }

    public User getUser() {
        return user;
    }

    public Server getServer() {
        return server;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    @Override
    public String toString() {
        return String.format("(User: %s, Server: %s (Owner: %s), Join Date: %s)",
                user.getLogin(), server.getName(), server.getOwner().getLogin(),
                joinDate);
    }
}