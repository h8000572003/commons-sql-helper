package io.github.h800572003.sql;

import org.apache.commons.lang3.StringUtils;

import java.util.function.BooleanSupplier;

public class SqlBody implements ISqlBody {

    enum Type implements ISql {
        EMPTY(""),


        ;
        final String value;

        Type(String value) {
            this.value = value;
        }


        public String getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    private ISql sql;


    public SqlBody() {
        this.sql = SqlBuilder.write("");
    }

    public SqlBody(ISql... sql) {
        this.sql = SqlBuilder.write(sql);
    }

    public SqlBody(String... sql) {
        this.sql = SqlBuilder.write(sql);
    }

    @Override
    public ISqlBody addWithSpace(String sql) {
        return new SqlBody(this.sql, SqlBuilder.write(StringUtils.SPACE), SqlBuilder.write(sql));
    }

    @Override
    public ISqlBody add(String sql) {
        return new SqlBody(this.sql, SqlBuilder.write(sql));
    }

    @Override
    public ISqlBody add(ISql sql) {
        return new SqlBody(this.sql, sql);
    }

    @Override
    public ISqlBody addWithSpace(ISql sql, BooleanSupplier supplier) {
        if (supplier.getAsBoolean()) {
            return this.addWithSpace(sql);
        } else {
            return add(Type.EMPTY);
        }

    }

    @Override
    public ISqlBody add(ISql sql, BooleanSupplier supplier) {
        if (supplier.getAsBoolean()) {
            return this.add(sql);
        } else {
            return add(Type.EMPTY);
        }
    }

    @Override
    public ISqlBody addWithSpace(String sql, BooleanSupplier supplier) {
        if (supplier.getAsBoolean()) {
            return this.addWithSpace(sql);
        } else {
            return add(Type.EMPTY);
        }
    }

    @Override
    public ISqlBody add(String sql, BooleanSupplier supplier) {
        if (supplier.getAsBoolean()) {
            return this.add(sql);
        } else {
            return add(Type.EMPTY);
        }
    }

    public ISqlBody addWithSpace(ISql sql) {
        return new SqlBody(this.sql, SqlBuilder.write(StringUtils.SPACE), sql);
    }

    @Override
    public String toString() {
        String string = sql.toString();
        return string.replaceAll("\\s+", " ").trim();
    }
}
