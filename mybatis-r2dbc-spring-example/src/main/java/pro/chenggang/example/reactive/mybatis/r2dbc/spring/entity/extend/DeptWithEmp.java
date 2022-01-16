package pro.chenggang.example.reactive.mybatis.r2dbc.spring.entity.extend;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.entity.model.Dept;
import pro.chenggang.example.reactive.mybatis.r2dbc.spring.entity.model.Emp;

import java.util.List;

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
public class DeptWithEmp extends Dept {

    private List<Emp> empList;
}
