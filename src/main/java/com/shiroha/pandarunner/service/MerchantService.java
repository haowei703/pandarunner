package com.shiroha.pandarunner.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.shiroha.pandarunner.domain.dto.MerchantDTO;
import com.shiroha.pandarunner.domain.entity.Address;
import com.shiroha.pandarunner.domain.entity.Merchant;
import com.shiroha.pandarunner.domain.vo.MerchantVO;

import java.util.Collection;
import java.util.List;

/**
 * 商家信息表 服务层。
 *
 * @author haowei703
 * @since 2025-07-03
 */
public interface MerchantService extends IService<Merchant> {

    /**
     * 根据商家ID查询单个商家信息
     *
     * @param merchantId   商家ID
     * @param ignoreStatus 是否忽略商家状态过滤
     *                     <ul>
     *                         <li>true: 不过滤状态，返回所有状态的商家记录</li>
     *                         <li>false: 仅返回非禁用状态的商家记录</li>
     *                     </ul>
     * @return 商家
     */
    Merchant getMerchantById(Long merchantId, boolean ignoreStatus);

    /**
     * 批量获取商家
     *
     * @param merchantIds 商家ID列表
     * @return 商家列表
     */
    List<Merchant> getMerchantsByIds(Collection<Long> merchantIds);

    /**
     * 方法重载，ignoreStatus默认值为true
     * @param merchantId        商家ID
     * @return                  商家
     */
    default Merchant getMerchantById(Long merchantId) {
        return getMerchantById(merchantId, true);
    }

    /**
     * 保存商家
     *
     * @param userId        用户ID
     * @param merchantDTO   DTO
     */
    void saveMerchant(Long userId, MerchantDTO merchantDTO);

    /**
     * 更新商家
     *
     * @param userId      用户ID
     * @param merchantId
     * @param merchantDTO DTO
     */
    void updateMerchant(Long userId, Long merchantId, MerchantDTO merchantDTO);

    /**
     * 删除商家
     *
     * @param userId            用户ID
     * @param merchantId        商家ID
     */
    void deleteMerchant(Long userId, Long merchantId);

    /**
     * 获取指定省份、城市、地区的商家
     *
     * @param address       地址
     * @return 商家列表
     */
    List<Merchant> getMerchantsByDistrict(Address address);

    /**
     * 重载方法，分页获取商家
     *
     * @param address       地址
     * @param pageNumber    页数
     * @param pageSize      页面大小
     * @see #getMerchantsByDistrict(Address)
     * @return 商家列表
     */
    Page<Merchant> getMerchantsByDistrict(Address address, int pageNumber, int pageSize);

    /**
     * 分页获取当前地址的附近商家
     *
     * @param userId            用户ID
     * @param addressId         地址ID
     * @param pageNumber        页数
     * @param pageSize          页面大小
     * @return                  分页
     */
    Page<MerchantVO> getMerchantsByPage(Long userId, Long addressId, int pageNumber, int pageSize);

    /**
     * 获取商家信息
     *
     * @param userId        用户ID
     * @param merchantId    商家ID
     * @return              商家
     */
    Merchant getMerchantInfo(Long userId, Long merchantId);

    /**
     * 通过用户ID获取商家
     *
     * @param userId        用户ID
     * @param ignoreStatus 是否忽略商家状态过滤
     *                     <ul>
     *                         <li>true: 不过滤状态，返回所有状态的商家记录</li>
     *                         <li>false: 仅返回非禁用状态的商家记录</li>
     *                     </ul>
     * @return              商家
     */
    Merchant getMerchantByUserId(Long userId, boolean ignoreStatus);

    /**
     * 接口重载方法，默认忽略商家状态过滤
     *
     * @param userId        用户ID
     * @return              商家
     */
    default Merchant getMerchantByUserId(Long userId) {
        return getMerchantByUserId(userId, true);
    }

    /**
     * 检验用户ID属于商家
     *
     * @param userId        用户ID
     * @param merchant      商家
     */
    void validateMerchantBelongToUser(Long userId, Merchant merchant);

    /**
     * 获取全部商家
     *
     * @param userId 获取全部商家
     * @param status 商家状态
     * @return 商家列表
     */
    List<Merchant> getAllMerchants(Long userId, Merchant.MerchantStatus status);

    /**
     * 重载方法，默认查询待审核商家
     *
     * @param userId 用户ID
     * @return 待审核商家列表
     */
    default List<Merchant> getAllMerchants(Long userId) {
        return getAllMerchants(userId, Merchant.MerchantStatus.PENDING_REVIEW);
    }

    /**
     * 审核商家并通过
     *
     * @param userId        操作的管理员ID
     * @param merchantId    商家ID
     */
    void verifyMerchant(Long userId, Long merchantId);

    /**
     * 禁用商家
     *
     * @param userId        操作的管理员ID
     * @param merchantId    商家ID
     */
    void banMerchant(Long userId, Long merchantId);

    /**
     * 接受订单
     *
     * @param userId 用户ID
     * @param merchantId 商家ID
     * @param orderId 订单ID
     */
    void acceptOrder(Long userId, Long merchantId, Long orderId);

    /**
     * 增加商家销量
     *
     * @param merchantId 商家ID
     */
    void updateMerchantSales(Long merchantId);
}
