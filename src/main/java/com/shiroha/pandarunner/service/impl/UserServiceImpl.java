package com.shiroha.pandarunner.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shiroha.pandarunner.converter.UserConverter;
import com.shiroha.pandarunner.domain.dto.UserDTO;
import com.shiroha.pandarunner.domain.entity.User;
import com.shiroha.pandarunner.domain.vo.UserVO;
import com.shiroha.pandarunner.exception.*;
import com.shiroha.pandarunner.mapper.UserMapper;
import com.shiroha.pandarunner.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.shiroha.pandarunner.domain.entity.table.UserTableDef.USER;

/**
 * 用户表 服务层实现。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserConverter converter;
    private final RedisTemplate<String, String> redisTemplate;

    // 角色-权限映射表
    private static final Map<User.Role, List<String>> ROLE_PERMISSIONS_MAP = Map.of(
            User.Role.USER, List.of("user:read", "user:edit"),
            User.Role.ADMIN, List.of("user:read", "user:edit", "admin:manage"),
            User.Role.SUPER_ADMIN, List.of("user:*", "admin:*", "system:*")
    );

    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return 用户
     */
    private User getUserByUsername(String username) {
        return getOne(query().where(USER.USERNAME.eq(username)));
    }

    /**
     * 根据电话获取用户
     *
     * @param phone 电话
     * @return 用户
     */
    private User getUserByPhone(String phone) {
        return getOne(query().where(USER.PHONE.eq(phone)));
    }

    /**
     * 获取SaToken登录ID
     *
     * @return 用户ID
     */
    @Override
    public Long getLoginId() {
        return StpUtil.getLoginIdAsLong();
    }

    /**
     * 根据ID查询用户
     *
     * @param userId 用户ID
     * @return 用户
     */
    @Override
    public User getUserById(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new UserNotFoundException("用户不存在");
        }
        return user;
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户登录令牌
     */
    @Override
    public SaTokenInfo login(String username, String password) {
        User user = getUserByUsername(username);
        if(!DigestUtil.bcryptCheck(password, user.getPassword())) {
            throw new CredentialsIncorrectException("密码错误");
        }

        StpUtil.login(user.getId());
        return StpUtil.getTokenInfo();
    }

    /**
     * 注册
     *
     * @param userDTO DTO对象
     */
    @Override
    public void register(UserDTO userDTO) {
        String code = redisTemplate.opsForValue().get(userDTO.getPhone());
        if(code == null || !code.equals(userDTO.getCode())) {
            throw new CodeExpiredException("验证码已过期或错误");
        }

        User user;
        user = getUserByUsername(userDTO.getUsername());
        if(user != null) {
            throw new AuthException("用户名已经存在");
        }

        user = getUserByPhone(userDTO.getPhone());
        if(user != null) {
            throw new AuthException("该手机号已经注册，请不要重复注册");
        }

        user = converter.userDtoToUser(userDTO);
        user.setPassword(DigestUtil.bcrypt(userDTO.getPassword()));
        save(user);
    }

    /**
     * 登出
     */
    @Override
    public void logout() {
        StpUtil.logout();
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @Override
    public UserVO getUserInfo(Long userId) {
        Long loginId = getLoginId();

        if(userId == null) {
            userId = loginId;
        }

        // 非本人访问时校验权限
        if(!userId.equals(loginId)) {
            checkPermission(loginId, User.Role.ADMIN);
        }
        User user = getUserById(userId);
        return converter.userToUserVO(user);
    }

    /**
     * 更新用户信息
     *
     * @param userDTO DTO对象
     */
    @Override
    public void updateUserInfo(UserDTO userDTO) {
        User user = converter.userDtoToUser(userDTO);
        // 设置用户id
        Long userId = getLoginId();
        user.setId(userId);
        // 排除敏感字段
        user.setUsername(null);
        user.setPassword(null);
        user.setPhone(null);
        updateById(user, true);
    }

    /**
     * 更新密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        Long userId = getLoginId();
        User user = getUserById(userId);

        if(!DigestUtil.bcryptCheck(oldPassword, user.getPassword())) {
            throw new CredentialsIncorrectException("密码错误");
        }

        // 旧密码匹配，更新密码
        user.setPassword(DigestUtil.bcrypt(newPassword));
        updateById(user);
    }

    /**
     * 发送验证码
     *
     * @param phone 手机号
     * @return 验证码
     */
    @Override
    public String sendCode(String phone) {
        String code = generateCode();
        // redis保存验证码，有效期一分钟
        redisTemplate.opsForValue().set(phone, code, Duration.ofMinutes(1));
        return code;
    }

    /**
     * 忘记密码
     *
     * @param phone       电话
     * @param code        验证码
     * @param newPassword 新密码
     */
    @Override
    public void forgetPassword(String phone, String code, String newPassword) {
        String redisCode = redisTemplate.opsForValue().get(phone);
        if(redisCode == null || !redisCode.equals(code)) {
            throw new CodeExpiredException("验证码已过期或错误");
        }

        User user = getUserByPhone(phone);
        user.setPassword(DigestUtil.bcrypt(newPassword));
        updateById(user);
    }

    /**
     * 检查用户权限
     *
     * @param userId    用户ID
     * @param roles     角色列表
     */
    @Override
    public void checkPermission(Long userId, User.Role ...roles) {
        String roleName = getRoleList(userId, null).getFirst();
        User.Role roleEnum = User.Role.valueOf(roleName);

        boolean checked = false;
        // 遍历角色列表，满足其中一个角色要求时则可以
        for(User.Role role : roles) {
            if (roleEnum.equals(role)) {
                checked = true;
                break;
            }
        }
        if(!checked) {
            throw new UserNotPermissionException(String.format("[用户没有权限] " +
                    "需要的角色%s 用户角色%s", Arrays.toString(roles), roleName));
        }
    }

    /**
     * 检查用户权限
     *
     * @param userId     用户ID
     * @param permission 需要的权限
     */
    @Override
    public void checkPermission(Long userId, String permission) {
        String roleName = getRoleList(userId, null).get(0);
        User.Role roleEnum = User.Role.valueOf(roleName);

        boolean checked = false;
        List<String> permissionList = ROLE_PERMISSIONS_MAP.get(roleEnum);
        // 遍历权限列表，有匹配则验证通过
        for(String pm : permissionList) {
            if (permission.equals(pm)) {
                checked = true;
                break;
            }
        }
        if(!checked) {
            throw new UserNotPermissionException(String.format("[用户没有权限] " +
                    "需要的权限%s 用户权限%s", permission, permissionList));
        }
    }

    /**
     * 生成随机6位验证码
     */
    private String generateCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        if(!(loginId instanceof Long userId)) {
            throw new InternalServiceException("内部服务错误");
        }

        User user = getUserById(userId);
        return ROLE_PERMISSIONS_MAP.get(user.getRole());
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        if(!(loginId instanceof Long userId)) {
            throw new InternalServiceException("内部服务错误");
        }

        User user = getUserById(userId);
        return List.of(user.getRole().name());
    }
}
