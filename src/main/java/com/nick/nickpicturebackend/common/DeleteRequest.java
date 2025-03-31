package com.nick.nickpicturebackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用的删除请求类
 *
 * @Author: Nick
 * @Date: 2025/3/9 下午9:41
 * @Version: 1.0
 * @Description: 通用的删除请求类
 */
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
}