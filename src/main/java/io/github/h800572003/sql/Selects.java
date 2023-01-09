package io.github.h800572003.sql;

/**
 * select查詢參數
 */
public enum Selects implements ISql {
    MAX("MAX "),
    MIN("MIN"),
    COUNT("COUNT "),
    DISTINCT("DISTINCT "),

    FROM(" FROM "),

    ASC(" ASC "),//

    DESC(" DESC "),//

    ORDER_BY(" ORDER BY "),//

    NULL(" NULL "),//

    ORACLE_JOIN(" (+) "),//


    GROUP_BY(" GROUP BY "),//

    SELECT("SELECT "),//
    SELECT_ALL("*"),//

    LEFT_CLAD("("),
    RIGHT_CLAD(")"),
    COMMA(","),
    QUOTATION("'"),//
    ;

    final String sql;

    Selects(String sql) {
        this.sql = sql;
    }



    @Override
    public String toString() {
        return this.sql;
    }
}
