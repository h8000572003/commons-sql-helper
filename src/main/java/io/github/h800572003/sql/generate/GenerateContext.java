package io.github.h800572003.sql.generate;

import com.helger.jcodemodel.*;

import java.util.HashMap;
import java.util.Map;

public class GenerateContext implements IGenerateContext {
    private JCodeModel jCodeModel = new JCodeModel();


    private GenerateWord generateWord;
    private String name;
    private Map<MapperCodes, Object> dataMap = new HashMap<>();

    @Override
    public JCodeModel getModel() {
        return jCodeModel;
    }

    @Override
    public GenerateWord getWord() {
        return generateWord;
    }

    @Override
    public void setWord(GenerateWord word) {
        this.generateWord = word;
    }


    @Override
    public <T> void put(MapperCodes key, T data) {
        dataMap.put(key, data);
    }

    @Override
    public <T> T get(MapperCodes key, Class<T> pClass) {
        return (T) dataMap.get(key);
    }

    public GenerateContext(String name) {
        this.name = name;

    }


}
