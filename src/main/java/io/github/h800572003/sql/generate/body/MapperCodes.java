package io.github.h800572003.sql.generate.body;

public enum MapperCodes {
    VAR,
    BODY,
    INVOKE,


    STATUS,

    INNER_SELECT,
    INNER_FROM,

    INNER_WHERE,
    INNER_WHERE_STATUS;

    public enum Status {
        SELECT_NEW, NONE,//新建
    }

}
