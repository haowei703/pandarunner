package com.shiroha.pandarunner.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shiroha.pandarunner.domain.entity.ReviewProduct;
import com.shiroha.pandarunner.mapper.ReviewProductMapper;
import com.shiroha.pandarunner.service.ReviewProductService;
import org.springframework.stereotype.Service;

/**
 * 商品评价表 服务层实现。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Service
public class ReviewProductServiceImpl extends ServiceImpl<ReviewProductMapper, ReviewProduct> implements ReviewProductService{

}
