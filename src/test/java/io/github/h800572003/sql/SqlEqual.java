package io.github.h800572003.sql;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class SqlEqual {


    public static boolean isEqual(String sql, String sql2) {
        String sqlCommand = getSqlCommand(sql);
        String sqlCommand1 = getSqlCommand(sql2);
        log.info("sql1:{}",sqlCommand);
        log.info("sql2:{}",sqlCommand1);
        return StringUtils.equalsIgnoreCase(sqlCommand, sqlCommand1);

    }

    private static String getSqlCommand(String sql) {
        Pattern compile = Pattern.compile("\\S+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = compile.matcher(sql);
        final List<String> sqls = new ArrayList<>();
        while (matcher.find()) {
            String group1 = matcher.group();
            sqls.add(group1);
        }
        return sqls.stream().collect(Collectors.joining("_"));
    }
}
