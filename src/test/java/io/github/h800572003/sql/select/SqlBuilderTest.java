package io.github.h800572003.sql.select;

import io.github.h800572003.sql.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static io.github.h800572003.sql.SqlBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class SqlBuilderTest {


    @Test
    void test_select_cmd1() {

        String sql = SelectBuilder.newSelect().select("*")
                .from("XXX")
                .createWhere(getParameter("value1", SqlOption.EQ, write(":value1")))
                .and(getParameter("value2", SqlOption.EQ, write(":value2")))
                .build().toUpperCase();

        log.info("sql:{}", sql);
        assertEquals(sql, "select * from XXX where value1 = :value1 AND value2 = :value2".toUpperCase());

    }

    @Test
    void test_select_all() {

        String sql = SelectBuilder.newSelect().selectAll()
                .from("XXX")
                .createWhere(getParameter("value1", SqlOption.EQ, write(":value1")))
                .and(getParameter("value2", SqlOption.EQ, write(":value2")))
                .build().toUpperCase();

        log.info("sql:{}", sql);
        assertEquals(sql, "select * from XXX where value1 = :value1 AND value2 = :value2".toUpperCase());

    }

    @Test
    void test_select_cmd1_with() {

        String sql = SelectBuilder.newSelect().select("*")
                .from("XXX")
                .createWhere(getParameter("value1", SqlOption.EQ, write(":value1")))
                .or(getParameter("value2", SqlOption.EQ, write(":value2")))
                .build().toUpperCase();

        log.info("sql:{}", sql);
        assertEquals(sql, "select * from XXX where value1 = :value1 OR value2 = :value2".toUpperCase());

    }

    @Test
    void test_select_cmd3() {

        String sql = SelectBuilder.newSelect().select("*")
                .from("XXX")
                .createWhere(getParameter("value1", SqlOption.EQ, write(":value1")))
                .and(getParameter("value2", SqlOption.EQ, write(":value2")))//
                .back()//
                .createOrderBy()//
                .add(write("value1"))//
                .add(write("value2"))//
                .isDesc()
                .build().toUpperCase();
        log.info("sql:{}", sql);
        assertEquals(sql, "select * from XXX where value1 = :value1 AND value2 = :value2 order by value1,value2 desc ".toUpperCase());

    }

    @Test
    void test_select_cmd4() {

        String sql = SelectBuilder.newSelect().createSelect()
                .body("value1")
                .body("value2")
                .back()
                .from("XXX")
                .createWhere(getParameter("value1", SqlOption.EQ, write(":value1")))
                .and(getParameter("value2", SqlOption.EQ, write(":value2")))//
                .back()//
                .createOrderBy()//
                .add(write("value1"))//
                .add(write("value2"))//

                .build().toUpperCase();
        log.info("sql:{}", sql);
        assertEquals(sql, "select value1,value2 from XXX where value1 = :value1 AND value2 = :value2 order by value1,value2".toUpperCase());

    }

    @Test
    void test_select_cmd5() {

        String sql = SelectBuilder.newSelect().createSelect()
                .body("value1")
                .body("value2")
                .back()
                .from("XXX")
                .createWhere(getParameter("value1", SqlOption.EQ, write(":value1")))
                .and(getParameter("value2", SqlOption.EQ, write(":value2")))//
                .and(getParameter("value2", SqlOption.IN, commaClad(":value2", ":value1")))//
                .back()//
                .createOrderBy()//
                .add(write("value1"))//
                .add(write("value2"))//
                .isDesc()
                .build().toUpperCase();
        log.info("sql:{}", sql);
        assertEquals(sql, "select value1,value2 from XXX where value1 = :value1 AND value2 = :value2 and value2 in (:value2,:value1) order by value1,value2 desc ".toUpperCase());

    }

    @Test
    void testSubQuery() {


        String sql = SelectBuilder.newSelect()
                .select("*")
                .from("student s").createWhere(
                        getParameter("id", SqlOption.IN, clad(SelectBuilder.newSelect().select("id").from("student")))
                ).build().toUpperCase();
        log.info("sql:{}", sql);

        assertEquals(sql, "select * from student s where id in (select id from student)".toUpperCase());


    }

    @Test
    void testJoin() {

        String sql = SelectBuilder.newSelect().select("s1.*").from("student s1")
                .createJoin()
                .join(SqlJoin.JoinType.JOIN, "student s2")
                .on(getParameter("s1.id", SqlOption.EQ, write("s2.id")))
                .back()
                .createWhere(getParameter("s1.id", SqlOption.EQ, quotation("B1234")))
                .build().toUpperCase();


        log.info("sql:{}", sql);

        final String ansSql = "select s1.* from student s1 join student s2 on s1.id = s2.id where s1.id = 'B1234'";
        assertEquals(sql, ansSql.toUpperCase());

    }


    @Test
    void testGroup() {
        String sql = SelectBuilder.newSelect()
                .createSelect()
                .add(max("id"))
                .back()
                .from("student")
                .groupBy("id", "name")
                .build().toUpperCase();
        log.info("sql:{}", sql);

        final String groupBy = "select max (id) from student group by id,name";

        assertEquals(sql, groupBy.toUpperCase());
    }


    @Test
    void testGroupWithSame() {

        ISql select = comma("id", "name");

        String sql = SelectBuilder.newSelect()
                .select(select)
                .from("student")
                .groupBy(write(select, write("")))
                .build().toUpperCase();
        log.info("sql:{}", sql);

        final String groupBy = "select id,name from student group by id,name";

        assertEquals(sql, groupBy.toUpperCase());
    }

    @Test
    void testGroup2_isql() {
        String sql = SelectBuilder.newSelect()
                .createSelect()
                .add(max("ID"))
                .back()
                .from("STUDENT")
                .groupBy(
                        comma(write("ID"),
                                write(write("NAME")
                                )))
                .build().toUpperCase();
        log.info("sql:{}", sql);
        final String groupBy = "select max (id) from student group by id,name";
        assertEquals(sql, groupBy.toUpperCase());
    }

    @Test
    void testDistinct() {
        String sql = SelectBuilder.newSelect()
                .createSelect()
                .add(distinct("id"))
                .back()
                .from("student")
                .build().toUpperCase();
        log.info("sql:{}", sql);

        final String groupBy = "select distinct id from student";


        assertEquals(sql, groupBy.toUpperCase());
    }

    @Test
    void testCreatSelect() {
        String sql = SelectBuilder.newSelect()
                .createSelect()
                .body("id")
                .body("name")
                .back()
                .from(write("student"))
                .build().toUpperCase();
        log.info("sql:{}", sql);

        final String ans = "select id,name from student";

        assertEquals(sql, ans.toUpperCase());
    }

    /**
     * 取消where 條件
     */
    @Test
    void testCreatSelectOptionNoShow() {
        String sql = SelectBuilder.newSelect()
                .createSelect()
                .body("id")
                .body("name")
                .back()
                .from(write("student"))
                .createWhere(getParameter("id", SqlOption.EQ, quotation("abc")), () -> false)
                .build().toUpperCase();
        log.info("sql:{}", sql);

        final String ans = "select id,name from student";

        assertEquals(sql, ans.toUpperCase());
    }

    /**
     * 顯示Where
     */
    @Test
    void testCreatSelectRequire() {


        String sql = SelectBuilder.newSelect()
                .createSelect()
                .body("id")
                .body("name")
                .back()
                .from(write("student"))
                .createWhere(getParameter("id", SqlOption.EQ, quotation("idA")), () -> true)
                .and(getParameter("nameX", SqlOption.EQ, quotation("nameXB")), () -> false)
                .and(getParameter("name", SqlOption.EQ, quotation("nameX")), () -> true)
                .build().toUpperCase();
        log.info("sql:{}", sql);

        final String ans = "select id,name from student where id = 'idA' and name = 'nameX'";

        assertEquals(sql, ans.toUpperCase());
    }

    @Test
    void testCreatSelectWithoutWhere() {
        String sql = SelectBuilder.newSelect()
                .createSelect()
                .body("id")
                .body("name")
                .back()
                .from(write("student"))
                .createWhere(getParameter("id", SqlOption.EQ, quotation("idA")), () -> false)
                .and(getParameter("nameX", SqlOption.EQ, quotation("nameXB")), () -> false)
                .and(getParameter("name", SqlOption.EQ, quotation("nameX")), () -> false)
                .build().toUpperCase();
        log.info("sql:{}", sql);

        final String ans = "select id,name from student";

        assertEquals(sql, ans.toUpperCase());
    }

    /**
     * 測試is null
     */
    @Test
    void testSqlIsNotNull() {

        String sql = SelectBuilder.newSelect()
                .select("*")
                .from("student")
                .createWhere(getParameter("id", SqlOption.IS_NOT, Selects.NULL))
                .build().toUpperCase();


        String ans = "select * from student where id is not null ";
        log.info("sql:{}", sql);

        assertTrue(SqlEqualUtils.isEqual(ans, sql));
    }

    @Test
    void testSqlIsNull() {

        String sql = SelectBuilder.newSelect()
                .select("*")
                .from("student")
                .createWhere(getParameter("id", SqlOption.IS, Selects.NULL))
                .build().toUpperCase();


        String ans = "select * from student where id is  null ";
        log.info("sql:{}", sql);
        assertEquals(sql, ans.toUpperCase());
    }

    @Test
    void test_append() {
        String sql = SelectBuilder.newSelect().append("HELLO")
                .append(" TOM ")
                .build().toUpperCase();

        String ans = "HELLO TOM ";
        log.info("sql:{}", sql);
        assertEquals(sql, ans.toUpperCase());
    }

    @Test
    public void test_with_as_name() {
        ValueParameter parameter = getParameter("value", "A", SqlOption.EQ, write("?"));


        String ans = "value A = ?";
        log.info("sql:{}", parameter.toString());
        assertEquals(parameter.toString().toUpperCase(), ans.toUpperCase());
    }

    @Test
    public void test_where_and_all() {
        ValueParameter parameterA = getParameter("id", SqlOption.IS, Selects.NULL);
        ValueParameter parameterB = getParameter("name", SqlOption.IS_NOT, Selects.NULL);

        String sql = SelectBuilder.newSelect()
                .createSelect()
                .addAll("id", "name").back()
                .from("student")
                .whereAllAnd(
                        parameterA, parameterB
                ).build().toUpperCase();


        final String ans = "select id,name from student where id is  null and name is not null";

        assertTrue(SqlEqualUtils.isEqual(ans, sql));


    }

    @Test
    public void test_where_or_all() {
        ValueParameter parameterA = getParameter("id", SqlOption.IS, Selects.NULL);
        ValueParameter parameterB = getParameter("name", SqlOption.IS_NOT, Selects.NULL);

        String sql = SelectBuilder.newSelect()
                .createSelect()
                .addAll("id", "name").back()
                .from("student")
                .whereAllOr(
                        parameterA, parameterB
                ).build().toUpperCase();


        final String ans = "select id,name from student where id is  null or name is not null";

        assertTrue(SqlEqualUtils.isEqual(ans, sql));


    }

    @Test
    public void test_where() {
        String sql = SelectBuilder.newSelect()
                .createSelect()
                .addAll("id", "name").back()
                .from("student")
                .createWhere(
                        getParameter("id1", SqlOption.EQ, write("?")), () -> false)
                .and(getParameter("id2", SqlOption.EQ, write("?")), () -> true)
                .and(getParameter("id3", SqlOption.EQ, write("?")), () -> false)
                .and(getParameter("id4", SqlOption.EQ, write("?")), () -> true)
                .build();


        final String all = "select id,name from student where id1 =? and) id2 = ? and id3 = ? and id4 = ? ";
        final String ans = "select id,name from student where  id2 = ?  and id4 = ? ";
        assertTrue(SqlEqualUtils.isEqual(ans, sql));

    }

    @Test
    public void test_where2() {
        String sql = SelectBuilder.newSelect()
                .createSelect()
                .addAll("id", "name").back()
                .from("student")
                .createWhere(
                        getParameter("id1", SqlOption.EQ, write("?")), () -> false)
                .and(getParameter("id2", SqlOption.EQ, write("?")), () -> true)
                .and(getParameter("id3", SqlOption.EQ, write("?")), () -> true)
                .or(getParameter("id4", SqlOption.EQ, write("?")), () -> true)
                .build();


        final String ans = "select id,name from student where  id2 = ?  and id3 = ? or id4 = ? ";
        assertTrue(SqlEqualUtils.isEqual(ans, sql));

    }

    /**
     * 測試整合body，提升靈活度
     */
    @Test
    public void test_body() {

        final String sql="select a,b,c from table where id1=? and id2=?";

        ISql iSql = body().//
                addWithSpace(
                SelectBuilder
                        .newSelect()
                        .createSelect()
                        .add("a")
                        .add("b")
                        .add("c")
                        .back()
                        .from("table")
                        .createWhere(SqlBuilder.body().addWithSpace("id1").addWithSpace("=?"),()->true)
                        .and(SqlBuilder.body().addWithSpace("id2").addWithSpace("=?"),()->true)
                        .back()



        );
        System.out.print(iSql);

    }
}
