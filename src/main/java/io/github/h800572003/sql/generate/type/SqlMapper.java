package io.github.h800572003.sql.generate.type;

import io.github.h800572003.sql.Selects;
import io.github.h800572003.sql.SqlBuilder;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class SqlMapper {

    public static final String REG = "\\$";
    private List<IKeyReplace> replaces = new ArrayList<>();

    private Map<String, IStartWithType> typeMaps = new HashMap<>();

    class KeyReplace implements IKeyReplace {

        private final String key;

        public KeyReplace(String key) {
            this.key = key;
        }


        public String getKey() {
            return key;
        }

        public String getReplace(String sql) {
            return sql.replaceAll("(?i)" + key + "", "\\$" + key);
        }
    }



    interface Generate {
        void generate(GenerateBody context);
    }

    @ToString
    public class GenerateAction {


        final Map<String, IStartWithType> generateMap = new HashMap<>();

        public GenerateAction() {
            this.generateMap.put("@START",new StartWithType(this));
            this.generateMap.put("SELECT",new SelectStartWithType(this));
            this.generateMap.put("FROM",new FromStartWithType(this));
            this.generateMap.put("UNION",new UnuiontStartWithType(this));
            this.generateMap.put("JOIN",new JoinStartWithType(this));
            this.generateMap.put("ORDER",new OrderStartWithType(this));
            this.generateMap.put("GROUP",new GroupStartWithType(this));
            this.generateMap.put(";",new EndStartWithType(this));
        }

        private String key = "";
        private String[] values;


        public GenerateAction(String values) {

            String[] split = values.split(StringUtils.SPACE);
            this.key = split[0].toUpperCase();
            if (split.length > 1) {
                this.values = new String[split.length - 1];
                System.arraycopy(split, 1, this.values, 0, split.length - 1);
            }
        }

        public String getKey() {
            return key;
        }

        public String[] getValues() {
            return values;
        }


        public void generate(GenerateBody context) {
            generateMap.get(key).values(context);
        }
    }

    public interface IKeyReplace {


        String getReplace(String text);


    }

    public void add(String startWith, IStartWithType startWithType) {
        typeMaps.put(startWith, startWithType);
    }


    public SqlMapper() {
        this.add(new KeyReplace("@START"));
        this.add(new KeyReplace("SELECT"));
        this.add(new KeyReplace("FROM"));
        this.add(new KeyReplace("UNION"));
        this.add(new KeyReplace("JOIN"));
        this.add(new KeyReplace("ORDER BY"));
        this.add(new KeyReplace("GROUP BY"));
        this.add(new KeyReplace(";"));

    }

    public void add(IKeyReplace sqlTypeMapper) {
        this.replaces.add(sqlTypeMapper);
    }

    public List<GenerateAction> get(String sql) {
        if(!sql.startsWith("@START")){
            sql="@START "+sql;
        }
        if(!sql.endsWith(";")){
            sql+=" ;";
        }

        sql = sql.replaceAll("\r+", " ").replace("\n", "");
        for (IKeyReplace sqlTypeMapper : this.replaces) {
            sql = sqlTypeMapper.getReplace(sql);
        }

        String[] split = sql.split(REG);
        return Arrays.stream(sql.split(REG))

                .map(String::trim)
                .map(i -> i.replaceAll("\\s+", StringUtils.SPACE))
                .filter(StringUtils::isNotBlank)
                .map(this::to)
                .collect(Collectors.toList());

    }

    private GenerateAction to(String values) {
        log.info("values:{}", values);
        return new GenerateAction(values);
    }

    public static void main(String[] args) {


        String sql = "(select id,name  from student a  join student_course sc \n" +
                "on a.id =sc.student_id and a.id=sc.student_id \n" +
                "where  a.id='A' group by a.id,a.name order by a.id \n" +
                "UNION\n" +
                "select id,name  from student a  join student_course sc \n" +
                "on a.id =sc.student_id and a.id=sc.student_id \n" +
                "where  a.id='A' group by a.id,a.name order by a.id) ";

        List<GenerateAction> sqlTypes = new SqlMapper().get(sql);


        sqlTypes.forEach(i -> log.info("{}", i));





    }
}
