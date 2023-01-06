package io.github.h800572003.sql;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlBuilder implements ISql {
    private final String sql;

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
                .map(Objects::toString)
                .collect(Collectors.joining()));//
    }

    public static ISql write(ISql... sqls) {
        return write(Stream//
                .of(sqls)//
                .map(Objects::toString)//
                .collect(Collectors.joining()));//
    }

    public static ISql clad(String... value) {
        return write("(" + Stream//
                .of(value)//
                .map(SqlBuilder::write)//
                .map(Objects::toString)
                .collect(Collectors.joining()) + ")");//
    }

    public static ISql commaClad(String... value) {
        return write("(" + Stream//
                .of(value)//
                .map(SqlBuilder::write)//
                .map(Objects::toString)
                .collect(Collectors.joining(",")) + ")");//
    }

    public static ISql commaClad(ISql... value) {
        return write("(" + Stream//
                .of(value)//
                .map(SqlBuilder::write)//
                .map(Objects::toString)
                .collect(Collectors.joining(",")) + ")");//
    }

    /**
     * 括弧
     *
     * @param sqls
     * @return
     */
    public static ISql clad(ISql... sqls) {
        return write("(" + Stream//
                .of(sqls)//
                .map(Objects::toString)//
                .collect(Collectors.joining()) + ")");//
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
                .collect(Collectors.joining(",")));//
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
                .collect(Collectors.joining(",")));//
    }

    public static ISql quotation(ISql sql) {
        return write(Stream//
                .of(sql)//
                .map(i -> "'" + i.toString() + "'")//
                .collect(Collectors.joining()));//
    }

    public static ISql quotation(String sql) {
        return quotation(SqlBuilder.write(sql));
    }

    public static ISql quotationAndComma(ISql... sqls) {
        return write(Stream//
                .of(sqls)//
                .map(i -> "'" + i.toString() + "'")//
                .collect(Collectors.joining(",")));//
    }

    public static ISql quotationAndComma(String... value) {
        return write(Stream//
                .of(value)//
                .map(i -> "'" + i + "'")//
                .collect(Collectors.joining(",")));//
    }

    public static ValueParameter getParameter(String field, ISql operation, ISql value){
        return new ValueParameter(field,operation,value);
    }

    @Override
    public String toString() {
        return this.sql;
    }

    public static class ValueParameter implements ISql {

        private ISql sql;

        public ValueParameter(ISql sql) {
            this.sql = sql;
        }

        ValueParameter(String field, ISql operation, ISql value) {
            this.sql = write(String.format("%s%s%s", field, operation, value));
        }


        @Override
        public String toString() {
            return sql.toString();
        }


    }
}
