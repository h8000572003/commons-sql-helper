package io.github.h800572003.sql;

/**
 * 跳回Builder
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
