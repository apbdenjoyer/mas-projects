package mp2;

import java.io.Serializable;

public class Role extends ObjectPlus implements Serializable {
    private String name;
    private User user;

    public Role(String name, User user) {
        super();

        this.name = name;
        this.user = user;

        if (name == null || name.isEmpty()) {
            removeFromExtent(this);
        }

        if (user == null) {
            removeFromExtent(this);
        }

    }

    public String getName() {
        return name;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return String.format("(Role: %s, User: %s)", name, user.getLogin());
    }
}
