package mp2;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class User extends ObjectPlus implements Serializable {
    private String login;

    private static final int MIN_LOGIN_LEN = 3;
    private static final int MAX_LOGIN_LEN = 20;

    List<UserOnServer> userOnServers;

    public User(String login) throws ServerAppException, ClassNotFoundException {
        super();

        if (!isLoginValid(login)) {
            removeFromExtent(this);
            throw new ServerAppException("Login is invalid (alphanumerics, _," +
                    " between 3 and 20 characters.)");
        }

        if (!isloginAvailable(login)) {
            removeFromExtent(this);
            throw new ServerAppException(String.format("Login %s is already " +
                    "in use.", login));
        }

        this.login = login;
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

    private boolean isloginAvailable(String login) throws ClassNotFoundException {
        for (User user : getExtent(User.class)) {
            if (user != null) {
                if (user.getLogin().equals(login) && !user.equals(this)) {
                    return false;
                }
            }
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

    public List<UserOnServer> getUserOnServers() {
        return Collections.unmodifiableList(userOnServers);
    }
}