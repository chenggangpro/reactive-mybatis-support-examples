package pro.chenggang.example.reactive.mybatis.r2dbc.suite.setup;

import java.time.Duration;

/**
 * @author Gang Cheng
 * @date 12/15/21.
 */
public class R2dbcTestConfig {

    protected Integer initialSize = 1;
    protected Integer maxSize = 3;
    protected Duration maxIdleTime = Duration.ofMinutes(30);
    protected String databaseIp;  //retrieve from the container
    protected static String databasePort = "3306";
    protected static String databaseDockerExposedPort = "34343";
    protected static String databaseName = "r2dbc";
    protected static String databaseUsername = "root";
    protected static String databasePassword = "root";
}
