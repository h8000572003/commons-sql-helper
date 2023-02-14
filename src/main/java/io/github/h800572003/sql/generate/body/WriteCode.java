package io.github.h800572003.sql.generate.body;

import com.helger.jcodemodel.AbstractJClass;
import com.helger.jcodemodel.JInvocation;
import io.github.h800572003.sql.generate.IGenerateContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class WriteCode implements ICodeWrite {
    private Set<String> keys = new HashSet<>();
    private Class<?> pClass;

    public <T extends Enum> WriteCode(Class<?> pClass, T... e) {
        this.pClass = pClass;
        this.keys.addAll(Arrays.stream(e).map(Enum::name).collect(Collectors.toList()));
    }

    @Override
    public String generate(String value, IGenerateContext context) {
        if (keys.contains(value)) {
            JInvocation jInvocation = context.get(MapperCodes.INVOKE, JInvocation.class);
            AbstractJClass ref = context.getModel().ref(this.pClass);
            JInvocation add = jInvocation.invoke("addWithSpace").arg(ref.enumConstantRef(value.toUpperCase()).invoke("name"));
            context.put(MapperCodes.INVOKE, add);
        }
        return StringUtils.EMPTY;
    }
}
