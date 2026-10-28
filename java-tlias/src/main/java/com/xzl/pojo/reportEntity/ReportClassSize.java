package com.xzl.pojo.reportEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 统计班级人数返回类*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportClassSize {

    private String[] clazzList;
    private Integer[] dataList;

}
