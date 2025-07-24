package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 订单商品表 表定义层。
 *
 * @author haowei703
 * @since 2025-07-10
 */
public class OrderItemTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单商品表
     */
    public static final OrderItemTableDef ORDER_ITEM = new OrderItemTableDef();

    /**
     * 订单商品项主键ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 规格组合信息，格式为"规格1,规格2,..."的字符串（如"大杯,少冰,半糖"）
     */
    public final QueryColumn SPEC = new QueryColumn(this, "spec");

    /**
     * 商品单价快照（下单时的价格，单位：元）
     */
    public final QueryColumn PRICE = new QueryColumn(this, "price");

    /**
     * 关联的订单ID，引用t_order表的id
     */
    public final QueryColumn ORDER_ID = new QueryColumn(this, "order_id");

    /**
     * 购买数量（正整数）
     */
    public final QueryColumn QUANTITY = new QueryColumn(this, "quantity");

    
    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    /**
     * 关联的商品ID，引用t_product表的id
     */
    public final QueryColumn PRODUCT_ID = new QueryColumn(this, "product_id");

    
    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    /**
     * 该商品项总金额（计算公式：price × quantity）
     */
    public final QueryColumn TOTAL_PRICE = new QueryColumn(this, "total_price");

    /**
     * 商品名称快照（下单时的名称，避免商品更名影响历史订单）
     */
    public final QueryColumn PRODUCT_NAME = new QueryColumn(this, "product_name");

    /**
     * 商品图片快照URL（下单时的图片）
     */
    public final QueryColumn PRODUCT_IMAGE = new QueryColumn(this, "product_image");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, ORDER_ID, PRODUCT_ID, PRODUCT_NAME, PRODUCT_IMAGE, PRICE, QUANTITY, SPEC, TOTAL_PRICE, IS_DELETED, CREATED_AT, UPDATED_AT};

    public OrderItemTableDef() {
        super("", "t_order_item");
    }

    private OrderItemTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public OrderItemTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new OrderItemTableDef("", "t_order_item", alias));
    }

}
