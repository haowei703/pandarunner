package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 购物车表 表定义层。
 *
 * @author 14363
 * @since 2025-07-16
 */
public class CartTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 购物车表
     */
    public static final CartTableDef CART = new CartTableDef();

    /**
     * 购物车ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 关联的用户ID
     */
    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    
    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    
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
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, USER_ID, MERCHANT_ID, IS_DELETED, CREATED_AT, UPDATED_AT};

    public CartTableDef() {
        super("", "t_cart");
    }

    private CartTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public CartTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new CartTableDef("", "t_cart", alias));
    }

}
