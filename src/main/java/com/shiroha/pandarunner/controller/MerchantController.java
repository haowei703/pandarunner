package com.shiroha.pandarunner.controller;

import com.mybatisflex.core.paginate.Page;
import com.shiroha.pandarunner.annotation.SaLoginId;
import com.shiroha.pandarunner.common.R;
import com.shiroha.pandarunner.config.ValidationGroups;
import com.shiroha.pandarunner.domain.dto.MerchantDTO;
import com.shiroha.pandarunner.domain.entity.Merchant;
import com.shiroha.pandarunner.domain.vo.MerchantVO;
import com.shiroha.pandarunner.service.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商家模块")
@RestController
@RequestMapping("/merchant")
@AllArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    /**
     * 检验用户是否注册商家
     *
     * @param userId 用户ID
     * @return {@code true}返回商家ID
     */
    @Operation(summary = "用户是否注册商家")
    @GetMapping
    public R<Long> isMerchant(@SaLoginId Long userId) {
        Merchant merchant = merchantService.getMerchantByUserId(userId);
        return R.ok(merchant.getId());
    }

    /**
     * 添加商家信息
     *
     * @param merchantDTO 商家信息
     */
    @Operation(summary = "添加商家信息")
    @PostMapping("save")
    public R<?> save(@Validated(ValidationGroups.Create.class) @RequestBody MerchantDTO merchantDTO, @SaLoginId Long userId) {
        merchantService.saveMerchant(userId, merchantDTO);
        return R.ok();
    }

    /**
     * 删除商家信息
     *
     * @param merchantId    商家ID
     */
    @Operation(summary = "删除商家信息")
    @DeleteMapping("remove/{id}")
    public R<?> remove(@PathVariable("id") Long merchantId, @SaLoginId Long userId) {
        merchantService.deleteMerchant(userId, merchantId);
        return R.ok();
    }

    /**
     * 更新商家信息
     *
     * @param merchantDTO 商家信息
     */
    @Operation(summary = "更新商家信息")
    @PutMapping("update/{id}")
    public R<?> update(@PathVariable("id") Long merchantId,
                       @Validated(ValidationGroups.Update.class) @RequestBody MerchantDTO merchantDTO,
                       @SaLoginId Long userId) {
        merchantService.updateMerchant(userId, merchantId, merchantDTO);
        return R.ok();
    }

    /**
     * 获取商家信息
     *
     * @param merchantId 商家ID
     */
    @Operation(summary = "获取商家信息")
    @GetMapping("info/{id}")
    public R<Merchant> info(@PathVariable("id") Long merchantId, @SaLoginId Long userId) {
        return R.ok(merchantService.getMerchantInfo(userId, merchantId));
    }

    /**
     * 分页查询商家信息
     *
     * @param addressId         地址ID
     * @param pageNumber        页数
     * @param pageSize          页面大小
     * @param userId            用户ID
     * @return 分页对象
     */
    @Operation(summary = "分页获取推荐商家")
    @GetMapping("page")
    public R<Page<MerchantVO>> page(@RequestParam("address") Long addressId,
                                 @RequestParam("page_num") int pageNumber,
                                 @RequestParam("page_size") int pageSize,
                                 @SaLoginId Long userId) {
        return R.ok(merchantService.getMerchantsByPage(userId, addressId, pageNumber, pageSize));
    }

    @Operation(summary = "获取待审核商家")
    @GetMapping("pending")
    public R<List<Merchant>> getPendingMerchants(@SaLoginId Long userId) {
        return R.ok(merchantService.getAllMerchants(userId));
    }

    /**
     * 审核商家并通过
     *
     * @param merchantId        商家ID
     * @param userId            用户ID
     */
    @Operation(summary = "审核商家")
    @PostMapping("verify/{id}")
    public R<?> verify(@PathVariable("id") Long merchantId, @SaLoginId Long userId) {
        merchantService.verifyMerchant(userId, merchantId);
        return R.ok();
    }

    /**
     * 禁用商家
     * @param merchantId 商家ID
     */
    @Operation(summary = "禁用商家")
    @PostMapping("ban/{id}")
    public R<?> ban(@PathVariable("id") Long merchantId, @SaLoginId Long userId) {
        merchantService.banMerchant(userId, merchantId);
        return R.ok();
    }

    @Operation(summary = "商家接单")
    @PostMapping("accept/{id}")
    public R<?> acceptOrder(@PathVariable("id") Long orderId, @RequestParam(name = "merchant_id") Long merchantId, @SaLoginId Long userId) {
        merchantService.acceptOrder(userId, merchantId, orderId);
        return R.ok();
    }

}
