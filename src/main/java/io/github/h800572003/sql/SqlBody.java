package io.github.h800572003.sql;

public class SqlBody implements ISqlBody {
    private ISql sql;


    public SqlBody() {
        this.sql = SqlBuilder.write("");
    }
    public SqlBody(ISql...sql) {
        this.sql = SqlBuilder.write(sql);
    }
    public SqlBody(String...sql) {
        this.sql = SqlBuilder.write(sql);
    }

    @Override
    public ISqlBody add(String sql) {
        return new SqlBody(this.sql, SqlBuilder.write(sql));
    }

    @Override
    public String toString() {
        String string = sql.toString();
        return string.replaceAll("\\s+"," ");
    }
}
