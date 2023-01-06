package io.github.h800572003.sql.select;

import io.github.h800572003.sql.ISql;

/**
 * AND OR 代碼
 */
public enum SqlAndOr implements ISql {
    AND(" AND "),
    OR(" OR "),
    ;

    SqlAndOr(String sql) {
        this.sql = sql;
    }

    final String sql;


    @Override
    public String toString() {
        return this.sql;
    }
}
