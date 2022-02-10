package pro.chenggang.example.reactive.mybatis.r2dbc.mapper.dynamic;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.where.WhereApplier;
import pro.chenggang.example.reactive.mybatis.r2dbc.entity.model.Dept;
import pro.chenggang.project.reactive.mybatis.support.r2dbc.dynamic.CommonSelectMapper;
import pro.chenggang.project.reactive.mybatis.support.r2dbc.dynamic.ReactiveMyBatis3Utils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static pro.chenggang.example.reactive.mybatis.r2dbc.mapper.dynamic.DeptDynamicSqlSupport.createTime;
import static pro.chenggang.example.reactive.mybatis.r2dbc.mapper.dynamic.DeptDynamicSqlSupport.dept;
import static pro.chenggang.example.reactive.mybatis.r2dbc.mapper.dynamic.DeptDynamicSqlSupport.deptName;
import static pro.chenggang.example.reactive.mybatis.r2dbc.mapper.dynamic.DeptDynamicSqlSupport.deptNo;
import static pro.chenggang.example.reactive.mybatis.r2dbc.mapper.dynamic.DeptDynamicSqlSupport.location;

/**
 * auto generated
 *
 * @author AutoGenerated
 */
@Mapper
public interface DeptDynamicMapper extends CommonSelectMapper {
    BasicColumn[] selectList = BasicColumn.columnList(deptNo, deptName, location, createTime);

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    Mono<Long> count(SelectStatementProvider selectStatement);

    @DeleteProvider(type = SqlProviderAdapter.class, method = "delete")
    Mono<Integer> delete(DeleteStatementProvider deleteStatement);

    @InsertProvider(type = SqlProviderAdapter.class, method = "insert")
    @SelectKey(before = false, keyProperty = "record.deptNo", statement = "SELECT currval('dept_dept_no_seq')" ,resultType = Integer.class)
    @Options(useGeneratedKeys = true, keyProperty = "record.deptNo")
    Mono<Integer> insert(InsertStatementProvider<Dept> insertStatement);

    @InsertProvider(type = SqlProviderAdapter.class, method = "insertMultiple")
    Mono<Integer> insertMultiple(MultiRowInsertStatementProvider<Dept> multipleInsertStatement);

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultMap("DeptResult")
    Mono<Dept> selectOne(SelectStatementProvider selectStatement);

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @Results(id = "DeptResult", value = {
            @Result(column = "dept_no", property = "deptNo", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "dept_name", property = "deptName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "location", property = "location", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP)
    })
    Flux<Dept> selectMany(SelectStatementProvider selectStatement);

    @UpdateProvider(type = SqlProviderAdapter.class, method = "update")
    Mono<Integer> update(UpdateStatementProvider updateStatement);

    default Mono<Long> count(CountDSLCompleter completer) {
        return ReactiveMyBatis3Utils.countFrom(this::count, dept, completer);
    }

    default Mono<Integer> delete(DeleteDSLCompleter completer) {
        return ReactiveMyBatis3Utils.deleteFrom(this::delete, dept, completer);
    }

    default Mono<Integer> insert(Dept record) {
        return ReactiveMyBatis3Utils.insert(this::insert, record, dept, c ->
                c.map(deptNo).toProperty("deptNo")
                        .map(deptName).toProperty("deptName")
                        .map(location).toProperty("location")
                        .map(createTime).toProperty("createTime")
        );
    }

    default Mono<Integer> insertMultiple(Collection<Dept> records) {
        return ReactiveMyBatis3Utils.insertMultiple(this::insertMultiple, records, dept, c ->
                c.map(deptNo).toProperty("deptNo")
                        .map(deptName).toProperty("deptName")
                        .map(location).toProperty("location")
                        .map(createTime).toProperty("createTime")
        );
    }

    default Mono<Integer> insertSelective(Dept record) {
        return ReactiveMyBatis3Utils.insert(this::insert, record, dept, c ->
                c.map(deptNo).toPropertyWhenPresent("deptNo", record::getDeptNo)
                        .map(deptName).toPropertyWhenPresent("deptName", record::getDeptName)
                        .map(location).toPropertyWhenPresent("location", record::getLocation)
                        .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
        );
    }

    default Mono<Dept> selectOne(SelectDSLCompleter completer) {
        return ReactiveMyBatis3Utils.selectOne(this::selectOne, selectList, dept, completer);
    }

    default Flux<Dept> select(SelectDSLCompleter completer) {
        return ReactiveMyBatis3Utils.selectList(this::selectMany, selectList, dept, completer);
    }

    default Flux<Dept> selectDistinct(SelectDSLCompleter completer) {
        return ReactiveMyBatis3Utils.selectDistinct(this::selectMany, selectList, dept, completer);
    }

    default Mono<Integer> update(UpdateDSLCompleter completer) {
        return ReactiveMyBatis3Utils.update(this::update, dept, completer);
    }

    default Mono<Integer> updateSelectiveByPrimaryKey(Dept record) {
        return update(c ->
                c.set(deptName).equalToWhenPresent(record::getDeptName)
                        .set(location).equalToWhenPresent(record::getLocation)
                        .set(createTime).equalToWhenPresent(record::getCreateTime)
                        .where(deptNo, isEqualTo(record::getDeptNo))
        );
    }

    default Mono<Integer> updateAllByPrimaryKey(Dept record) {
        return update(c ->
                c.set(deptName).equalToWhenPresent(record::getDeptName)
                        .set(location).equalToWhenPresent(record::getLocation)
                        .set(createTime).equalToWhenPresent(record::getCreateTime)
                        .where(deptNo, isEqualTo(record::getDeptNo))
        );
    }

    default Mono<Integer> updateAll(Dept record, WhereApplier whereApplier) {
        return update(c ->
                c.set(deptName).equalToWhenPresent(record::getDeptName)
                        .set(location).equalToWhenPresent(record::getLocation)
                        .set(createTime).equalToWhenPresent(record::getCreateTime)
                        .applyWhere(whereApplier)
        );
    }

    default Mono<Integer> updateSelective(Dept record, WhereApplier whereApplier) {
        return update(c ->
                c.set(deptName).equalToWhenPresent(record::getDeptName)
                        .set(location).equalToWhenPresent(record::getLocation)
                        .set(createTime).equalToWhenPresent(record::getCreateTime)
                        .applyWhere(whereApplier)
        );
    }
}