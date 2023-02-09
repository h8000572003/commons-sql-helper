package io.github.h800572003.sql.generate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class SqlRoleMap {

    private List<Function<GenerateWord, IGenerateType>> generateTypes = new ArrayList<>();


   public SqlRoleMap(){
       this.add(new BodyMapper(this)::lookup);
    }
    public void add(Function<GenerateWord, IGenerateType> function) {
        this.generateTypes.add(function);
    }

    public IGenerateType findKeyWord(GenerateWord key) {
        Optional<IGenerateType> any = generateTypes.stream().map(i -> i.apply(key))
                .filter(Objects::nonNull)
                .findAny();
        if (!any.isPresent()) {
            throw new ISqlGenerateException("查無此關鍵字" + key);
        }
        return any.get();
    }
}
