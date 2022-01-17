package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConnectionConfig {

    private static final class ConnectionConfigHolder {
        private static final ConnectionConfig connectionConfig = new ConnectionConfig();
    }

    public static ConnectionConfig getInstance() {
        return ConnectionConfigHolder.connectionConfig;
    }

    private String url = null;
    private String username = null;
    private String password = null;

    private ConnectionConfig() {
        try (InputStream inputStream = ResourceProvider.getConfigResource()) {

            Properties properties = new Properties();
            properties.load(inputStream);

            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
