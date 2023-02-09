package io.github.h800572003.sql.select;

import io.github.h800572003.sql.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 查詢
 */
public class Select<T extends ISqlBuilder> implements ISql, ISqlBack<T> {
    private List<ISql> sqls = new ArrayList<>();
    private final T builder;

    public Select(T builder) {
        this.builder = builder;
    }

    /**
     * 一次加入
     *
     * @param sqls
     * @return
     */
    public Select<T> addAll(String... sqls) {
        for (String sql : sqls) {
            body(sql);
        }
        return this;
    }



    public Select<T> add(ISql sql) {
        this.sqls.add(sql);
        return this;
    }

    /**
     * 單一欄位
     *
     * @param sql
     * @return
     */
    public Select<T> body(String sql) {
        this.add(SqlBuilder.write(sql));
        return this;
    }


    /**
     * 攜帶別名
     *
     * @param sql
     * @param asName
     * @return
     */
    public Select<T> add(String sql, String asName) {
        this.add(SqlBuilder.write(sql, StringUtils.SPACE, asName));
        return this;
    }

    @Override
    public T back() {
        return builder;
    }

    @Override
    public String build() {
        return builder.build();
    }

    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder(Selects.SELECT.toString());
        stringBuffer.append(this.sqls
                .stream()//
                .filter(Objects::nonNull)
                .map(Objects::toString)//
                .collect(Collectors
                        .joining(",")));
        return stringBuffer.toString();
    }
}
