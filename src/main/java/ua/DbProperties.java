package ua;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbProperties {
    private String connection;
    private String user;
    private String password;

    public DbProperties() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            Properties props = new Properties();
            props.load(is);
            connection = props.getProperty("db.connection");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getConnection() {
        return connection;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
