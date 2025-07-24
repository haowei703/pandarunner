package com.shiroha.pandarunner.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shiroha.pandarunner.converter.SpecConverter;
import com.shiroha.pandarunner.domain.dto.SpecOptionDTO;
import com.shiroha.pandarunner.domain.entity.SpecOption;
import com.shiroha.pandarunner.domain.vo.SpecOptionVO;
import com.shiroha.pandarunner.exception.BusinessDataConsistencyException;
import com.shiroha.pandarunner.mapper.SpecOptionMapper;
import com.shiroha.pandarunner.service.SpecOptionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.shiroha.pandarunner.domain.entity.table.SpecOptionTableDef.SPEC_OPTION;

/**
 * 规格选项表 服务层实现。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Service
@AllArgsConstructor
public class SpecOptionServiceImpl extends ServiceImpl<SpecOptionMapper, SpecOption> implements SpecOptionService {

    private final SpecConverter converter;

    /**
     * 获取全部的规格项
     *
     * @param groupId 规格组ID
     * @return 规格项列表
     */
    @Override
    public List<SpecOptionVO> getSpecOptionsByGroupId(Long groupId) {
        List<SpecOption> options = list(query().where(SPEC_OPTION.GROUP_ID.eq(groupId)));
        return converter.specOptionListToSpecOptionVOList(options);
    }

    private List<SpecOption> getSpecOptions(Long groupId) {
        return list(query().where(SPEC_OPTION.GROUP_ID.eq(groupId)));
    }

    /**
     * 批量获取规格选项
     *
     * @param optionIds 规格ID列表
     * @return 规格选项列表
     */
    @Override
    public List<SpecOptionVO> getSpecOptionsByIds(List<Long> optionIds) {
        if(optionIds.isEmpty()) {
            return List.of();
        }
        List<SpecOption> options = list(query().where(SPEC_OPTION.ID.in(optionIds)));
        return converter.specOptionListToSpecOptionVOList(options);
    }

    /**
     * 批量获取全部的规格项
     *
     * @param groupIds 规格组ID列表
     * @return 规格组ID-规格项列表的映射
     */
    @Override
    public Map<String, List<SpecOptionVO>> getSpecOptionsByGroupIds(Collection<Long> groupIds) {
        // 初始化map，预填充key和空列表
        Map<String, List<SpecOptionVO>> optionsByGroupId = new HashMap<>((int) (groupIds.size() / 0.75f));
        for (Long groupId : groupIds) {
            optionsByGroupId.put(String.valueOf(groupId), new ArrayList<>());
        }

        // 遍历批量查询结果，将item插入到对应列表
        list(query().where(SPEC_OPTION.GROUP_ID.in(groupIds))
                .orderBy(SPEC_OPTION.ID.asc())).forEach(option -> {
            List<SpecOptionVO> options = optionsByGroupId.get(String.valueOf(option.getGroupId()));
            options.add(converter.specOptionToSpecOptionVO(option));
        });

        return optionsByGroupId;
    }

    /**
     * 批量保存规格选项
     *
     * @param groupId   规格组ID
     * @param productId 商品ID
     * @param options   规格选项
     */
    @Override
    public void saveSpecOptionList(Long groupId, Long productId, List<SpecOptionDTO> options) {
        List<SpecOption> converted = converter.specOptionDTOListToSpecOptionList(options);
        converted.forEach(specOption -> {
            specOption.setGroupId(groupId);
            specOption.setProductId(productId);
        });
        saveBatch(converted);
    }

    /**
     * 批量更新规格选项
     *
     * @param groupId   规格组ID
     * @param productId 商品ID
     * @param options   规格选项
     */
    @Override
    @Transactional
    public void updateSpecOptionList(Long groupId, Long productId, List<SpecOptionDTO> options) {
        // 1. 获取旧数据（optionVOs）并构建辅助映射
        List<SpecOption> oldOptions = getSpecOptions(groupId); // 旧数据
        // 旧数据ID到对象的映射（用于快速查找）
        Map<Long, SpecOption> oldIdMap = oldOptions.stream()
                .collect(Collectors.toMap(SpecOption::getId, o -> o));
        // 旧数据所有ID的集合（用于后续判断需要删除的ID）
        Set<Long> oldIds = oldIdMap.keySet();


        // 2. 处理新数据（converted），区分需要更新和新增的记录
        List<SpecOption> converted = converter.specOptionDTOListToSpecOptionList(options); // 新数据
        List<SpecOption> toUpdate = new ArrayList<>(); // 待更新集合
        List<SpecOption> toInsert = new ArrayList<>(); // 待新增集合
        Set<Long> newIds = new HashSet<>(); // 新数据中存在的ID集合（用于判断删除）

        for (SpecOption newOption : converted) {
            // 设置公共字段（分组ID、商品ID）
            newOption.setGroupId(groupId);
            newOption.setProductId(productId);

            // 关键：通过业务唯一键（此处假设用"name"作为匹配依据，需根据实际业务调整）
            // 注意：如果新数据有唯一标识（如code），优先用该字段匹配旧数据
            String newOptionName = newOption.getName(); // 假设name是业务唯一键

            // 查找旧数据中是否有匹配的记录（按业务键）
            SpecOption matchedOld = oldOptions.stream()
                    .filter(old -> newOptionName.equals(old.getName()))
                    .findFirst()
                    .orElse(null);

            if (matchedOld != null) {
                // 2.1 有匹配的旧数据：标记为更新（复用旧ID）
                newOption.setId(matchedOld.getId()); // 设置旧ID，确保更新目标正确
                toUpdate.add(newOption);
                newIds.add(matchedOld.getId()); // 记录已匹配的旧ID（避免被删除）
            } else {
                // 2.2 无匹配的旧数据：标记为新增（ID通常由数据库生成，无需手动设置）
                toInsert.add(newOption);
            }
        }


        // 3. 执行数据库操作
        // 3.1 更新存在的记录
        if (!toUpdate.isEmpty()) {
            updateBatch(toUpdate); // 调用已有的批量更新方法
        }

        // 3.2 插入新记录
        if (!toInsert.isEmpty()) {
            saveBatch(toInsert); // 假设存在批量插入方法
        }

        // 3.3 真实删除旧数据中不存在于新数据的记录
        Set<Long> toDeleteIds = oldIds.stream()
                .filter(oldId -> !newIds.contains(oldId)) // 旧ID不在新数据ID集合中
                .collect(Collectors.toSet());

        if (!toDeleteIds.isEmpty()) {
            // 使用Db工具类执行真实删除（绕过逻辑删除）
            Db.deleteByQuery(SPEC_OPTION.getTableName(),
                    QueryWrapper.create()
                    .from(SpecOption.class)
                    .where(SpecOption::getId).in(toDeleteIds)
            );
        }
    }

    /**
     * 验证规格属于指定商品
     *
     * @param productId     商品ID
     * @param specs         规格项列表
     */
    @Override
    public void validateSpecsBelongToProduct(Long productId, Collection<SpecOption> specs) {
        specs.forEach(spec -> {
            if (!productId.equals(spec.getProductId())) {
                throw new BusinessDataConsistencyException(
                        "规格[" + spec.getId() + "]不属于商品[" + productId + "]",
                        spec.getId().toString(),
                        productId.toString(),
                        BusinessDataConsistencyException.ErrorType.RELATION_MISMATCH);
            }
        });
    }
}
