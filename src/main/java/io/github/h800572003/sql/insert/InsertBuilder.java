package io.github.h800572003.sql.insert;

import io.github.h800572003.sql.ISql;
import io.github.h800572003.sql.ISqlBuilder;
import io.github.h800572003.sql.SqlBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InsertBuilder implements ISqlBuilder, ISql {

    private List<ISql> sqls = new ArrayList<>();

    private final List<InsertKeyValue> insertKeyValues = new ArrayList<>();
    private boolean isAdd = false;

    private InsertBuilder(String table) {
        this.sqls.add(SqlBuilder.write("INSERT INTO " + SqlBuilder.write(table)));
    }

    public static InsertBuilder insertTable(String table) {
        return new InsertBuilder(table);
    }

    public InsertBuilder add(ISql field, ISql value) {
        insertKeyValues.add(new InsertKeyValue(field, value));
        return this;
    }
    public InsertBuilder add(String field, String value) {
        insertKeyValues.add(new InsertKeyValue(SqlBuilder.write(field), SqlBuilder.write(value)));
        return this;
    }

    @Override
    public String build() {
        return  toString();

    }

    @Override
    public String toString() {

        if (!isAdd) {
            List<ISql> fields = insertKeyValues.stream().map(i -> i.field)
                    .collect(Collectors.toList());


            List<ISql> value = insertKeyValues.stream().map(i -> i.value)
                    .collect(Collectors.toList());


            this.sqls.add(SqlBuilder.commaClad((fields.toArray(new ISql[fields.size()]))));
            this.sqls.add(SqlBuilder.write(" VALUES "));
            this.sqls.add(SqlBuilder.commaClad(value.toArray(new ISql[value.size()])));
            isAdd = true;
        }


        return this.sqls.stream().map(Objects::toString).collect(Collectors.joining());


    }

    class InsertKeyValue {
        ISql field;
        ISql value;

        public InsertKeyValue(ISql field, ISql value) {
            this.field = field;
            this.value = value;
        }

    }
}
