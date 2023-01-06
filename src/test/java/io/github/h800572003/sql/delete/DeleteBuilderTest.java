package io.github.h800572003.sql.delete;

import io.github.h800572003.sql.SqlBuilder;
import io.github.h800572003.sql.select.SqlOption;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
class DeleteBuilderTest {

    @Test
    void test_1() {

        String sql = DeleteBuilder.deleteFrom("student")
                .where(SqlBuilder.getParameter("id", SqlOption.EQ, SqlBuilder.quotation("AAAA")))
                .build().toUpperCase();



        String ans = "delete from student where id = 'AAAA'";

        log.info("sql:{}", sql);
        Assertions.assertEquals(sql, ans.toUpperCase());

    }

}