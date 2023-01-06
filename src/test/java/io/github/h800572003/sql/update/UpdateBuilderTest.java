package io.github.h800572003.sql.update;

import io.github.h800572003.sql.SqlBuilder;
import io.github.h800572003.sql.delete.DeleteBuilder;
import io.github.h800572003.sql.select.SqlOption;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UpdateBuilderTest {


    @Test
    void test_1() {

        String sql = UpdateBuilder.update("STUDENT")
                .set(SqlBuilder.write("NAME"),SqlBuilder.quotation("小寶"))
                .back()
                .where(SqlBuilder.getParameter("id", SqlOption.EQ, SqlBuilder.quotation("B1234")))
                .build().toUpperCase();



        String ans = "update STUDENT set NAME = '小寶' where id = 'B1234'";

        log.info("sql:{}", sql);
        Assertions.assertEquals(sql, ans.toUpperCase());

    }
}