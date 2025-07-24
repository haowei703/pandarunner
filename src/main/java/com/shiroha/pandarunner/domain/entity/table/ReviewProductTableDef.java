package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 商品评价表 表定义层。
 *
 * @author haowei703
 * @since 2025-07-10
 */
public class ReviewProductTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品评价表
     */
    public static final ReviewProductTableDef REVIEW_PRODUCT = new ReviewProductTableDef();

    /**
     * 商品评价主键ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 评分(0-5)
     */
    public final QueryColumn RATING = new QueryColumn(this, "rating");

    /**
     * 评价内容
     */
    public final QueryColumn CONTENT = new QueryColumn(this, "content");

    /**
     * 关联的评价主表ID
     */
    public final QueryColumn REVIEW_ID = new QueryColumn(this, "review_id");

    
    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    /**
     * 评价图片列表
     */
    public final QueryColumn IMAGE_URLS = new QueryColumn(this, "image_urls");

    
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    /**
     * 评价的商品ID
     */
    public final QueryColumn PRODUCT_ID = new QueryColumn(this, "product_id");

    
    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, REVIEW_ID, PRODUCT_ID, RATING, CONTENT, IMAGE_URLS, IS_DELETED, CREATED_AT, UPDATED_AT};

    public ReviewProductTableDef() {
        super("", "t_review_product");
    }

    private ReviewProductTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public ReviewProductTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new ReviewProductTableDef("", "t_review_product", alias));
    }

}
