package pro.chenggang.example.reactive.mybatis.r2dbc.suite;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectModel;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.Buildable;
import pro.chenggang.example.reactive.mybatis.r2dbc.entity.model.Dept;
import pro.chenggang.example.reactive.mybatis.r2dbc.mapper.dynamic.DeptDynamicMapper;
import pro.chenggang.example.reactive.mybatis.r2dbc.mapper.dynamic.DeptDynamicSqlSupport;
import pro.chenggang.example.reactive.mybatis.r2dbc.mapper.dynamic.EmpDynamicMapper;
import pro.chenggang.example.reactive.mybatis.r2dbc.suite.setup.MybatisR2dbcBaseTests;
import pro.chenggang.project.reactive.mybatis.support.r2dbc.ReactiveSqlSession;
import pro.chenggang.project.reactive.mybatis.support.r2dbc.ReactiveSqlSessionOperator;
import pro.chenggang.project.reactive.mybatis.support.r2dbc.defaults.DefaultReactiveSqlSessionOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author Gang Cheng
 * @date 12/15/21.
 */
@Slf4j
public class DynamicMapperTests extends MybatisR2dbcBaseTests {

    private ReactiveSqlSession reactiveSqlSession;
    private DeptDynamicMapper deptDynamicMapper;
    private EmpDynamicMapper empDynamicMapper;

    private ReactiveSqlSessionOperator reactiveSqlSessionOperator;

    @BeforeAll
    public void initSqlSession () throws Exception {
        this.reactiveSqlSession = super.reactiveSqlSessionFactory.openSession();
        this.reactiveSqlSessionOperator = new DefaultReactiveSqlSessionOperator(reactiveSqlSessionFactory);
        this.deptDynamicMapper = this.reactiveSqlSession.getMapper(DeptDynamicMapper.class);
        this.empDynamicMapper = this.reactiveSqlSession.getMapper(EmpDynamicMapper.class);
    }

    @Test
    public void testGetDeptTotalCount () throws Exception {
        SelectStatementProvider ssp = new SelectStatementProvider() {
            @Override
            public Map<String, Object> getParameters() {
                Map<String, Object> map= new HashMap<>();
                map.put("empty", new Object());
                return map;
            }

            @Override
            public String getSelectStatement() {
                return "select count(*) from dept";
            }
        };

        reactiveSqlSessionOperator.execute(
                this.deptDynamicMapper.count(ssp)
        )
                .as(StepVerifier::create)
                .expectNext(4L)
                .verifyComplete();
    }


    @Test
    public void testGetAllDept () throws Exception {
        //for lambdas
        //SelectDSLCompleter dsl = selectModelQueryExpressionDSL ->
        //        selectModelQueryExpressionDSL.where(DeptDynamicSqlSupport.deptNo, SqlBuilder.isGreaterThan(0L));
        SelectDSLCompleter dsl = new SelectDSLCompleter() {
            @Override
            public Buildable<SelectModel> apply(QueryExpressionDSL<SelectModel> selectModelQueryExpressionDSL) {
                return selectModelQueryExpressionDSL.where(DeptDynamicSqlSupport.deptNo, SqlBuilder.isGreaterThan(0L));
            };
        };
        reactiveSqlSessionOperator.executeMany(
                        this.deptDynamicMapper.select(dsl)
                )
                .as(StepVerifier::create)
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testGetDeptByDeptNo () throws Exception {
        Long deptNo = 1L;

        SelectDSLCompleter dsl = new SelectDSLCompleter() {
            @Override
            public Buildable<SelectModel> apply(QueryExpressionDSL<SelectModel> selectModelQueryExpressionDSL) {
                return selectModelQueryExpressionDSL.where(DeptDynamicSqlSupport.deptNo, SqlBuilder.isEqualTo(deptNo));
            };
        };
        this.reactiveSqlSessionOperator.execute(
                //using lambdas to get same functionality
                //this.deptDynamicMapper.selectOne(selectModelQueryExpressionDSL ->
                //                        selectModelQueryExpressionDSL.where(DeptDynamicSqlSupport.deptNo, SqlBuilder.isEqualTo(deptNo)))
                this.deptDynamicMapper.selectOne(dsl)
                )
                .as(StepVerifier::create)
                .expectNextMatches(dept -> deptNo.equals(dept.getDeptNo()))
                .verifyComplete();
    }

    /**
    @Test
    public void testGetDeptListByCreateTime () throws Exception {
        LocalDateTime createTime = LocalDateTime.now();
        this.reactiveSqlSessionOperator.executeMany(
                this.deptMapper.selectListByTime(createTime)
        )
                .as(StepVerifier::create)
                .thenConsumeWhile(result -> {
                    assertThat(result)
                            .extracting(dept -> dept.getCreateTime().toLocalDate())
                            .matches(dateTime -> createTime.toLocalDate().equals(dateTime));
                    return true;
                })
                .verifyComplete();
    }
     **/

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
        System.out.println("Department instance after insertSelective is " + dept.toString());

        //Mono.just("Hello")
        Dept dept1 = new Dept();
        dept1.setDeptName("Test_dept_name");
        dept1.setCreateTime(LocalDateTime.now());
        dept1.setLocation("Test_location");
        Mono<Integer> primaryKeyMono = reactiveSqlSessionOperator.executeAndRollback(this.deptDynamicMapper.insertSelective(dept1));
        primaryKeyMono.subscribe(data -> System.out.println("printing data:" + data), // onNext
                err -> System.out.println(err),  // onError
                () -> System.out.println("Completed!") // onComplete
        );
    }

    /**

    @Test
    public void testInsertAndReturnGenerateKeyShyam() throws Exception{
        Dept dept = new Dept();
        dept.setDeptName("Test_dept_name");
        dept.setCreateTime(LocalDateTime.now());
        dept.setLocation("Test_location");
        reactiveSqlSessionOperator.executeAndRollback(this.deptMapperShyam.insertOne1(dept))
                .log()
                .as(StepVerifier::create)
                .expectNextMatches(effectRowCount -> effectRowCount == 1)
                .verifyComplete();
        assertThat(dept.getDeptNo()).isNotNull();
    }

    @Test
    public void testInsertBySelectKeyAndReturnGenerateKey() throws Exception{
        Dept dept = new Dept();
        dept.setDeptName("Test_dept_name");
        dept.setCreateTime(LocalDateTime.now());
        dept.setLocation("Test_location");
        reactiveSqlSessionOperator.executeAndRollback(this.deptMapper.insertUseSelectKey(dept))
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
                this.deptMapper.insertOne(dept)
                        .then(Mono.defer(() -> this.deptMapper.deleteByDeptNo(dept.getDeptNo())))
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
        reactiveSqlSessionOperator.executeAndRollback(this.deptMapper.updateByDeptNo(dept))
                .as(StepVerifier::create)
                .expectNextMatches(effectRowCount -> effectRowCount == 1)
                .verifyComplete();
    }

    @Test
    public void testGetDeptWithEmp() throws Exception {
        this.reactiveSqlSessionOperator.executeMany(
                this.deptMapper.selectDeptWithEmpList()
        )
                .as(StepVerifier::create)
                .expectNextMatches(deptWithEmp -> {
                    assertThat(deptWithEmp)
                            .extracting(DeptWithEmp::getEmpList)
                            .matches(empList -> empList.size() >0 );
                    return true;
                })
                .expectNextCount(2L)
                .expectNextMatches(deptWithEmp -> {
                    assertThat(deptWithEmp)
                            .extracting(DeptWithEmp::getEmpList)
                            .matches(empList -> empList.size() ==0 );
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testGetEmpWithDept() throws Exception {
        this.reactiveSqlSessionOperator.executeMany(
                this.empMapper.selectEmpWithDeptList()
        )
                .as(StepVerifier::create)
                .thenConsumeWhile(empWithDept -> {
                    assertThat(empWithDept.getDept()).isNotNull();
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testGetEmpByParameterMap() throws Exception {
        Emp emp = new Emp();
        emp.setCreateTime(LocalDateTime.now());
        this.reactiveSqlSessionOperator.executeMany(
                this.empMapper.selectByParameterMap(emp)
        )
                .as(StepVerifier::create)
                .expectNextCount(14)
                .verifyComplete();
    }
    **/
}
