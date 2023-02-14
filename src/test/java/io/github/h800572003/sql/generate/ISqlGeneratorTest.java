package io.github.h800572003.sql.generate;

import io.github.h800572003.sql.*;
import io.github.h800572003.sql.select.SelectBuilder;
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

        Assertions.assertTrue(code.contains(" ISql sql = SqlBuilder.body().addWithSpace(SelectBuilder.newSelect().createSelect().add(\"c1,c2,c3\").back().from(SqlBuilder.write(\"table\")));"));


    }



    @Test
    void exportWithWhere() {
        ISqlGenerator generator = new ISqlGenerator();

        String code = generator.export("Test", "select c1,c2,c3 from table where a=:a and b=:b and c=:c");

        log.info("code:{}", code);

        Assertions.assertTrue(code.contains("ISql sql = SqlBuilder.body().addWithSpace(SelectBuilder.newSelect().createSelect().add(\"c1,c2,c3\").back().from(SqlBuilder.write(\"table\")).createWhere(SqlBuilder.write(\"a=:a\")).and(SqlBuilder.write(\"b=:b\")).and(SqlBuilder.write(\"c=:c\")).back())"));





    }
    @Test
    void exportUnion() {

        ISqlGenerator generator = new ISqlGenerator();

        String code = generator.export("Test", "select c3,c2,c1 from table union select c1,c2,c3 from table2");
        log.info("code:{}", code);

        Assertions.assertTrue(code.contains("ISql sql = SqlBuilder.body().addWithSpace(SelectBuilder.newSelect().createSelect().add(\"c3,c2,c1\").back().from(SqlBuilder.write(\"table\")).appendWithSpace(\"union\")).addWithSpace(SelectBuilder.newSelect().createSelect().add(\"c1,c2,c3\").back().from(SqlBuilder.write(\"table2\")))"));

    }




        public static void main(String[] args) {
            ISql sql = SqlBuilder.body()
                    .addWithSpace(SelectBuilder.newSelect().
                            createSelect().add("c3,c2,c1").back()
                            .from(SqlBuilder.write("table"))
                            .appendWithSpace("union"))
                    .addWithSpace(SelectBuilder.newSelect().createSelect().add("c1,c2,c3").back().from(SqlBuilder.write("table2")));


            System.out.print(sql);
        }


}