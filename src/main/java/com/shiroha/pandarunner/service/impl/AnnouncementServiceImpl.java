package com.shiroha.pandarunner.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shiroha.pandarunner.domain.entity.Announcement;
import com.shiroha.pandarunner.mapper.AnnouncementMapper;
import com.shiroha.pandarunner.service.AnnouncementService;
import org.springframework.stereotype.Service;

/**
 * 系统公告表 服务层实现。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

}
