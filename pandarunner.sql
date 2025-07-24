-- 用户表
DROP TABLE IF EXISTS "t_user";
CREATE TABLE "t_user"
(
    "id"         BIGSERIAL PRIMARY KEY,
    "username"   VARCHAR(50)  NOT NULL UNIQUE,
    "password"   VARCHAR(100) NOT NULL,
    "phone"      VARCHAR(20)  NOT NULL UNIQUE,
    "avatar"     VARCHAR(255),
    "gender"     SMALLINT              DEFAULT 0 CHECK ("gender" IN (0, 1, 2)),
    "role"       SMALLINT              DEFAULT 0 CHECK ( "role" IN (0, 1, 2) ),
    "status"     SMALLINT              DEFAULT 1 CHECK ("status" IN (0, 1)),
    "is_deleted" BOOLEAN               DEFAULT FALSE,
    "created_at" TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_user" IS '用户表';
COMMENT ON COLUMN "t_user"."id" IS '用户ID';
COMMENT ON COLUMN "t_user".username IS '用户名';
COMMENT ON COLUMN "t_user".password IS '密码';
COMMENT ON COLUMN "t_user".phone IS '电话';
COMMENT ON COLUMN "t_user".avatar IS '头像URL';
COMMENT ON COLUMN "t_user"."gender" IS '性别(0-未知 1-男 2-女)';
COMMENT ON COLUMN "t_user"."role" IS '角色(0-普通用户 1-管理员 2-超级管理员)';
COMMENT ON COLUMN "t_user"."status" IS '状态(0-禁用 1-正常)';

-- 用户地址表
DROP TABLE IF EXISTS "t_user_address";
CREATE TABLE "t_user_address"
(
    "id"             BIGSERIAL PRIMARY KEY,
    "user_id"        BIGINT       NOT NULL,
    "receiver_name"  VARCHAR(50)  NOT NULL,
    "receiver_phone" VARCHAR(20)  NOT NULL,
    "province"       VARCHAR(50)  NOT NULL,
    "city"           VARCHAR(50)  NOT NULL,
    "district"       VARCHAR(50)  NOT NULL,
    "detail_address" VARCHAR(255) NOT NULL,
    "location"       VARCHAR(50)  NOT NULL,
    "is_default"     BOOLEAN               DEFAULT FALSE,
    "tag"            VARCHAR(20),
    "is_deleted"     BOOLEAN               DEFAULT FALSE,
    "created_at"     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_user_address" IS '用户地址表';
COMMENT ON COLUMN "t_user_address".id IS '地址ID';
COMMENT ON COLUMN "t_user_address".user_id IS '关联的用户ID';
COMMENT ON COLUMN "t_user_address".receiver_name IS '收货人姓名';
COMMENT ON COLUMN "t_user_address".receiver_phone IS '收货人电话';
COMMENT ON COLUMN "t_user_address".province IS '收货地址省份';
COMMENT ON COLUMN "t_user_address".city IS '收货地址城市';
COMMENT ON COLUMN "t_user_address".district IS '收货地址地区';
COMMENT ON COLUMN "t_user_address".detail_address IS '收货地址详细信息';
COMMENT ON COLUMN "t_user_address"."location" IS '收货地址经纬度坐标';
COMMENT ON COLUMN "t_user_address".tag IS '地址标签(如"家"、"公司")';
COMMENT ON COLUMN "t_user_address"."is_default" IS '是否默认';
COMMENT ON COLUMN "t_user_address"."is_deleted" IS '逻辑删除';

-- 商家表
DROP TABLE IF EXISTS "t_merchant";
CREATE TABLE "t_merchant"
(
    "id"                BIGSERIAL PRIMARY KEY,
    "user_id"           BIGINT       NOT NULL,
    "name"              VARCHAR(100) NOT NULL,
    "description"       TEXT,
    "logo"              VARCHAR(255),
    "contact_phone"     VARCHAR(20)  NOT NULL,
    "province"          VARCHAR(50)  NOT NULL,
    "city"              VARCHAR(50)  NOT NULL,
    "district"          VARCHAR(50)  NOT NULL,
    "detail_address"    VARCHAR(255) NOT NULL,
    "location"          VARCHAR(50)  NOT NULL,
    "business_license"  VARCHAR(100) NOT NULL,
    "status"            SMALLINT              DEFAULT 0 CHECK ("status" IN (0, 1, 2)),
    "avg_delivery_time" INT,
    "min_order_amount"  DECIMAL(10, 2),
    "delivery_fee"      DECIMAL(10, 2),
    "packing_fee"       DECIMAL(10, 2),
    "score"             DECIMAL(3, 1)         DEFAULT 5.0,
    "monthly_sales"     INT                   DEFAULT 0,
    "total_sales"       INT                   DEFAULT 0,
    "is_deleted"        BOOLEAN               DEFAULT FALSE,
    "created_at"        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_merchant" IS '商家信息表';
COMMENT ON COLUMN "t_merchant"."id" IS '商家ID';
COMMENT ON COLUMN "t_merchant".user_id IS '关联的用户ID';
COMMENT ON COLUMN "t_merchant"."name" IS '商家名称';
COMMENT ON COLUMN "t_merchant"."description" IS '商家描述';
COMMENT ON COLUMN "t_merchant"."logo" IS '商家Logo图片URL';
COMMENT ON COLUMN "t_merchant"."contact_phone" IS '商家联系电话';
COMMENT ON COLUMN "t_merchant"."province" IS '商家实体店省份';
COMMENT ON COLUMN "t_merchant"."city" IS '商家实体店城市';
COMMENT ON COLUMN "t_merchant"."district" IS '商家实体店地区';
COMMENT ON COLUMN "t_merchant"."detail_address" IS '商家实体店详细地址';
COMMENT ON COLUMN "t_merchant"."location" IS '商家实体店经纬度';
COMMENT ON COLUMN "t_merchant"."business_license" IS '营业执照编号';
COMMENT ON COLUMN "t_merchant"."status" IS '状态(0-待审核 1-正常 2-禁用)';
COMMENT ON COLUMN "t_merchant"."avg_delivery_time" IS '平均配送时间(分钟)';
COMMENT ON COLUMN "t_merchant"."min_order_amount" IS '最低起送金额';
COMMENT ON COLUMN "t_merchant"."delivery_fee" IS '配送费';
COMMENT ON COLUMN "t_merchant"."packing_fee" IS '打包费';
COMMENT ON COLUMN "t_merchant"."score" IS '商家评分(1.0-5.0)';
COMMENT ON COLUMN "t_merchant"."monthly_sales" IS '月销量';
COMMENT ON COLUMN "t_merchant"."total_sales" IS '历史总销量';

-- 商品分类表
DROP TABLE IF EXISTS "t_product_category";
CREATE TABLE "t_product_category"
(
    "id"          BIGSERIAL PRIMARY KEY,
    "merchant_id" BIGINT      NOT NULL,
    "name"        VARCHAR(50) NOT NULL,
    "sort_order"  INT                  DEFAULT 0,
    "is_deleted"  BOOLEAN              DEFAULT FALSE,
    "created_at"  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_product_category" IS '商品分类表';
COMMENT ON COLUMN "t_product_category".id IS '分类ID';
COMMENT ON COLUMN "t_product_category".merchant_id IS '关联的商家ID';
COMMENT ON COLUMN "t_product_category".name IS '商品分类名称';
COMMENT ON COLUMN "t_product_category".sort_order IS '排序权重';

-- 商品表
DROP TABLE IF EXISTS "t_product";
CREATE TABLE "t_product"
(
    "id"             BIGSERIAL PRIMARY KEY,
    "merchant_id"    BIGINT         NOT NULL,
    "category_id"    BIGINT,
    "name"           VARCHAR(100)   NOT NULL,
    "description"    TEXT,
    "is_priority" BOOLEAN DEFAULT FALSE,
    "price"          DECIMAL(10, 2) NOT NULL,
    "original_price" DECIMAL(10, 2),
    "image"          VARCHAR(255),
    "stock"          INT                     DEFAULT -1,
    "sales"          INT                     DEFAULT 0,
    "status"         SMALLINT                DEFAULT 1 CHECK ("status" IN (0, 1)),
    "sort_order"     INT                     DEFAULT 0,
    "is_deleted"     BOOLEAN                 DEFAULT FALSE,
    "created_at"     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_product" IS '商品表';
COMMENT ON COLUMN "t_product".id IS '商品ID';
COMMENT ON COLUMN "t_product".merchant_id IS '关联的商家ID';
COMMENT ON COLUMN "t_product".category_id IS '分类ID';
COMMENT ON COLUMN "t_product".name IS '商品名称';
COMMENT ON COLUMN "t_product".description IS '商品描述';
COMMENT ON COLUMN "t_product"."is_priority" IS '是否优先展示商品';
COMMENT ON COLUMN "t_product".price IS '商品当前价格';
COMMENT ON COLUMN "t_product".original_price IS '商品原始价格';
COMMENT ON COLUMN "t_product".image IS '商品图片';
COMMENT ON COLUMN "t_product"."stock" IS '库存(-1表示无限库存)';
COMMENT ON COLUMN "t_product".sales IS '商品折扣';
COMMENT ON COLUMN "t_product"."status" IS '状态(0-下架 1-上架)';
COMMENT ON COLUMN "t_product".sort_order IS '排序权重';

-- 规格组表（规格分类）
DROP TABLE IF EXISTS "t_spec_group";
CREATE TABLE "t_spec_group"
(
    "id"          BIGSERIAL PRIMARY KEY,
    "product_id"  BIGINT      NOT NULL,
    "name"        VARCHAR(50) NOT NULL,
    "is_required" BOOLEAN              DEFAULT TRUE,
    "is_deleted"  BOOLEAN              DEFAULT FALSE,
    "created_at"  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_spec_group" IS '规格组表';
COMMENT ON COLUMN "t_spec_group".id IS '标识ID';
COMMENT ON COLUMN "t_spec_group".product_id IS '关联的商品ID';
COMMENT ON COLUMN "t_spec_group".name IS '规格名称';
COMMENT ON COLUMN "t_spec_group".is_required IS '是否必选';

-- 规格选项表（具体可选项）
DROP TABLE IF EXISTS "t_spec_option";
CREATE TABLE "t_spec_option"
(
    "id"           BIGSERIAL PRIMARY KEY,
    "group_id"     BIGINT      NOT NULL,
    "product_id"   BIGINT      NOT NULL,
    "name"         VARCHAR(50) NOT NULL,
    "price_adjust" DECIMAL(10, 2)       DEFAULT 0,
    "is_default"   BOOLEAN              DEFAULT FALSE,
    "is_deleted"   BOOLEAN              DEFAULT FALSE,
    "created_at"   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_spec_option" IS '规格选项表';
COMMENT ON COLUMN "t_spec_option".group_id IS '关联的规格组ID';
COMMENT ON COLUMN "t_spec_option".product_id IS '关联的商品ID';
COMMENT ON COLUMN "t_spec_option".name IS '规格选项名称';
COMMENT ON COLUMN "t_spec_option".price_adjust IS '价格偏移量';
COMMENT ON COLUMN "t_spec_option".is_default IS '是否默认选择';

-- 购物车表
DROP TABLE IF EXISTS "t_cart";
CREATE TABLE "t_cart"
(
    "id"          BIGSERIAL PRIMARY KEY,
    "user_id"     BIGINT         NOT NULL,
    "merchant_id" BIGINT         NOT NULL,
    "is_deleted"  BOOLEAN                 DEFAULT FALSE,
    "created_at"  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_cart" IS '购物车表';
COMMENT ON COLUMN "t_cart"."id" IS '购物车ID';
COMMENT ON COLUMN "t_cart"."user_id" IS '关联的用户ID';
COMMENT ON COLUMN "t_cart"."merchant_id" IS '关联的商家ID';

-- 购物车项表
DROP TABLE IF EXISTS "t_cart_item";
CREATE TABLE "t_cart_item"
(
    "id"         BIGSERIAL PRIMARY KEY,
    "cart_id"    BIGINT    NOT NULL,
    "product_id" BIGINT    NOT NULL,
    "option_ids" BIGINT[],
    "quantity"   INT       NOT NULL,
    "is_deleted" BOOLEAN            DEFAULT FALSE,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_cart_item" IS '购物车项表';
COMMENT ON COLUMN "t_cart_item"."id" IS '购物车ID';
COMMENT ON COLUMN "t_cart_item"."cart_id" IS '关联的购物车ID';
COMMENT ON COLUMN "t_cart_item"."option_ids" IS '关联的规格选项ID列表';
COMMENT ON COLUMN "t_cart_item"."quantity" IS '商品数量';

DROP TABLE IF EXISTS "t_order";
CREATE TABLE "t_order"
(
    "id"              BIGSERIAL PRIMARY KEY,
    "order_no"        VARCHAR(32) NOT NULL UNIQUE,
    "user_id"         BIGINT      NOT NULL,
    "merchant_id"     BIGINT      NOT NULL,
    "address_id"      BIGINT      NOT NULL,
    "total_amount"    DECIMAL(10, 2)       DEFAULT 0,
    "actual_amount"   DECIMAL(10, 2)       DEFAULT 0,
    "delivery_fee"    DECIMAL(10, 2)       DEFAULT 0,
    "packing_fee"     DECIMAL(10, 2)       DEFAULT 0,
    "discount_amount" DECIMAL(10, 2)       DEFAULT 0,
    "remark"          VARCHAR(200),
    "utensils_count"  SMALLINT             DEFAULT 0 CHECK ( "utensils_count" BETWEEN -1 AND 15 AND "utensils_count" <> -2),
    "status"          SMALLINT    NOT NULL DEFAULT 0,
    "payment_time"    TIMESTAMP,
    "delivery_time"   TIMESTAMP,
    "completion_time" TIMESTAMP,
    "is_deleted"      BOOLEAN              DEFAULT FALSE,
    "created_at"      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_order" IS '订单表';
COMMENT ON COLUMN "t_order"."id" IS '订单主键ID';
COMMENT ON COLUMN "t_order"."order_no" IS '订单编号，业务唯一标识(如ORD20230701123456)';
COMMENT ON COLUMN "t_order"."user_id" IS '关联用户ID，引用t_user表的id';
COMMENT ON COLUMN "t_order"."merchant_id" IS '关联商家ID，引用t_merchant表的id';
COMMENT ON COLUMN "t_order"."address_id" IS '关联地址ID，引用地址表的id';
COMMENT ON COLUMN "t_order"."total_amount" IS '订单原总金额(商品总额+配送费)';
COMMENT ON COLUMN "t_order"."actual_amount" IS '实际支付金额(扣除优惠后)';
COMMENT ON COLUMN "t_order"."delivery_fee" IS '配送费用';
COMMENT ON COLUMN "t_order"."packing_fee" IS '打包费用';
COMMENT ON COLUMN "t_order"."discount_amount" IS '优惠减免金额(促销/优惠券等)';
COMMENT ON COLUMN "t_order"."remark" IS '用户备注(如"不要辣，放门口")';
COMMENT ON COLUMN "t_order".utensils_count IS '餐具数量(-1-不需要 0-商家按出餐量提供 1-15-用户选择具体数量，不超过15份)';
COMMENT ON COLUMN "t_order"."status" IS '订单状态(0-待支付 1-已支付待接单 2-已接单制作中 3-配送中 4-已完成 5-已取消 6-已退款)';
COMMENT ON COLUMN "t_order"."payment_time" IS '支付成功时间';
COMMENT ON COLUMN "t_order"."delivery_time" IS '开始配送时间';
COMMENT ON COLUMN "t_order"."completion_time" IS '订单完成/确认收货时间';
COMMENT ON COLUMN "t_order"."is_deleted" IS '逻辑删除';

-- 订单商品表
DROP TABLE IF EXISTS "t_order_item";
CREATE TABLE "t_order_item"
(
    "id"            BIGSERIAL PRIMARY KEY,
    "order_id"      BIGINT         NOT NULL,
    "product_id"    BIGINT         NOT NULL,
    "product_name"  VARCHAR(100)   NOT NULL,
    "product_image" VARCHAR(255),
    "price"         DECIMAL(10, 2) NOT NULL,
    "quantity"      INT            NOT NULL,
    "spec"          VARCHAR(100),
    "total_price"   DECIMAL(10, 2) NOT NULL,
    "is_deleted"    BOOLEAN                 DEFAULT FALSE,
    "created_at"    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_order_item" IS '订单商品表';
COMMENT ON COLUMN "t_order_item"."id" IS '订单商品项主键ID';
COMMENT ON COLUMN "t_order_item"."order_id" IS '关联的订单ID，引用t_order表的id';
COMMENT ON COLUMN "t_order_item"."product_id" IS '关联的商品ID，引用t_product表的id';
COMMENT ON COLUMN "t_order_item"."product_name" IS '商品名称快照（下单时的名称，避免商品更名影响历史订单）';
COMMENT ON COLUMN "t_order_item"."product_image" IS '商品图片快照URL（下单时的图片）';
COMMENT ON COLUMN "t_order_item"."price" IS '商品单价快照（下单时的价格，单位：元）';
COMMENT ON COLUMN "t_order_item"."quantity" IS '购买数量（正整数）';
COMMENT ON COLUMN "t_order_item"."spec" IS '规格组合信息，格式为"规格1,规格2,..."的字符串（如"大杯,少冰,半糖"）';
COMMENT ON COLUMN "t_order_item"."total_price" IS '该商品项总金额（计算公式：price × quantity）';

-- 配送员表
DROP TABLE IF EXISTS "t_delivery_rider";
CREATE TABLE "t_delivery_rider"
(
    "id"               BIGSERIAL PRIMARY KEY,
    "user_id"          BIGINT      NOT NULL,
    "name"             VARCHAR(50) NOT NULL,
    "phone"            VARCHAR(20) NOT NULL UNIQUE,
    "id_card"          VARCHAR(20) NOT NULL,
    "status"           SMALLINT             DEFAULT 1 CHECK ("status" IN (0, 1, 2)),
    "current_location" VARCHAR(100),
    "rating"           DECIMAL(3, 1)        DEFAULT 5.0,
    "delivery_count"   INT                  DEFAULT 0,
    "is_deleted"       BOOLEAN              DEFAULT FALSE,
    "created_at"       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_delivery_rider" IS '骑手表';
COMMENT ON COLUMN "t_delivery_rider".id IS '骑手主键ID';
COMMENT ON COLUMN "t_delivery_rider".user_id IS '关联的用户ID';
COMMENT ON COLUMN "t_delivery_rider".name IS '骑手姓名';
COMMENT ON COLUMN "t_delivery_rider".phone IS '骑手电话';
COMMENT ON COLUMN "t_delivery_rider".id_card IS '骑手身份证号';
COMMENT ON COLUMN "t_delivery_rider".status IS '状态(0-休息 1-可接单 2-忙碌)';
COMMENT ON COLUMN "t_delivery_rider".current_location IS '骑手位置';
COMMENT ON COLUMN "t_delivery_rider".rating IS '骑手评分(1.0-5.0)';
COMMENT ON COLUMN "t_delivery_rider".delivery_count IS '累计配送单数';

-- 配送订单表
DROP TABLE IF EXISTS "t_delivery_order";
CREATE TABLE "t_delivery_order"
(
    "id"                BIGSERIAL PRIMARY KEY,
    "order_id"          BIGINT    NOT NULL UNIQUE,
    "delivery_rider_id" BIGINT,
    "accept_time"       TIMESTAMP,
    "pickup_time"       TIMESTAMP,
    "deliver_time"      TIMESTAMP,
    "completion_time"   TIMESTAMP,
    "status"            SMALLINT  NOT NULL DEFAULT 0,
    "is_deleted"        BOOLEAN            DEFAULT FALSE,
    "created_at"        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_delivery_order" IS '配送订单表';
COMMENT ON COLUMN "t_delivery_order".id IS '配送订单主键ID';
COMMENT ON COLUMN "t_delivery_order".order_id IS '关联的订单ID';
COMMENT ON COLUMN "t_delivery_order".delivery_rider_id IS '骑手ID';
COMMENT ON COLUMN "t_delivery_order".accept_time IS '接单时间';
COMMENT ON COLUMN "t_delivery_order".pickup_time IS '取货时间';
COMMENT ON COLUMN "t_delivery_order".deliver_time IS '开始配送时间';
COMMENT ON COLUMN "t_delivery_order".completion_time IS '配送完成时间';
COMMENT ON COLUMN "t_delivery_order"."status" IS '配送状态(0-待接单 1-已接单 2-已取货 3-配送中 4-已完成 5-已取消)';

-- 支付记录表
DROP TABLE IF EXISTS "t_payment_record";
CREATE TABLE "t_payment_record"
(
    "id"             BIGSERIAL PRIMARY KEY,
    "order_id"       BIGINT         NOT NULL,
    "payment_no"     VARCHAR(32)    NOT NULL UNIQUE,
    "payment_method" SMALLINT       NOT NULL,
    "amount"         DECIMAL(10, 2) NOT NULL,
    "status"         SMALLINT       NOT NULL,
    "pay_time"       TIMESTAMP,
    "is_deleted"     BOOLEAN                 DEFAULT FALSE,
    "created_at"     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_payment_record" IS '支付记录表';
COMMENT ON COLUMN "t_payment_record".id IS '支付记录主键ID';
COMMENT ON COLUMN "t_payment_record".order_id IS '关联的订单ID';
COMMENT ON COLUMN "t_payment_record".payment_no IS '支付流水号';
COMMENT ON COLUMN "t_payment_record"."payment_method" IS '支付方式(1-支付宝 2-微信 3-银行卡)';
COMMENT ON COLUMN "t_payment_record".amount IS '实际支付金额';
COMMENT ON COLUMN "t_payment_record"."status" IS '支付状态(0-未支付 1-支付成功 2-支付失败 3-已退款)';
COMMENT ON COLUMN "t_payment_record".pay_time IS '支付成功时间';

-- 评价总表
DROP TABLE IF EXISTS "t_review";
CREATE TABLE "t_review"
(
    "id"              BIGSERIAL PRIMARY KEY,
    "user_id"         BIGINT    NOT NULL,
    "order_id"        BIGINT    NOT NULL UNIQUE,
    "merchant_id"     BIGINT    NOT NULL,
    "merchant_rating" SMALLINT  NOT NULL CHECK ("merchant_rating" BETWEEN 1 AND 5),
    "delivery_rating" SMALLINT CHECK ("delivery_rating" BETWEEN 1 AND 5),
    "content"         TEXT,
    "is_deleted"      BOOLEAN            DEFAULT FALSE,
    "created_at"      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_review" IS '评价表';
COMMENT ON COLUMN "t_review".id IS '评价主键ID';
COMMENT ON COLUMN "t_review".user_id IS '评价用户ID';
COMMENT ON COLUMN "t_review".order_id IS '关联的订单ID';
COMMENT ON COLUMN "t_review".merchant_id IS '关联的商家ID';
COMMENT ON COLUMN "t_review".merchant_rating IS '商家评分';
COMMENT ON COLUMN "t_review".delivery_rating IS '配送评分';
COMMENT ON COLUMN "t_review".content IS '评价内容';

-- 商品评价表
DROP TABLE IF EXISTS "t_review_product";
CREATE TABLE "t_review_product"
(
    "id"         BIGSERIAL PRIMARY KEY,
    "review_id"  BIGINT    NOT NULL,
    "product_id" BIGINT    NOT NULL UNIQUE,
    "rating"     SMALLINT CHECK ("rating" BETWEEN 1 AND 5),
    "content"    TEXT,
    "image_urls" VARCHAR(255)[],
    "is_deleted" BOOLEAN            DEFAULT FALSE,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_review_product" IS '商品评价表';
COMMENT ON COLUMN "t_review_product".id IS '商品评价主键ID';
COMMENT ON COLUMN "t_review_product".review_id IS '关联的评价主表ID';
COMMENT ON COLUMN "t_review_product".product_id IS '评价的商品ID';
COMMENT ON COLUMN "t_review_product".rating IS '评分(0-5)';
COMMENT ON COLUMN "t_review_product".content IS '评价内容';
COMMENT ON COLUMN "t_review_product".image_urls IS '评价图片列表';

-- 评价回复表
DROP TABLE IF EXISTS "t_review_reply";
CREATE TABLE "t_review_reply"
(
    "id"         BIGSERIAL PRIMARY KEY,
    "review_id"  BIGINT    NOT NULL,
    "content"    TEXT,
    "is_deleted" BOOLEAN            DEFAULT FALSE,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_review_reply" IS '评价回复表';
COMMENT ON COLUMN "t_review_reply".id IS '评价回复主键ID';
COMMENT ON COLUMN "t_review_reply".review_id IS '关联的评价ID';
COMMENT ON COLUMN "t_review_reply".content IS '回复内容';

-- 系统公告表
DROP TABLE IF EXISTS "t_announcement";
CREATE TABLE "t_announcement"
(
    "id"           BIGSERIAL PRIMARY KEY,
    "title"        VARCHAR(100) NOT NULL,
    "content"      TEXT         NOT NULL,
    "publisher_id" BIGINT       NOT NULL,
    "status"       SMALLINT              DEFAULT 1 CHECK ("status" IN (0, 1)),
    "is_deleted"   BOOLEAN               DEFAULT FALSE,
    "created_at"   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at"   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "t_announcement" IS '系统公告表';
COMMENT ON COLUMN "t_announcement".id IS '公告主键ID';
COMMENT ON COLUMN "t_announcement".title IS '公告标题';
COMMENT ON COLUMN "t_announcement".content IS '公告内容';
COMMENT ON COLUMN "t_announcement".publisher_id IS '公告发布者ID';
COMMENT ON COLUMN "t_announcement".status is '公告状态(0-关闭 1-正常)';
COMMENT ON COLUMN "t_announcement".is_deleted IS '逻辑删除';
