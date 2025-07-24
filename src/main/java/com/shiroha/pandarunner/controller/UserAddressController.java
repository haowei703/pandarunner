package com.shiroha.pandarunner.controller;

import com.shiroha.pandarunner.annotation.SaLoginId;
import com.shiroha.pandarunner.common.R;
import com.shiroha.pandarunner.domain.dto.UserAddressDTO;
import com.shiroha.pandarunner.domain.vo.UserAddressVO;
import com.shiroha.pandarunner.service.MapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.shiroha.pandarunner.service.UserAddressService;

import java.util.List;

@Tag(name = "用户地址模块")
@RestController
@RequestMapping("/address")
@AllArgsConstructor
public class UserAddressController {

    private final UserAddressService userAddressService;
    private final MapService mapService;

    /**
     * 添加用户地址
     *
     * @param userId            用户ID
     * @param userAddressDTO    用户地址DTO
     */
    @Operation(summary = "添加用户地址")
    @PostMapping("save")
    public R<Long> saveUserAddress(@SaLoginId Long userId, @Validated @RequestBody UserAddressDTO userAddressDTO) {
        return R.ok(userAddressService.saveUserAddress(userId, userAddressDTO));
    }

    /**
     * 根据主键删除用户地址
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @Operation(summary = "删除用户地址")
    @DeleteMapping("remove/{id}")
    public R<?> removeUserAddressById(@SaLoginId Long userId, @PathVariable Long id) {
        userAddressService.deleteUserAddress(userId, id);
        return R.ok();
    }

    /**
     * 根据主键更新用户地址表。
     *
     * @param userId            用户ID
     * @param addressId         地址ID
     * @param userAddressDTO    用户地址DTO
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @Operation(summary = "更新用户地址")
    @PutMapping("update/{id}")
    public R<?> update(
            @SaLoginId Long userId,
            @PathVariable("id") Long addressId,
            @Validated @RequestBody UserAddressDTO userAddressDTO) {
        userAddressService.updateUserAddress(userId, addressId, userAddressDTO);
        return R.ok();
    }

    /**
     * 查询用户所有地址
     *
     * @param userId        用户ID
     * @return 所有数据
     */
    @Operation(summary = "查询用户全部地址")
    @GetMapping("list")
    public R<List<UserAddressVO>> list(@SaLoginId Long userId) {
        return R.ok(userAddressService.getUserAddressesByUserId(userId));
    }

    /**
     * 查询三级地址
     *
     * @return      三级地址
     */
    @Operation(summary = "查询三级地址")
    @GetMapping("distinct")
    public R<?> getDistinct() {
        return R.ok(mapService.getDistricts());
    }

    /**
     * 获取两个经纬度点的距离
     *
     * @param origins           起始点坐标
     * @param destination       终点坐标
     * @return 两点的距离
     */
    @Operation(summary = "获取两个经纬度点的距离")
    @GetMapping("distance")
    public R<?> getDistance(@RequestParam(name = "origins") String origins, @RequestParam(name = "destination") String destination) {
        return R.ok(mapService.getDistance(origins, destination));
    }
}
