package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 商品分类表 表定义层。
 *
 * @author haowei703
 * @since 2025-07-10
 */
public class ProductCategoryTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品分类表
     */
    public static final ProductCategoryTableDef PRODUCT_CATEGORY = new ProductCategoryTableDef();

    /**
     * 分类ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 商品分类名称
     */
    public final QueryColumn NAME = new QueryColumn(this, "name");

    
    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    /**
     * 排序权重
     */
    public final QueryColumn SORT_ORDER = new QueryColumn(this, "sort_order");

    
    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    /**
     * 关联的商家ID
     */
    public final QueryColumn MERCHANT_ID = new QueryColumn(this, "merchant_id");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, MERCHANT_ID, NAME, SORT_ORDER, IS_DELETED, CREATED_AT, UPDATED_AT};

    public ProductCategoryTableDef() {
        super("", "t_product_category");
    }

    private ProductCategoryTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public ProductCategoryTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new ProductCategoryTableDef("", "t_product_category", alias));
    }

}
