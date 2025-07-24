package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 商家信息表 表定义层。
 *
 * @author 14363
 * @since 2025-07-15
 */
public class MerchantTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商家信息表
     */
    public static final MerchantTableDef MERCHANT = new MerchantTableDef();

    /**
     * 商家ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 商家实体店城市
     */
    public final QueryColumn CITY = new QueryColumn(this, "city");

    /**
     * 商家Logo图片URL
     */
    public final QueryColumn LOGO = new QueryColumn(this, "logo");

    /**
     * 商家名称
     */
    public final QueryColumn NAME = new QueryColumn(this, "name");

    /**
     * 商家评分(1.0-5.0)
     */
    public final QueryColumn SCORE = new QueryColumn(this, "score");

    /**
     * 状态(0-待审核 1-正常 2-禁用)
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 关联的用户ID
     */
    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    /**
     * 商家实体店地区
     */
    public final QueryColumn DISTRICT = new QueryColumn(this, "district");

    /**
     * 商家实体店经纬度
     */
    public final QueryColumn LOCATION = new QueryColumn(this, "location");

    /**
     * 商家实体店省份
     */
    public final QueryColumn PROVINCE = new QueryColumn(this, "province");

    
    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    
    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    /**
     * 打包费
     */
    public final QueryColumn PACKING_FEE = new QueryColumn(this, "packing_fee");

    /**
     * 历史总销量
     */
    public final QueryColumn TOTAL_SALES = new QueryColumn(this, "total_sales");

    /**
     * 配送费
     */
    public final QueryColumn DELIVERY_FEE = new QueryColumn(this, "delivery_fee");

    /**
     * 商家描述
     */
    public final QueryColumn DESCRIPTION = new QueryColumn(this, "description");

    /**
     * 商家联系电话
     */
    public final QueryColumn CONTACT_PHONE = new QueryColumn(this, "contact_phone");

    /**
     * 月销量
     */
    public final QueryColumn MONTHLY_SALES = new QueryColumn(this, "monthly_sales");

    /**
     * 商家实体店详细地址
     */
    public final QueryColumn DETAIL_ADDRESS = new QueryColumn(this, "detail_address");

    /**
     * 最低起送金额
     */
    public final QueryColumn MIN_ORDER_AMOUNT = new QueryColumn(this, "min_order_amount");

    /**
     * 平均配送时间(分钟)
     */
    public final QueryColumn AVG_DELIVERY_TIME = new QueryColumn(this, "avg_delivery_time");

    /**
     * 营业执照编号
     */
    public final QueryColumn BUSINESS_LICENSE = new QueryColumn(this, "business_license");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, USER_ID, NAME, DESCRIPTION, LOGO, CONTACT_PHONE, PROVINCE, CITY, DISTRICT, DETAIL_ADDRESS, LOCATION, BUSINESS_LICENSE, STATUS, AVG_DELIVERY_TIME, MIN_ORDER_AMOUNT, DELIVERY_FEE, PACKING_FEE, SCORE, MONTHLY_SALES, TOTAL_SALES, IS_DELETED, CREATED_AT, UPDATED_AT};

    public MerchantTableDef() {
        super("", "t_merchant");
    }

    private MerchantTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public MerchantTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new MerchantTableDef("", "t_merchant", alias));
    }

}
