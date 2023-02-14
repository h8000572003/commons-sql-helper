package io.github.h800572003.sql;

import java.util.function.BooleanSupplier;

public interface ISqlBody extends ISql {

    /**
     * 加入
     *
     * @param sql
     * @return
     */
    ISqlBody addWithSpace(String sql);

    ISqlBody addWithSpace(ISql sql);


    ISqlBody add(String sql);

    ISqlBody add(ISql sql);

     ISqlBody addWithSpace(ISql sql, BooleanSupplier supplier);
    ISqlBody add(ISql sql, BooleanSupplier supplier);

    ISqlBody addWithSpace(String sql, BooleanSupplier supplier);
    ISqlBody add(String sql, BooleanSupplier supplier);

}
