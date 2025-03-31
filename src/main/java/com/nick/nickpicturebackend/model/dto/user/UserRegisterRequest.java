package com.nick.nickpicturebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求
 *
 * @Author: Nick
 * @Date: 2025/3/30 下午12:55
 * @Version: 1.0
 * @Description: TODO
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -2446499961188278297L;

    /**
     * 账号
     */
    private String userAccount;
    /**
     * 密码
     */
    private String userPassword;
    /**
     * 确认密码
     */
    private String checkPassword;

}
