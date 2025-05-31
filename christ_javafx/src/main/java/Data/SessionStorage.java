package Data;

import java.io.*;
import java.util.Properties;

public class SessionStorage {
    private static final String FILE_PATH = "session.properties";

    public static void saveSession(int userId, String username) {
        try (OutputStream output = new FileOutputStream(FILE_PATH)) {
            Properties prop = new Properties();
            prop.setProperty("userId", String.valueOf(userId));
            prop.setProperty("username", username);
            prop.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearSession() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    public static boolean loadSession() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return false;

        try (InputStream input = new FileInputStream(FILE_PATH)) {
            Properties prop = new Properties();
            prop.load(input);

            int userId = Integer.parseInt(prop.getProperty("userId", "0"));
            String username = prop.getProperty("username", null);

            if (userId != 0 && username != null) {
                SessionManager.getInstance().login(userId, username);
                return true;
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }
}
