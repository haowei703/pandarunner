package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 系统公告表 表定义层。
 *
 * @author haowei703
 * @since 2025-07-10
 */
public class AnnouncementTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 系统公告表
     */
    public static final AnnouncementTableDef ANNOUNCEMENT = new AnnouncementTableDef();

    /**
     * 公告主键ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 公告标题
     */
    public final QueryColumn TITLE = new QueryColumn(this, "title");

    /**
     * 公告状态(0-关闭 1-正常)
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 公告内容
     */
    public final QueryColumn CONTENT = new QueryColumn(this, "content");

    
    public final QueryColumn CREATED_AT = new QueryColumn(this, "created_at");

    /**
     * 逻辑删除
     */
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    
    public final QueryColumn UPDATED_AT = new QueryColumn(this, "updated_at");

    /**
     * 公告发布者ID
     */
    public final QueryColumn PUBLISHER_ID = new QueryColumn(this, "publisher_id");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, TITLE, CONTENT, PUBLISHER_ID, STATUS, IS_DELETED, CREATED_AT, UPDATED_AT};

    public AnnouncementTableDef() {
        super("", "t_announcement");
    }

    private AnnouncementTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public AnnouncementTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new AnnouncementTableDef("", "t_announcement", alias));
    }

}
