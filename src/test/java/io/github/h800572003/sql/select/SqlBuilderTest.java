package io.github.h800572003.sql.select;

import io.github.h800572003.sql.SqlBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class SqlBuilderTest {



    @Test
    void test_select_cmd1() {

        String sql = SelectBuilder.newSelect().select("*")
                .from("XXX")
                .where(SqlBuilder.getParameter("value1", SqlOption.EQ, SqlBuilder.write(":value1")))
                .and(SqlBuilder.getParameter("value2", SqlOption.EQ, SqlBuilder.write(":value2")))
                .build().toUpperCase();

        log.info("sql:{}", sql);
        Assertions.assertEquals(sql, "select * from XXX where value1 = :value1 AND value2 = :value2".toUpperCase());

    }

    @Test
    void test_select_cmd1_with() {

        String sql = SelectBuilder.newSelect().select("*")
                .from("XXX")
                .where(SqlBuilder.getParameter("value1", SqlOption.EQ, SqlBuilder.write(":value1")))
                .or(SqlBuilder.getParameter("value2", SqlOption.EQ, SqlBuilder.write(":value2")))
                .build().toUpperCase();

        log.info("sql:{}", sql);
        Assertions.assertEquals(sql, "select * from XXX where value1 = :value1 OR value2 = :value2".toUpperCase());

    }

    @Test
    void test_select_cmd3() {

        String sql = SelectBuilder.newSelect().select("*")
                .from("XXX")
                .where(SqlBuilder.getParameter("value1", SqlOption.EQ, SqlBuilder.write(":value1")))
                .and(SqlBuilder.getParameter("value2", SqlOption.EQ, SqlBuilder.write(":value2")))//
                .back()//
                .orderBy()//
                .add(SqlBuilder.write("value1"))//
                .add(SqlBuilder.write("value2"))//
                .isAsc()
                .build().toUpperCase();
        log.info("sql:{}", sql);
        Assertions.assertEquals(sql, "select * from XXX where value1 = :value1 AND value2 = :value2 order by value1,value2 asc ".toUpperCase());

    }

    @Test
    void test_select_cmd4() {

        String sql = SelectBuilder.newSelect().createSelect()
                .add("value1")
                .add("value2")
                .back()
                .from("XXX")
                .where(SqlBuilder.getParameter("value1", SqlOption.EQ, SqlBuilder.write(":value1")))
                .and(SqlBuilder.getParameter("value2", SqlOption.EQ, SqlBuilder.write(":value2")))//
                .back()//
                .orderBy()//
                .add(SqlBuilder.write("value1"))//
                .add(SqlBuilder.write("value2"))//
                .isAsc()
                .build().toUpperCase();
        log.info("sql:{}", sql);
        Assertions.assertEquals(sql, "select value1,value2 from XXX where value1 = :value1 AND value2 = :value2 order by value1,value2 asc ".toUpperCase());

    }

    @Test
    void test_select_cmd5() {

        String sql = SelectBuilder.newSelect().createSelect()
                .add("value1")
                .add("value2")
                .back()
                .from("XXX")
                .where(SqlBuilder.getParameter("value1", SqlOption.EQ, SqlBuilder.write(":value1")))
                .and(SqlBuilder.getParameter("value2", SqlOption.EQ, SqlBuilder.write(":value2")))//
                .and(SqlBuilder.getParameter("value2", SqlOption.IN, SqlBuilder.commaClad(":value2", ":value1")))//
                .back()//
                .orderBy()//
                .add(SqlBuilder.write("value1"))//
                .add(SqlBuilder.write("value2"))//
                .isAsc()
                .build().toUpperCase();
        log.info("sql:{}", sql);
        Assertions.assertEquals(sql, "select value1,value2 from XXX where value1 = :value1 AND value2 = :value2 and value2 in (:value2,:value1) order by value1,value2 asc ".toUpperCase());

    }

    @Test
    void testSubQuery() {


        String sql = SelectBuilder.newSelect()
                .select("*")
                .from("student s").where(
                        SqlBuilder.getParameter("id", SqlOption.IN, SqlBuilder.clad(    SelectBuilder.newSelect().select("id").from("student")))
                ).build().toUpperCase();
        log.info("sql:{}", sql);

        Assertions.assertEquals(sql, "select * from student s where id in (select id from student)".toUpperCase());


    }

    @Test
    void testJoin() {

        String sql = SelectBuilder.newSelect().select("s1.*").from("student s1")
                .createJoin()
                .join(SqlJoin.JoinType.JOIN,"student s2")
                .on(SqlBuilder.getParameter("s1.id", SqlOption.EQ, SqlBuilder.write("s2.id")))
                .back()
                .where(SqlBuilder.getParameter("s1.id", SqlOption.EQ, SqlBuilder.quotation("B1234")))
                .build().toUpperCase();




        log.info("sql:{}", sql);

        final String ansSql = "select s1.* from student s1 join student s2 on s1.id = s2.id where s1.id = 'B1234'";
        Assertions.assertEquals(sql, ansSql.toUpperCase());

    }
}