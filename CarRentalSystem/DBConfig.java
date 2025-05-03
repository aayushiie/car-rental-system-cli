package CarRentalSystem;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DBConfig {
    private String url;
    private String username;
    private String password;

    public DBConfig(String configFilePath) throws IOException{
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream(configFilePath);
        props.load(fis);
        this.url = props.getProperty("db.url");
        this.username = props.getProperty("db.username");
        this.password = props.getProperty("db.password");
    } 

    public String getUrl(){
        return url;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}
