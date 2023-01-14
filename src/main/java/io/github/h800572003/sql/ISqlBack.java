package io.github.h800572003.sql;

/**
 * 有返回sql
 * @param <T>
 */
public interface ISqlBack<T extends ISqlBuilder> {
    /**
     * 回去
     * @return
     */
    T back();

    String build();
}
