package mp2;

import java.io.Serializable;

public class Role extends ObjectPlus implements Serializable {
    private String name;
    private User user;

    public Role(String name, User user) {
        super();
        if (name == null || name.isEmpty()) {
            removeFromExtent(this);
        }

        if (user == null) {
            removeFromExtent(this);
        }

        this.name = name;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public User getUser() {
        return user;
    }

}
