package io.github.h800572003.sql;

public enum Selects implements ISql {
    MAX("MAX "),
    MIN("MIN"),
    COUNT("COUNT "),
    DISTINCT("DISTINCT "),

    FROM(" FROM "),

    ASC(" ASC "),//

    DESC(" DESC "),//

    ORDER_BY(" Order by "),//

    NULL(" NULL "),//

    ORACLE_JOIN(" (+) "),//

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
