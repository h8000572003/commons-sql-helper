package io.github.h800572003.sql.generate;

import io.github.h800572003.sql.generate.GenerateWord;
import io.github.h800572003.sql.generate.IStringSqlMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class StringSqlMapper implements IStringSqlMapper {

    private List<Converter> converters = new ArrayList<>();

    interface Converter {
        List<GenerateWord> converter(String sql);
    }

    class BaseConverter implements Converter {


        public BaseConverter() {

        }

        @Override
        public List<GenerateWord> converter(String sql) {
            String replace = sql.replace("\\s", " ");
            return Arrays.stream(replace.split(" ")).map(GenerateWord::new)
                    .collect(Collectors.toList());
        }


    }


    private void add(Converter converter) {
        this.converters.add(converter);
    }


    public StringSqlMapper() {
        this.converters.add(new BaseConverter());

    }


    @Override
    public List<GenerateWord> mapper(String stringSql) {
        final List<GenerateWord> generateWords = new ArrayList<>();
        this.mapper(stringSql, generateWords);
        return generateWords;

    }

    private void mapper(String sql, List<GenerateWord> generateWords) {
        for (Converter converter : this.converters) {
            generateWords.addAll(converter.converter(sql));
        }
    }


}
