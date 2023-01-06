package io.github.h800572003.sql.update;

import io.github.h800572003.sql.ISql;
import io.github.h800572003.sql.ISqlBuilder;
import io.github.h800572003.sql.SqlBuilder;
import io.github.h800572003.sql.select.OnWhere;
import io.github.h800572003.sql.select.SqlAndOrCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UpdateBuilder implements ISqlBuilder, ISql {

    private List<ISql> sqls = new ArrayList<>();


    private UpdateBuilder(String table) {
        this.sqls.add(SqlBuilder.write("UPDATE " + SqlBuilder.write(table)));
    }

    public UpdateSets set(ISql sql, ISql value) {
        UpdateSets updateSets = new UpdateSets(this, sql, value);
        this.sqls.add(SqlBuilder.write(" SET "));
        this.sqls.add(updateSets);
        return updateSets;
    }



    public static UpdateBuilder update(String table) {
        return new UpdateBuilder(table);
    }

    public SqlAndOrCondition<UpdateBuilder> where(SqlBuilder.ValueParameter sqlWhere) {
        SqlAndOrCondition<UpdateBuilder> selectWhere = new SqlAndOrCondition<>(this, OnWhere.WHERE, sqlWhere);
        this.sqls.add(selectWhere);
        return selectWhere;

    }


    @Override
    public String build() {
        return sqls.stream().map(Objects::toString).collect(Collectors.joining());
    }

}
