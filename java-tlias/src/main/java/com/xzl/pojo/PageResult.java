package com.xzl.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** 分页统一返回类*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult {
    private Long total; //总记录数
    private List rows; //当前页数据列表
}
