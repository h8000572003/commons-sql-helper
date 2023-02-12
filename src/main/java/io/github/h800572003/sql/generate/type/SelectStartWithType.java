package io.github.h800572003.sql.generate.type;

import io.github.h800572003.sql.Selects;
import io.github.h800572003.sql.SqlBuilder;

/**
 *
 */
public class SelectStartWithType implements IStartWithType {
    private SqlMapper.GenerateAction generateAction;

    public SelectStartWithType(SqlMapper.GenerateAction generateAction) {
        this.generateAction = generateAction;
    }


    @Override
    public void values(GenerateBody generateContext) {




//        SqlBuilder.body()
//                .add("(")
//                .add(Selects.SELECT.name())
//                .add()
//                .add(Selects.FROM.name())
    }
}
