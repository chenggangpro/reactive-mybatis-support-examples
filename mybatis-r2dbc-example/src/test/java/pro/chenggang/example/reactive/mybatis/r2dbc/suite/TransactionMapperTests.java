package pro.chenggang.example.reactive.mybatis.r2dbc.suite;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pro.chenggang.example.reactive.mybatis.r2dbc.entity.model.Dept;
import pro.chenggang.example.reactive.mybatis.r2dbc.mapper.DeptMapper;
import pro.chenggang.example.reactive.mybatis.r2dbc.mapper.EmpMapper;
import pro.chenggang.example.reactive.mybatis.r2dbc.suite.setup.MybatisR2dbcBaseTests;
import pro.chenggang.project.reactive.mybatis.support.r2dbc.ReactiveSqlSession;
import pro.chenggang.project.reactive.mybatis.support.r2dbc.ReactiveSqlSessionOperator;
import pro.chenggang.project.reactive.mybatis.support.r2dbc.defaults.DefaultReactiveSqlSessionOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Gang Cheng
 * @date 12/15/21.
 */
public class TransactionMapperTests extends MybatisR2dbcBaseTests {

    private DeptMapper deptMapper;
    private EmpMapper empMapper;
    private ReactiveSqlSession reactiveSqlSession;
    private ReactiveSqlSessionOperator reactiveSqlSessionOperator;

    @BeforeAll
    public void initSqlSession () throws Exception {
        this.reactiveSqlSession = super.reactiveSqlSessionFactory.openSession().usingTransaction(true);
        this.deptMapper = this.reactiveSqlSession.getMapper(DeptMapper.class);
        this.empMapper = this.reactiveSqlSession.getMapper(EmpMapper.class);
        this.reactiveSqlSessionOperator = new DefaultReactiveSqlSessionOperator(reactiveSqlSessionFactory);
    }

    @Test
    public void testManuallyCommit() throws Exception {
        Dept dept = new Dept();
        dept.setDeptName("Test_dept_name");
        dept.setCreateTime(LocalDateTime.now());
        dept.setLocation("Test_location");
        Mono<Long> commitExecution = this.deptMapper.countAll()
                .flatMap(totalCount -> {
                    assertThat(totalCount).isEqualTo(4);
                    return this.deptMapper.insertOne(dept);
                })
                .flatMap(effectRowCount -> {
                    assertThat(effectRowCount).isEqualTo(1);
                    return this.deptMapper.countAll();
                });
        this.reactiveSqlSessionOperator.executeAndCommit(commitExecution)
                .as(StepVerifier::create)
                .expectNext(5L)
                .verifyComplete();
        Mono<Long> deleteExecution = this.deptMapper.countAll()
                .flatMap(totalCount -> {
                    assertThat(totalCount).isEqualTo(5);
                    return this.deptMapper.deleteByDeptNo(dept.getDeptNo());
                })
                .flatMap(effectRowCount -> {
                    assertThat(effectRowCount).isEqualTo(1);
                    return this.deptMapper.countAll();
                });
        this.reactiveSqlSessionOperator.executeAndCommit(deleteExecution)
                .as(StepVerifier::create)
                .expectNext(4L)
                .verifyComplete();
    }

    @Test
    public void testManuallyRollback() throws Exception {
        Dept dept = new Dept();
        dept.setDeptName("Test_dept_name");
        dept.setCreateTime(LocalDateTime.now());
        dept.setLocation("Test_location");
        Mono<Long> execution = this.deptMapper.countAll()
                .flatMap(totalCount -> {
                    assertThat(totalCount).isEqualTo(4);
                    return this.deptMapper.insertOne(dept);
                })
                .flatMap(effectRowCount -> {
                    assertThat(effectRowCount).isEqualTo(1);
                    return this.deptMapper.countAll();
                });
        this.reactiveSqlSessionOperator.executeAndRollback(execution)
                .as(StepVerifier::create)
                .expectNext(5L)
                .verifyComplete();
        this.reactiveSqlSessionOperator.executeAndRollback(this.deptMapper.countAll())
                .as(StepVerifier::create)
                .expectNext(4L)
                .verifyComplete();
    }


}
