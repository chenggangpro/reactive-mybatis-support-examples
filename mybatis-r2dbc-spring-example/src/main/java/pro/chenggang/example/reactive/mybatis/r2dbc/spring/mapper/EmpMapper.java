package pro.chenggang.example.reactive.mybatis.r2dbc.spring.mapper;

import org.apache.ibatis.annotations.Mapper;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.entity.extend.EmpWithDept;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.mapper.dynamic.EmpDynamicMapper;
import reactor.core.publisher.Flux;

/**
 * auto generated
 * @author AutoGenerated
 */
@Mapper
public interface EmpMapper extends EmpDynamicMapper {

    Flux<EmpWithDept> selectEmpWithDeptList();
}