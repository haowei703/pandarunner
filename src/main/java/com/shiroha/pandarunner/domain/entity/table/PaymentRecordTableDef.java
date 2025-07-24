package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 支付记录表 表定义层。
 *
 * @author haowei703
 * @since 2025-07-10
 */
public class PaymentRecordTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 支付记录表
     */
    public static final PaymentRecordTableDef PAYMENT_RECORD = new PaymentRecordTableDef();

    /**
     * 支付记录主键ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 实际支付金额
     */
    public final QueryColumn AMOUNT = new QueryColumn(this, "amount");

    /**
     * 支付状态(0-未支付 1-支付成功 2-支付失败 3-已退款)
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 关联的订单ID
     */
    public final QueryColumn ORDER_ID = new QueryColumn(this, "order_id");

    /**
     * 支付成功时间
     */
    public final QueryColumn PAY_TIME = new QueryColumn(this, "pay_time");

    
    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    /**
     * 支付流水号
     */
    public final QueryColumn PAYMENT_NO = new QueryColumn(this, "payment_no");

    
    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    /**
     * 支付方式(1-支付宝 2-微信 3-银行卡)
     */
    public final QueryColumn PAYMENT_METHOD = new QueryColumn(this, "payment_method");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, ORDER_ID, PAYMENT_NO, PAYMENT_METHOD, AMOUNT, STATUS, PAY_TIME, IS_DELETED, CREATED_AT, UPDATED_AT};

    public PaymentRecordTableDef() {
        super("", "t_payment_record");
    }

    private PaymentRecordTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public PaymentRecordTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new PaymentRecordTableDef("", "t_payment_record", alias));
    }

}
