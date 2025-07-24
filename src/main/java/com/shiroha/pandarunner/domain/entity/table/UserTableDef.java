package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 用户表 表定义层。
 *
 * @author haowei703
 * @since 2025-07-10
 */
public class UserTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户表
     */
    public static final UserTableDef USER = new UserTableDef();

    /**
     * 用户ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 角色(0-普通用户 1-管理员 2-超级管理员)
     */
    public final QueryColumn ROLE = new QueryColumn(this, "role");

    /**
     * 电话
     */
    public final QueryColumn PHONE = new QueryColumn(this, "phone");

    /**
     * 头像URL
     */
    public final QueryColumn AVATAR = new QueryColumn(this, "avatar");

    /**
     * 性别(0-未知 1-男 2-女)
     */
    public final QueryColumn GENDER = new QueryColumn(this, "gender");

    /**
     * 状态(0-禁用 1-正常)
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 密码
     */
    public final QueryColumn PASSWORD = new QueryColumn(this, "password");

    /**
     * 用户名
     */
    public final QueryColumn USERNAME = new QueryColumn(this, "username");

    
    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    
    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, USERNAME, PASSWORD, PHONE, AVATAR, GENDER, ROLE, STATUS, IS_DELETED, CREATED_AT, UPDATED_AT};

    public UserTableDef() {
        super("", "t_user");
    }

    private UserTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public UserTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new UserTableDef("", "t_user", alias));
    }

}
