package io.github.h800572003.sql.generate.type;

public class EndStartWithType implements IStartWithType{
    private SqlMapper.GenerateAction generateAction;

    public EndStartWithType(SqlMapper.GenerateAction generateAction) {
        this.generateAction = generateAction;
    }

    @Override
    public void values(GenerateBody generateContext) {

    }
}
