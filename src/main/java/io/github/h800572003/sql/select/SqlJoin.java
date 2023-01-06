package io.github.h800572003.sql.select;

import io.github.h800572003.sql.ISql;
import io.github.h800572003.sql.ISqlBack;
import io.github.h800572003.sql.ISqlBuilder;
import io.github.h800572003.sql.SqlBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SqlJoin<T extends ISqlBuilder> implements ISql, ISqlBack<T> {


    enum JoinType implements ISql {
        LEFT_JOIN(" LEFT JOIN "),
        JOIN(" JOIN "),
        RIGHT(" RIGHT JOIN "),
        ;
        final String sql;

        JoinType(String sql) {
            this.sql = sql;
        }

        @Override
        public String toString() {
            return this.sql;
        }
    }

    private List<ISql> sqls = new ArrayList<>();
    private final T selectBuilder;


    public SqlJoin<T> join(JoinType join, ISql iSql) {
        sqls.add(SqlBuilder.write(join, iSql));
        return this;
    }

    public SqlJoin<T> join(JoinType join, String sql) {
        return this.join(join, SqlBuilder.write(sql));

    }


    public SqlJoin(T selectBuilder) {
        this.selectBuilder = selectBuilder;

    }

    public SqlAndOrCondition<T> on(ISql sql) {
        SqlAndOrCondition<T> sqlJoinAndOr = new SqlAndOrCondition<>(selectBuilder, OnWhere.ON, sql);
        sqls.add(sqlJoinAndOr);
        return sqlJoinAndOr;
    }

    @Override
    public T back() {
        return selectBuilder;
    }

    @Override
    public String build() {
        return selectBuilder.build();
    }


    @Override
    public String toString() {
        return sqls.stream()//
                .map(Objects::toString)//
                .collect(Collectors.joining());//
    }
}
