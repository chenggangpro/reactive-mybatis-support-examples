package pro.chenggang.example.reactive.mybatis.r2dbc.spring.service;

import reactor.core.publisher.Mono;

/**
 * @author Gang Cheng
 * @date 1/4/22.
 * @version 1.0.0
 */
public interface BenchmarkService {

    Mono<?> getFromDb();

    Mono<?> getFromDb(String id);

    Mono<?> insertDb();

    Mono<?> insertThenDeleteDb();
}
