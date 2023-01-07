package io.github.h800572003.sql.delete;

import io.github.h800572003.sql.ISql;
import io.github.h800572003.sql.ISqlBuilder;
import io.github.h800572003.sql.SqlBuilder;
import io.github.h800572003.sql.select.OnWhere;
import io.github.h800572003.sql.select.SqlAndOrCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DeleteBuilder  implements ISqlBuilder, ISql {

    private List<ISql> sqls=new ArrayList<>();


    private DeleteBuilder(String table){
        this.sqls.add(SqlBuilder.write("delete from "+SqlBuilder.write(table)));
    }
    public static DeleteBuilder deleteFrom(String table){
       return new DeleteBuilder(table);
    }

    public SqlAndOrCondition<DeleteBuilder> where(SqlBuilder.ValueParameter sqlWhere) {
        SqlAndOrCondition<DeleteBuilder> selectWhere = new SqlAndOrCondition<>(this, OnWhere.WHERE, sqlWhere);
        this.sqls.add(selectWhere);
        return selectWhere;
    }
    public SqlAndOrCondition<DeleteBuilder> where(ISql sql) {
        SqlAndOrCondition<DeleteBuilder> selectWhere = new SqlAndOrCondition<>(this, OnWhere.WHERE, sql);
        this.sqls.add(selectWhere);
        return selectWhere;
    }


    @Override
    public String build() {
        return sqls.stream().map(Objects::toString).collect(Collectors.joining());
    }
}
