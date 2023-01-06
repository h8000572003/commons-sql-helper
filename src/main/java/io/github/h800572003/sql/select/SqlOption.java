package io.github.h800572003.sql.select;

import io.github.h800572003.sql.ISql;

/**
 * 運算符號
 */
public enum SqlOption implements ISql {
    GE(" >= "),//
    LE(" <= "),//
    NE(" != "),//
    EQ(" = "),//
    IN(" in "),//
    NOT_IN(" not in "),//
    BETWEEN(" between ");

    final String sql;




    SqlOption(String sql) {
        this.sql = sql;

    }

    @Override
    public String toString() {
        return sql;
    }


}
