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
    protected String databaseIp = "127.0.0.1";
    protected String databasePort = "1433";
    protected String databaseName = "r2dbc";
    protected String databaseUsername = "SA";
    protected String databasePassword = "Abcd@1234";
}
