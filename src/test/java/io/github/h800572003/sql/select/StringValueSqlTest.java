package io.github.h800572003.sql.select;

import io.github.h800572003.sql.ISql;
import io.github.h800572003.sql.SqlBuilder;
import io.github.h800572003.sql.SqlEqual;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class StringValueSqlTest {


    @Test
    void testWrute() {


        //GIVE
        ISql abc = SqlBuilder.write("ABC");

        //WHEN
        String value = abc.toString();
        log.info("value:{}", value);
        Assertions.assertEquals("ABC", value);
    }


    @Test
    void test2Text() {


        //GIVE
        ISql abc = SqlBuilder.write("ABC", "CDE");

        //WHEN
        String value = abc.toString();
        log.info("value:{}", value);
        Assertions.assertEquals("ABCCDE", value);
    }

    @Test
    void test3Text() {


        //GIVE
        ISql abc = SqlBuilder.comma("ABC", "CDE");

        //WHEN
        String value = abc.toString();
        log.info("value:{}", value);
        Assertions.assertEquals("ABC,CDE", value);
    }

    @Test
    void test4Text() {


        //GIVE
        ISql abc = SqlBuilder.quotationAndComma("ABC", "CDE");

        //WHEN
        String value = abc.toString();
        log.info("value:{}", value);
        Assertions.assertEquals("'ABC','CDE'", value);
    }

    @Test
    void test5Text() {


        //GIVE
        ISql abc = SqlBuilder.clad(SqlBuilder.quotationAndComma("ABC", "CDE"));

        //WHEN
        String value = abc.toString();
        log.info("value:{}", value);
        Assertions.assertEquals("('ABC','CDE')", value);
    }

    @Test
    void test6Text() {
        //GIVE
        ISql build = SqlBuilder.createValue()
                .clad()
                .comma()
                .quotation()
                .build("ABC", "CDE");

        //WHEN
        String value = build.toString();
        log.info("value:{}", value);
        Assertions.assertEquals("('ABC','CDE')", value);
    }

    @Test
    void test_union(){

        ISql sql = SelectBuilder.newSelect().select("id").from("student");
        ISql sql1 = SelectBuilder.newSelect().select("id").from("student");

        ISql sql2 = SqlBuilder.union(sql, sql1);
        log.info("value:{}", sql2);
        String ans="select id from student union select id from student";

        Assertions.assertEquals(ans.toUpperCase(), sql2.toString().toUpperCase());
    }


    @Test
    void test_union_all(){

        ISql sql = SelectBuilder.newSelect().select("id").from("student");
        ISql sql1 = SelectBuilder.newSelect().select("id").from("student");

        ISql sql2 = SqlBuilder.unionAll(sql, sql1);
        log.info("value:{}", sql2);
        String ans="SELECT ID FROM STUDENT UNION ALL SELECT ID FROM STUDENT";

        Assertions.assertEquals(ans.toUpperCase(), sql2.toString().toUpperCase());
    }

    @Test
    void teat_select(){
        ISql sql = SelectBuilder.newSelect().
                createSelect().addAll("ID","NAME").back()//
                .from("STUDENT");


        String ans="SELECT ID,name FROM STUDENT";

        Assertions.assertEquals(ans.toUpperCase(), sql.toString().toUpperCase());
    }

    @Test
    void test_to_char(){
      final String sql=  SqlBuilder.toChar("TEXT").toString();
        assertTrue(SqlEqual.isEqual(" to_char(TEXT)",sql));
    }
}