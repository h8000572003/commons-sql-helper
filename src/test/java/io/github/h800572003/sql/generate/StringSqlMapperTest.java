package io.github.h800572003.sql.generate;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringSqlMapperTest {

    public static final String SELECT_FROM_TABLE_WHERE_A_A_AND_B_B_ORDER_BY_ABC = "SELECT * FROM table WHERE a=:a and b=:b ORDER BY abc";

    @Test
    void mapper() {
        IStringSqlMapper stringSqlMapper = new StringSqlMapper();
        List<GenerateWord> mapper = stringSqlMapper.mapper(SELECT_FROM_TABLE_WHERE_A_A_AND_B_B_ORDER_BY_ABC);

        mapper.forEach(System.out::println);

        assertEquals(11, mapper.size());

    }

}