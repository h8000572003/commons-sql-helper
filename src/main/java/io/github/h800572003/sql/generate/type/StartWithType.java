package io.github.h800572003.sql.generate.type;

import com.helger.jcodemodel.*;
import io.github.h800572003.sql.SqlBuilder;

public class StartWithType implements IStartWithType{
    private SqlMapper.GenerateAction generateAction;

    public StartWithType(SqlMapper.GenerateAction generateAction) {
        this.generateAction = generateAction;
    }

    @Override
    public void values(GenerateBody generateContext) {

        JCodeModel jCodeModel = generateContext.getjCodeModel();

        try {
            JDefinedClass sqlTest = jCodeModel._class(JMod.PUBLIC, "SqlTest");

            JMethod sql = sqlTest.method(JMod.PUBLIC, jCodeModel.VOID, "sql");






        } catch (JCodeModelException e) {
            throw new RuntimeException(e);
        }
    }
}
