package com.shiroha.pandarunner.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shiroha.pandarunner.converter.MerchantConverter;
import com.shiroha.pandarunner.domain.dto.MerchantDTO;
import com.shiroha.pandarunner.domain.entity.*;
import com.shiroha.pandarunner.domain.vo.MerchantVO;
import com.shiroha.pandarunner.domain.vo.ProductVO;
import com.shiroha.pandarunner.exception.BusinessDataConsistencyException;
import com.shiroha.pandarunner.exception.BusinessDataNotFoundException;
import com.shiroha.pandarunner.exception.InvalidParamException;
import com.shiroha.pandarunner.exception.NotSupportException;
import com.shiroha.pandarunner.mapper.MerchantMapper;
import com.shiroha.pandarunner.service.*;
import com.shiroha.pandarunner.util.AddressUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.shiroha.pandarunner.domain.entity.table.MerchantTableDef.MERCHANT;

/**
 * 商家信息表 服务层实现。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Service
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {

    private final MerchantConverter converter;
    private final UserAddressService userAddressService;
    private final MapService mapService;
    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;

    public MerchantServiceImpl(MerchantConverter converter,
                               UserAddressService userAddressService,
                               MapService mapService,
                               UserService userService,
                               @Lazy ProductService productService,
                               @Lazy OrderService orderService) {
        this.converter = converter;
        this.userAddressService = userAddressService;
        this.mapService = mapService;
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
    }

    /**
     * 根据商家ID查询单个商家信息
     *
     * @param merchantId   商家唯一标识
     * @param ignoreStatus 是否忽略商家状态过滤
     *                     <ul>
     *                         <li>true: 不过滤状态，返回所有状态的商家记录</li>
     *                         <li>false: 仅返回非禁用状态的商家记录</li>
     *                     </ul>
     * @return 商家
     */
    @Override
    public Merchant getMerchantById(Long merchantId, boolean ignoreStatus) {
        Merchant merchant;
        if(ignoreStatus) {
            merchant = getById(merchantId);
        } else {
            merchant = getOne(query()
                    .where(MERCHANT.ID.eq(merchantId))
                    .and(MERCHANT.STATUS.eq(1)));
        }
        if(merchant == null) {
            throw new BusinessDataNotFoundException("商家ID不存在", merchantId);
        }
        return merchant;
    }

    /**
     * 批量获取商家
     *
     * @param merchantIds 商家ID列表
     * @return 商家列表
     */
    @Override
    public List<Merchant> getMerchantsByIds(Collection<Long> merchantIds) {
        if(merchantIds == null || merchantIds.isEmpty()) {
            return List.of();
        }

        return listByIds(merchantIds);
    }

    /**
     * 保存商家
     *
     * @param userId      用户ID
     * @param merchantDTO DTO
     */
    @Override
    public void saveMerchant(Long userId, MerchantDTO merchantDTO) {
        Merchant merchant = getOne(query().where(MERCHANT.USER_ID.eq(userId)));
        if(merchant != null) {
            throw new NotSupportException("该账号已经注册了商家");
        }

        Merchant converted = converter.merchantDtoToMerchant(merchantDTO);
        converted.setUserId(userId);
        converted.setLocation(mapService.getLocation(converted.getAddress()));
        save(converted);
    }

    /**
     * 更新商家
     *
     * @param userId        用户ID
     * @param merchantId    商家ID
     * @param merchantDTO   DTO
     */
    @Override
    public void updateMerchant(Long userId, Long merchantId, MerchantDTO merchantDTO) {
        Merchant merchant = getMerchantById(merchantId);

        // 检验用户ID属于商家
        validateMerchantBelongToUser(userId, merchant);

        Merchant converted = converter.merchantDtoToMerchant(merchantDTO);
        converted.setId(merchantId);
        converted.setUserId(userId);

        // 忽略部分字段确保不被修改
        converted.setProvince(null);
        converted.setCity(null);
        converted.setDistrict(null);
        converted.setDetailAddress(null);
        converted.setBusinessLicense(null);
        updateById(converted, true);
    }

    /**
     * 删除商家
     *
     * @param userId     用户ID
     * @param merchantId 商家ID
     */
    @Override
    public void deleteMerchant(Long userId, Long merchantId) {
        Merchant merchant = getMerchantById(merchantId);

        // 检验用户ID属于商家
        validateMerchantBelongToUser(userId, merchant);

        removeById(merchantId);
    }

    /**
     * 获取指定省份、城市、地区的商家
     *
     * @param address@return 商家列表
     */
    @Override
    public List<Merchant> getMerchantsByDistrict(Address address) {
        String province = address.getProvince();
        String city = address.getCity();
        String district = address.getDistrict();

        return list(query()
                .where(MERCHANT.PROVINCE.eq(province))
                .and(MERCHANT.CITY.eq(city))
                .and(MERCHANT.DISTRICT.eq(district))
                .and(MERCHANT.STATUS.eq(1)));
    }

    /**
     * 重载方法，分页获取商家
     *
     * @param address    地址
     * @param pageNumber 页数
     * @param pageSize   页面大小
     * @return 商家列表
     * @see #getMerchantsByDistrict(Address)
     */
    @Override
    public Page<Merchant> getMerchantsByDistrict(Address address, int pageNumber, int pageSize) {
        String province = address.getProvince();
        String city = address.getCity();
        String district = address.getDistrict();

        return page(new Page<>(pageNumber, pageSize),
                query()
                        .where(MERCHANT.PROVINCE.eq(province))
                        .and(MERCHANT.CITY.eq(city))
                        .and(MERCHANT.DISTRICT.eq(district))
                        .and(MERCHANT.STATUS.eq(1))
                        .orderBy(MERCHANT.ID, true));
    }

    /**
     * 分页获取当前地址的附近商家
     *
     * @param userId     用户ID
     * @param addressId  地址ID
     * @param pageNumber 页数
     * @param pageSize   页面大小
     * @return 分页
     */
    @Override
    public Page<MerchantVO> getMerchantsByPage(Long userId, Long addressId, int pageNumber, int pageSize) {
        // 获取地址
        UserAddress userAddress = userAddressService.getUserAddressById(addressId);

        // 检验地址属于用户
        userAddressService.validateAddressBelongToUser(userId, userAddress);

        // 分页获取到该地区的商家
        Page<Merchant> merchantPage = getMerchantsByDistrict(userAddress, pageNumber, pageSize);
        List<Merchant> merchants = merchantPage.getRecords();

        // 如果查询的商家列表为空，则抛出异常
        if(merchants == null || merchants.isEmpty()) {
            throw new BusinessDataNotFoundException("附近没有商家", addressId);
        }

        String[] locations = merchants.stream().map(Merchant::getLocation).toArray(String[]::new);
        // 商家坐标
        String origins = locations.length > 1 ? AddressUtils.joinCoordinates(locations) : locations[0];

        // 用户收货地址坐标
        String destination = userAddress.getLocation();

        // 获取该地区商家到用户地址的距离
        JSON response = mapService.getDistance(origins, destination);

        List<MerchantVO> filteredMerchants = new ArrayList<>();
        Map<String, List<ProductVO>> merchantProductsMap;

        // 解析距离数据并过滤商家
        if(response instanceof JSONArray results) {
            // 收集符合条件的商家ID和对应的MerchantVO
            Map<Long, MerchantVO> merchantVOMap = new HashMap<>();
            for (int i = 0; i < results.size(); i++) {
                JSONObject result = results.getJSONObject(i);
                int distance = Integer.parseInt(result.getStr("distance")); // 单位：米
                if (distance < 4000) { // 过滤距离小于4公里的商家
                    // 转换为VO类并设置distance字段
                    Merchant merchant = merchants.get(i);
                    MerchantVO merchantVO = converter.merchantToMerchantVO(merchant);
                    merchantVO.setDistance(distance);
                    merchantVOMap.put(merchant.getId(), merchantVO);
                }
            }

            if (!merchantVOMap.isEmpty()) {
                // 批量获取商品
                List<Long> merchantIds = new ArrayList<>(merchantVOMap.keySet());
                merchantProductsMap = productService.getProductsByMerchantIds(merchantIds);

                // 设置每个商家的商品列表（直接在Map中操作，避免额外循环）
                merchantProductsMap.forEach((merchantId, products) -> {
                    MerchantVO merchantVO = merchantVOMap.get(Long.valueOf(merchantId));
                    if (merchantVO != null) {
                        merchantVO.setProducts(products);
                    }
                });

                // 将处理好的MerchantVO添加到结果列表
                filteredMerchants.addAll(merchantVOMap.values());
            }
        }


        long totalRow = merchantPage.getTotalRow();
        return new Page<>(filteredMerchants, pageNumber, pageSize, totalRow);
    }

    /**
     * 获取商家信息
     *
     * @param userId     用户ID
     * @param merchantId 商家ID
     * @return 商家
     */
    @Override
    public Merchant getMerchantInfo(Long userId, Long merchantId) {
        Merchant merchant = getMerchantById(merchantId);

        validateMerchantBelongToUser(userId, merchant);

        return merchant;
    }

    /**
     * 通过用户ID获取商家
     *
     * @param userId       用户ID
     * @param ignoreStatus 是否忽略商家状态过滤
     *                     <ul>
     *                         <li>true: 不过滤状态，返回所有状态的商家记录</li>
     *                         <li>false: 仅返回非禁用状态的商家记录</li>
     *                     </ul>
     * @return 商家
     */
    @Override
    public Merchant getMerchantByUserId(Long userId, boolean ignoreStatus) {
        QueryWrapper query;
        if(ignoreStatus) {
            query = query().where(MERCHANT.USER_ID.eq(userId));
        } else {
            query = query().where(MERCHANT.USER_ID.eq(userId))
                    .and(MERCHANT.STATUS.eq(1));
        }

        Merchant merchant = getOne(query);
        if(merchant == null) {
            throw new BusinessDataNotFoundException("用户ID不存在", userId);
        }
        return merchant;
    }

    /**
     * 检验用户ID属于商家
     *
     * @param userId   用户ID
     * @param merchant 商家
     */
    @Override
    public void validateMerchantBelongToUser(Long userId, Merchant merchant) {
        // 检验地址属于用户
        if(!merchant.getUserId().equals(userId)) {
            throw new BusinessDataConsistencyException(
                    String.format("商家[%s]不属于用户[%s]", merchant.getId(), userId),
                    merchant.getId(),
                    userId,
                    BusinessDataConsistencyException.ErrorType.RELATION_MISMATCH);
        }
    }

    /**
     * 获取全部商家
     *
     * @param userId 获取全部商家
     * @param status 商家状态
     * @return 商家列表
     */
    @Override
    public List<Merchant> getAllMerchants(Long userId, Merchant.MerchantStatus status) {
        userService.checkPermission(userId, User.Role.ADMIN);
        return list(query().where(MERCHANT.STATUS.eq(status)));
    }

    /**
     * 审核商家并通过
     *
     * @param userId     操作的管理员ID
     * @param merchantId 商家ID
     */
    @Override
    public void verifyMerchant(Long userId, Long merchantId) {
        // 检验管理员权限
        userService.checkPermission(userId, User.Role.ADMIN);

        Merchant merchant = getMerchantById(merchantId);
        // 如果商家不是待审核则拒绝
        if(!merchant.getStatus().equals(Merchant.MerchantStatus.PENDING_REVIEW)) {
            throw new NotSupportException("操作失败");
        }

        merchant.setStatus(Merchant.MerchantStatus.NORMAL);
        updateById(merchant, true);
    }

    /**
     * 禁用商家
     *
     * @param userId     操作的管理员ID
     * @param merchantId 商家ID
     */
    @Override
    public void banMerchant(Long userId, Long merchantId) {
        // 检验管理员权限
        userService.checkPermission(userId, User.Role.ADMIN);

        Merchant merchant = getMerchantById(merchantId);
        merchant.setStatus(Merchant.MerchantStatus.DISABLED);
        updateById(merchant, true);
    }

    /**
     * 订单出餐
     *
     * @param userId     用户ID
     * @param merchantId 商家ID
     * @param orderId    订单ID
     */
    @Override
    public void acceptOrder(Long userId, Long merchantId, Long orderId) {
        Merchant merchant = getMerchantById(merchantId, false);

        validateMerchantBelongToUser(userId, merchant);

        Order order = orderService.getById(orderId);
        if (order == null) {
            throw new BusinessDataNotFoundException("订单不存在", orderId);
        }

        if (order.getStatus() != Order.OrderStatus.PAID && !order.getMerchantId().equals(merchantId)) {
            throw new InvalidParamException("订单状态不允许接受", "orderId", InvalidParamException.ErrorType.ILLEGAL_VALUE);
        }

        order.setStatus(Order.OrderStatus.ACCEPTED);
        orderService.updateById(order);
    }

    /**
     * 增加商家销量
     *
     * @param merchantId 商家ID
     */
    @Override
    public void updateMerchantSales(Long merchantId) {
        Merchant merchant = getMerchantById(merchantId, false);

        // 销量+1
        Integer sales = merchant.getTotalSales() + 1;
        merchant.setTotalSales(sales);
        updateById(merchant, true);
    }
}
