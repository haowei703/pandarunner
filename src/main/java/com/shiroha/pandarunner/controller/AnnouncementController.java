package com.shiroha.pandarunner.controller;

import com.mybatisflex.core.paginate.Page;
import com.shiroha.pandarunner.domain.entity.Announcement;
import com.shiroha.pandarunner.service.AnnouncementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "系统公告模块")
@RestController
@RequestMapping("/announcement")
@AllArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    /**
     * 添加系统公告表。
     *
     * @param announcement 系统公告表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody Announcement announcement) {
        return announcementService.save(announcement);
    }

    /**
     * 根据主键删除系统公告表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Long id) {
        return announcementService.removeById(id);
    }

    /**
     * 根据主键更新系统公告表。
     *
     * @param announcement 系统公告表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody Announcement announcement) {
        return announcementService.updateById(announcement);
    }

    /**
     * 查询所有系统公告表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<Announcement> list() {
        return announcementService.list();
    }

    /**
     * 根据系统公告表主键获取详细信息。
     *
     * @param id 系统公告表主键
     * @return 系统公告表详情
     */
    @GetMapping("getInfo/{id}")
    public Announcement getInfo(@PathVariable Long id) {
        return announcementService.getById(id);
    }

    /**
     * 分页查询系统公告表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<Announcement> page(Page<Announcement> page) {
        return announcementService.page(page);
    }

}
