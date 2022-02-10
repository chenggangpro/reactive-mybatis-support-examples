package pro.chenggang.example.reactive.mybatis.r2dbc.mapper.dynamic;

import java.sql.JDBCType;
import java.time.LocalDateTime;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DeptDynamicSqlSupport {
    public static final Dept dept = new Dept();

    public static final SqlColumn<Integer> deptNo = dept.deptNo;

    public static final SqlColumn<String> deptName = dept.deptName;

    public static final SqlColumn<String> location = dept.location;

    public static final SqlColumn<LocalDateTime> createTime = dept.createTime;

    public static final class Dept extends SqlTable {
        public final SqlColumn<Integer> deptNo = column("dept_no", JDBCType.INTEGER);

        public final SqlColumn<String> deptName = column("dept_name", JDBCType.VARCHAR);

        public final SqlColumn<String> location = column("location", JDBCType.VARCHAR);

        public final SqlColumn<LocalDateTime> createTime = column("create_time", JDBCType.TIMESTAMP);

        public Dept() {
            super("dept");
        }
    }
}