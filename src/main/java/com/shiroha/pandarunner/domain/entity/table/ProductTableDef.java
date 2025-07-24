package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 商品表 表定义层。
 *
 * @author 14363
 * @since 2025-07-15
 */
public class ProductTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品表
     */
    public static final ProductTableDef PRODUCT = new ProductTableDef();

    /**
     * 商品ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 商品名称
     */
    public final QueryColumn NAME = new QueryColumn(this, "name");

    /**
     * 商品图片
     */
    public final QueryColumn IMAGE = new QueryColumn(this, "image");

    /**
     * 商品当前价格
     */
    public final QueryColumn PRICE = new QueryColumn(this, "price");

    /**
     * 商品折扣
     */
    public final QueryColumn SALES = new QueryColumn(this, "sales");

    /**
     * 库存(-1表示无限库存)
     */
    public final QueryColumn STOCK = new QueryColumn(this, "stock");

    /**
     * 状态(0-下架 1-上架)
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    
    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    /**
     * 排序权重
     */
    public final QueryColumn SORT_ORDER = new QueryColumn(this, "sort_order");

    
    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    /**
     * 分类ID
     */
    public final QueryColumn CATEGORY_ID = new QueryColumn(this, "category_id");

    /**
     * 是否优先展示商品
     */
    public final QueryColumn IS_PRIORITY = new QueryColumn(this, "is_priority");

    /**
     * 关联的商家ID
     */
    public final QueryColumn MERCHANT_ID = new QueryColumn(this, "merchant_id");

    /**
     * 商品描述
     */
    public final QueryColumn DESCRIPTION = new QueryColumn(this, "description");

    /**
     * 商品原始价格
     */
    public final QueryColumn ORIGINAL_PRICE = new QueryColumn(this, "original_price");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, MERCHANT_ID, CATEGORY_ID, NAME, DESCRIPTION, IS_PRIORITY, PRICE, ORIGINAL_PRICE, IMAGE, STOCK, SALES, STATUS, SORT_ORDER, IS_DELETED, CREATED_AT, UPDATED_AT};

    public ProductTableDef() {
        super("", "t_product");
    }

    private ProductTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public ProductTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new ProductTableDef("", "t_product", alias));
    }

}
