package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 规格组表 表定义层。
 *
 * @author haowei703
 * @since 2025-07-10
 */
public class SpecGroupTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 规格组表
     */
    public static final SpecGroupTableDef SPEC_GROUP = new SpecGroupTableDef();

    /**
     * 标识ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 规格名称
     */
    public final QueryColumn NAME = new QueryColumn(this, "name");

    
    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    /**
     * 关联的商品ID
     */
    public final QueryColumn PRODUCT_ID = new QueryColumn(this, "product_id");

    
    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    /**
     * 是否必选
     */
    public final QueryColumn IS_REQUIRED = new QueryColumn(this, "is_required");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, PRODUCT_ID, NAME, IS_REQUIRED, IS_DELETED, CREATED_AT, UPDATED_AT};

    public SpecGroupTableDef() {
        super("", "t_spec_group");
    }

    private SpecGroupTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SpecGroupTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SpecGroupTableDef("", "t_spec_group", alias));
    }

}
