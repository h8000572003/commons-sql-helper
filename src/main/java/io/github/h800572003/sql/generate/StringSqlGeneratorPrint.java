package io.github.h800572003.sql.generate;

import io.github.h800572003.sql.ISqlGenerator;

public class StringSqlGeneratorPrint {

    public static void print(String sql){
      System.out.print(new ISqlGenerator().export("",sql)) ;
    }
}
