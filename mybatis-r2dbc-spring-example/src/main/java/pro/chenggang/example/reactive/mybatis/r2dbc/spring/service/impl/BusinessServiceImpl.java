package pro.chenggang.example.reactive.mybatis.r2dbc.spring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.entity.extend.DeptWithEmp;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.entity.model.Dept;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.mapper.DeptMapper;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.service.BusinessService;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static pro.chenggang.example.reactive.mybatis.r2dbc.spring.mapper.dynamic.DeptDynamicSqlSupport.deptNo;

/**
 * @author Gang Cheng
 * @date 7/5/21.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final DeptMapper deptMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<Dept> doWithTransactionBusiness() {
        return this.doBusinessInternal();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<Dept> doWithTransactionBusinessRollback() {
        return this.doBusinessInternal()
                .then(Mono.defer(() -> {
                    if(true){
                        throw new RuntimeException("manually rollback with @Transaction");
                    }
                    return Mono.empty();
                }));
    }

    @Override
    public Mono<DeptWithEmp> doWithoutTransaction() {
        return deptMapper.count()
                .filter(count -> count > 0)
                .flatMap(count -> deptMapper.selectDeptWithEmpList()
                        .take(1,true)
                        .singleOrEmpty()
                );

    }

    private Mono<Dept> doBusinessInternal(){
        return deptMapper.selectOne(dsl -> dsl.where(deptNo, isEqualTo(4)))
                .doOnNext(people -> log.debug("[Before] Get People ,People:{}",people))
                .flatMap(people -> deptMapper.updateSelective(new Dept()
                        .setDeptName("InsertDept")
                        .setLocation("InsertLocation")
                        .setCreateTime(LocalDateTime.now()),
                        dsl -> dsl.where(deptNo,isEqualTo(4))))
                .flatMap(value -> deptMapper.selectOne(dsl -> dsl.where(deptNo, isEqualTo(4))))
                .doOnNext(updatePeople -> log.debug("[After Update] Get People ,People:{}",updatePeople))
                .flatMap(updatePeople -> deptMapper.delete(dsl -> dsl.where(deptNo, isEqualTo(4))))
                .flatMap(deleteResult -> deptMapper.selectOne(dsl -> dsl.where(deptNo, isEqualTo(4))));
    }
}
