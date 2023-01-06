package io.github.h800572003.sql.select;

public enum OnWhere {
    ON(" ON "),
    WHERE(" WHERE "),
    ;
    final String sql;

    OnWhere(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return sql;
    }
}
