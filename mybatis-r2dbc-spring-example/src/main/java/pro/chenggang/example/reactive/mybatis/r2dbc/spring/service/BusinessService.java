package pro.chenggang.example.reactive.mybatis.r2dbc.spring.service;

import pro.chenggang.example.reactive.mybatis.r2dbc.spring.entity.extend.DeptWithEmp;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.entity.model.Dept;
import reactor.core.publisher.Mono;

/**
 * @author Gang Cheng
 * @date 7/5/21.
 */
public interface BusinessService {

    Mono<Dept> doWithTransactionBusiness();

    Mono<Dept> doWithTransactionBusinessRollback();

    Mono<DeptWithEmp> doWithoutTransaction();

}
