package com.xzl.pojo.reportEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 员工职位人数统计实体*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportEmpJob {
    private String[] jobList; // 员工职位名称
    private Integer[] dataList; // 员工各职位人数
}
