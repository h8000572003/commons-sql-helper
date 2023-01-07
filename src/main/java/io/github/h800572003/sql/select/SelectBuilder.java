package io.github.h800572003.sql.select;

import io.github.h800572003.sql.ISql;
import io.github.h800572003.sql.ISqlBuilder;
import io.github.h800572003.sql.Selects;
import io.github.h800572003.sql.SqlBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 查詢建立
 */
public class SelectBuilder implements ISqlBuilder, ISql {

    public static final String SELECT = "SELECT ";
    public static final String FROM = "FROM";
    public static final String GROUP_BY = "GROUP BY";
    private List<ISql> orders = new ArrayList<>();//順序


    private SelectBuilder() {

    }

    public static SelectBuilder newSelect() {
        return new SelectBuilder();
    }

    public SelectBuilder select(String sql) {
        ISql select = SqlBuilder.write(SELECT + sql);
        this.orders.add(select);
        return this;

    }

    public Select<SelectBuilder> createSelect() {
        final Select<SelectBuilder> select = new Select<>(this);
        this.orders.add(select);
        return select;
    }

    public SelectBuilder from(ISql sql) {
        this.orders.add(SqlBuilder.write(Selects.FROM,sql));
        return this;
    }

    public SelectBuilder from(String sql) {
        ISql write = SqlBuilder.write(Selects.FROM,SqlBuilder.write(sql));
        this.orders.add(write);
        return this;
    }

    public SqlAndOrCondition<SelectBuilder> where(SqlBuilder.ValueParameter sqlWhere) {
        SqlAndOrCondition<SelectBuilder> selectWhere = new SqlAndOrCondition<>(this, OnWhere.WHERE, sqlWhere);
        this.orders.add(selectWhere);
        return selectWhere;
    }
    public SqlAndOrCondition<SelectBuilder> where(ISql iSql) {
        SqlAndOrCondition<SelectBuilder> selectWhere = new SqlAndOrCondition<>(this, OnWhere.WHERE, iSql);
        this.orders.add(selectWhere);
        return selectWhere;
    }

    public SelectBuilder append(String append) {
        this.orders.add(SqlBuilder.write(append));
        return this;
    }


    public SqlOrderBy orderBy() {
        final SqlOrderBy orderBy = new SqlOrderBy(this);
        this.orders.add(orderBy);
        return orderBy;
    }


    @Override
    public String build() {
        return orders.stream()
                .map(Objects::toString)
                .collect(Collectors.joining());
    }

    @Override
    public String toString() {
        return build();
    }

    public SqlJoin<SelectBuilder> createJoin() {


        SqlJoin<SelectBuilder> sqlJoin = new SqlJoin<>(this);
        orders.add(sqlJoin);
        return sqlJoin;
    }

    public SelectBuilder groupBy(ISql iSql) {
        orders.add(iSql);
        return this;
    }

    public SelectBuilder groupBy(String... fields) {
        orders.add(SqlBuilder.write(SqlBuilder.write(" " + GROUP_BY + " "), SqlBuilder.comma(fields)));
        return this;
    }
}
