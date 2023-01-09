package io.github.h800572003.sql.select;

import io.github.h800572003.sql.ISql;
import io.github.h800572003.sql.Selects;
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
                .isDesc()
                .build().toUpperCase();
        log.info("sql:{}", sql);
        Assertions.assertEquals(sql, "select * from XXX where value1 = :value1 AND value2 = :value2 order by value1,value2 desc ".toUpperCase());

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

                .build().toUpperCase();
        log.info("sql:{}", sql);
        Assertions.assertEquals(sql, "select value1,value2 from XXX where value1 = :value1 AND value2 = :value2 order by value1,value2".toUpperCase());

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
                .isDesc()
                .build().toUpperCase();
        log.info("sql:{}", sql);
        Assertions.assertEquals(sql, "select value1,value2 from XXX where value1 = :value1 AND value2 = :value2 and value2 in (:value2,:value1) order by value1,value2 desc ".toUpperCase());

    }

    @Test
    void testSubQuery() {


        String sql = SelectBuilder.newSelect()
                .select("*")
                .from("student s").where(
                        SqlBuilder.getParameter("id", SqlOption.IN, SqlBuilder.clad(SelectBuilder.newSelect().select("id").from("student")))
                ).build().toUpperCase();
        log.info("sql:{}", sql);

        Assertions.assertEquals(sql, "select * from student s where id in (select id from student)".toUpperCase());


    }

    @Test
    void testJoin() {

        String sql = SelectBuilder.newSelect().select("s1.*").from("student s1")
                .createJoin()
                .join(SqlJoin.JoinType.JOIN, "student s2")
                .on(SqlBuilder.getParameter("s1.id", SqlOption.EQ, SqlBuilder.write("s2.id")))
                .back()
                .where(SqlBuilder.getParameter("s1.id", SqlOption.EQ, SqlBuilder.quotation("B1234")))
                .build().toUpperCase();


        log.info("sql:{}", sql);

        final String ansSql = "select s1.* from student s1 join student s2 on s1.id = s2.id where s1.id = 'B1234'";
        Assertions.assertEquals(sql, ansSql.toUpperCase());

    }


    @Test
    void testGroup() {
        String sql = SelectBuilder.newSelect()
                .createSelect()
                .add(SqlBuilder.max("id"))
                .back()
                .from("student")
                .groupBy("id", "name")
                .build().toUpperCase();
        log.info("sql:{}", sql);

        final String groupBy = "select max (id) from student group by id,name";


        Assertions.assertEquals(sql, groupBy.toUpperCase());
    }

    @Test
    void testDistinct() {
        String sql = SelectBuilder.newSelect()
                .createSelect()
                .add(SqlBuilder.distinct("id"))
                .back()
                .from("student")
                .build().toUpperCase();
        log.info("sql:{}", sql);

        final String groupBy = "select distinct id from student";


        Assertions.assertEquals(sql, groupBy.toUpperCase());
    }

    @Test
    void testCreatSelect() {
        String sql = SelectBuilder.newSelect()
                .createSelect()
                .add("id")
                .add("name")
                .back()
                .from(SqlBuilder.write("student"))
                .build().toUpperCase();
        log.info("sql:{}", sql);

        final String ans = "select id,name from student";

        Assertions.assertEquals(sql, ans.toUpperCase());
    }

    /**
     * 取消where 條件
     */
    @Test
    void testCreatSelectOptionNoShow() {
        String sql = SelectBuilder.newSelect()
                .createSelect()
                .add("id")
                .add("name")
                .back()
                .from(SqlBuilder.write("student"))
                .where(SqlBuilder.getParameter("id",SqlOption.EQ,SqlBuilder.quotation("abc")),()->false)
                .build().toUpperCase();
        log.info("sql:{}", sql);

        final String ans = "select id,name from student";

        Assertions.assertEquals(sql, ans.toUpperCase());
    }

    /**
     * 顯示Where
     */
    @Test
    void testCreatSelectRequire() {
        String sql = SelectBuilder.newSelect()
                .createSelect()
                .add("id")
                .add("name")
                .back()
                .from(SqlBuilder.write("student"))
                .where(SqlBuilder.getParameter("id",SqlOption.EQ,SqlBuilder.quotation("idA")),()->true)
                .and(SqlBuilder.getParameter("nameX",SqlOption.EQ,SqlBuilder.quotation("nameXB")),()->false)
                .and(SqlBuilder.getParameter("name",SqlOption.EQ,SqlBuilder.quotation("nameX")),()->true)
                .build().toUpperCase();
        log.info("sql:{}", sql);

        final String ans = "select id,name from student where id = 'idA' and name = 'nameX'";

        Assertions.assertEquals(sql, ans.toUpperCase());
    }

    @Test
    void testCreatSelectWithoutWhere() {
        String sql = SelectBuilder.newSelect()
                .createSelect()
                .add("id")
                .add("name")
                .back()
                .from(SqlBuilder.write("student"))
                .where(SqlBuilder.getParameter("id",SqlOption.EQ,SqlBuilder.quotation("idA")),()->false)
                .and(SqlBuilder.getParameter("nameX",SqlOption.EQ,SqlBuilder.quotation("nameXB")),()->false)
                .and(SqlBuilder.getParameter("name",SqlOption.EQ,SqlBuilder.quotation("nameX")),()->false)
                .build().toUpperCase();
        log.info("sql:{}", sql);

        final String ans = "select id,name from student";

        Assertions.assertEquals(sql, ans.toUpperCase());
    }

    /**
     * 測試is null
     */
    @Test
    void testSqlIsNotNull() {

        String sql = SelectBuilder.newSelect()
                .select("*")
                .from("student")
                .where(SqlBuilder.getParameter("id",SqlOption.IS_NOT, Selects.NULL))
                .build().toUpperCase();


        String ans = "select * from student where id is not null ";
        log.info("sql:{}", sql);
        Assertions.assertEquals(sql, ans.toUpperCase());
    }
    @Test
    void testSqlIsNull() {

        String sql = SelectBuilder.newSelect()
                .select("*")
                .from("student")
                .where(SqlBuilder.getParameter("id",SqlOption.IS, Selects.NULL))
                .build().toUpperCase();


        String ans = "select * from student where id is  null ";
        log.info("sql:{}", sql);
        Assertions.assertEquals(sql, ans.toUpperCase());
    }
}
