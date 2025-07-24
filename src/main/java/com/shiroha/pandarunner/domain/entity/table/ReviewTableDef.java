package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 评价表 表定义层。
 *
 * @author haowei703
 * @since 2025-07-10
 */
public class ReviewTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 评价表
     */
    public static final ReviewTableDef REVIEW = new ReviewTableDef();

    /**
     * 评价主键ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 评价用户ID
     */
    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    /**
     * 评价内容
     */
    public final QueryColumn CONTENT = new QueryColumn(this, "content");

    /**
     * 关联的订单ID
     */
    public final QueryColumn ORDER_ID = new QueryColumn(this, "order_id");

    
    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    
    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    /**
     * 关联的商家ID
     */
    public final QueryColumn MERCHANT_ID = new QueryColumn(this, "merchant_id");

    /**
     * 配送评分
     */
    public final QueryColumn DELIVERY_RATING = new QueryColumn(this, "delivery_rating");

    /**
     * 商家评分
     */
    public final QueryColumn MERCHANT_RATING = new QueryColumn(this, "merchant_rating");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, USER_ID, ORDER_ID, MERCHANT_ID, MERCHANT_RATING, DELIVERY_RATING, CONTENT, IS_DELETED, CREATED_AT, UPDATED_AT};

    public ReviewTableDef() {
        super("", "t_review");
    }

    private ReviewTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public ReviewTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new ReviewTableDef("", "t_review", alias));
    }

}
