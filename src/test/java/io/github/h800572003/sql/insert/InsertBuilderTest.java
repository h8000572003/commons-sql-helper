package io.github.h800572003.sql.insert;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class InsertBuilderTest {



    @Test
    void test(){
        String sql = InsertBuilder.insertTable("STUDENT")
                .add("ID", "'B1234'")
                .add("NAME", "'大寶'")
                .build().toUpperCase();

        String ans = "INSERT INTO STUDENT(ID,NAME) VALUES ('B1234','大寶')";

        log.info("sql:{}", sql);
        Assertions.assertEquals(sql, ans.toUpperCase());

    }

}