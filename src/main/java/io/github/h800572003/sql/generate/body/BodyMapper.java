package io.github.h800572003.sql.generate.body;

import com.helger.jcodemodel.*;
import com.helger.jcodemodel.writer.JCMWriter;
import com.helger.jcodemodel.writer.OutputStreamCodeWriter;
import io.github.h800572003.sql.*;
import io.github.h800572003.sql.generate.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class BodyMapper implements IGenerateType {




    private List<ICodeWrite> writes = new ArrayList<>();



    class EndWriteCode implements ICodeWrite {
        public EndWriteCode() {
        }

        @Override
        public String generate(String value, IGenerateContext context) {
            if (StringUtils.equalsIgnoreCase(value.trim(), ";")) {
                JInvocation jInvocation = context.get(MapperCodes.INVOKE, JInvocation.class);
                return this.getString(context, jInvocation);
            }
            return StringUtils.EMPTY;
        }

        private String getString(IGenerateContext context, JInvocation jInvocation) {
            JCodeModel model = context.getModel();
            JBlock jBlock = context.get(MapperCodes.BODY, JBlock.class);
            jBlock.decl(model._ref(ISql.class), "sql", jInvocation);

            final JCMWriter jcmWriter = new JCMWriter(context.getModel());
            try {
                jcmWriter.build(new OutputStreamCodeWriter(outputStream, Charset.defaultCharset()));
                return outputStream.toString();
            } catch (IOException e) {
                throw new ISqlGenerateException("輸出異常", e);
            }
        }
    }


    class StringOutputStream extends OutputStream {

        private StringBuilder string = new StringBuilder();

        @Override
        public void write(int b) throws IOException {
            this.string.append((char) b);
        }


        public String toString() {
            return this.string.toString();
        }
    }

    private StringOutputStream outputStream = new StringOutputStream();


    public BodyMapper(SqlRoleMap sqlRoleMap) {
        sqlRoleMap.add(this::lookup);
        writes.add(new AddBodyWriteCode());
        writes.add(new SelectWriteCode());
        writes.add(new SqlOptionWriteCode());
        writes.add(new EndWriteCode());
    }

    public IGenerateType lookup(GenerateWord generateWord) {
        return this;
    }


    @Override
    public String generate(IGenerateContext context) {
        GenerateWord word1 = context.getWord();
        init(context, context.get(MapperCodes.INVOKE, JInvocation.class));
        return writes.stream().map(i -> i.generate(word1.getKey(), context))
                .collect(Collectors.joining());
    }






    private JInvocation init(IGenerateContext context, JInvocation jInvocation) {
        if (jInvocation == null) {
            return initVar(context);
        } else {
            return jInvocation;
        }

    }


    private JInvocation initVar(IGenerateContext context) {
        try {
            JCodeModel model = context.getModel();
            JDefinedClass generate = model._class(JMod.PUBLIC, "generate");
            JMethod sql = generate.method(JMod.PUBLIC, model.VOID, "sql");
            JBlock body = sql.body();
            JInvocation invocation = model.ref(SqlBuilder.class).staticInvoke("body");
            context.put(MapperCodes.INVOKE, invocation);

            context.put(MapperCodes.BODY, body);
            return invocation;
        } catch (JCodeModelException e) {
            throw new ISqlGenerateException("產生異常", e);
        }
    }
}
