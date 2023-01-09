package io.github.h800572003.sql.select;

import io.github.h800572003.sql.ISql;
import io.github.h800572003.sql.SqlBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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


}