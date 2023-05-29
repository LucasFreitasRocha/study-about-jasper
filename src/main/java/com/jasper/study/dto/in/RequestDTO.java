package com.jasper.study.dto.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestDTO {
    private String nameTemplate ;
    private List<Field> fields = new ArrayList<>();

}
