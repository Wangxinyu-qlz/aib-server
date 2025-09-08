-- 测试数据
USE aib_server;

INSERT INTO t_system_config (config_key, config_value, config_type, description, is_system, created_by)
VALUES
('site_name', 'AI Image Bed', 'string', '网站名称', 1, 1),
('max_upload_size', '10485760', 'number', '最大上传大小（字节）', 1, 1),
('enable_register', 'true', 'boolean', '是否允许注册', 0, 1);

-- 插入测试用户数据
INSERT INTO t_user (username, password, email, nickname, avatar, status, create_time, update_time, deleted) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'admin@example.com', '管理员', 'https://example.com/avatar/admin.jpg', 1, NOW(), NOW(), 0),
('testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'test@example.com', '测试用户', 'https://example.com/avatar/test.jpg', 1, NOW(), NOW(), 0),
('demo', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'demo@example.com', '演示用户', 'https://example.com/avatar/demo.jpg', 1, NOW(), NOW(), 0);
-- 注意：上述密码都是 'password123' 的BCrypt加密结果
-- 用于测试的密码规则：至少6位，包含英文字母和数字
