package io.github.h800572003.sql;

public interface ISqlBody extends ISql{

    /**
     * 加入
     * @param sql
     * @return
     */
    ISqlBody addSpace(String sql);
    ISqlBody add(String sql);
}
