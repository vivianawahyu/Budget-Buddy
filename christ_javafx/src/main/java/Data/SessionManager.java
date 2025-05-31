package Data;

public class SessionManager {
    private static SessionManager instance;

    private int userId;
    private String username;

    private SessionManager() {
        this.userId = 0;
        this.username = null;
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public void logout() {
        this.userId = 0;
        this.username = null;
    }

    public boolean isLoggedIn() {
        return this.userId != 0;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
