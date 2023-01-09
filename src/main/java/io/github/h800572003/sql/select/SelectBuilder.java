package io.github.h800572003.sql.select;

import io.github.h800572003.sql.ISql;
import io.github.h800572003.sql.ISqlBuilder;
import io.github.h800572003.sql.Selects;
import io.github.h800572003.sql.SqlBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

/**
 * 查詢建立
 */
public class SelectBuilder implements ISqlBuilder, ISql {

    private List<ISql> orders = new ArrayList<>();//順序


    private SelectBuilder() {

    }

    public static SelectBuilder newSelect() {
        return new SelectBuilder();
    }

    /**
     * 全部查詢
     * @return
     */
    public SelectBuilder selectAll() {
        return select(Selects.SELECT_ALL.toString());
    }

    /**
     *
     * @param sql 文字
     * @return
     */
    public SelectBuilder select(String sql) {
        ISql select = SqlBuilder.write(SqlBuilder.write(Selects.SELECT) + sql);
        this.orders.add(select);
        return this;

    }

    /**
     * 客製化Select
     * @param sql
     * @return
     */
    public SelectBuilder select(ISql sql) {
        ISql select = SqlBuilder.write(Selects.SELECT , sql);
        this.orders.add(select);
        return this;

    }

    /**
     * 欄位建立
     * @return
     */
    public Select<SelectBuilder> createSelect() {
        final Select<SelectBuilder> select = new Select<>(this);
        this.orders.add(select);
        return select;
    }

    public SelectBuilder from(ISql sql) {
        this.orders.add(SqlBuilder.write(Selects.FROM, sql));
        return this;
    }

    public SelectBuilder from(String sql) {
        ISql write = SqlBuilder.write(Selects.FROM, SqlBuilder.write(sql));
        this.orders.add(write);
        return this;
    }

    public SqlAndOrCondition<SelectBuilder> where(SqlBuilder.ValueParameter sqlWhere) {
        return this.where(sqlWhere, () -> true);
    }

    public SqlAndOrCondition<SelectBuilder> where(SqlBuilder.ValueParameter sqlWhere, BooleanSupplier condition) {
        SqlAndOrCondition<SelectBuilder> selectWhere = new SqlAndOrCondition<>(this, OnWhere.WHERE, condition.getAsBoolean() ? sqlWhere : null);
        this.orders.add(selectWhere);
        return selectWhere;
    }

    public SqlAndOrCondition<SelectBuilder> where(ISql iSql) {
        return where(iSql, () -> true);
    }

    public SqlAndOrCondition<SelectBuilder> where(ISql iSql, BooleanSupplier condition) {
        SqlAndOrCondition<SelectBuilder> selectWhere = new SqlAndOrCondition<>(this, OnWhere.WHERE, condition.getAsBoolean() ? iSql : null);
        this.orders.add(selectWhere);
        return selectWhere;
    }

    /**
     * 額外加入
     * @param append
     * @return
     */
    public SelectBuilder append(String append) {
        this.orders.add(SqlBuilder.write(append));
        return this;
    }


    public SqlOrderBy createOrderBy() {
        final SqlOrderBy orderBy = new SqlOrderBy(this);
        this.orders.add(orderBy);
        return orderBy;
    }


    @Override
    public String build() {
        return orders.stream()
                .map(Objects::toString)
                .filter(Objects::nonNull)
                .collect(Collectors.joining());
    }

    @Override
    public String toString() {
        return build();
    }

    public SqlJoin<SelectBuilder> createJoin() {
        final SqlJoin<SelectBuilder> sqlJoin = new SqlJoin<>(this);
        orders.add(sqlJoin);
        return sqlJoin;
    }

    public SelectBuilder groupBy(ISql iSql) {
        orders.add(SqlBuilder.write(SqlBuilder.write(Selects.GROUP_BY),iSql));
        return this;
    }

    public SelectBuilder groupBy(String... fields) {
        orders.add(SqlBuilder.write(SqlBuilder.write(Selects.GROUP_BY), SqlBuilder.comma(fields)));
        return this;
    }
}
