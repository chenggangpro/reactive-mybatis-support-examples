package pro.chenggang.example.reactive.mybatis.r2dbc.spring.multi;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.ValidationDepth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.TransactionAwareConnectionFactoryProxy;
import org.springframework.util.Assert;
import pro.chenggang.project.reactive.mybatis.support.r2dbc.ReactiveSqlSessionFactory;
import pro.chenggang.project.reactive.mybatis.support.r2dbc.defaults.DefaultReactiveSqlSessionFactory;
import pro.chenggang.project.reactive.mybatis.support.r2dbc.delegate.R2dbcMybatisConfiguration;
import pro.chenggang.project.reactive.mybatis.support.r2dbc.spring.executor.SpringReactiveMybatisExecutor;
import pro.chenggang.project.reactive.mybatis.support.r2dbc.spring.properties.R2dbcMybatisConnectionFactoryProperties;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

/**
 * @author evans
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class MultiDatabaseConfiguration {

    @Bean
    public ReactiveSqlSessionFactory reactiveSqlSessionFactory(R2dbcMybatisConfiguration configuration, BeanNameRoutingConnectionFactory beanNameRoutingConnectionFactory) {
        configuration.setConnectionFactory(beanNameRoutingConnectionFactory);
        SpringReactiveMybatisExecutor springReactiveMybatisExecutor = new SpringReactiveMybatisExecutor(configuration);
        return DefaultReactiveSqlSessionFactory.newBuilder()
                .withR2dbcMybatisConfiguration(configuration)
                .withReactiveMybatisExecutor(springReactiveMybatisExecutor)
                .withDefaultConnectionFactoryProxy(false)
                .build();
    }

    @Bean
    public BeanNameRoutingConnectionFactory beanNameRoutingConnectionFactory(ApplicationContext applicationContext) {
        BeanNameRoutingConnectionFactory beanNameRoutingConnectionFactory = new BeanNameRoutingConnectionFactory();
        Map<String, ConnectionFactory> beansOfType = applicationContext.getBeansOfType(ConnectionFactory.class)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new TransactionAwareConnectionFactoryProxy(entry.getValue())
                ));
        if (beansOfType.containsKey("mySqlConnectionFactory")) {
            beanNameRoutingConnectionFactory.setDefaultTargetConnectionFactory(beansOfType.get("mySqlConnectionFactory"));
        }
        beanNameRoutingConnectionFactory.setTargetConnectionFactories(beansOfType);
        beanNameRoutingConnectionFactory.setLenientFallback(false);
        beanNameRoutingConnectionFactory.afterPropertiesSet();
        return beanNameRoutingConnectionFactory;
    }

    @Bean
    public R2dbcTransactionManager connectionFactoryTransactionManager(BeanNameRoutingConnectionFactory beanNameRoutingConnectionFactory) {
        return new R2dbcTransactionManager(beanNameRoutingConnectionFactory);
    }

    @Bean(destroyMethod = "dispose")
    public ConnectionPool mySqlConnectionFactory(R2dbcMybatisConnectionFactoryProperties r2DbcMybatisConnectionFactoryProperties) {
        r2DbcMybatisConnectionFactoryProperties.setName("mysql-r2dbc");
        String determineConnectionFactoryUrl = r2DbcMybatisConnectionFactoryProperties.determineConnectionFactoryUrl();
        Assert.notNull(determineConnectionFactoryUrl, "R2DBC Connection URL must not be null");
        ConnectionFactoryOptions connectionFactoryOptions = ConnectionFactoryOptions.parse(determineConnectionFactoryUrl);
        ConnectionFactory connectionFactory = ConnectionFactories.get(connectionFactoryOptions);
        if (connectionFactory instanceof ConnectionPool) {
            return (ConnectionPool) connectionFactory;
        }
        R2dbcMybatisConnectionFactoryProperties.Pool pool = r2DbcMybatisConnectionFactoryProperties.getPool();
        ConnectionPoolConfiguration.Builder builder = ConnectionPoolConfiguration.builder(connectionFactory)
                .name(r2DbcMybatisConnectionFactoryProperties.determineConnectionFactoryName() + " -> mysql")
                .maxSize(pool.getMaxSize())
                .initialSize(pool.getInitialSize())
                .maxIdleTime(pool.getMaxIdleTime())
                .acquireRetry(pool.getAcquireRetry())
                .backgroundEvictionInterval(pool.getBackgroundEvictionInterval())
                .maxAcquireTime(pool.getMaxAcquireTime())
                .maxCreateConnectionTime(pool.getMaxCreateConnectionTime())
                .maxLifeTime(pool.getMaxLifeTime())
                .metricsRecorder(pool.getMetricsRecorder())
                .validationDepth(pool.getValidationDepth());
        if (hasText(pool.getValidationQuery())) {
            builder.validationQuery(pool.getValidationQuery());
        } else {
            builder.validationDepth(ValidationDepth.LOCAL);
        }
        ConnectionPool connectionPool = new ConnectionPool(builder.build());
        log.info("Initialize MySQL Connection Pool Success");
        return connectionPool;
    }

    @Bean(destroyMethod = "dispose")
    public ConnectionPool msSqlConnectionFactory(R2dbcMybatisConnectionFactoryProperties r2DbcMybatisConnectionFactoryProperties) {
        r2DbcMybatisConnectionFactoryProperties.setName("mssql-r2dbc");
        r2DbcMybatisConnectionFactoryProperties.setR2dbcUrl("r2dbc:mssql://127.0.0.1:1433/r2dbc");
        r2DbcMybatisConnectionFactoryProperties.setUsername("SA");
        r2DbcMybatisConnectionFactoryProperties.setPassword("Abcd@1234");
        String determineConnectionFactoryUrl = r2DbcMybatisConnectionFactoryProperties.determineConnectionFactoryUrl();
        Assert.notNull(determineConnectionFactoryUrl, "R2DBC Connection URL must not be null");
        ConnectionFactoryOptions connectionFactoryOptions = ConnectionFactoryOptions.parse(determineConnectionFactoryUrl);
        ConnectionFactory connectionFactory = ConnectionFactories.get(connectionFactoryOptions);
        if (connectionFactory instanceof ConnectionPool) {
            return (ConnectionPool) connectionFactory;
        }
        R2dbcMybatisConnectionFactoryProperties.Pool pool = r2DbcMybatisConnectionFactoryProperties.getPool();
        ConnectionPoolConfiguration.Builder builder = ConnectionPoolConfiguration.builder(connectionFactory)
                .name(r2DbcMybatisConnectionFactoryProperties.determineConnectionFactoryName() + " -> mssql")
                .maxSize(pool.getMaxSize())
                .initialSize(pool.getInitialSize())
                .maxIdleTime(pool.getMaxIdleTime())
                .acquireRetry(pool.getAcquireRetry())
                .backgroundEvictionInterval(pool.getBackgroundEvictionInterval())
                .maxAcquireTime(pool.getMaxAcquireTime())
                .maxCreateConnectionTime(pool.getMaxCreateConnectionTime())
                .maxLifeTime(pool.getMaxLifeTime())
                .metricsRecorder(pool.getMetricsRecorder())
                .validationDepth(pool.getValidationDepth());
        if (hasText(pool.getValidationQuery())) {
            builder.validationQuery(pool.getValidationQuery());
        } else {
            builder.validationDepth(ValidationDepth.LOCAL);
        }
        ConnectionPool connectionPool = new ConnectionPool(builder.build());
        log.info("Initialize MSSQL Connection Pool Success");
        return connectionPool;
    }
}
