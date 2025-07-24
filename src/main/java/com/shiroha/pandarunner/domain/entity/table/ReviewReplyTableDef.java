package com.shiroha.pandarunner.domain.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 评价回复表 表定义层。
 *
 * @author haowei703
 * @since 2025-07-10
 */
public class ReviewReplyTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 评价回复表
     */
    public static final ReviewReplyTableDef REVIEW_REPLY = new ReviewReplyTableDef();

    /**
     * 评价回复主键ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 回复内容
     */
    public final QueryColumn CONTENT = new QueryColumn(this, "content");

    /**
     * 关联的评价ID
     */
    public final QueryColumn REVIEW_ID = new QueryColumn(this, "review_id");

    
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
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, REVIEW_ID, CONTENT, IS_DELETED, CREATED_AT, UPDATED_AT};

    public ReviewReplyTableDef() {
        super("", "t_review_reply");
    }

    private ReviewReplyTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public ReviewReplyTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new ReviewReplyTableDef("", "t_review_reply", alias));
    }

}
