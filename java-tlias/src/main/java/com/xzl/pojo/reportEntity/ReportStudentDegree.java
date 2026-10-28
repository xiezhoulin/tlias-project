package com.xzl.pojo.reportEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 统计学员的学历人数返回类*/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportStudentDegree {

    private String name;
    private Integer value;

}
