package io.github.h800572003.sql.generate.body;

import io.github.h800572003.sql.select.SqlOption;

class SqlOptionWriteCode extends WriteCode {

    public SqlOptionWriteCode() {
        super(SqlOption.class, SqlOption.values());
    }


}
