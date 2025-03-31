package com.nick.nickpicturebackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nick.nickpicturebackend.constant.UserConstant;
import com.nick.nickpicturebackend.exception.BusinessException;
import com.nick.nickpicturebackend.exception.ErrorCode;
import com.nick.nickpicturebackend.exception.ThrowUtils;
import com.nick.nickpicturebackend.mapper.UserMapper;
import com.nick.nickpicturebackend.model.entity.User;
import com.nick.nickpicturebackend.model.enums.UserRoleEnum;
import com.nick.nickpicturebackend.model.vo.LoginUserVo;
import com.nick.nickpicturebackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @Author: Nick
 * @Date: 2025/3/30 下午12:55
 * @Version: 1.0
 * @Description: 针对表 [user] 的数据库操作 Service实现
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 确认密码
     * @return 用户id
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验参数
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        ThrowUtils.throwIf(userAccount.length() > 16, ErrorCode.PARAMS_ERROR, "用户账号过长");
        ThrowUtils.throwIf(userAccount.length() < 4, ErrorCode.PARAMS_ERROR, "用户账号过短");
        ThrowUtils.throwIf(userPassword.length() < 8, ErrorCode.PARAMS_ERROR, "用户密码过短");
        ThrowUtils.throwIf(!userPassword.equals(checkPassword), ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        // 2. 校验用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        Long count = baseMapper.selectCount(queryWrapper);
        ThrowUtils.throwIf(count > 0, ErrorCode.PARAMS_ERROR, "用户已存在");
        // 3. 密码加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 4. 写入数据库
        User user = new User();
        user.setUserName("默认用户");
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败,数据库错误");
        }
        return user.getId();
    }

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request      请求
     * @return 脱敏后的用户信息
     */
    @Override
    public LoginUserVo userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验参数
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        boolean accountLength = userAccount.length() < 4 || userAccount.length() > 16;
        ThrowUtils.throwIf(accountLength, ErrorCode.PARAMS_ERROR, "用户账号错误");
        boolean passwordLength = userPassword.length() < 8 || userPassword.length() > 16;
        ThrowUtils.throwIf(passwordLength, ErrorCode.PARAMS_ERROR, "用户密码错误");
        // 2. 对用户传递的密码加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 3. 校验用户是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userAccount);
        queryWrapper.eq(User::getUserPassword, encryptPassword);
        User user = baseMapper.selectOne(queryWrapper);
        // 不存在抛出异常
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 4. 记录用户的登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
        return this.getLoginUserVo(user);
    }

    /**
     * 获取加密密码
     *
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    @Override
    public String getEncryptPassword(String userPassword) {
        // 加盐，混淆密码
        String salt = "nick";
        return DigestUtils.md5DigestAsHex((salt + userPassword).getBytes());
    }


    /**
     * 获得脱敏后的用户信息
     *
     * @param user 用户
     * @return 脱敏后的用户信息
     */
    @Override
    public LoginUserVo getLoginUserVo(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtil.copyProperties(user, loginUserVo);
        return loginUserVo;
    }

    /**
     * 获取当前登录用户
     *
     * @param request 请求
     * @return 当前登录用户
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 判断是否已经登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        // 获取登录状态
        boolean loginState = currentUser == null || currentUser.getId() == null;
        ThrowUtils.throwIf(loginState, ErrorCode.NOT_LOGIN_ERROR);
        // 从数据库中再查一遍，避免缓存被清空或被修改
        // 如果追求性能的话可以注释下面代码，直接从session中获取
        currentUser = this.getById(currentUser.getId());
        ThrowUtils.throwIf(currentUser == null, ErrorCode.NOT_LOGIN_ERROR);
        return currentUser;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 判断是否已经登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        ThrowUtils.throwIf(userObj == null, ErrorCode.OPERATION_ERROR, "未登录");
        // 移除登录状态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }

}




