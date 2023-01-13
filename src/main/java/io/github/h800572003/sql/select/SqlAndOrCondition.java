package io.github.h800572003.sql.select;

import io.github.h800572003.sql.ISql;
import io.github.h800572003.sql.ISqlBack;
import io.github.h800572003.sql.ISqlBuilder;
import io.github.h800572003.sql.SqlBuilder;
import org.junit.platform.commons.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

public class SqlAndOrCondition<T extends ISqlBuilder> implements ISql, ISqlBack<T> {

    private final T build;
    private final List<ISql> sqlList = new ArrayList<>();

    private final OnWhere onWhere;



    public SqlAndOrCondition(T build, OnWhere onWhere, ISql sql) {
        this.onWhere = onWhere;
        this.build = build;
        if (sql != null) {
            sqlList.add(sql);
        }
    }


    public SqlAndOrCondition<T> or(ISql sql) {
        return or(sql, () -> true);
    }

    public SqlAndOrCondition<T> or(ISql sql, BooleanSupplier condition) {
        orAnd(SqlAndOr.OR, sql, condition);
        return this;
    }

    public SqlAndOrCondition<T> and(ISql sql, BooleanSupplier condition) {
        orAnd(SqlAndOr.AND, sql, condition);
        return this;
    }

    public SqlAndOrCondition<T> and(ISql sql) {
        return and(sql, () -> true);
    }

    public SqlAndOrCondition<T> orAnd(SqlAndOr andOr, ISql sql, BooleanSupplier condition) {

        if (condition.getAsBoolean()) {
            if (sqlList.isEmpty()) {
                this.sqlList.add(SqlBuilder.write(sql));
            } else {
                this.sqlList.add(SqlBuilder.write(andOr, sql));
            }
        }
        return this;
    }

    @Override
    public T back() {
        return build;
    }

    public String build() {
        return build.build();
    }


    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        if (!sqlList.isEmpty()) {
            stringBuffer.append(onWhere);
        }
        stringBuffer.append(sqlList.stream().map(Object::toString).collect(Collectors.joining()));
        return stringBuffer.toString();


    }
}
