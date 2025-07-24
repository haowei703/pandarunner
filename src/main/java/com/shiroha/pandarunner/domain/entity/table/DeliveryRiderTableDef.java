package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 骑手表 表定义层。
 *
 * @author haowei703
 * @since 2025-07-10
 */
public class DeliveryRiderTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 骑手表
     */
    public static final DeliveryRiderTableDef DELIVERY_RIDER = new DeliveryRiderTableDef();

    /**
     * 骑手主键ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 骑手姓名
     */
    public final QueryColumn NAME = new QueryColumn(this, "name");

    /**
     * 骑手电话
     */
    public final QueryColumn PHONE = new QueryColumn(this, "phone");

    /**
     * 骑手身份证号
     */
    public final QueryColumn ID_CARD = new QueryColumn(this, "id_card");

    /**
     * 骑手评分(1.0-5.0)
     */
    public final QueryColumn RATING = new QueryColumn(this, "rating");

    /**
     * 状态(0-休息 1-可接单 2-忙碌)
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 关联的用户ID
     */
    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    
    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    
    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    /**
     * 累计配送单数
     */
    public final QueryColumn DELIVERY_COUNT = new QueryColumn(this, "delivery_count");

    /**
     * 骑手位置
     */
    public final QueryColumn CURRENT_LOCATION = new QueryColumn(this, "current_location");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, USER_ID, NAME, PHONE, ID_CARD, STATUS, CURRENT_LOCATION, RATING, DELIVERY_COUNT, IS_DELETED, CREATED_AT, UPDATED_AT};

    public DeliveryRiderTableDef() {
        super("", "t_delivery_rider");
    }

    private DeliveryRiderTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public DeliveryRiderTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new DeliveryRiderTableDef("", "t_delivery_rider", alias));
    }

}
