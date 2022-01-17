package config;

import java.io.InputStream;

public class ResourceProvider {

    private ResourceProvider() {
    }

    public static InputStream getConfigResource() {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
    }
}
