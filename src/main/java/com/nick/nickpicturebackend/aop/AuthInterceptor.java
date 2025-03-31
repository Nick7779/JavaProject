package com.nick.nickpicturebackend.aop;

import com.nick.nickpicturebackend.annotation.AuthCheck;
import com.nick.nickpicturebackend.exception.BusinessException;
import com.nick.nickpicturebackend.exception.ErrorCode;
import com.nick.nickpicturebackend.model.entity.User;
import com.nick.nickpicturebackend.model.enums.UserRoleEnum;
import com.nick.nickpicturebackend.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 定义
 *
 * @Author: Nick
 * @Date: 2025/3/30 下午6:59
 * @Version: 1.0
 * @Description: TODO
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;


    /* @Around() 环绕切点,前后执行逻辑
    @Before() 在切点前执行
    @After() 在切点后执行 */
    /**
     * 执行拦截
     *
     * @param joinPoint 切入点
     * @param authCheck 权限校验注解
     */
    @Around("@annotation(authCheck)")
    public Object doIntercept(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        // 获取 @AuthCheck 注解中定义的角色权限内容
        String mustRole = authCheck.mustRole();
        // 获取当前请求的所有属性
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        // 从上下文中获取当前的 HttpServletRequest 对象
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 获取当前登录用户的信息
        User loginUser = userService.getLoginUser(request);
        // 将字符串形式的角色转换为枚举类型
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        // 如果不需要特定权限（mustRole 为空或无效），直接放行
        if (mustRoleEnum == null) {
            return joinPoint.proceed();
        }
        // 获取当前登录用户的实际角色，并进行权限校验
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        // 如果用户的角色为空（未定义或无效），抛出无权限异常
        if (userRoleEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 如果需要管理员权限，但当前用户不是管理员，抛出无权限异常
        if (UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 如果通过了所有权限校验，执行被拦截的方法
        return joinPoint.proceed();
    }

}
