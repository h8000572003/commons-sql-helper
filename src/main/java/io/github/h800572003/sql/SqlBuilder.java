package io.github.h800572003.sql;

import io.github.h800572003.sql.select.SelectBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlBuilder implements ISql {
    public static final String LC = Selects.LEFT_CLAD.toString();
    public static final String RC = Selects.RIGHT_CLAD.toString();
    public static final String COMMA = Selects.COMMA.toString();
    public static final String QUOTATION = Selects.QUOTATION.toString();
    private final String sql;

    public static ISql union(ISqlBack<SelectBuilder> sqlBack1, ISqlBack<SelectBuilder> sqlBack2) {
        return SqlBuilder.write(sqlBack1.back(), Selects.UNION, sqlBack2.back());
    }

    public static ISql union(ISql sql1, ISql sql2) {
        return SqlBuilder.write(sql1, Selects.UNION, sql2);
    }

    public static ISql unionAll(ISql sql1, ISql sql2) {
        return SqlBuilder.write(sql1, Selects.UNION_ALL, sql2);
    }


    public static ISql unionAll(ISqlBack<SelectBuilder> sqlBack1, ISqlBack<SelectBuilder> sqlBack2) {
        return SqlBuilder.write(sqlBack1.back(), Selects.UNION_ALL, sqlBack2.back());
    }

    public static ISql toChar(String sql) {
        return SqlBuilder.write(write("TO_CHAR"), SqlBuilder.clad(sql));

    }


    public static class StringValueSqlHolder {

        private boolean isComma = false;//是否逗號
        private boolean isClad = false;//是否有括弧
        private boolean isQuotation = false;//單引號

        public StringValueSqlHolder comma() {
            this.isComma = true;
            return this;
        }

        public StringValueSqlHolder clad() {
            this.isClad = true;
            return this;
        }

        public StringValueSqlHolder quotation() {
            this.isQuotation = true;
            return this;
        }

        public ISql build(ISql... sqls) {
            ISql innerSql = SqlBuilder.clad("");
            if (isQuotation && isComma) {//單引號與,
                innerSql = SqlBuilder.quotationAndComma(sqls);
            } else if (isComma) {//僅有逗點
                innerSql = SqlBuilder.comma(sqls);
            }
            if (isClad) {
                return SqlBuilder.clad(innerSql);
            } else {
                return innerSql;
            }
        }

        public ISql build(String... value) {
            ISql innerSql = SqlBuilder.clad("");
            if (isQuotation && isComma) {//單引號與,
                innerSql = SqlBuilder.quotationAndComma(value);
            } else if (isComma) {//僅有逗點
                innerSql = SqlBuilder.comma(value);
            }
            if (isClad) {
                return SqlBuilder.clad(innerSql);
            } else {
                return innerSql;
            }
        }

    }

    public static StringValueSqlHolder createValue() {
        return new StringValueSqlHolder();
    }

    private SqlBuilder(String sql) {
        this.sql = sql;
    }

    public static ISql write(String sql) {
        return new SqlBuilder(sql);
    }

    public static ISql write(String... values) {
        return write(Stream//
                .of(values)//
                .map(SqlBuilder::write)//
                .filter(Objects::nonNull)
                .map(Objects::toString)
                .collect(Collectors.joining()));//
    }


    public static ISql write(ISql... sqls) {
        return write(Stream//
                .of(sqls)//
                .filter(Objects::nonNull)
                .map(Objects::toString)//
                .collect(Collectors.joining()));//
    }

    public static ISql clad(String... value) {
        return write(LC + Stream//
                .of(value)//
                .filter(Objects::nonNull)
                .map(SqlBuilder::write)//
                .map(Objects::toString)
                .collect(Collectors.joining()) + RC);//
    }

    public static ISql commaClad(String... value) {
        return write(LC + Stream//
                .of(value)//
                .filter(Objects::nonNull)
                .map(SqlBuilder::write)//
                .map(Objects::toString)
                .collect(Collectors.joining(COMMA)) + RC);//
    }

    public static ISqlBody body() {
        return new SqlBody();
    }


    public static ISql commaClad(ISql... value) {
        return write(LC + Stream//
                .of(value)//
                .filter(Objects::nonNull)
                .map(SqlBuilder::write)//
                .map(Objects::toString)
                .collect(Collectors.joining(COMMA)) + RC);//
    }

    /**
     * 括弧
     *
     * @param sqls
     * @return
     */
    public static ISql clad(ISql... sqls) {
        return write(LC + Stream//
                .of(sqls)//
                .map(Objects::toString)//
                .collect(Collectors.joining()) + RC);//
    }

    /**
     * 逗點分開
     *
     * @param sqls
     * @return
     */
    public static ISql comma(ISql... sqls) {
        return write(Stream//
                .of(sqls)//
                .map(Objects::toString)//
                .collect(Collectors.joining(COMMA)));//
    }

    public static ISql count(String value) {
        return write(Selects.COUNT, clad(value));//
    }

    public static ISql count(ISql sql) {
        return write(Selects.COUNT, clad(sql));//
    }

    public static ISql max(String value) {
        return write(Selects.MAX, SqlBuilder.write(LC, value, RC));
    }

    public static ISql distinct(String value) {
        return write(Selects.DISTINCT, SqlBuilder.write(value));
    }

    public static ISql min(String value) {
        return write(Selects.MIN, SqlBuilder.write(LC, value, RC));
    }


    /**
     * 逗點分開
     *
     * @param values
     * @return
     */
    public static ISql comma(String... values) {
        return write(Stream//
                .of(values)//
                .map(Objects::toString)//
                .collect(Collectors.joining(COMMA)));//
    }

    public static ISql quotation(ISql sql) {
        return write(Stream//
                .of(sql)//
                .map(i -> QUOTATION + i.toString() + QUOTATION)//
                .collect(Collectors.joining()));//
    }

    public static ISql quotation(String sql) {
        return quotation(SqlBuilder.write(sql));
    }

    public static ISql quotationAndComma(ISql... sqls) {
        return write(Stream//
                .of(sqls)//
                .map(i -> QUOTATION + i.toString() + QUOTATION)//
                .collect(Collectors.joining(COMMA)));//
    }

    public static ISql quotationAndComma(String... value) {
        return write(Stream//
                .of(value)//
                .map(i -> QUOTATION + i + QUOTATION)//
                .collect(Collectors.joining(COMMA)));//
    }

    public static ValueParameter getParameter(String field, ISql operation, ISql value) {
        return new ValueParameter(field, operation, value);
    }
    public static ValueParameter getParameter(String field, String operation, String value) {
        return new ValueParameter(field, write(operation), write(value));
    }

    public static ValueParameter getParameter(String field, String asName, ISql operation, ISql value) {
        return new ValueParameter(field + StringUtils.SPACE + asName, operation, value);
    }


    @Override
    public String toString() {
        return this.sql;
    }

    public static class ValueParameter implements ISql {

        public static final String VALUE_FORMAT = "%s%s%s";
        private ISql sql;

        public ValueParameter(ISql sql) {
            this.sql = sql;
        }

        ValueParameter(String field, ISql operation, ISql value) {
            this.sql = write(String.format(VALUE_FORMAT, field, operation, value));
        }


        @Override
        public String toString() {
            return sql.toString();
        }



    }
}
