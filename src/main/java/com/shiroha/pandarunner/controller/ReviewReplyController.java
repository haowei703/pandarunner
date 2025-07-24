package com.shiroha.pandarunner.controller;

import com.mybatisflex.core.paginate.Page;
import com.shiroha.pandarunner.domain.entity.ReviewReply;
import com.shiroha.pandarunner.service.ReviewReplyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "评价回复模块")
@RestController
@RequestMapping("/review/reply")
@AllArgsConstructor
public class ReviewReplyController {

    private final ReviewReplyService reviewReplyService;

    /**
     * 添加评价回复表。
     *
     * @param reviewReply 评价回复表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ReviewReply reviewReply) {
        return reviewReplyService.save(reviewReply);
    }

    /**
     * 根据主键删除评价回复表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Long id) {
        return reviewReplyService.removeById(id);
    }

    /**
     * 根据主键更新评价回复表。
     *
     * @param reviewReply 评价回复表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ReviewReply reviewReply) {
        return reviewReplyService.updateById(reviewReply);
    }

    /**
     * 查询所有评价回复表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ReviewReply> list() {
        return reviewReplyService.list();
    }

    /**
     * 根据评价回复表主键获取详细信息。
     *
     * @param id 评价回复表主键
     * @return 评价回复表详情
     */
    @GetMapping("getInfo/{id}")
    public ReviewReply getInfo(@PathVariable Long id) {
        return reviewReplyService.getById(id);
    }

    /**
     * 分页查询评价回复表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ReviewReply> page(Page<ReviewReply> page) {
        return reviewReplyService.page(page);
    }

}
