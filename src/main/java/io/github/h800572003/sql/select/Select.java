package io.github.h800572003.sql.select;

import io.github.h800572003.sql.ISql;
import io.github.h800572003.sql.ISqlBack;
import io.github.h800572003.sql.ISqlBuilder;
import io.github.h800572003.sql.SqlBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 查詢
 */
public class Select<T extends  ISqlBuilder> implements ISql, ISqlBack<T> {
    private List<ISql> sqls = new ArrayList<>();
    private final T builder;

    public Select(T builder) {
        this.builder = builder;
    }

    public Select<T> add(ISql sql) {
        this.sqls.add(sql);
        return this;
    }

    public Select<T> add(String sql) {
        this.add(SqlBuilder.write(sql));
        return this;
    }
    @Override
    public T back(){
        return builder;
    }

    @Override
    public String build() {
        return builder.build();
    }

    @Override
    public String toString() {
        StringBuilder stringBuffer=new StringBuilder("SELECT ");
        stringBuffer.append( this.sqls
                .stream()//
                .map(Objects::toString)//
                .collect(Collectors
                        .joining(",")));
        return stringBuffer.toString();
    }
}
