package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 订单表 表定义层。
 *
 * @author 14363
 * @since 2025-07-23
 */
public class OrderTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单表
     */
    public static final OrderTableDef ORDER = new OrderTableDef();

    /**
     * 订单主键ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 用户备注(如"不要辣，放门口")
     */
    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    /**
     * 订单状态(0-待支付 1-已支付待接单 2-已接单制作中 3-配送中 4-已完成 5-已取消 6-已退款)
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 关联用户ID，引用t_user表的id
     */
    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    /**
     * 订单编号，业务唯一标识(如ORD20230701123456)
     */
    public final QueryColumn ORDER_NO = new QueryColumn(this, "order_no");

    /**
     * 关联地址ID，引用地址表的id
     */
    public final QueryColumn ADDRESS_ID = new QueryColumn(this, "address_id");

    
    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    /**
     * 逻辑删除
     */
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    
    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    /**
     * 关联商家ID，引用t_merchant表的id
     */
    public final QueryColumn MERCHANT_ID = new QueryColumn(this, "merchant_id");

    /**
     * 打包费用
     */
    public final QueryColumn PACKING_FEE = new QueryColumn(this, "packing_fee");

    /**
     * 配送费用
     */
    public final QueryColumn DELIVERY_FEE = new QueryColumn(this, "delivery_fee");

    /**
     * 支付成功时间
     */
    public final QueryColumn PAYMENT_TIME = new QueryColumn(this, "payment_time");

    /**
     * 订单原总金额(商品总额+配送费)
     */
    public final QueryColumn TOTAL_AMOUNT = new QueryColumn(this, "total_amount");

    /**
     * 实际支付金额(扣除优惠后)
     */
    public final QueryColumn ACTUAL_AMOUNT = new QueryColumn(this, "actual_amount");

    /**
     * 开始配送时间
     */
    public final QueryColumn DELIVERY_TIME = new QueryColumn(this, "delivery_time");

    /**
     * 餐具数量(-1-不需要 0-商家按出餐量提供 1-15-用户选择具体数量，不超过15份)
     */
    public final QueryColumn UTENSILS_COUNT = new QueryColumn(this, "utensils_count");

    /**
     * 订单完成/确认收货时间
     */
    public final QueryColumn COMPLETION_TIME = new QueryColumn(this, "completion_time");

    /**
     * 优惠减免金额(促销/优惠券等)
     */
    public final QueryColumn DISCOUNT_AMOUNT = new QueryColumn(this, "discount_amount");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, ORDER_NO, USER_ID, MERCHANT_ID, ADDRESS_ID, TOTAL_AMOUNT, ACTUAL_AMOUNT, DELIVERY_FEE, PACKING_FEE, DISCOUNT_AMOUNT, REMARK, UTENSILS_COUNT, STATUS, PAYMENT_TIME, DELIVERY_TIME, COMPLETION_TIME, IS_DELETED, CREATED_AT, UPDATED_AT};

    public OrderTableDef() {
        super("", "t_order");
    }

    private OrderTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public OrderTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new OrderTableDef("", "t_order", alias));
    }

}
