package com.shiroha.pandarunner.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpInterface;
import com.mybatisflex.core.service.IService;
import com.shiroha.pandarunner.domain.dto.UserDTO;
import com.shiroha.pandarunner.domain.entity.User;
import com.shiroha.pandarunner.domain.vo.UserVO;

/**
 * 用户表 服务层。
 *
 * @author haowei703
 * @since 2025-07-03
 */
public interface UserService extends IService<User>, StpInterface {

    /**
     * 获取SaToken登录ID
     *
     * @return 用户ID
     */
    Long getLoginId();

    /**
     * 根据ID查询用户
     *
     * @param userId 用户ID
     * @return 用户
     */
    User getUserById(Long userId);

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户登录令牌
     */
    SaTokenInfo login(String username, String password);

    /**
     * 注册
     *
     * @param userDTO DTO对象
     */
    void register(UserDTO userDTO);

    /**
     * 登出
     */
    void logout();

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    UserVO getUserInfo(Long userId);

    /**
     * 更新用户信息
     *
     * @param userDTO DTO对象
     */
    void updateUserInfo(UserDTO userDTO);

    /**
     * 更新密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void updatePassword(String oldPassword, String newPassword);

    /**
     * 发送验证码
     *
     * @param phone 手机号
     * @return 验证码
     */
    String sendCode(String phone);

    /**
     * 忘记密码
     *
     * @param phone 电话
     * @param code 验证码
     * @param newPassword 新密码
     */
    void forgetPassword(String phone, String code, String newPassword);

    /**
     * 检查用户权限
     *
     * @param userId        用户ID
     * @param roles         角色列表
     */
    void checkPermission(Long userId, User.Role ...roles);

    /**
     * 检查用户权限
     *
     * @param userId        用户ID
     * @param permission    需要的权限
     */
    void checkPermission(Long userId, String permission);
}
