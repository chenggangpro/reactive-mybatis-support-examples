CREATE TABLE dept
(
    [dept_no]     BIGINT check ([dept_no] > 0) NOT NULL IDENTITY ,
    [dept_name]   varchar(64)         NOT NULL ,
    [location]    varchar(100)        NOT NULL ,
    [create_time] datetime2(0)            NOT NULL ,
    PRIMARY KEY ([dept_no])
    )
;

-- Records of dept

INSERT INTO dept (dept_name,location,create_time)VALUES ( 'ACCOUNTING', 'NEW YORK', GETDATE());
INSERT INTO dept (dept_name,location,create_time)VALUES ( 'RESEARCH', 'DALLAS', GETDATE());
INSERT INTO dept (dept_name,location,create_time)VALUES ( 'SALES', 'CHICAGO', GETDATE());
INSERT INTO dept (dept_name,location,create_time)VALUES ( 'OPERATIONS', 'BOSTON', GETDATE());

-- SQLINES DEMO *** or emp

-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE emp
(
    [emp_no]      BIGINT check ([emp_no] > 0) NOT NULL IDENTITY ,
    [emp_name]    varchar(64)         NOT NULL ,
    [job]         varchar(100)        NOT NULL ,
    [manager]     varchar(100)        NULL DEFAULT NULL ,
    [hire_date]   date                NOT NULL ,
    [salary]      INT      NOT NULL ,
    [kpi]      DECIMAL(3,2)      NOT NULL ,
    [dept_no]     BIGINT check ([dept_no] > 0) NOT NULL ,
    [create_time] datetime2(0)            NOT NULL ,
    PRIMARY KEY ([emp_no])
    ,
    CONSTRAINT FK_DEPTNO FOREIGN KEY (dept_no) REFERENCES dept (dept_no)
    )
;

CREATE INDEX FK_DEPTNO ON emp (dept_no);

-- Records of emp
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time) VALUES ('SMITH', 'CLERK', '13', '1980-12-17', '800', '0.82','2',GETDATE());
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time) VALUES ('ALLEN', 'SALESMAN', '6', '1981-02-20', '1600', '0.57', '3',GETDATE());
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time) VALUES ('WARD', 'SALESMAN', '6', '1981-02-22', '1250', '0.73', '3',GETDATE());
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time) VALUES ('JONES', 'MANAGER', '9', '1981-04-02', '2975', '0.94', '2',GETDATE());
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time) VALUES ('MARTIN', 'SALESMAN', '6', '1981-09-28', '1250', '0.83', '3',GETDATE());
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time) VALUES ('BLAKE', 'MANAGER', '9', '1981-05-01', '2850', '0.50', '3',GETDATE());
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time) VALUES ('CLARK', 'MANAGER', '9', '1981-06-09', '2450', '0.69', '1',GETDATE());
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time) VALUES ('SCOTT', 'ANALYST', '4', '1987-04-19', '3000', '0.47', '2',GETDATE());
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time) VALUES ('KING', 'PRESIDENT', null, '1981-11-17', '5000', '1.00', '1',GETDATE());
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time) VALUES ( 'TURNER', 'SALESMAN', '6', '1981-09-08', '1500', '0.52', '3',GETDATE());
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time) VALUES ( 'ADAMS', 'CLERK', '8', '1987-05-23', '1100', '0.74', '2',GETDATE());
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time) VALUES ( 'JAMES', 'CLERK', '6', '1981-12-03', '950', '0.91', '3',GETDATE());
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time) VALUES ( 'FORD', 'ANALYST', '4', '1981-12-03', '3000', '1.00', '2',GETDATE());
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time) VALUES ( 'MILLER', 'CLERK', '7', '1982-01-23', '1300', '0.99', '1',GETDATE());