package io.github.h800572003.sql;

public enum Selects implements ISql {
    MAX("MAX "),
    MIN("MIN"),
    COUNT("COUNT "),
    DISTINCT("DISTINCT "),
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
