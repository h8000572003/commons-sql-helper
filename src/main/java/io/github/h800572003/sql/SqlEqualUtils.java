package io.github.h800572003.sql;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class SqlEqualUtils {

    public static final String IS_BLANK = "\\S+";

    /**
     *
     * @param actual 事實
     * @param expected 期望
     * @return
     */
    public static boolean isEqual(ISql actual, String expected) {
        return isEqual(actual.toString().trim().toUpperCase(),expected.trim().toUpperCase());
    }

    /**
     *
     * @param actual 事實
     * @param expected 期望
     * @return
     */
    public static boolean isEqual(String actual, String expected) {
        String sqlCommand = getSqlCommand(actual.trim());
        String sqlCommand1 = getSqlCommand(expected.trim());
        log.info("exp:{}",sqlCommand);
        log.info("ans:{}",sqlCommand1);
        return StringUtils.equalsIgnoreCase(sqlCommand, sqlCommand1);

    }

    private static String getSqlCommand(String sql) {
        Pattern compile = Pattern.compile(IS_BLANK, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compile.matcher(sql);
        final List<String> sqls = new ArrayList<>();
        while (matcher.find()) {
            String group1 = matcher.group();
            sqls.add(group1);
        }
        return sqls.stream().collect(Collectors.joining(" "));
    }
}
