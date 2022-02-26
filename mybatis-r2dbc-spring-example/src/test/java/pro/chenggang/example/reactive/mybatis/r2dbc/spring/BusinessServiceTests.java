package pro.chenggang.example.reactive.mybatis.r2dbc.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.entity.extend.DeptWithEmp;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.service.BusinessService;
import reactor.test.StepVerifier;

import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Gang Cheng
 * @date 7/6/21.
 */
public class BusinessServiceTests extends ReactiveMybatisSupportR2dbcSpringApplicationTests {

    @Autowired
    private BusinessService businessService;

    @Test
    public void testDoWithoutTransaction() throws Exception{
        businessService.doWithoutTransaction()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void testDoWithTransactionBusiness(){
        businessService.doWithTransactionBusiness()
                .as(this::withRollback)
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void testDoWithTransactionBusinessRollback() throws Exception{
        businessService.doWithTransactionBusinessRollback()
                .as(this::withRollback)
                .as(StepVerifier::create)
                .expectErrorMatches(throwable -> {
                    return "manually rollback with @Transaction".equals(throwable.getMessage());
                })
                .verify();
    }

    @Test
    public void testDoWithoutTransactionThenWithTransaction(){
        businessService.doWithoutTransaction()
                .flatMap(deptWithEmp -> {
                    assertThat(deptWithEmp)
                            .extracting(DeptWithEmp::getEmpList)
                            .matches(empList -> empList.size() >0 );
                    return businessService.doWithTransactionBusinessRollback();
                })
                .as(StepVerifier::create)
                .expectErrorMatches(throwable -> {
                    return "manually rollback with @Transaction".equals(throwable.getMessage());
                })
                .verify();
    }
}
