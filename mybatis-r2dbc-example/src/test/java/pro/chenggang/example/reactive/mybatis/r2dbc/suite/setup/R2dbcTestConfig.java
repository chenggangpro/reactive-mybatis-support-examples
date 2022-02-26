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
    protected String databaseIp = "192.168.31.133";
    protected String databasePort = "1521";
    protected String databaseName = "srp";
    protected String databaseUsername = "srp";
    protected String databasePassword = "password";
}
