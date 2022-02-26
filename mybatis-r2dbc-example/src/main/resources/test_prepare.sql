CREATE TABLE dept
(
    dept_no     NUMBER(19)  NOT NULL ,
    dept_name   varchar2(64)         NOT NULL ,
    location    varchar2(100)        NOT NULL ,
    create_time timestamp(0)            NOT NULL ,
    PRIMARY KEY (dept_no)
)
;


-- Generate ID using sequence and trigger
CREATE SEQUENCE dept_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER dept_seq_tr
                                             BEFORE INSERT ON dept FOR EACH ROW
 WHEN (NEW.dept_no IS NULL)
BEGIN
SELECT dept_seq.NEXTVAL INTO:NEW.dept_no FROM DUAL;
END;



-- Records of dept

INSERT INTO dept (dept_name,location,create_time) VALUES ('ACCOUNTING', 'NEW YORK', SYSTIMESTAMP);
INSERT INTO dept (dept_name,location,create_time) VALUES ('RESEARCH', 'DALLAS', SYSTIMESTAMP);
INSERT INTO dept (dept_name,location,create_time) VALUES ('SALES', 'CHICAGO', SYSTIMESTAMP);
INSERT INTO dept (dept_name,location,create_time) VALUES ('OPERATIONS', 'BOSTON', SYSTIMESTAMP);

-- SQLINES DEMO *** or emp

-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE emp
(
    emp_no      NUMBER(19) check (emp_no > 0) NOT NULL ,
    emp_name    varchar2(64)         NOT NULL ,
    job         varchar2(100)        NOT NULL ,
    manager     varchar2(100) DEFAULT NULL        NULL ,
    hire_date   date                NOT NULL ,
    salary      NUMBER(10)      NOT NULL ,
    kpi      NUMBER(3,2)      NOT NULL ,
    dept_no     NUMBER(19) check (dept_no > 0) NOT NULL ,
    create_time timestamp(0)            NOT NULL ,
    PRIMARY KEY (emp_no)
    ,
    CONSTRAINT FK_DEPTNO FOREIGN KEY (dept_no) REFERENCES dept (dept_no)
)
;


-- Generate ID using sequence and trigger
CREATE SEQUENCE emp_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER emp_seq_tr
                                            BEFORE INSERT ON emp FOR EACH ROW
 WHEN (NEW.emp_no IS NULL)
BEGIN
SELECT emp_seq.NEXTVAL INTO:NEW.emp_no FROM DUAL;
END;


CREATE INDEX FK_DEPTNO ON emp (dept_no);

-- Records of emp
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time)VALUES ('SMITH', 'CLERK', '13', DATE'1980-12-17', '800', '0.82','2',SYSTIMESTAMP);
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time)VALUES ('ALLEN', 'SALESMAN', '6', DATE'1981-02-20', '1600', '0.57', '3',SYSTIMESTAMP);
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time)VALUES ('WARD', 'SALESMAN', '6', DATE'1981-02-22', '1250', '0.73', '3',SYSTIMESTAMP);
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time)VALUES ('JONES', 'MANAGER', '9', DATE'1981-04-02', '2975', '0.94', '2',SYSTIMESTAMP);
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time)VALUES ('MARTIN', 'SALESMAN', '6', DATE'1981-09-28', '1250', '0.83', '3',SYSTIMESTAMP);
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time)VALUES ('BLAKE', 'MANAGER', '9', DATE'1981-05-01', '2850', '0.50', '3',SYSTIMESTAMP);
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time)VALUES ('CLARK', 'MANAGER', '9', DATE'1981-06-09', '2450', '0.69', '1',SYSTIMESTAMP);
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time)VALUES ('SCOTT', 'ANALYST', '4', DATE'1987-04-19', '3000', '0.47', '2',SYSTIMESTAMP);
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time)VALUES ('KING', 'PRESIDENT', null, DATE'1981-11-17', '5000', '1.00', '1',SYSTIMESTAMP);
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time)VALUES ( 'TURNER', 'SALESMAN', '6', DATE'1981-09-08', '1500', '0.52', '3',SYSTIMESTAMP);
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time)VALUES ( 'ADAMS', 'CLERK', '8', DATE'1987-05-23', '1100', '0.74', '2',SYSTIMESTAMP);
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time)VALUES ( 'JAMES', 'CLERK', '6', DATE'1981-12-03', '950', '0.91', '3',SYSTIMESTAMP);
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time)VALUES ( 'FORD', 'ANALYST', '4', DATE'1981-12-03', '3000', '1.00', '2',SYSTIMESTAMP);
INSERT INTO emp (emp_name,job,manager,hire_date,salary,kpi,dept_no,create_time)VALUES ( 'MILLER', 'CLERK', '7', DATE'1982-01-23', '1300', '0.99', '1',SYSTIMESTAMP);