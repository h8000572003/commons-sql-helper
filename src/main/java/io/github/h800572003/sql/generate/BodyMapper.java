package io.github.h800572003.sql.generate;

import com.helger.jcodemodel.*;
import com.helger.jcodemodel.writer.JCMWriter;
import com.helger.jcodemodel.writer.OutputStreamCodeWriter;
import io.github.h800572003.sql.ISql;
import io.github.h800572003.sql.SqlBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

@Slf4j
public class BodyMapper implements IGenerateType {

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
    private  StringOutputStream outputStream=new StringOutputStream();


    public BodyMapper(SqlRoleMap sqlRoleMap) {
        sqlRoleMap.add(this::lookup);
    }

    public IGenerateType lookup(GenerateWord generateWord) {
        return this;
    }


    @Override
    public String generate(IGenerateContext context) {
        JInvocation jInvocation = context.get(MapperCodes.INVOKE, JInvocation.class);
        GenerateWord word1 = context.getWord();
        jInvocation = initi(context, jInvocation);
        if (!word1.getKey().equalsIgnoreCase(";")) {
            final String key = word1.getKey();
            log.info("key:{}", key);
            JInvocation add = jInvocation.invoke("add").arg(StringUtils.SPACE+word1.getKey()+StringUtils.SPACE);
            context.put(MapperCodes.INVOKE, add);
        } else {
            return getString(context, jInvocation);
        }
        return StringUtils.EMPTY;

    }

    private String getString(IGenerateContext context, JInvocation jInvocation) {
        JCodeModel model = context.getModel();
        JBlock jBlock = context.get(MapperCodes.BODY, JBlock.class);
        jBlock.decl(model._ref(ISql.class),"sql",jInvocation);

        final JCMWriter jcmWriter = new JCMWriter(context.getModel());
        try {
            jcmWriter.build(new OutputStreamCodeWriter(outputStream, Charset.defaultCharset()));
            return outputStream.toString();
        } catch (IOException e) {
            throw new ISqlGenerateException("輸出異常", e);
        }
    }

    private JInvocation initi(IGenerateContext context, JInvocation jInvocation) {
        if (jInvocation == null) {
            return initVar(context);
        } else {
            return jInvocation;
        }

    }

    public void sql() {
        SqlBuilder.body().add("select")
                .add("c1,c2,c3")//
                .add("from")
                .add("table");
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
