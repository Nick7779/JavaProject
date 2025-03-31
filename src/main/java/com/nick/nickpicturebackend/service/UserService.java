package com.nick.nickpicturebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nick.nickpicturebackend.model.entity.User;
import com.nick.nickpicturebackend.model.vo.LoginUserVo;

import javax.servlet.http.HttpServletRequest;


/**
 * 用户服务
 *
 * @Author: Nick
 * @Date: 2025/3/30 下午12:55
 * @Version: 1.0
 * @Description: 针对表 [user] 的数据库操作 Service实现
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 确认密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request      请求
     * @return 脱敏后的用户信息
     */
    LoginUserVo userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取加密密码
     *
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String userPassword);

    /**
     * 获得脱敏后的用户信息
     *
     * @param user 用户
     * @return 脱敏后的用户信息
     */
    LoginUserVo getLoginUserVo(User user);

    /**
     * 获取当前登录用户
     *
     * @param request 请求
     * @return 当前登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 退出登录
     *
     * @param request 请求
     * @return 退出登录
     */
    boolean userLogout(HttpServletRequest request);

}
