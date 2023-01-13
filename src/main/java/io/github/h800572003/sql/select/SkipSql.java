package io.github.h800572003.sql.select;

import io.github.h800572003.sql.ISql;
import org.apache.commons.lang3.StringUtils;

class SkipSql implements ISql {

    @Override
    public String toString() {
        return StringUtils.EMPTY;
    }
}
