package io.github.h800572003.sql;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SqlEqualTest {


    @Test
    void test() {
        Assertions.assertTrue(
                SqlEqualUtils.isEqual(
                        "select * from    student",//
                        "SELECT * from student"//
                ));
    }
}
