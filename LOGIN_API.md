# 登录功能API文档

## 概述

本系统实现了完整的用户注册和登录功能，包括以下特性：

- 用户名唯一性验证
- 密码强度验证（长度大于6位，至少包含英文字母和数字）
- JWT令牌认证
- 用户状态管理

## API接口

### 1. 用户注册

**接口地址：** `POST /api/auth/register`

**请求参数：**
```json
{
    "username": "testuser",
    "password": "password123",
    "confirmPassword": "password123",
    "email": "test@example.com",
    "nickname": "测试用户"
}
```

**参数说明：**
- `username`: 用户名，3-20个字符，不能重复
- `password`: 密码，6-20个字符，必须包含英文字母和数字
- `confirmPassword`: 确认密码，必须与password一致
- `email`: 邮箱，可选，格式必须正确
- `nickname`: 昵称，可选，最大50个字符

**响应示例：**
```json
{
    "code": 200,
    "message": "注册成功",
    "data": "注册成功",
    "timestamp": 1704067200000
}
```

### 2. 用户登录

**接口地址：** `POST /api/auth/login`

**请求参数：**
```json
{
    "username": "testuser",
    "password": "password123"
}
```

**响应示例：**
```json
{
    "code": 200,
    "message": "登录成功",
    "data": {
        "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
        "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
        "tokenType": "Bearer",
        "expiresIn": 86400,
        "userInfo": {
            "id": 1,
            "username": "testuser",
            "email": "test@example.com",
            "nickname": "测试用户",
            "avatar": null
        }
    },
    "timestamp": 1704067200000
}
```

### 3. 检查用户名是否可用

**接口地址：** `GET /api/auth/check-username?username=testuser`

**响应示例：**
```json
{
    "code": 200,
    "message": "操作成功",
    "data": true,
    "timestamp": 1704067200000
}
```

### 4. 检查邮箱是否可用

**接口地址：** `GET /api/auth/check-email?email=test@example.com`

**响应示例：**
```json
{
    "code": 200,
    "message": "操作成功",
    "data": true,
    "timestamp": 1704067200000
}
```

## 密码规则

密码必须满足以下条件：
1. 长度大于6位
2. 至少包含一个英文字母
3. 至少包含一个数字
4. 可以包含特殊字符：@$!%*#?&

**有效密码示例：**
- `password123`
- `abc123`
- `MyPass123`
- `test@123`

**无效密码示例：**
- `123456` (缺少英文字母)
- `password` (缺少数字)
- `123` (长度不足)

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 认证失败 |
| 500 | 服务器内部错误 |

## 测试数据

系统预置了以下测试用户：

| 用户名 | 密码 | 邮箱 | 状态 |
|--------|------|------|------|
| admin | password123 | admin@example.com | 启用 |
| testuser | password123 | test@example.com | 启用 |
| demo | password123 | demo@example.com | 启用 |

## 使用示例

### 使用curl测试注册接口

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "password": "password123",
    "confirmPassword": "password123",
    "email": "newuser@example.com",
    "nickname": "新用户"
  }'
```

### 使用curl测试登录接口

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123"
  }'
```

## 安全特性

1. **密码加密**：使用BCrypt算法对密码进行加密存储
2. **JWT认证**：使用JWT令牌进行用户认证
3. **输入验证**：对所有输入参数进行严格验证
4. **SQL注入防护**：使用MyBatis Plus防止SQL注入
5. **逻辑删除**：支持用户数据的逻辑删除

## 数据库表结构

用户表（user）包含以下字段：
- `id`: 主键ID
- `username`: 用户名（唯一）
- `password`: 加密后的密码
- `email`: 邮箱（唯一）
- `nickname`: 昵称
- `avatar`: 头像URL
- `status`: 用户状态（0-禁用，1-启用）
- `create_time`: 创建时间
- `update_time`: 更新时间
- `deleted`: 逻辑删除标记
