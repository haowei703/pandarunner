package com.shiroha.pandarunner.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shiroha.pandarunner.domain.entity.Review;
import com.shiroha.pandarunner.mapper.ReviewMapper;
import com.shiroha.pandarunner.service.ReviewService;
import org.springframework.stereotype.Service;

/**
 * 评价表 服务层实现。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService{

}
