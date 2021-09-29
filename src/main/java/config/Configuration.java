package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private final String apiVersion;
    private final String accessKey;

    public Configuration() throws IOException {
        try (InputStream input = new FileInputStream("src/main/resources/application.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            apiVersion = prop.getProperty("api_version");
            accessKey = prop.getProperty("access_token");
        } catch (IOException e) {
            throw new IOException("Can't read properties file", e);
        }
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getAccessKey() {
        return accessKey;
    }
}
