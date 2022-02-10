package pro.chenggang.example.reactive.mybatis.r2dbc.spring.mapper.dynamic;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class EmpDynamicSqlSupport {
    public static final Emp emp = new Emp();

    public static final SqlColumn<Integer> empNo = emp.empNo;

    public static final SqlColumn<String> empName = emp.empName;

    public static final SqlColumn<String> job = emp.job;

    public static final SqlColumn<String> manager = emp.manager;

    public static final SqlColumn<LocalDate> hireDate = emp.hireDate;

    public static final SqlColumn<Integer> salary = emp.salary;

    public static final SqlColumn<BigDecimal> kpi = emp.kpi;

    public static final SqlColumn<Integer> deptNo = emp.deptNo;

    public static final SqlColumn<LocalDateTime> createTime = emp.createTime;

    public static final class Emp extends SqlTable {
        public final SqlColumn<Integer> empNo = column("emp_no", JDBCType.INTEGER);

        public final SqlColumn<String> empName = column("emp_name", JDBCType.VARCHAR);

        public final SqlColumn<String> job = column("job", JDBCType.VARCHAR);

        public final SqlColumn<String> manager = column("manager", JDBCType.VARCHAR);

        public final SqlColumn<LocalDate> hireDate = column("hire_date", JDBCType.DATE);

        public final SqlColumn<Integer> salary = column("salary", JDBCType.INTEGER);

        public final SqlColumn<BigDecimal> kpi = column("kpi", JDBCType.NUMERIC);

        public final SqlColumn<Integer> deptNo = column("dept_no", JDBCType.INTEGER);

        public final SqlColumn<LocalDateTime> createTime = column("create_time", JDBCType.TIMESTAMP);

        public Emp() {
            super("emp");
        }
    }
}