package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 规格选项表 表定义层。
 *
 * @author haowei703
 * @since 2025-07-10
 */
public class SpecOptionTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 规格选项表
     */
    public static final SpecOptionTableDef SPEC_OPTION = new SpecOptionTableDef();

    
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 规格选项名称
     */
    public final QueryColumn NAME = new QueryColumn(this, "name");

    /**
     * 关联的规格组ID
     */
    public final QueryColumn GROUP_ID = new QueryColumn(this, "group_id");

    
    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    /**
     * 是否默认选择
     */
    public final QueryColumn IS_DEFAULT = new QueryColumn(this, "is_default");

    
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    /**
     * 关联的商品ID
     */
    public final QueryColumn PRODUCT_ID = new QueryColumn(this, "product_id");

    
    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    /**
     * 价格偏移量
     */
    public final QueryColumn PRICE_ADJUST = new QueryColumn(this, "price_adjust");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, GROUP_ID, PRODUCT_ID, NAME, PRICE_ADJUST, IS_DEFAULT, IS_DELETED, CREATED_AT, UPDATED_AT};

    public SpecOptionTableDef() {
        super("", "t_spec_option");
    }

    private SpecOptionTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SpecOptionTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SpecOptionTableDef("", "t_spec_option", alias));
    }

}
