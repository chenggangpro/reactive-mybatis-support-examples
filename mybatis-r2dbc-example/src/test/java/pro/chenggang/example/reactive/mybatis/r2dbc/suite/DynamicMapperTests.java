package pro.chenggang.example.reactive.mybatis.r2dbc.suite;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import pro.chenggang.example.reactive.mybatis.r2dbc.entity.model.Dept;
import pro.chenggang.example.reactive.mybatis.r2dbc.mapper.DeptMapper;
import pro.chenggang.example.reactive.mybatis.r2dbc.mapper.EmpMapper;
import pro.chenggang.example.reactive.mybatis.r2dbc.mapper.dynamic.*;
import pro.chenggang.example.reactive.mybatis.r2dbc.suite.setup.MybatisR2dbcBaseTests;
import pro.chenggang.project.reactive.mybatis.support.r2dbc.ReactiveSqlSession;
import pro.chenggang.project.reactive.mybatis.support.r2dbc.ReactiveSqlSessionOperator;
import pro.chenggang.project.reactive.mybatis.support.r2dbc.defaults.DefaultReactiveSqlSessionOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
public class DynamicMapperTests extends MybatisR2dbcBaseTests {

    private ReactiveSqlSession reactiveSqlSession;
    private DeptMapper deptDynamicMapper;
    private EmpMapper empDynamicMapper;

    private ReactiveSqlSessionOperator reactiveSqlSessionOperator;

    @BeforeAll
    public void initSqlSession () throws Exception {
        this.reactiveSqlSession = super.reactiveSqlSessionFactory.openSession();
        this.reactiveSqlSessionOperator = new DefaultReactiveSqlSessionOperator(reactiveSqlSessionFactory);
        this.deptDynamicMapper = this.reactiveSqlSession.getMapper(DeptMapper.class);
        this.empDynamicMapper = this.reactiveSqlSession.getMapper(EmpMapper.class);
    }

    @Test
    public void testGetDeptTotalCount () throws Exception {
        SelectStatementProvider selectStatementProvider = SqlBuilder.select(SqlBuilder.count())
                .from(DeptDynamicSqlSupport.dept)
                .build()
                .render(RenderingStrategies.MYBATIS3);
        this.reactiveSqlSessionOperator.execute(deptDynamicMapper.count(selectStatementProvider)
                )
                .as(StepVerifier::create)
                .expectNext(4L)
                .verifyComplete();

    }

    @Test
    public void testGetAllDept () throws Exception {

        SelectStatementProvider selectStatementProvider = SqlBuilder.select(DeptDynamicMapper.selectList)
                .from(DeptDynamicSqlSupport.dept)
                .build()
                .render(RenderingStrategies.MYBATIS3);

        reactiveSqlSessionOperator.executeMany(
                        this.deptDynamicMapper.selectMany(selectStatementProvider)
                )
                .as(StepVerifier::create)
                .expectNextCount(4)
                .verifyComplete();

    }

    @Test
    public void testGetDeptByDeptNo () throws Exception {
        Long deptNo = 1L;

        SelectStatementProvider selectStatementProvider = SqlBuilder.select(DeptDynamicMapper.selectList)
                .from(DeptDynamicSqlSupport.dept)
                .where(DeptDynamicSqlSupport.deptNo, SqlBuilder.isEqualTo(deptNo))
                .build()
                .render(RenderingStrategies.MYBATIS3);

        reactiveSqlSessionOperator.executeMany(
                        this.deptDynamicMapper.selectMany(selectStatementProvider)
                )
                .as(StepVerifier::create)
                .expectNextMatches(dept -> deptNo.equals(dept.getDeptNo()))
                .verifyComplete();

    }

    @Test
    public void testGetDeptListByCreateTime () throws Exception {
        LocalDateTime createTime = LocalDateTime.now();
        Long deptNo = 1L;

        SelectStatementProvider selectStatementProvider = SqlBuilder.select(DeptDynamicMapper.selectList)
                .from(DeptDynamicSqlSupport.dept)
                .where(DeptDynamicSqlSupport.createTime, SqlBuilder.isLessThan(createTime))
                .build()
                .render(RenderingStrategies.MYBATIS3);

        reactiveSqlSessionOperator.executeMany(
                        this.deptDynamicMapper.selectMany(selectStatementProvider)
                )
                .as(StepVerifier::create)
                .expectNextCount(4)
                .thenConsumeWhile(result -> {
                    assertThat(result)
                            .extracting(dept -> dept.getCreateTime().toLocalDate())
                            .matches(dateTime -> createTime.toLocalDate().isAfter(dateTime));
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testInsertAndReturnGenerateKey() throws Exception{
        Dept dept = new Dept();
        dept.setDeptName("Test_dept_name");
        dept.setCreateTime(LocalDateTime.now());
        dept.setLocation("Test_location");
        reactiveSqlSessionOperator.executeAndRollback(this.deptDynamicMapper.insertSelective(dept))
                .as(StepVerifier::create)
                .expectNextMatches(effectRowCount -> effectRowCount == 1)
                .verifyComplete();
        assertThat(dept.getDeptNo()).isNotNull();
    }

    // TODO insertOne does not seem to work.
    @Test
    public void testInsertOneAndReturnGenerateKey() throws Exception{
        Dept dept = new Dept();
        dept.setDeptName("Test_dept_name");
        dept.setCreateTime(LocalDateTime.now());
        dept.setLocation("Test_location");
        reactiveSqlSessionOperator.executeAndRollback(this.deptDynamicMapper.insertOne(dept))
                .as(StepVerifier::create)
                .expectNextMatches(effectRowCount -> effectRowCount == 1)
                .verifyComplete();
        assertThat(dept.getDeptNo()).isNotNull();
    }

    @Test
    public void testDeleteByDeptNo() throws Exception {
        Dept dept = new Dept();
        dept.setDeptName("Test_dept_name");
        dept.setCreateTime(LocalDateTime.now());
        dept.setLocation("Test_location");

        reactiveSqlSessionOperator.executeAndRollback(
                this.deptDynamicMapper.insertSelective(dept)
                        .then(Mono.defer(() -> {
                            DeleteStatementProvider deleteStatementProvider = SqlBuilder.deleteFrom(DeptDynamicSqlSupport.dept)
                                    .where(DeptDynamicSqlSupport.deptNo, SqlBuilder.isEqualTo(dept.getDeptNo()))
                                    .build()
                                    .render(RenderingStrategies.MYBATIS3);

                            return this.deptDynamicMapper.delete(deleteStatementProvider);
                        }))
        )
                .as(StepVerifier::create)
                .expectNextMatches(effectRowCount -> effectRowCount == 1)
                .verifyComplete();
    }


    @Test
    public void testUpdateByDeptNo() throws Exception {
        Dept dept = new Dept();
        dept.setDeptNo(1L);
        dept.setDeptName("Update_dept_name");
        reactiveSqlSessionOperator.executeAndRollback(this.deptDynamicMapper.updateSelectiveByPrimaryKey(dept))
                .as(StepVerifier::create)
                .expectNextMatches(effectRowCount -> effectRowCount == 1)
                .verifyComplete();
    }

    @Test
    public void testGetDeptWithEmp() throws Exception {
        BasicColumn[] selectColumns = BasicColumn.columnList(DeptDynamicSqlSupport.deptNo,
                DeptDynamicSqlSupport.deptName,
                DeptDynamicSqlSupport.location,
                DeptDynamicSqlSupport.createTime,
                EmpDynamicSqlSupport.empNo.as("emp_emp_no"),
                EmpDynamicSqlSupport.empName.as("emp_emp_name"),
                EmpDynamicSqlSupport.job.as("emp_job"),
                EmpDynamicSqlSupport.manager.as("emp_manager"),
                EmpDynamicSqlSupport.hireDate.as("emp_hire_date"),
                EmpDynamicSqlSupport.salary.as("emp_salary"),
                EmpDynamicSqlSupport.kpi.as("emp_kpi"),
                EmpDynamicSqlSupport.deptNo.as("emp_dept_no"),
                EmpDynamicSqlSupport.createTime.as("emp_create_time")
        );
        // Due to @ResultMap("pro.chenggang.example.reactive.mybatis.r2dbc.mapper.DeptMapper.DeptWithEmp") 's mapping definition in xml,
        // I set column alias manually for `ResultMap`
        SelectStatementProvider selectStatementProvider = SqlBuilder.select(selectColumns)
                .from(DeptDynamicSqlSupport.dept)
                .leftJoin(EmpDynamicSqlSupport.emp, SqlBuilder.on(DeptDynamicSqlSupport.deptNo, SqlBuilder.equalTo(EmpDynamicSqlSupport.deptNo)))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        AtomicInteger recordCount = new AtomicInteger();
        this.reactiveSqlSessionOperator.executeMany(
                        this.deptDynamicMapper.selectManyDeptWithEmp(selectStatementProvider)
                )
                .as(StepVerifier::create)
                .thenConsumeWhile(deptWithEmp -> {
                    recordCount.getAndIncrement();
                    log.info("printing deptWithEmp: " + deptWithEmp);
                    Assertions.assertThat(deptWithEmp.getDeptNo()).isNotNull();
                    return true;
                })
                .verifyComplete();
        assertEquals(4 , recordCount.get());
    }

    @Test
    public void testGetEmpWithDept() throws Exception {
        BasicColumn[] selectColumns = BasicColumn.columnList(
                DeptDynamicSqlSupport.deptNo.as("dept_dept_no"),
                DeptDynamicSqlSupport.deptName.as("dept_dept_name"),
                DeptDynamicSqlSupport.location.as("dept_location"),
                DeptDynamicSqlSupport.createTime.as("dept_create_time"),
                EmpDynamicSqlSupport.empNo,
                EmpDynamicSqlSupport.empName,
                EmpDynamicSqlSupport.job,
                EmpDynamicSqlSupport.manager,
                EmpDynamicSqlSupport.hireDate,
                EmpDynamicSqlSupport.salary,
                EmpDynamicSqlSupport.kpi,
                EmpDynamicSqlSupport.deptNo,
                EmpDynamicSqlSupport.createTime
        );

        SelectStatementProvider selectStatementProvider = SqlBuilder.select(selectColumns)
                .from(EmpDynamicSqlSupport.emp)
                .leftJoin(DeptDynamicSqlSupport.dept, SqlBuilder.on(EmpDynamicSqlSupport.deptNo, SqlBuilder.equalTo(DeptDynamicSqlSupport.deptNo)))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        AtomicInteger recordCount = new AtomicInteger();
        this.reactiveSqlSessionOperator.executeMany(
                        this.empDynamicMapper.selectManyEmpWithDept(selectStatementProvider)
                )
                .as(StepVerifier::create)
                .thenConsumeWhile(empWithDept -> {
                    recordCount.getAndIncrement();
                    log.info("printing deptWithEmp: " + empWithDept);
                    Assertions.assertThat(empWithDept.getDeptNo()).isNotNull();
                    return true;
                })
                .verifyComplete();
        assertEquals(14 , recordCount.get());
    }

    @AfterAll
    public static void tearDown(){
        container.stop();
    }
}
