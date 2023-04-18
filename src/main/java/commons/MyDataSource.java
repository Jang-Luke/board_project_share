package commons;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MyDataSource {
    private static BasicDataSource basicDataSource = null;
    private static void setBasicDataSource(String fileName){
        Properties properties = new Properties();
        try(FileInputStream fileInputStream = new FileInputStream(fileName)){
            properties.load(fileInputStream);
        } catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(properties.getProperty("url"));
        basicDataSource.setUsername(properties.getProperty("id"));
        basicDataSource.setPassword(properties.getProperty("pw"));
        basicDataSource.setInitialSize(Integer.parseInt(properties.getProperty("pool_size")));
        basicDataSource.setDefaultAutoCommit(Boolean.parseBoolean(properties.getProperty("default_auto_commit")));
        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    }

    private MyDataSource(){}
    public synchronized static BasicDataSource getBasicDataSource(String fileName) {
        if (basicDataSource == null) {
            setBasicDataSource(fileName);
        }
        return basicDataSource;
    }
}
