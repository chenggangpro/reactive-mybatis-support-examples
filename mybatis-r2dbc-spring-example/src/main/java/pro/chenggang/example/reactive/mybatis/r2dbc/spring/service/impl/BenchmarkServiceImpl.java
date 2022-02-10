package pro.chenggang.example.reactive.mybatis.r2dbc.spring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.entity.model.Dept;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.mapper.DeptMapper;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.service.BenchmarkService;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Gang Cheng
 * @date 1/4/22.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BenchmarkServiceImpl implements BenchmarkService {

    private final DeptMapper deptMapper;

    @Override
    public Mono<?> getFromDb() {
        return deptMapper.selectDeptWithEmpList()
                .collectList();
    }

    @Override
    public Mono<?> getFromDb(String id) {
        return deptMapper.selectOneByDeptNo(Integer.parseInt(id))
                .cast(Object.class)
                .switchIfEmpty(Mono.defer(() -> Mono.just("data not exist")));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<?> insertDb() {
        Dept dept = new Dept();
        dept.setDeptName(UUID.randomUUID().toString());
        dept.setCreateTime(LocalDateTime.now());
        dept.setLocation(UUID.randomUUID().toString());
        return deptMapper.insertSelective(dept)
                .flatMap(insertResult -> Mono.just("Generated DeptNo: " + dept.getDeptNo()));
    }

    @Transactional(rollbackFor = Exception.class,isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public Mono<?> insertThenDeleteDb() {
        Dept dept = new Dept();
        dept.setDeptName(UUID.randomUUID().toString());
        dept.setCreateTime(LocalDateTime.now());
        dept.setLocation(UUID.randomUUID().toString());
        return deptMapper.insertSelective(dept)
                .flatMap(insertResult -> deptMapper.deleteByDeptNo(dept.getDeptNo())
                        .flatMap(deleteResult -> deptMapper.count()
                                .map(countResult -> "TotalCount : " + countResult))
                );
    }
}
