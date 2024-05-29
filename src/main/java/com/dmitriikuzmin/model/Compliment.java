package com.dmitriikuzmin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compliment {
    private static long lastIndex = 0;
    private long id = lastIndex++;
    private String text;
}
