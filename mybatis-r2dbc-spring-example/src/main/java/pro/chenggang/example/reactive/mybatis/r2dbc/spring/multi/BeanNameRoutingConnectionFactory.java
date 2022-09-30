package pro.chenggang.example.reactive.mybatis.r2dbc.spring.multi;

import io.r2dbc.spi.ConnectionFactoryMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import pro.chenggang.project.reactive.mybatis.support.r2dbc.executor.placeholder.dialect.MariaDBPlaceholderDialect;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * The routing connection factory based on bean name
 *
 * @author Gang Cheng
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
public class BeanNameRoutingConnectionFactory extends AbstractRoutingConnectionFactory {

    /**
     * initialize routing context
     *
     * @param context                         the original context
     * @param targetConnectionFactoryBeanName the target connection factory's bean name
     * @return the new context
     */
    public static Context initializeRoutingContext(Context context, String targetConnectionFactoryBeanName) {
        return context.put(BeanNameRoutingConnectionFactory.class, targetConnectionFactoryBeanName);
    }

    @Override
    public ConnectionFactoryMetadata getMetadata() {
        // set default metadata is mariaDB
        return () -> MariaDBPlaceholderDialect.DIALECT_NAME;
    }

    @Override
    protected Mono<Object> determineCurrentLookupKey() {
        return Mono.deferContextual(contextView -> Mono.justOrEmpty(contextView.getOrEmpty(BeanNameRoutingConnectionFactory.class)))
                .filter(contextValue -> contextValue instanceof String)
                .doOnNext(value -> log.info("Determine current lookup key : {}", value));
    }

}
