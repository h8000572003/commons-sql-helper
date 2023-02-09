package io.github.h800572003.sql;

import io.github.h800572003.sql.generate.*;
import io.github.h800572003.sql.generate.StringSqlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ISqlGenerator {


    private final SqlRoleMap map = new SqlRoleMap();
    private final IStringSqlMapper stringSqlMapper = new StringSqlMapper();


    public String export(String name, String sql) {
        sql = sql.replaceAll("\n", StringUtils.EMPTY);
        sql = sql.replaceAll("\r", StringUtils.EMPTY);
        sql = appendEnd(sql);

        IGenerateContext generateContext = new GenerateContext(name);
        List<GenerateWord> collect = stringSqlMapper.mapper(sql);
        List<String> outPuts = new ArrayList<>();
        collect.forEach(i -> {
            generateContext.setWord(i);
            String generate = map.findKeyWord(i).generate(generateContext);
            if (StringUtils.isNotBlank(generate)) {
                outPuts.add(generate);
            }

        });

        return outPuts.stream().collect(Collectors.joining(""));
    }

    /**
     * 加入結尾符號
     *
     * @param sql
     * @return
     */
    private static String appendEnd(String sql) {
        String newSql = sql;
        if (!StringUtils.endsWith(sql, ";")) {
            newSql += " ;";
        }
        return newSql;
    }
}
