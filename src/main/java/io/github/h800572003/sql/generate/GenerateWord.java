package io.github.h800572003.sql.generate;

import lombok.Data;

@Data
public class GenerateWord {
    private String key = "";


    public GenerateWord(String key) {
        this.key = key;
    }

}
