package com.shiroha.pandarunner.service;

import com.mybatisflex.core.service.IService;
import com.shiroha.pandarunner.domain.dto.SpecGroupDTO;
import com.shiroha.pandarunner.domain.entity.SpecGroup;
import com.shiroha.pandarunner.domain.vo.SpecGroupVO;

import java.util.List;

/**
 * 规格组表 服务层。
 *
 * @author haowei703
 * @since 2025-07-03
 */
public interface SpecGroupService extends IService<SpecGroup> {

    /**
     * 新增规格组
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @param specGroupDTO 规格组信息
     * @return 规格组ID
     */
    Long saveSpecGroup(Long userId, Long productId, SpecGroupDTO specGroupDTO);

    /**
     * 批量保存规格组
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @param specGroupDTOs 规格组列表
     */
    void saveSpecGroupBatch(Long userId, Long productId, List<SpecGroupDTO> specGroupDTOs);

    /**
     * 更新规格组
     *
     * @param userId 用户ID
     * @param groupId 组ID
     * @param specGroupDTO 规格组信息
     */
    void updateSpecGroup(Long userId, Long groupId, SpecGroupDTO specGroupDTO);

    /**
     * 删除规格组信息
     *
     * @param userId 用户ID
     * @param specGroupId 规格组ID
     */
    void deleteSpecGroupById(Long userId, Long specGroupId);

    /**
     * 获取商品的规格组
     *
     * @param productId 商品ID
     * @return 规格信息
     */
    List<SpecGroupVO> getSpecGroupByProductId(Long productId);

}
