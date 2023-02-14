package io.github.h800572003.sql.generate.body;

import io.github.h800572003.sql.generate.IGenerateContext;

public interface ICodeWrite {
    String generate(String value, IGenerateContext context);
}
