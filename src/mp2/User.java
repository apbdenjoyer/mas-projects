package mp2;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class User extends ObjectPlus implements Serializable {
    private String login;

    private static final int MIN_LOGIN_LEN = 3;
    private static final int MAX_LOGIN_LEN = 20;

    public User(String login) {
        super();

        this.login = login;

        if (!isLoginValid(login)) {
            removeFromExtent(this);
        }
        if (!isloginAvailable(login)) {
            removeFromExtent(this);
        }
    }

    private boolean isLoginValid(String login) {
        if (login == null || login.isEmpty()) {
            return false;
        }

        if (login.length() < MIN_LOGIN_LEN || login.length() > MAX_LOGIN_LEN) {
            return false;
        }

        return login.matches("\\w+");
    }

    private boolean isloginAvailable(String login) {
        try {
            for (User user : getExtent(User.class)) {
                if (user != null && user.getLogin().equals(login) && !user.equals(this)) {
                    return false;
                }
            }
        } catch (
                ClassNotFoundException ignored) {
//            no need to handle, since it'd mean no users
        }
        return true;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if (!isLoginValid(login)) {
            this.login = login;
        }
    }


    @Override
    public String toString() {
        return String.format("(User: %s)", login);
    }
}