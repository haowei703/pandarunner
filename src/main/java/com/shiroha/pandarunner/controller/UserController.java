package com.shiroha.pandarunner.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaTokenInfo;
import com.shiroha.pandarunner.common.R;
import com.shiroha.pandarunner.config.ValidationGroups;
import com.shiroha.pandarunner.domain.dto.UserDTO;
import com.shiroha.pandarunner.domain.vo.UserLoginVO;
import com.shiroha.pandarunner.domain.vo.UserVO;
import com.shiroha.pandarunner.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户模块")
@Slf4j
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 用户登录
     */
    @Operation(summary = "登录")
    @SaIgnore
    @PostMapping("login")
    public R<SaTokenInfo> login(@Validated @RequestBody UserLoginVO req) {
        SaTokenInfo tokenInfo = userService.login(req.getUsername(), req.getPassword());
        return R.ok(tokenInfo);
    }

    /**
     * 用户注册
     */
    @Operation(summary = "注册")
    @SaIgnore
    @PostMapping("register")
    public R<Void> register(@Validated(ValidationGroups.Create.class) @RequestBody UserDTO userDTO) {
        userService.register(userDTO);
        return R.ok();
    }

    /**
     * 发送验证码
     */
    @Operation(summary = "发送验证码")
    @SaIgnore
    @GetMapping("code")
    public R<Void> code(@RequestParam(name = "phone") String phone) {
        String code = userService.sendCode(phone);
        log.info("[验证码发送] 手机号: {}, 验证码：{}", phone, code);
        return R.ok();
    }

    /**
     * 用户登出
     */
    @Operation(summary = "登出")
    @PostMapping("logout")
    public R<Void> logout() {
        userService.logout();
        return R.ok();
    }

    /**
     * 获取当前用户信息
     */
    @Operation(summary = "获取用户信息")
    @GetMapping("info")
    public R<UserVO> getUserInfo(@RequestParam(name = "id", required = false) Long userId) {
        return R.ok(userService.getUserInfo(userId));
    }

    /**
     * 更新用户信息
     */
    @Operation(summary = "更新用户信息")
    @PutMapping("update")
    @SaCheckLogin
    public R<Void> updateUser(@Validated(ValidationGroups.Update.class) @RequestBody UserDTO userDTO) {
        userService.updateUserInfo(userDTO);
        return R.ok();
    }

    /**
     * 更新密码
     */
    @Operation(summary = "更新密码")
    @PutMapping("updatePwd")
    public R<Void> updatePassword(@RequestParam(name = "old_pwd") String oldPwd, @RequestParam(name = "new_pwd") String newPwd) {
        userService.updatePassword(oldPwd, newPwd);
        return R.ok();
    }

    /**
     * 忘记密码
     */
    @Operation(summary = "忘记密码")
    @SaIgnore
    @PutMapping("forgetPwd")
    public R<Void> forgetPassword(@RequestParam(name = "phone") String phone, @RequestParam(name = "code") String code, @RequestParam(name = "new_pwd") String newPwd) {
        userService.forgetPassword(phone, code, newPwd);
        return R.ok();
    }
}
