package pro.chenggang.example.reactive.mybatis.r2dbc.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.entity.model.Dept;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.mapper.DeptMapper;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.mapper.EmpMapper;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.multi.BeanNameRoutingConnectionFactory;
import reactor.test.StepVerifier;

import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

/**
 * @author evans
 * @version 1.0.0
 * @since 1.0.0
 */
public class MultiDatabaseTests extends ReactiveMybatisSupportR2dbcSpringApplicationTests{

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private EmpMapper empMapper;

    @Test
    public void multiConnectionTest() throws Exception{
        Dept dept = new Dept();
        dept.setDeptNo(1L);
        dept.setDeptName("Update_dept_name");
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread threadA = new Thread(() -> {
            try {
                this.deptMapper.updateByDeptNo(dept)
                        .as(this::withRollback)
                        .contextWrite(context -> BeanNameRoutingConnectionFactory.initializeRoutingContext(context, "mySqlConnectionFactory"))
                        .as(StepVerifier::create)
                        .expectNextMatches(effectRowCount -> effectRowCount == 1)
                        .verifyComplete();
            } finally {
                countDownLatch.countDown();
            }
        });
        Thread threadB = new Thread(() -> {
            try {
                this.deptMapper.updateByDeptNo(dept)
                        .as(this::withRollback)
                        .contextWrite(context -> BeanNameRoutingConnectionFactory.initializeRoutingContext(context, "msSqlConnectionFactory"))
                        .as(StepVerifier::create)
                        .expectNextMatches(effectRowCount -> effectRowCount == 1)
                        .verifyComplete();
            } finally {
                countDownLatch.countDown();
            }
        });
        Stream.of(threadA,threadB)
                .parallel()
                .forEach(Thread::start);
        countDownLatch.await();
    }
}
