package com.nick.nickpicturebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求
 *
 * @Author: Nick
 * @Date: 2025/3/30 下午2:52
 * @Version: 1.0
 * @Description: TODO
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 2751324321088098648L;

    /**
     * 账号
     */
    private String userAccount;
    /**
     * 密码
     */
    private String userPassword;

}
