package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 用户地址表 表定义层。
 *
 * @author haowei703
 * @since 2025-07-11
 */
public class UserAddressTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户地址表
     */
    public static final UserAddressTableDef USER_ADDRESS = new UserAddressTableDef();

    /**
     * 地址ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 地址标签(如"家"、"公司")
     */
    public final QueryColumn TAG = new QueryColumn(this, "tag");

    /**
     * 收货地址城市
     */
    public final QueryColumn CITY = new QueryColumn(this, "city");

    /**
     * 关联的用户ID
     */
    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    /**
     * 收货地址地区
     */
    public final QueryColumn DISTRICT = new QueryColumn(this, "district");

    /**
     * 收货地址经纬度坐标
     */
    public final QueryColumn LOCATION = new QueryColumn(this, "location");

    /**
     * 收货地址省份
     */
    public final QueryColumn PROVINCE = new QueryColumn(this, "province");

    
    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    /**
     * 是否默认
     */
    public final QueryColumn IS_DEFAULT = new QueryColumn(this, "is_default");

    /**
     * 逻辑删除
     */
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    
    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    /**
     * 收货人姓名
     */
    public final QueryColumn RECEIVER_NAME = new QueryColumn(this, "receiver_name");

    /**
     * 收货地址详细信息
     */
    public final QueryColumn DETAIL_ADDRESS = new QueryColumn(this, "detail_address");

    /**
     * 收货人电话
     */
    public final QueryColumn RECEIVER_PHONE = new QueryColumn(this, "receiver_phone");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, USER_ID, RECEIVER_NAME, RECEIVER_PHONE, PROVINCE, CITY, DISTRICT, DETAIL_ADDRESS, LOCATION, IS_DEFAULT, TAG, IS_DELETED, CREATED_AT, UPDATED_AT};

    public UserAddressTableDef() {
        super("", "t_user_address");
    }

    private UserAddressTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public UserAddressTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new UserAddressTableDef("", "t_user_address", alias));
    }

}
