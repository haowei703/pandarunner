package com.shiroha.pandarunner.controller;

import com.shiroha.pandarunner.annotation.SaLoginId;
import com.shiroha.pandarunner.common.R;
import com.shiroha.pandarunner.config.ValidationGroups;
import com.shiroha.pandarunner.domain.dto.SpecGroupDTO;
import com.shiroha.pandarunner.domain.vo.SpecGroupVO;
import com.shiroha.pandarunner.service.SpecGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "规格组模块")
@RestController
@RequestMapping("/spec/group")
@AllArgsConstructor
public class SpecGroupController {

    private final SpecGroupService specGroupService;

    /**
     * 新增规格组。
     *
     * @param specGroupDTO 规格组
     */
    @Operation(summary = "新增规格组")
    @PostMapping("save")
    public R<Long> save(@RequestParam(name = "product_id") Long productId,
                    @Validated(ValidationGroups.Create.class) @RequestBody SpecGroupDTO specGroupDTO,
                    @SaLoginId Long userId) {
        return R.ok(specGroupService.saveSpecGroup(userId, productId, specGroupDTO));
    }

    /**
     * 删除规格组
     *
     * @param groupId 规格组ID
     */
    @Operation(summary = "删除规格组")
    @DeleteMapping("remove/{id}")
    public R<?> remove(@PathVariable("id") Long groupId, @SaLoginId Long userId) {
        specGroupService.deleteSpecGroupById(userId, groupId);
        return R.ok();
    }

    /**
     * 根据主键更新规格组
     *
     * @param groupId 规格组ID
     * @param specGroupDTO 规格组信息
     */
    @Operation(summary = "更新规格组")
    @PutMapping("update/{id}")
    public R<?> update(@PathVariable("id") Long groupId,
                          @Validated(ValidationGroups.Update.class) @RequestBody SpecGroupDTO specGroupDTO,
                          @SaLoginId Long userId) {
        specGroupService.updateSpecGroup(userId, groupId, specGroupDTO);
        return R.ok();
    }

    /**
     * 获取商品规格组
     *
     */
    @Operation(summary = "获取商品规格组")
    @GetMapping("list")
    public R<List<SpecGroupVO>> list(@RequestParam(name = "product_id") Long productId) {
        return R.ok(specGroupService.getSpecGroupByProductId(productId));
    }

}
