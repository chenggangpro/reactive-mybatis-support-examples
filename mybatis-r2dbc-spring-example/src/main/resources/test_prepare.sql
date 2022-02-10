CREATE TABLE IF NOT EXISTS dept
(
    dept_no     serial4 NOT NULL,
    dept_name   varchar(64)  NOT NULL,
    location    varchar(100) NOT NULL,
    create_time timestamp without time zone NOT NULL,
    PRIMARY KEY (dept_no)
)
;

INSERT INTO dept VALUES ('1', 'ACCOUNTING', 'NEW YORK', NOW());
INSERT INTO dept VALUES ('2', 'RESEARCH', 'DALLAS', NOW());
INSERT INTO dept VALUES ('3', 'SALES', 'CHICAGO', NOW());
INSERT INTO dept VALUES ('4', 'OPERATIONS', 'BOSTON', NOW());


CREATE TABLE IF NOT EXISTS emp
(
    emp_no      serial4 NOT NULL,
    emp_name    varchar(64)     NOT NULL,
    job         varchar(100)    NOT NULL,
    manager     varchar(100)    NULL DEFAULT NULL,
    hire_date   date            NOT NULL,
    salary      INT             NOT NULL,
    kpi         DECIMAL(3, 2)   NOT NULL,
    dept_no     INT NOT NULL,
    create_time timestamp without time zone    NOT NULL,
    PRIMARY KEY (emp_no),
    CONSTRAINT FK_DEPTNO FOREIGN KEY (dept_no) REFERENCES dept (dept_no)
)
;

INSERT INTO emp VALUES ('1', 'SMITH', 'CLERK', '13', '1980-12-17', '800', '0.82','2',NOW());
INSERT INTO emp VALUES ('2', 'ALLEN', 'SALESMAN', '6', '1981-02-20', '1600', '0.57', '3',NOW());
INSERT INTO emp VALUES ('3', 'WARD', 'SALESMAN', '6', '1981-02-22', '1250', '0.73', '3',NOW());
INSERT INTO emp VALUES ('4', 'JONES', 'MANAGER', '9', '1981-04-02', '2975', '0.94', '2',NOW());
INSERT INTO emp VALUES ('5', 'MARTIN', 'SALESMAN', '6', '1981-09-28', '1250', '0.83', '3',NOW());
INSERT INTO emp VALUES ('6', 'BLAKE', 'MANAGER', '9', '1981-05-01', '2850', '0.50', '3',NOW());
INSERT INTO emp VALUES ('7', 'CLARK', 'MANAGER', '9', '1981-06-09', '2450', '0.69', '1',NOW());
INSERT INTO emp VALUES ('8', 'SCOTT', 'ANALYST', '4', '1987-04-19', '3000', '0.47', '2',NOW());
INSERT INTO emp VALUES ('9', 'KING', 'PRESIDENT', null, '1981-11-17', '5000', '1.00', '1',NOW());
INSERT INTO emp VALUES ('10', 'TURNER', 'SALESMAN', '6', '1981-09-08', '1500', '0.52', '3',NOW());
INSERT INTO emp VALUES ('11', 'ADAMS', 'CLERK', '8', '1987-05-23', '1100', '0.74', '2',NOW());
INSERT INTO emp VALUES ('12', 'JAMES', 'CLERK', '6', '1981-12-03', '950', '0.91', '3',NOW());
INSERT INTO emp VALUES ('13', 'FORD', 'ANALYST', '4', '1981-12-03', '3000', '1.00', '2',NOW());
INSERT INTO emp VALUES ('14', 'MILLER', 'CLERK', '7', '1982-01-23', '1300', '0.99', '1',NOW());