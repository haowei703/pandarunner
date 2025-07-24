package com.shiroha.pandarunner.controller;

import com.mybatisflex.core.paginate.Page;
import com.shiroha.pandarunner.domain.entity.Review;
import com.shiroha.pandarunner.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "评价模块")
@RestController
@RequestMapping("/review")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 添加评价表。
     *
     * @param review 评价表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody Review review) {
        return reviewService.save(review);
    }

    /**
     * 根据主键删除评价表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Long id) {
        return reviewService.removeById(id);
    }

    /**
     * 根据主键更新评价表。
     *
     * @param review 评价表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody Review review) {
        return reviewService.updateById(review);
    }

    /**
     * 查询所有评价表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<Review> list() {
        return reviewService.list();
    }

    /**
     * 根据评价表主键获取详细信息。
     *
     * @param id 评价表主键
     * @return 评价表详情
     */
    @GetMapping("getInfo/{id}")
    public Review getInfo(@PathVariable Long id) {
        return reviewService.getById(id);
    }

    /**
     * 分页查询评价表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<Review> page(Page<Review> page) {
        return reviewService.page(page);
    }

}
