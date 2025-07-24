package com.shiroha.pandarunner.controller;

import com.mybatisflex.core.paginate.Page;
import com.shiroha.pandarunner.annotation.SaLoginId;
import com.shiroha.pandarunner.common.R;
import com.shiroha.pandarunner.config.ValidationGroups;
import com.shiroha.pandarunner.domain.dto.DeliveryRiderDTO;
import com.shiroha.pandarunner.domain.entity.DeliveryRider;
import com.shiroha.pandarunner.domain.vo.AvailableOrderVO;
import com.shiroha.pandarunner.domain.vo.DeliveryOrderVO;
import com.shiroha.pandarunner.domain.vo.DeliveryRiderVO;
import com.shiroha.pandarunner.service.DeliveryRiderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "配送骑手模块")
@RestController
@RequestMapping("/delivery/rider")
@AllArgsConstructor
public class DeliveryRiderController {

    private final DeliveryRiderService deliveryRiderService;

    /**
     * 用户注册为骑手
     *
     * @param deliveryRiderDTO 骑手信息
     * @param userId 用户ID
     * @return 骑手ID
     */
    @Operation(summary = "注册为骑手")
    @PostMapping("register")
    public R<Long> registerRider(@Validated(ValidationGroups.Create.class) @RequestBody DeliveryRiderDTO deliveryRiderDTO,
                                @SaLoginId Long userId) {
        Long riderId = deliveryRiderService.registerRider(userId, deliveryRiderDTO);
        return R.ok(riderId);
    }

    /**
     * 检查用户是否已注册骑手
     *
     * @param userId 用户ID
     * @return 骑手信息
     */
    @Operation(summary = "检查是否已注册骑手")
    @GetMapping("check")
    public R<DeliveryRiderVO> checkRider(@SaLoginId Long userId) {
        DeliveryRider rider = deliveryRiderService.getRiderByUserId(userId);
        if (rider == null) {
            return R.ok(null);
        }
        DeliveryRiderVO riderInfo = deliveryRiderService.getRiderInfo(userId);
        return R.ok(riderInfo);
    }

    /**
     * 获取骑手信息
     *
     * @param userId 用户ID
     * @return 骑手信息
     */
    @Operation(summary = "获取骑手信息")
    @GetMapping("info")
    public R<DeliveryRiderVO> getRiderInfo(@SaLoginId Long userId) {
        DeliveryRiderVO riderInfo = deliveryRiderService.getRiderInfo(userId);
        return R.ok(riderInfo);
    }

    /**
     * 更新骑手状态
     *
     * @param status 状态(0-休息 1-可接单 2-忙碌)
     * @param userId 用户ID
     */
    @Operation(summary = "更新骑手状态")
    @PutMapping("status")
    public R<?> updateStatus(@RequestParam("status") Integer status,
                            @SaLoginId Long userId) {
        DeliveryRider.DeliveryRiderStatus statusEnum = DeliveryRider.DeliveryRiderStatus.values()[status];
        deliveryRiderService.updateRiderStatus(userId, statusEnum);
        return R.ok();
    }

    /**
     * 更新骑手位置
     *
     * @param location 位置信息
     * @param userId 用户ID
     */
    @Operation(summary = "更新骑手位置")
    @PutMapping("location")
    public R<?> updateLocation(@RequestParam("location") String location,
                              @SaLoginId Long userId) {
        deliveryRiderService.updateRiderLocation(userId, location);
        return R.ok();
    }

    /**
     * 分页查询可接订单
     *
     * @param pageNumber 页码
     * @param pageSize 页面大小
     * @param userId 骑手用户ID
     * @return 可接订单列表
     */
    @Operation(summary = "分页查询可接订单")
    @GetMapping("available-orders")
    public R<Page<AvailableOrderVO>> getAvailableOrders(@RequestParam("page_num") int pageNumber,
                                                        @RequestParam("page_size") int pageSize,
                                                        @SaLoginId Long userId) {
        Page<AvailableOrderVO> orders = deliveryRiderService.getAvailableOrders(userId, pageNumber, pageSize);
        return R.ok(orders);
    }

    /**
     * 骑手抢单
     *
     * @param orderId 订单ID
     * @param userId 骑手用户ID
     * @return 抢单结果
     */
    @Operation(summary = "骑手抢单")
    @PostMapping("grab-order")
    public R<Boolean> grabOrder(@RequestParam("order_id") Long orderId,
                               @SaLoginId Long userId) {
        boolean result = deliveryRiderService.grabOrder(userId, orderId);
        return R.ok(result);
    }

    /**
     * 骑手开始配送
     *
     * @param orderId 订单ID
     * @param userId 骑手用户ID
     * @return 开始配送结果
     */
    @Operation(summary = "开始配送")
    @PostMapping("start-order")
    public R<Boolean> startOrder(@RequestParam("order_id") Long orderId,
                                 @SaLoginId Long userId) {
        boolean result = deliveryRiderService.startDelivery(userId, orderId);
        return R.ok(result);
    }

    /**
     * 模拟送达
     *
     * @param orderId 订单ID
     * @param userId 骑手用户ID
     * @return 送达结果
     */
    @Operation(summary = "模拟送达")
    @PostMapping("complete-delivery")
    public R<Boolean> completeDelivery(@RequestParam("order_id") Long orderId,
                                      @SaLoginId Long userId) {
        boolean result = deliveryRiderService.completeDelivery(userId, orderId);
        return R.ok(result);
    }

    /**
     * 添加骑手表。
     *
     * @param deliveryRider 骑手表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @Operation(summary = "添加骑手")
    @PostMapping("save")
    public R<Boolean> save(@RequestBody DeliveryRider deliveryRider) {
        return R.ok(deliveryRiderService.save(deliveryRider));
    }

    /**
     * 根据主键删除骑手表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @Operation(summary = "删除骑手")
    @DeleteMapping("remove/{id}")
    public R<Boolean> remove(@PathVariable Long id) {
        return R.ok(deliveryRiderService.removeById(id));
    }

    /**
     * 根据主键更新骑手表。
     *
     * @param deliveryRider 骑手表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @Operation(summary = "更新骑手信息")
    @PutMapping("update")
    public R<Boolean> update(@RequestBody DeliveryRider deliveryRider) {
        return R.ok(deliveryRiderService.updateById(deliveryRider));
    }

    /**
     * 查询所有骑手表。
     *
     * @return 所有数据
     */
    @Operation(summary = "查询所有骑手")
    @GetMapping("list")
    public R<List<DeliveryRider>> list() {
        return R.ok(deliveryRiderService.list());
    }

    /**
     * 根据骑手表主键获取详细信息。
     *
     * @param id 骑手表主键
     * @return 骑手表详情
     */
    @Operation(summary = "根据ID获取骑手信息")
    @GetMapping("getInfo/{id}")
    public R<DeliveryRider> getInfo(@PathVariable Long id) {
        return R.ok(deliveryRiderService.getById(id));
    }

    /**
     * 分页查询骑手表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @Operation(summary = "分页查询骑手")
    @GetMapping("page")
    public R<Page<DeliveryRider>> page(Page<DeliveryRider> page) {
        return R.ok(deliveryRiderService.page(page));
    }

    /**
     * 查询骑手待配送的订单列表
     *
     * @param pageNumber 页码
     * @param pageSize 页面大小
     * @param userId 骑手用户ID
     * @return 骑手订单列表
     */
    @Operation(summary = "查询骑手待配送的订单列表")
    @GetMapping("pending-orders")
    public R<Page<DeliveryOrderVO>> getPendingDeliveryOrders(@RequestParam("page_num") int pageNumber,
                                                             @RequestParam("page_size") int pageSize,
                                                             @SaLoginId Long userId) {
        Page<DeliveryOrderVO> orders = deliveryRiderService.getPendingDeliveryOrders(userId, pageNumber, pageSize);
        return R.ok(orders);
    }

    @Operation(summary = "查询骑手配送中的订单列表")
    @GetMapping("delivering-orders")
    public R<Page<DeliveryOrderVO>> getDeliveringOrders(@RequestParam("page_num") int pageNumber,
                                                        @RequestParam("page_size") int pageSize,
                                                        @SaLoginId Long userId) {
        Page<DeliveryOrderVO> orders = deliveryRiderService.getDeliveringOrders(userId, pageNumber, pageSize);
        return R.ok(orders);
    }

    /**
     * 查询骑手配送完成的订单列表
     *
     * @param pageNumber 页码
     * @param pageSize 页面大小
     * @param userId 骑手用户ID
     * @return 骑手完成的订单列表
     */
    @Operation(summary = "查询骑手配送完成的订单列表")
    @GetMapping("completed-orders")
    public R<Page<DeliveryOrderVO>> getCompletedOrders(@RequestParam("page_num") int pageNumber,
                                                      @RequestParam("page_size") int pageSize,
                                                      @SaLoginId Long userId) {
        Page<DeliveryOrderVO> orders = deliveryRiderService.getCompletedOrders(userId, pageNumber, pageSize);
        return R.ok(orders);
    }
}
