package io.github.h800572003.sql.generate;

import io.github.h800572003.sql.ISql;
import io.github.h800572003.sql.ISqlGenerator;
import io.github.h800572003.sql.Selects;
import io.github.h800572003.sql.SqlBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


@Slf4j
class ISqlGeneratorTest {


    @Test
    void export() {
        ISqlGenerator generator = new ISqlGenerator();

        String code = generator.export("Test", "select c1,c2,c3 from table");

        ;
        log.info("code:{}", code);

        Assertions.assertTrue(code.contains("ISql sql = SqlBuilder.body().add(\" select \").add(\" c1,c2,c3 \").add(\" from \").add(\" table \");"));


    }


    @Test
    void exportWithWhere() {
        ISqlGenerator generator = new ISqlGenerator();

        String code = generator.export("Test", "select c1,c2,c3 from table where a=:a and b=:b and c=:c");

        log.info("code:{}", code);

        Assertions.assertTrue(code.contains("ISql sql = SqlBuilder.body().add(\" select \").add(\" c1,c2,c3 \").add(\" from \").add(\" table \").add(\" where \").add(\" a=:a \").add(\" and \").add(\" b=:b \").add(\" and \").add(\" c=:c \");"));



    }
    @Test
    void test(){
        StringSqlGeneratorPrint.print("select * from c1,c2 where a='b' \n \t \r and d =: d");
        System.out.print(this.sql());
    }
    public ISql sql() {
        ISql sql = SqlBuilder.body().addSpace(Selects.SELECT.name()).addSpace("*").addSpace(Selects.FROM.name()).addSpace("c1,c2").addSpace(Selects.WHERE.name()).addSpace("a='b'").addSpace(Selects.AND.name()).addSpace("d").addSpace("=:").addSpace("d");
   return sql;
    }


    public static void main(String[] args) {
        ISql isql = SqlBuilder.body()
                .addSpace("select")
                .addSpace("c1,c2,c3")
                .addSpace("from").addSpace("table").addSpace("where").addSpace("a=:a").addSpace("and").addSpace("b=:b").addSpace("and").addSpace("c=:c");
        System.out.print(isql);
    }

}