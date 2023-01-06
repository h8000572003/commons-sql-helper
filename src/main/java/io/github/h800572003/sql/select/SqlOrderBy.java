package io.github.h800572003.sql.select;

import io.github.h800572003.sql.ISql;
import io.github.h800572003.sql.ISqlBack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

public class SqlOrderBy implements ISql, ISqlBack<SelectBuilder> {
    public static final String ORDER_BY = " Order by ";
    public static final String ASC = " asc ";
    private final SelectBuilder selectBuilder;
    private final List<ISql> sqls = new ArrayList<>();
    private boolean isAsc = false;

    public SqlOrderBy(SelectBuilder selectBuilder) {
        this.selectBuilder = selectBuilder;
    }

    public SqlOrderBy add(ISql sql) {
        return this.add(sql, () -> Boolean.TRUE);
    }

    public SqlOrderBy add(ISql sql, BooleanSupplier supplier) {
        if (supplier.getAsBoolean()) {
            this.sqls.add(sql);
        }
        return this;

    }

    public SqlOrderBy isAsc() {
        isAsc = true;
        return this;
    }

    @Override
    public SelectBuilder back() {
        return selectBuilder;
    }

    public String build() {
        return this.selectBuilder.build();
    }

    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(ORDER_BY);
        stringBuffer.append(sqls.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
        if (isAsc) {
            stringBuffer.append(ASC);
        }
        return stringBuffer.toString();
    }
}
