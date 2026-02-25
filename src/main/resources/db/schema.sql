-- 洗衣店平台数据库初始化脚本

CREATE DATABASE IF NOT EXISTS washshop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE washshop;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    avatar VARCHAR(255) COMMENT '头像',
    gender TINYINT DEFAULT 0 COMMENT '性别：0-未知 1-男 2-女',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    last_login_time DATETIME COMMENT '最后登录时间',
    user_type TINYINT DEFAULT 0 COMMENT '用户类型：0-普通用户 1-管理员',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT,
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    version INT DEFAULT 1,
    INDEX idx_username (username),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 门店表
CREATE TABLE IF NOT EXISTS store (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    store_name VARCHAR(100) NOT NULL COMMENT '门店名称',
    store_code VARCHAR(50) NOT NULL UNIQUE COMMENT '门店编码',
    address VARCHAR(255) NOT NULL COMMENT '地址',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    contact_person VARCHAR(50) COMMENT '联系人',
    business_start_time TIME COMMENT '营业开始时间',
    business_end_time TIME COMMENT '营业结束时间',
    longitude DECIMAL(10, 7) COMMENT '经度',
    latitude DECIMAL(10, 7) COMMENT '纬度',
    description TEXT COMMENT '门店描述',
    images VARCHAR(500) COMMENT '门店图片',
    status TINYINT DEFAULT 1 COMMENT '状态：0-关闭 1-营业',
    sort_order INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT,
    deleted TINYINT DEFAULT 0,
    version INT DEFAULT 1,
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门店表';

-- 服务分类表
CREATE TABLE IF NOT EXISTS service_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL COMMENT '分类名称',
    category_code VARCHAR(50) NOT NULL UNIQUE COMMENT '分类编码',
    description VARCHAR(255) COMMENT '描述',
    icon VARCHAR(255) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT,
    deleted TINYINT DEFAULT 0,
    version INT DEFAULT 1,
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务分类表';

-- 洗衣服务表
CREATE TABLE IF NOT EXISTS wash_service (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_id BIGINT NOT NULL COMMENT '分类ID',
    service_name VARCHAR(100) NOT NULL COMMENT '服务名称',
    service_code VARCHAR(50) NOT NULL UNIQUE COMMENT '服务编码',
    description TEXT COMMENT '服务描述',
    price DECIMAL(10, 2) NOT NULL COMMENT '价格',
    unit VARCHAR(20) DEFAULT '件' COMMENT '单位',
    estimated_duration INT COMMENT '预计完成时长（分钟）',
    images VARCHAR(500) COMMENT '服务图片',
    notes TEXT COMMENT '注意事项',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-下架 1-上架',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT,
    deleted TINYINT DEFAULT 0,
    version INT DEFAULT 1,
    INDEX idx_category (category_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='洗衣服务表';

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    store_id BIGINT NOT NULL COMMENT '门店ID',
    order_type TINYINT DEFAULT 1 COMMENT '订单类型：1-普通订单',
    delivery_type TINYINT NOT NULL COMMENT '取送方式：1-到店自取 2-上门取送',
    pickup_address VARCHAR(255) COMMENT '取件地址',
    delivery_address VARCHAR(255) COMMENT '送件地址',
    pickup_time VARCHAR(50) COMMENT '预约取件时间',
    delivery_time VARCHAR(50) COMMENT '预约送件时间',
    contact_name VARCHAR(50) COMMENT '联系人姓名',
    contact_phone VARCHAR(20) COMMENT '联系人电话',
    total_amount DECIMAL(10, 2) NOT NULL COMMENT '订单总金额',
    discount_amount DECIMAL(10, 2) DEFAULT 0 COMMENT '优惠金额',
    coupon_amount DECIMAL(10, 2) DEFAULT 0 COMMENT '优惠券抵扣金额',
    delivery_fee DECIMAL(10, 2) DEFAULT 0 COMMENT '配送费',
    actual_amount DECIMAL(10, 2) NOT NULL COMMENT '实付金额',
    pay_type TINYINT COMMENT '支付方式：1-微信支付 2-支付宝 3-余额支付',
    pay_time DATETIME COMMENT '支付时间',
    pay_no VARCHAR(100) COMMENT '支付流水号',
    order_status TINYINT DEFAULT 0 COMMENT '订单状态：0-待支付 1-待接单 2-洗护中 3-待取件 4-配送中 5-已完成 6-已取消 7-退款中 8-已退款',
    remark VARCHAR(500) COMMENT '备注',
    cancel_reason VARCHAR(255) COMMENT '取消原因',
    confirm_time DATETIME COMMENT '接单时间',
    start_wash_time DATETIME COMMENT '开始洗护时间',
    finish_wash_time DATETIME COMMENT '完成洗护时间',
    ready_time DATETIME COMMENT '待取件时间',
    deliver_time DATETIME COMMENT '配送时间',
    complete_time DATETIME COMMENT '完成时间',
    reminder_count INT DEFAULT 0 COMMENT '提醒次数',
    voucher_no VARCHAR(50) COMMENT '凭证编号',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT,
    deleted TINYINT DEFAULT 0,
    version INT DEFAULT 1,
    INDEX idx_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_store_id (store_id),
    INDEX idx_status (order_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单项表
CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    service_id BIGINT NOT NULL COMMENT '服务ID',
    service_name VARCHAR(100) NOT NULL COMMENT '服务名称',
    quantity INT NOT NULL COMMENT '数量',
    unit_price DECIMAL(10, 2) NOT NULL COMMENT '单价',
    total_price DECIMAL(10, 2) NOT NULL COMMENT '总价',
    item_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '项目状态',
    description VARCHAR(500) COMMENT '描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT,
    deleted TINYINT DEFAULT 0,
    version INT DEFAULT 1,
    INDEX idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单项表';

-- 服务记录表
CREATE TABLE IF NOT EXISTS service_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_item_id BIGINT COMMENT '订单项ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    store_id BIGINT NOT NULL COMMENT '门店ID',
    service_id BIGINT NOT NULL COMMENT '服务ID',
    service_name VARCHAR(100) COMMENT '服务名称',
    sign_in_time DATETIME COMMENT '签收时间',
    start_wash_time DATETIME COMMENT '开始洗护时间',
    finish_wash_time DATETIME COMMENT '完成洗护时间',
    deliver_time DATETIME COMMENT '交付时间',
    actual_duration INT COMMENT '实际洗护时长（分钟）',
    rating TINYINT COMMENT '评分：1-5星',
    review_content TEXT COMMENT '评价内容',
    review_images VARCHAR(500) COMMENT '评价图片',
    review_time DATETIME COMMENT '评价时间',
    voucher_no VARCHAR(50) COMMENT '凭证编号',
    voucher_content TEXT COMMENT '凭证内容',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT,
    deleted TINYINT DEFAULT 0,
    version INT DEFAULT 1,
    INDEX idx_order_id (order_id),
    INDEX idx_user_id (user_id),
    INDEX idx_voucher_no (voucher_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务记录表';

-- 会员表
CREATE TABLE IF NOT EXISTS member (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE COMMENT '用户ID',
    level TINYINT DEFAULT 0 COMMENT '会员等级：0-普通 1-银卡 2-金卡',
    points INT DEFAULT 0 COMMENT '积分',
    balance DECIMAL(10, 2) DEFAULT 0 COMMENT '余额',
    total_consumption DECIMAL(10, 2) DEFAULT 0 COMMENT '总消费金额',
    total_orders INT DEFAULT 0 COMMENT '总订单数',
    upgrade_time DATETIME COMMENT '升级时间',
    expire_time DATETIME COMMENT '过期时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT,
    deleted TINYINT DEFAULT 0,
    version INT DEFAULT 1,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员表';

-- 积分记录表
CREATE TABLE IF NOT EXISTS points_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    points INT NOT NULL COMMENT '积分变动值',
    type TINYINT NOT NULL COMMENT '类型：1-获得 2-使用',
    source VARCHAR(50) COMMENT '来源',
    source_id BIGINT COMMENT '来源ID',
    description VARCHAR(255) COMMENT '描述',
    balance INT COMMENT '变动后余额',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT,
    deleted TINYINT DEFAULT 0,
    version INT DEFAULT 1,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分记录表';

-- 优惠券表
CREATE TABLE IF NOT EXISTS coupon (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    coupon_name VARCHAR(100) NOT NULL COMMENT '优惠券名称',
    coupon_code VARCHAR(50) NOT NULL UNIQUE COMMENT '优惠券编码',
    coupon_type TINYINT NOT NULL COMMENT '类型：1-满减券 2-折扣券 3-免运费券',
    discount_amount DECIMAL(10, 2) COMMENT '优惠金额',
    discount_rate DECIMAL(3, 2) COMMENT '折扣率',
    min_amount DECIMAL(10, 2) DEFAULT 0 COMMENT '最低使用金额',
    max_discount DECIMAL(10, 2) COMMENT '最大优惠金额',
    total_quantity INT NOT NULL COMMENT '总数量',
    remaining_quantity INT NOT NULL COMMENT '剩余数量',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    limit_per_user INT DEFAULT 1 COMMENT '每人限领数量',
    applicable_services VARCHAR(500) COMMENT '适用服务',
    description VARCHAR(255) COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT,
    deleted TINYINT DEFAULT 0,
    version INT DEFAULT 1,
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';

-- 用户优惠券表
CREATE TABLE IF NOT EXISTS user_coupon (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    coupon_id BIGINT NOT NULL COMMENT '优惠券ID',
    receive_time DATETIME COMMENT '领取时间',
    use_time DATETIME COMMENT '使用时间',
    order_id BIGINT COMMENT '使用订单ID',
    status TINYINT DEFAULT 0 COMMENT '状态：0-未使用 1-已使用 2-已过期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT,
    deleted TINYINT DEFAULT 0,
    version INT DEFAULT 1,
    INDEX idx_user_id (user_id),
    INDEX idx_coupon_id (coupon_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券表';

-- 充值记录表
CREATE TABLE IF NOT EXISTS recharge_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    recharge_no VARCHAR(50) NOT NULL UNIQUE COMMENT '充值编号',
    amount DECIMAL(10, 2) NOT NULL COMMENT '充值金额',
    gift_amount DECIMAL(10, 2) DEFAULT 0 COMMENT '赠送金额',
    pay_type TINYINT COMMENT '支付方式',
    pay_no VARCHAR(100) COMMENT '支付流水号',
    pay_time DATETIME COMMENT '支付时间',
    status TINYINT DEFAULT 0 COMMENT '状态：0-待支付 1-已完成 2-已取消',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT,
    deleted TINYINT DEFAULT 0,
    version INT DEFAULT 1,
    INDEX idx_user_id (user_id),
    INDEX idx_recharge_no (recharge_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值记录表';

-- 门店库存表
CREATE TABLE IF NOT EXISTS store_inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    store_id BIGINT NOT NULL COMMENT '门店ID',
    item_name VARCHAR(100) NOT NULL COMMENT '物品名称',
    item_code VARCHAR(50) NOT NULL COMMENT '物品编码',
    item_type TINYINT NOT NULL COMMENT '类型：1-洗护耗材 2-包装物料',
    unit VARCHAR(20) COMMENT '单位',
    stock INT DEFAULT 0 COMMENT '库存数量',
    min_stock INT DEFAULT 0 COMMENT '最小库存',
    max_stock INT DEFAULT 9999 COMMENT '最大库存',
    unit_price DECIMAL(10, 2) COMMENT '单价',
    supplier VARCHAR(100) COMMENT '供应商',
    description VARCHAR(255) COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT,
    deleted TINYINT DEFAULT 0,
    version INT DEFAULT 1,
    INDEX idx_store_id (store_id),
    INDEX idx_item_type (item_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门店库存表';

-- 员工表
CREATE TABLE IF NOT EXISTS employee (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    store_id BIGINT NOT NULL COMMENT '门店ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    employee_no VARCHAR(50) NOT NULL COMMENT '员工编号',
    position VARCHAR(50) COMMENT '职位',
    department VARCHAR(50) COMMENT '部门',
    entry_date DATETIME COMMENT '入职日期',
    id_card VARCHAR(18) COMMENT '身份证号',
    emergency_contact VARCHAR(50) COMMENT '紧急联系人',
    emergency_phone VARCHAR(20) COMMENT '紧急联系电话',
    status TINYINT DEFAULT 1 COMMENT '状态：0-离职 1-在职',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT,
    deleted TINYINT DEFAULT 0,
    version INT DEFAULT 1,
    INDEX idx_store_id (store_id),
    INDEX idx_user_id (user_id),
    UNIQUE KEY uk_employee_no (employee_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- 插入初始数据
INSERT INTO sys_user (username, password, real_name, phone, email, user_type, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '管理员', '13800138000', 'admin@washshop.com', 1, 1);

INSERT INTO service_category (category_name, category_code, description, sort_order, status) VALUES
('干洗', 'DRY_CLEAN', '专业干洗服务', 1, 1),
('水洗', 'WASH', '水洗服务', 2, 1),
('熨烫', 'IRONING', '熨烫服务', 3, 1),
('洗护保养', 'CARE', '洗护保养服务', 4, 1),
('奢侈品护理', 'LUXURY', '奢侈品护理服务', 5, 1);

INSERT INTO wash_service (category_id, service_name, service_code, description, price, unit, estimated_duration, sort_order, status) VALUES
(1, '西装干洗', 'SUIT_DRY', '西装专业干洗', 50.00, '件', 1440, 1, 1),
(1, '大衣干洗', 'COAT_DRY', '大衣专业干洗', 80.00, '件', 1440, 2, 1),
(2, 'T恤水洗', 'TSHIRT_WASH', 'T恤水洗服务', 15.00, '件', 720, 1, 1),
(2, '牛仔裤水洗', 'JEANS_WASH', '牛仔裤水洗服务', 20.00, '件', 720, 2, 1),
(3, '衬衫熨烫', 'SHIRT_IRON', '衬衫熨烫服务', 10.00, '件', 60, 1, 1),
(4, '皮鞋保养', 'SHOE_CARE', '皮鞋清洗保养', 30.00, '双', 480, 1, 1),
(5, '皮包护理', 'BAG_LUXURY', '奢侈品皮包护理', 200.00, '个', 2880, 1, 1);
