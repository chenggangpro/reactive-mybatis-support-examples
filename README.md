# reactive-mybatis-support-examples

`reactive-mybatis-support` latest version :  [![Pom Maven Central](https://maven-badges.herokuapp.com/maven-central/pro.chenggang/reactive-mybatis-support/badge.svg)](https://maven-badges.herokuapp.com/maven-central/pro.chenggang/reactive-mybatis-support)

##### This is reactive-mybatis-support's examples

1. `mybatis-r2dbc-example`

* simple example with mybatis and mybatis-dynamic-sql

2. mybatis-r2dbc-spring-example

* spring example with mybatis and mybatis-dynamic-sql

#### Notice
* Both `mybatis-dynamic-sql` is optional if you do not want to use it
* Test cases 
  * use `test-prepare.sql` to setup schema 
  * branch :
    * `master` : use MySQL database and `r2dbc-mariadb` as `r2dbc`'s driver dependency
    * `oracle` : use Oracle database and `r2dbc-oracle` as `r2dbc`'s driver dependency
    * `postgresql` : use PostgreSQL database and `r2dbc-postgresql` as `r2dbc`'s driver dependency
    * `mssql` : use SQL-Server database and `r2dbc-mssql` as `r2dbc`'s driver dependency
    * `h2` : use H2 database and `r2dbc-h2` as `r2dbc`'s driver dependency