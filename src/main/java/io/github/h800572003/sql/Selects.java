package io.github.h800572003.sql;

import org.apache.commons.lang3.StringUtils;

/**
 * select查詢參數
 */
public enum Selects implements ISql {
    MAX("MAX "),
    MIN("MIN"),
    COUNT0("COUNT(0)"),
    COUNT("COUNT"),
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

    AS(" AS "),//

    DOT("."),//

    UNION_ALL(" UNION ALL "),//
    UNION(" UNION "),//

    LIMIT(" LIMIT "),//

    PERCENT("%"),//百分筆

    Q("?"),//?
    Q_SPACE(StringUtils.SPACE+"?"+StringUtils.SPACE),//?

    FETCH_NEXT (" FETCH_NEXT "),//
    FETCH_FIRST(" FETCH FIRST "),//
    ROWS(" ROWS "),
    ROW(" ROW "),

    OFFSET(" OFFSET "),
    ROWS_ONLY(" ROWS ONLY "),
    ROWNUM(" ROWNUM "),
    OVER(" OVER "),
    ONLY(" ONLY "),
    WITH_TIES(" WITH TIES"),
    PERCENT_V(" PERCENT "),//
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
