package com.shiroha.pandarunner.controller;

import com.mybatisflex.core.paginate.Page;
import com.shiroha.pandarunner.domain.entity.ReviewProduct;
import com.shiroha.pandarunner.service.ReviewProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品评价模块")
@RestController
@RequestMapping("/review/product")
@AllArgsConstructor
public class ReviewProductController {


    private final ReviewProductService reviewProductService;

    /**
     * 添加商品评价表。
     *
     * @param reviewProduct 商品评价表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ReviewProduct reviewProduct) {
        return reviewProductService.save(reviewProduct);
    }

    /**
     * 根据主键删除商品评价表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Long id) {
        return reviewProductService.removeById(id);
    }

    /**
     * 根据主键更新商品评价表。
     *
     * @param reviewProduct 商品评价表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ReviewProduct reviewProduct) {
        return reviewProductService.updateById(reviewProduct);
    }

    /**
     * 查询所有商品评价表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ReviewProduct> list() {
        return reviewProductService.list();
    }

    /**
     * 根据商品评价表主键获取详细信息。
     *
     * @param id 商品评价表主键
     * @return 商品评价表详情
     */
    @GetMapping("getInfo/{id}")
    public ReviewProduct getInfo(@PathVariable Long id) {
        return reviewProductService.getById(id);
    }

    /**
     * 分页查询商品评价表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ReviewProduct> page(Page<ReviewProduct> page) {
        return reviewProductService.page(page);
    }

}
