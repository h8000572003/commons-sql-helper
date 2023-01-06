package io.github.h800572003.sql.update;

import io.github.h800572003.sql.ISql;
import io.github.h800572003.sql.ISqlBack;
import io.github.h800572003.sql.SqlBuilder;
import io.github.h800572003.sql.select.SqlOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UpdateSets implements ISql, ISqlBack<UpdateBuilder> {


    private final UpdateBuilder updateBuilder;
    private final List<ISql> sqls = new ArrayList<>();

    public UpdateSets(UpdateBuilder updateBuilder, ISql sql, ISql value) {
        this.updateBuilder = updateBuilder;
        this.set(sql, value);
    }

    public UpdateSets set(ISql sql, ISql value) {
        this.sqls.add(new UpdateKeyValue(sql, value));
        return this;
    }

    @Override
    public UpdateBuilder back() {
        return updateBuilder;
    }

    @Override
    public String build() {
        return updateBuilder.build();
    }

    @Override
    public String toString() {
        return sqls.stream().map(Objects::toString).collect(Collectors.joining());
    }

    class UpdateKeyValue implements ISql {
        ISql field;
        ISql value;

        public UpdateKeyValue(ISql field, ISql value) {
            this.field = field;
            this.value = value;
        }

        public ISql toSql() {
            return SqlBuilder.write(field, SqlOption.EQ, value);
        }


        @Override
        public String toString() {
            return toSql().toString();
        }
    }


}
