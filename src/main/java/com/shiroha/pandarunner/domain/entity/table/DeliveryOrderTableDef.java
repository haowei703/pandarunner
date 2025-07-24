package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 配送订单表 表定义层。
 *
 * @author haowei703
 * @since 2025-07-10
 */
public class DeliveryOrderTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配送订单表
     */
    public static final DeliveryOrderTableDef DELIVERY_ORDER = new DeliveryOrderTableDef();

    /**
     * 配送订单主键ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 配送状态(0-待接单 1-已接单 2-已取货 3-配送中 4-已完成 5-已取消)
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 关联的订单ID
     */
    public final QueryColumn ORDER_ID = new QueryColumn(this, "order_id");

    
    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    
    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    /**
     * 接单时间
     */
    public final QueryColumn ACCEPT_TIME = new QueryColumn(this, "accept_time");

    /**
     * 取货时间
     */
    public final QueryColumn PICKUP_TIME = new QueryColumn(this, "pickup_time");

    /**
     * 开始配送时间
     */
    public final QueryColumn DELIVER_TIME = new QueryColumn(this, "deliver_time");

    /**
     * 配送完成时间
     */
    public final QueryColumn COMPLETION_TIME = new QueryColumn(this, "completion_time");

    /**
     * 骑手ID
     */
    public final QueryColumn DELIVERY_RIDER_ID = new QueryColumn(this, "delivery_rider_id");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, ORDER_ID, DELIVERY_RIDER_ID, ACCEPT_TIME, PICKUP_TIME, DELIVER_TIME, COMPLETION_TIME, STATUS, IS_DELETED, CREATED_AT, UPDATED_AT};

    public DeliveryOrderTableDef() {
        super("", "t_delivery_order");
    }

    private DeliveryOrderTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public DeliveryOrderTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new DeliveryOrderTableDef("", "t_delivery_order", alias));
    }

}
