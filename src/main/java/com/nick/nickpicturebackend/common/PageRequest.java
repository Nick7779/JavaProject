package com.nick.nickpicturebackend.common;

import lombok.Data;

/**
 * 通用的分页请求类
 *
 * @Author: Nick
 * @Date: 2025/3/9 下午9:41
 * @Version: 1.0
 * @Description: 通用的分页请求类
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private int current = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = "descend";
}