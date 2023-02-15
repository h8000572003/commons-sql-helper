package io.github.h800572003.sql.generate;

import io.github.h800572003.sql.ISql;
import io.github.h800572003.sql.ISqlGenerator;
import io.github.h800572003.sql.SqlBuilder;
import io.github.h800572003.sql.select.SelectBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


@Slf4j
class ISqlGeneratorTest {

    ISqlGenerator generator = new ISqlGenerator();

    @Test
    void export() {

        String code = generator.export("Test", "select c1,c2,c3 from table");


        log.info("code:{}", code);

        Assertions.assertTrue(code.contains("ISql sql = SqlBuilder.body().addWithSpace(SelectBuilder.newSelect().createSelect().add(\"c1\").add(\"c2\").add(\"c3\").back().from(SqlBuilder.write(\"table\")));"));


    }


    @Test
    void exportWithWhere() {


        String code = generator.export("Test", "select c1,c2,c3 from table where a=:a and b=:b and c=:c");

        log.info("code:{}", code);

        Assertions.assertTrue(code.contains("ISql sql = SqlBuilder.body().addWithSpace(SelectBuilder.newSelect().createSelect().add(\"c1\").add(\"c2\").add(\"c3\").back().from(SqlBuilder.write(\"table\")).createWhere(SqlBuilder.write(\"a=:a\")).and(SqlBuilder.write(\"b=:b\")).and(SqlBuilder.write(\"c=:c\")).back())"));


    }

    @Test
    void exportUnion() {


        String code = generator.export("Test", "select c3,c2,c1 from table union select c1,c2,c3 from table2");
        log.info("code:{}", code);

        Assertions.assertTrue(code.contains("ISql sql = SqlBuilder.body().addWithSpace(SelectBuilder.newSelect().createSelect().add(\"c3\").add(\"c2\").add(\"c1\").back().from(SqlBuilder.write(\"table\")).appendWithSpace(\"union\")).addWithSpace(SelectBuilder.newSelect().createSelect().add(\"c1\").add(\"c2\").add(\"c3\").back().from(SqlBuilder.write(\"table2\")));"));

    }

    @Test
    void exportGroup() {


        String code = generator.export("Test", "select c1,c2,c3 from table group by c3,c2,c1");

        log.info("code:{}", code);

        Assertions.assertTrue(code.contains("ISql sql = SqlBuilder.body().addWithSpace(SelectBuilder.newSelect().createSelect().add(\"c1\").add(\"c2\").add(\"c3\").back().from(SqlBuilder.write(\"table\")).groupBy(\"c3\", \"c2\", \"c1\"));"));


    }

    @Test
    void exportOrderBy() {


        String code = generator.export("Test", "select c1,c2,c3 from table order by c3,c2,c1 desc");

        log.info("code:{}", code);

        Assertions.assertTrue(code.contains("ISql sql = SqlBuilder.body().addWithSpace(SelectBuilder.newSelect().createSelect().add(\"c1\").add(\"c2\").add(\"c3\").back().from(SqlBuilder.write(\"table\")).createOrderBy().add(SqlBuilder.write(\"c3\")).add(SqlBuilder.write(\"c2\")).add(SqlBuilder.write(\"c1\")).isDesc().back())"));


    }

    @Test
    void exportSub() {


        String code = generator.export("Test", "select c1,c2,to_char(system,'HH24') from table ");

        log.info("code:{}", code);

        Assertions.assertTrue(code.contains("ISql sql = SqlBuilder.body().addWithSpace(SelectBuilder.newSelect().createSelect().add(\"c1\").add(\"c2\").add(\"to_char(system,'HH24')\").back().from(SqlBuilder.write(\"table\")));\n"));


    }






}