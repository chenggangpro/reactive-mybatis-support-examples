package pro.chenggang.example.reactive.mybatis.r2dbc.entity.extend;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pro.chenggang.example.reactive.mybatis.r2dbc.entity.model.Dept;
import pro.chenggang.example.reactive.mybatis.r2dbc.entity.model.Emp;

/**
 * @author Gang Cheng
 * @date 12/15/21.
 */
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmpWithDept extends Emp {

    private Dept dept;
}
