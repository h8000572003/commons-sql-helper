package io.github.h800572003.sql.generate;

import com.helger.jcodemodel.JCodeModel;
import io.github.h800572003.sql.generate.body.MapperCodes;

public interface IGenerateContext {




    JCodeModel getModel();

    GenerateWord getWord();

    void setWord(GenerateWord word);

    <T> void put(MapperCodes key, T data);

    <T> T get(MapperCodes key, Class<T> pClass);
}
