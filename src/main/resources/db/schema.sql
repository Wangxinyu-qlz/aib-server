-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS aib_server CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE aib_server;

-- 删除旧表
DROP TABLE IF EXISTS t_operation_log;
DROP TABLE IF EXISTS t_search_history;
DROP TABLE IF EXISTS t_system_config;
DROP TABLE IF EXISTS t_images_tags;
DROP TABLE IF EXISTS t_tag;
DROP TABLE IF EXISTS t_images;
DROP TABLE IF EXISTS t_user;

-- 公共字段（BaseEntity）：
-- id BIGINT PK
-- created_by BIGINT
-- created_time DATETIME
-- updated_by BIGINT
-- updated_time DATETIME
-- deleted TINYINT
-- version INT
-- remark VARCHAR

-- 用户表
CREATE TABLE t_user (
    id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(64) NOT NULL COMMENT '用户名',
    password VARCHAR(128) NOT NULL COMMENT '加密后的密码',
    email VARCHAR(100) NULL DEFAULT NULL COMMENT '邮箱',
    nickname VARCHAR(64) NULL DEFAULT NULL COMMENT '用户昵称',
    avatar VARCHAR(255) NULL DEFAULT NULL COMMENT '用户头像URL',
    status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '用户状态：0-禁用，1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',

    PRIMARY KEY (id),
    UNIQUE INDEX idx_username (username),
    UNIQUE INDEX idx_email (email),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted),
    INDEX idx_create_time (create_time),
    INDEX idx_update_time (update_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 图片表
CREATE TABLE t_images (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '图片ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    file_name VARCHAR(255) NOT NULL COMMENT '保存时生成的唯一文件名',
    original_file_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
    file_path VARCHAR(500) NOT NULL COMMENT '文件路径',
    file_url VARCHAR(500) NOT NULL COMMENT '文件URL',
    content_type VARCHAR(100) NOT NULL COMMENT '文件类型',
    width INT NOT NULL COMMENT '宽度',
    height INT NOT NULL COMMENT '高度',
    file_size BIGINT NOT NULL COMMENT '文件大小(字节)',
    description TEXT COMMENT '描述',
    upload_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',

    -- 公共字段
    created_by BIGINT DEFAULT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-正常 1-已删除',
    version INT DEFAULT 0 COMMENT '乐观锁版本号',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',

    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_upload_time (upload_time),
    KEY idx_deleted (deleted),
    KEY idx_content_type (content_type),
    KEY idx_file_size (file_size),
    KEY idx_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图片表';

-- 标签表
CREATE TABLE t_tag (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '标签ID',
    tag_name VARCHAR(50) NOT NULL COMMENT '标签名称',
    tag_type VARCHAR(50) DEFAULT 'auto' COMMENT '标签类型: auto-自动, manual-手动',
    confidence_score DECIMAL(5,4) COMMENT '置信度分数(0-1)',
    usage_count INT DEFAULT 0 COMMENT '使用次数',

    -- 公共字段
    created_by BIGINT DEFAULT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-正常 1-已删除',
    version INT DEFAULT 0 COMMENT '乐观锁版本号',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',

    PRIMARY KEY (id),
    UNIQUE KEY uk_tag_name (tag_name),
    KEY idx_tag_type (tag_type),
    KEY idx_confidence_score (confidence_score),
    KEY idx_usage_count (usage_count)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- 图片标签关联表
CREATE TABLE t_images_tags (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    image_id BIGINT NOT NULL COMMENT '图片ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',

    -- 公共字段
    created_by BIGINT DEFAULT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    version INT DEFAULT 0 COMMENT '乐观锁版本号',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',

    PRIMARY KEY (id),
    UNIQUE KEY uk_image_tag (image_id, tag_id),
    KEY idx_image_id (image_id),
    KEY idx_tag_id (tag_id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图片标签关联表';

-- 搜索历史表
CREATE TABLE t_search_history (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '搜索ID',
    user_id BIGINT NULL COMMENT '用户ID(可为空，支持匿名搜索)',
    query_text VARCHAR(500) NOT NULL COMMENT '搜索关键词',
    search_type VARCHAR(50) DEFAULT 'text' COMMENT '搜索类型: text-文本, image-图片, ai-AI搜索',
    result_count INT DEFAULT 0 COMMENT '结果数量',
    search_params JSON COMMENT '搜索参数',
    ip_address VARCHAR(45) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT '用户代理',

    -- 公共字段
    created_by BIGINT DEFAULT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-正常 1-已删除',
    version INT DEFAULT 0 COMMENT '乐观锁版本号',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',

    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_query_text (query_text),
    KEY idx_search_type (search_type),
    KEY idx_created_time (created_time),
    KEY idx_ip_address (ip_address)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='搜索历史表';

-- 系统配置表
CREATE TABLE t_system_config (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    config_key VARCHAR(100) NOT NULL COMMENT '配置键',
    config_value TEXT COMMENT '配置值',
    config_type VARCHAR(50) DEFAULT 'string' COMMENT '配置类型: string, number, boolean, json',
    description VARCHAR(500) COMMENT '配置描述',
    is_system TINYINT(1) DEFAULT 0 COMMENT '是否系统配置: 0-否, 1-是',

    -- 公共字段
    created_by BIGINT DEFAULT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-正常 1-已删除',
    version INT DEFAULT 0 COMMENT '乐观锁版本号',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',

    PRIMARY KEY (id),
    UNIQUE KEY uk_config_key (config_key),
    KEY idx_config_type (config_type),
    KEY idx_is_system (is_system)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 操作日志表
CREATE TABLE t_operation_log (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT NULL COMMENT '用户ID',
    operation_type VARCHAR(50) NOT NULL COMMENT '操作类型: login, upload, download, delete, search',
    operation_desc VARCHAR(500) COMMENT '操作描述',
    request_method VARCHAR(10) COMMENT '请求方法',
    request_url VARCHAR(500) COMMENT '请求URL',
    request_params JSON COMMENT '请求参数',
    response_data JSON COMMENT '响应数据',
    ip_address VARCHAR(45) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT '用户代理',
    execution_time BIGINT COMMENT '执行时间(毫秒)',
    status TINYINT(1) DEFAULT 1 COMMENT '状态: 0-失败, 1-成功',
    error_message TEXT COMMENT '错误信息',

    -- 公共字段
    created_by BIGINT DEFAULT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-正常 1-已删除',
    version INT DEFAULT 0 COMMENT '乐观锁版本号',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',

    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_operation_type (operation_type),
    KEY idx_status (status),
    KEY idx_created_time (created_time),
    KEY idx_ip_address (ip_address)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';
