package com.shiroha.pandarunner.service;

import com.mybatisflex.core.service.IService;
import com.shiroha.pandarunner.domain.dto.SpecOptionDTO;
import com.shiroha.pandarunner.domain.entity.SpecOption;
import com.shiroha.pandarunner.domain.vo.SpecOptionVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 规格选项表 服务层。
 *
 * @author haowei703
 * @since 2025-07-03
 */
public interface SpecOptionService extends IService<SpecOption> {

    /**
     * 获取全部的规格项
     *
     * @param groupId       规格组ID
     * @return              规格项列表
     */
    List<SpecOptionVO> getSpecOptionsByGroupId(Long groupId);

    /**
     * 批量获取规格选项
     *
     * @param optionIds 规格ID列表
     * @return          规格选项列表
     */
    List<SpecOptionVO> getSpecOptionsByIds(List<Long> optionIds);

    /**
     * 批量获取全部的规格项
     *
     * @param groupIds      规格组ID列表
     * @return              规格组ID-规格项列表的映射
     */
    Map<String, List<SpecOptionVO>> getSpecOptionsByGroupIds(Collection<Long> groupIds);

    /**
     * 批量保存规格选项
     *
     * @param groupId   规格组ID
     * @param productId 商品ID
     * @param options   规格选项
     */
    void saveSpecOptionList(Long groupId, Long productId, List<SpecOptionDTO> options);

    /**
     * 批量更新规格选项
     *
     * @param groupId   规格组ID
     * @param productId 商品ID
     * @param options   规格选项
     */
    void updateSpecOptionList(Long groupId, Long productId, List<SpecOptionDTO> options);

    /**
     * 验证规格属于指定商品
     *
     * @param productId     商品ID
     * @param specs         规格项
     */
    void validateSpecsBelongToProduct(Long productId, Collection<SpecOption> specs);
}
