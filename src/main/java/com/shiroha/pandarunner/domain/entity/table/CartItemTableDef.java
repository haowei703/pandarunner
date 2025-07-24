package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 购物车项表 表定义层。
 *
 * @author 14363
 * @since 2025-07-22
 */
public class CartItemTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 购物车项表
     */
    public static final CartItemTableDef CART_ITEM = new CartItemTableDef();

    /**
     * 购物车ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 关联的购物车ID
     */
    public final QueryColumn CART_ID = new QueryColumn(this, "cart_id");

    /**
     * 商品数量
     */
    public final QueryColumn QUANTITY = new QueryColumn(this, "quantity");

    
    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    /**
     * 关联的规格选项ID列表
     */
    public final QueryColumn OPTION_IDS = new QueryColumn(this, "option_ids");

    
    public final QueryColumn PRODUCT_ID = new QueryColumn(this, "product_id");

    
    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, CART_ID, PRODUCT_ID, OPTION_IDS, QUANTITY, IS_DELETED, CREATED_AT, UPDATED_AT};

    public CartItemTableDef() {
        super("", "t_cart_item");
    }

    private CartItemTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public CartItemTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new CartItemTableDef("", "t_cart_item", alias));
    }

}
