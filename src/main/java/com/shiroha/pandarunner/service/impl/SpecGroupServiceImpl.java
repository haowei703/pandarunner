package com.shiroha.pandarunner.service.impl;

import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shiroha.pandarunner.converter.SpecConverter;
import com.shiroha.pandarunner.domain.dto.SpecGroupDTO;
import com.shiroha.pandarunner.domain.dto.SpecOptionDTO;
import com.shiroha.pandarunner.domain.entity.Merchant;
import com.shiroha.pandarunner.domain.entity.SpecGroup;
import com.shiroha.pandarunner.domain.vo.SpecGroupVO;
import com.shiroha.pandarunner.domain.vo.SpecOptionVO;
import com.shiroha.pandarunner.exception.BusinessDataNotFoundException;
import com.shiroha.pandarunner.exception.NotSupportException;
import com.shiroha.pandarunner.mapper.MerchantMapper;
import com.shiroha.pandarunner.mapper.ProductMapper;
import com.shiroha.pandarunner.mapper.SpecGroupMapper;
import com.shiroha.pandarunner.service.SpecGroupService;
import com.shiroha.pandarunner.service.SpecOptionService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.shiroha.pandarunner.domain.entity.table.MerchantTableDef.MERCHANT;
import static com.shiroha.pandarunner.domain.entity.table.ProductTableDef.PRODUCT;
import static com.shiroha.pandarunner.domain.entity.table.SpecGroupTableDef.SPEC_GROUP;
import static com.shiroha.pandarunner.domain.entity.table.SpecOptionTableDef.SPEC_OPTION;

/**
 * 规格组表 服务层实现。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Service
@AllArgsConstructor
@CacheConfig(cacheNames = "spec")
public class SpecGroupServiceImpl extends ServiceImpl<SpecGroupMapper, SpecGroup> implements SpecGroupService {

    private final SpecConverter converter;
    private final SpecOptionService specOptionService;
    private final MerchantMapper merchantMapper;
    private final ProductMapper productMapper;

    /**
     * 新增规格组
     *
     * @param userId       用户ID
     * @param productId    商品ID
     * @param specGroupDTO 规格组信息
     * @return 规格组ID
     */
    @Override
    @CacheEvict(key = "#productId")
    @Transactional
    public Long saveSpecGroup(Long userId, Long productId, SpecGroupDTO specGroupDTO) {
        Merchant merchant = QueryChain.of(merchantMapper)
                .where(MERCHANT.USER_ID.eq(userId))
                .one();

        if(merchant == null) {
            throw new BusinessDataNotFoundException("用户未注册商家", userId);
        }

        Long merchantId = merchant.getId();

        // 合并查询：验证商家存在且商品存在并属于该商家
        boolean isMerchantAndProductExisted = QueryChain.of(merchantMapper)
                .leftJoin(PRODUCT).on(PRODUCT.MERCHANT_ID.eq(MERCHANT.ID))
                .where(MERCHANT.ID.eq(merchantId))
                .and(MERCHANT.USER_ID.eq(userId))
                .and(PRODUCT.ID.eq(productId))
                .exists();

        if(!isMerchantAndProductExisted) {
            throw new NotSupportException("参数错误");
        }

        SpecGroup specGroup = converter.specGroupDTOToSpecGroup(specGroupDTO);
        specGroup.setProductId(productId);

        // 首先保存规格选项组
        save(specGroup);

        Long groupId = specGroup.getId();

        // 批量保存规格选项
        specOptionService.saveSpecOptionList(groupId, productId, specGroupDTO.getOptions());

        return groupId;
    }

    /**
     * 批量保存规格组
     *
     * @param userId        用户ID
     * @param productId     商品ID
     * @param specGroupDTOs 规格组列表
     */
    @Override
    @CacheEvict(allEntries = true)
    public void saveSpecGroupBatch(Long userId, Long productId, List<SpecGroupDTO> specGroupDTOs) {
        Merchant merchant = QueryChain.of(merchantMapper)
                .where(MERCHANT.USER_ID.eq(userId))
                .one();

        if(merchant == null) {
            throw new BusinessDataNotFoundException("用户未注册商家", userId);
        }

        Long merchantId = merchant.getId();

        // 合并查询：验证商家存在且商品存在并属于该商家
        boolean isMerchantAndProductExisted = QueryChain.of(merchantMapper)
                .leftJoin(PRODUCT).on(PRODUCT.MERCHANT_ID.eq(MERCHANT.ID))
                .where(MERCHANT.ID.eq(merchantId))
                .and(MERCHANT.USER_ID.eq(userId))
                .and(PRODUCT.ID.eq(productId))
                .exists();

        if(!isMerchantAndProductExisted) {
            throw new NotSupportException("参数错误");
        }

        List<SpecGroup> groups = converter.specGroupDtoListToSpecGroupList(specGroupDTOs);
        groups.forEach(group -> group.setProductId(productId));

        // 首先批量保存规格列表
        saveBatch(groups);

        List<Long> groupIds = groups.stream().map(SpecGroup::getId).toList();

        // 建立groupId到options的映射
        Map<Long, List<SpecOptionDTO>> optionsByGroupId = IntStream.range(0, specGroupDTOs.size())
                .boxed()
                .collect(Collectors.toMap(
                        // 以对应group的ID作为键
                        index -> groups.get(index).getId(),
                        // 以DTO中的options作为值
                        index -> specGroupDTOs.get(index).getOptions()
                ));

        // 批量保存规格选项
        optionsByGroupId.forEach((groupId, options) -> {
            specOptionService.saveSpecOptionList(groupId, productId, options);
        });
    }

    /**
     * 更新规格组
     *
     * @param userId       用户ID
     * @param groupId      商品ID
     * @param specGroupDTO 规格组信息
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public void updateSpecGroup(Long userId, Long groupId, SpecGroupDTO specGroupDTO) {
         Long productId = QueryChain.of(productMapper)
                .select(PRODUCT.ID)
                .leftJoin(MERCHANT).on(MERCHANT.ID.eq(PRODUCT.MERCHANT_ID))
                .leftJoin(SPEC_GROUP).on(SPEC_GROUP.PRODUCT_ID.eq(PRODUCT.ID))
                .where(MERCHANT.USER_ID.eq(userId))
                .and(SPEC_GROUP.ID.eq(groupId))
                .oneAs(Long.class);
        if(productId == null) {
            throw new NotSupportException("操作失败");
        }

        SpecGroup specGroup = converter.specGroupDTOToSpecGroup(specGroupDTO);
        specGroup.setId(groupId);
        specGroup.setProductId(productId);
        // 更新规格组
        updateById(specGroup);

        // 批量更新规格信息
        specOptionService.updateSpecOptionList(groupId, productId, specGroupDTO.getOptions());
    }


    /**
     * 删除规格组信息
     *
     * @param userId      用户ID
     * @param specGroupId 规格组ID
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public void deleteSpecGroupById(Long userId, Long specGroupId) {
        Merchant merchant = QueryChain.of(merchantMapper)
                .where(MERCHANT.USER_ID.eq(userId))
                .one();

        if(merchant == null) {
            throw new BusinessDataNotFoundException("用户未注册商家", userId);
        }

        Long merchantId = merchant.getId();

        // 合并查询：验证商家存在且商品存在并属于该商家，规格组属于商家的商品
        boolean isMerchantAndProductExisted = QueryChain.of(merchantMapper)
                .leftJoin(PRODUCT).on(PRODUCT.MERCHANT_ID.eq(MERCHANT.ID))
                .leftJoin(SPEC_GROUP).on(SPEC_GROUP.ID.eq(specGroupId))
                .where(MERCHANT.ID.eq(merchantId))
                .and(PRODUCT.ID.eq(SPEC_GROUP.PRODUCT_ID))
                .exists();

        if(!isMerchantAndProductExisted) {
            throw new NotSupportException("操作失败");
        }

        // 首先删除规格组
        removeById(specGroupId);

        // 然后删除规格选项
        QueryWrapper query = QueryWrapper.create()
                .from(SPEC_OPTION)
                .where(SPEC_OPTION.GROUP_ID.eq(specGroupId));
        specOptionService.remove(query);
    }

    /**
     * 获取商品的规格信息
     *
     * @param productId 商品ID
     * @return 规格信息
     */
    @Override
    @Cacheable(key = "#productId", unless = "#result.isEmpty()")
    public List<SpecGroupVO> getSpecGroupByProductId(Long productId) {
        // 查询规格组列表
        List<SpecGroup> groups = list(query()
                .where(SPEC_GROUP.PRODUCT_ID.eq(productId))
                .orderBy(SPEC_GROUP.ID.asc()));

        // 如果商品规格组为空，则直接返回空列表
        if(groups.isEmpty()) {
            return List.of();
        }

        // 收集所有groupIds
        List<Long> groupIds = groups.stream()
                .map(SpecGroup::getId)
                .collect(Collectors.toList());

        // 批量查询规格项
        Map<String, List<SpecOptionVO>> optionsByGroupId = specOptionService.getSpecOptionsByGroupIds(groupIds);

        // 初始化容器
        List<SpecGroupVO> groupVOS = new ArrayList<>(groups.size());
        for (SpecGroup group : groups) {
            // 转换为VO
            SpecGroupVO groupVO = converter.specGroupToSpecGroupVO(group);
            // 直接从Map获取并设置规格项
            groupVO.setOptions(optionsByGroupId.getOrDefault(String.valueOf(group.getId()), new ArrayList<>()));
            groupVOS.add(groupVO);
        }

        return groupVOS;
    }

}
