# 影院管理系统API文档

具体内容请在运行后端项目之后访问（9001为后端项目运行时的端口号）：http://localhost:9001/doc.html

## 目录

- [1. 概述](#1-概述)
- [2. 接口通用说明](#2-接口通用说明)
- [3. 用户管理](#3-用户管理)
- [4. 电影管理](#4-电影管理)
- [5. 影厅和座位管理](#5-影厅和座位管理)
- [6. 放映场次管理](#6-放映场次管理)
- [7. 票务管理](#7-票务管理)
- [8. 影院评价管理](#8-影院评价管理)
- [9. 管理员接口](#9-管理员接口)

## 1. 概述

本文档详细描述了影院管理系统的API接口，包括用户管理、电影管理、影厅和座位管理、放映场次管理以及票务管理等功能。

系统主要功能：
- 用户注册、登录和个人信息管理
- 电影信息管理，包括上映中和即将下架的电影
- 影厅和座位管理
- 放映场次管理
- 票务管理，包括手动选座和自动选座购票
- 影院评价管理
- 管理员统计和用户管理

## 2. 接口通用说明

### 2.1 基础URL

所有API的基础URL为：`http://localhost:8080`

### 2.2 请求格式

- GET请求：参数通过URL查询字符串传递
- POST/PUT请求：参数通过JSON格式在请求体中传递
- DELETE请求：参数通过URL路径参数传递

### 2.3 响应格式

所有API响应均为JSON格式，遵循以下统一结构：

```json
{
  "code": 200,      // 状态码，200表示成功，其他表示失败
  "message": "操作成功", // 响应消息
  "data": {}        // 响应数据，可能是对象、数组或null
}
```

### 2.4 状态码说明

| 状态码 | 说明 |
| ----- | ---- |
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未认证 |
| 403 | 未授权 |
| 404 | 资源不存在 |
| 405 | 不允许的请求方法 |
| 415 | 不支持的媒体类型 |
| 500 | 服务器内部错误 |
| 501 | 业务异常 |

### 2.5 认证方式

除了注册和登录接口外，其他接口都需要在请求头中携带token进行认证：

```
Authorization: Bearer {token}
```

## 3. 用户管理

### 3.1 用户注册

#### 接口描述

注册新用户

#### 请求

- 方法：`POST`
- URL：`/api/users/register`
- 请求体：

```json
{
  "username": "user123",
  "password": "password123",
  "email": "user123@example.com",
  "phone": "13800138000"
}
```

#### 响应

- 成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 4,
    "username": "user123",
    "email": "user123@example.com",
    "phone": "13800138000",
    "registerTime": "2025-07-04T18:30:00",
    "role": "USER",
    "balance": 0.00
  }
}
```

- 失败响应（用户名已存在）：

```json
{
  "code": 501,
  "message": "用户名已存在",
  "data": null
}
```

#### 测试用例

**请求**：
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "testpassword",
    "email": "testuser@example.com",
    "phone": "13900000000"
  }'
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 4,
    "username": "testuser",
    "email": "testuser@example.com",
    "phone": "13900000000",
    "registerTime": "2025-07-04T18:30:00",
    "role": "USER",
    "balance": 0.00
  }
}
```

### 3.2 用户登录

#### 接口描述

用户登录并获取token

#### 请求

- 方法：`POST`
- URL：`/api/users/login`
- 参数：
  - `username`：用户名
  - `password`：密码

#### 响应

- 成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "user": {
      "userId": 2,
      "username": "user1",
      "email": "user1@example.com",
      "phone": "13900000001",
      "registerTime": "2023-07-04T15:30:00",
      "role": "USER",
      "balance": 200.00
    },
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInVzZXJJZCI6MiwicGhvbmUiOiIxMzkwMDAwMDAwMSIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNjI1NDg2NDAwfQ.8Yw2M5MdzUVdq6D6X7X8X6X8X6X8X6X8"
  }
}
```

- 失败响应（用户名或密码错误）：

```json
{
  "code": 401,
  "message": "用户名或密码错误",
  "data": null
}
```

#### 测试用例

**请求**：
```bash
curl -X POST "http://localhost:8080/api/users/login?username=user1&password=password"
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "user": {
      "userId": 2,
      "username": "user1",
      "email": "user1@example.com",
      "phone": "13900000001",
      "registerTime": "2023-07-04T15:30:00",
      "role": "USER",
      "balance": 200.00
    },
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInVzZXJJZCI6MiwicGhvbmUiOiIxMzkwMDAwMDAwMSIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNjI1NDg2NDAwfQ.8Yw2M5MdzUVdq6D6X7X8X6X8X6X8X6X8"
  }
}
```

### 3.3 获取用户信息

#### 接口描述

获取指定用户的详细信息

#### 请求

- 方法：`GET`
- URL：`/api/users/info`
- 参数：
  - `userId`：用户ID

#### 响应

- 成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 2,
    "username": "user1",
    "email": "user1@example.com",
    "phone": "13900000001",
    "registerTime": "2023-07-04T15:30:00",
    "role": "USER",
    "balance": 200.00
  }
}
```

- 失败响应（用户不存在）：

```json
{
  "code": 501,
  "message": "用户不存在",
  "data": null
}
```

#### 测试用例

**请求**：
```bash
curl -X GET "http://localhost:8080/api/users/info?userId=2" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInVzZXJJZCI6MiwicGhvbmUiOiIxMzkwMDAwMDAwMSIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNjI1NDg2NDAwfQ.8Yw2M5MdzUVdq6D6X7X8X6X8X6X8X6X8"
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 2,
    "username": "user1",
    "email": "user1@example.com",
    "phone": "13900000001",
    "registerTime": "2023-07-04T15:30:00",
    "role": "USER",
    "balance": 200.00
  }
}
```

### 3.4 更新用户信息

#### 接口描述

更新用户信息

#### 请求

- 方法：`PUT`
- URL：`/api/users/info`
- 请求体：

```json
{
  "userId": 2,
  "username": "user1_updated",
  "email": "user1_updated@example.com",
  "phone": "13900000001"
}
```

#### 响应

- 成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

- 失败响应（用户不存在）：

```json
{
  "code": 501,
  "message": "用户不存在",
  "data": null
}
```

#### 测试用例

**请求**：
```bash
curl -X PUT http://localhost:8080/api/users/info \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInVzZXJJZCI6MiwicGhvbmUiOiIxMzkwMDAwMDAwMSIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNjI1NDg2NDAwfQ.8Yw2M5MdzUVdq6D6X7X8X6X8X6X8X6X8" \
  -d '{
    "userId": 2,
    "username": "user1_updated",
    "email": "user1_updated@example.com",
    "phone": "13900000001"
  }'
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

### 3.5 账户充值

#### 接口描述

为用户账户充值

#### 请求

- 方法：`POST`
- URL：`/api/users/recharge`
- 参数：
  - `userId`：用户ID
  - `amount`：充值金额

#### 响应

- 成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

- 失败响应（充值金额必须大于0）：

```json
{
  "code": 500,
  "message": "充值金额必须大于0",
  "data": null
}
```

#### 测试用例

**请求**：
```bash
curl -X POST "http://localhost:8080/api/users/recharge?userId=2&amount=100" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInVzZXJJZCI6MiwicGhvbmUiOiIxMzkwMDAwMDAwMSIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNjI1NDg2NDAwfQ.8Yw2M5MdzUVdq6D6X7X8X6X8X6X8X6X8"
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

## 4. 电影管理

### 4.1 分页查询电影列表

#### 接口描述

分页查询电影列表，可根据电影标题进行筛选

#### 请求

- 方法：`GET`
- URL：`/api/movies/page`
- 参数：
  - `current`：当前页码，默认为1
  - `size`：每页大小，默认为10
  - `title`：电影标题，可选

#### 响应

- 成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "movieId": 1,
        "title": "流浪地球2",
        "director": "郭帆",
        "actors": "吴京,刘德华,李雪健",
        "duration": 173,
        "releaseDate": "2023-01-22",
        "description": "太阳即将毁灭，人类在地球表面建造出巨大的推进器，寻找新家园。然而宇宙之路危机四伏，为了拯救地球，流浪地球时代的年轻人再次挺身而出，展开争分夺秒的生死之战。",
        "posterUrl": "https://example.com/poster1.jpg",
        "status": "SHOWING"
      },
      {
        "movieId": 2,
        "title": "满江红",
        "director": "张艺谋",
        "actors": "沈腾,易烊千玺,张译",
        "duration": 159,
        "releaseDate": "2023-01-22",
        "description": "南宋绍兴年间，岳飞死后四年，秦桧率领的朝廷腐败透顶，以致民不聊生。一个名为"满江红"的秘密组织崛起，志在推翻秦桧，恢复大宋荣光。",
        "posterUrl": "https://example.com/poster2.jpg",
        "status": "SHOWING"
      }
    ],
    "total": 3,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

#### 测试用例

**请求**：
```bash
curl -X GET "http://localhost:8080/api/movies/page?current=1&size=10" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInVzZXJJZCI6MiwicGhvbmUiOiIxMzkwMDAwMDAwMSIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNjI1NDg2NDAwfQ.8Yw2M5MdzUVdq6D6X7X8X6X8X6X8X6X8"
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "movieId": 1,
        "title": "流浪地球2",
        "director": "郭帆",
        "actors": "吴京,刘德华,李雪健",
        "duration": 173,
        "releaseDate": "2023-01-22",
        "description": "太阳即将毁灭，人类在地球表面建造出巨大的推进器，寻找新家园。然而宇宙之路危机四伏，为了拯救地球，流浪地球时代的年轻人再次挺身而出，展开争分夺秒的生死之战。",
        "posterUrl": "https://example.com/poster1.jpg",
        "status": "SHOWING"
      },
      {
        "movieId": 2,
        "title": "满江红",
        "director": "张艺谋",
        "actors": "沈腾,易烊千玺,张译",
        "duration": 159,
        "releaseDate": "2023-01-22",
        "description": "南宋绍兴年间，岳飞死后四年，秦桧率领的朝廷腐败透顶，以致民不聊生。一个名为"满江红"的秘密组织崛起，志在推翻秦桧，恢复大宋荣光。",
        "posterUrl": "https://example.com/poster2.jpg",
        "status": "SHOWING"
      },
      {
        "movieId": 3,
        "title": "独行月球",
        "director": "张吃鱼",
        "actors": "沈腾,马丽",
        "duration": 122,
        "releaseDate": "2022-07-29",
        "description": "人类为抵御小行星的撞击，拯救地球，在月球部署了月盾计划。陨石提前来袭，全员紧急撤离时，维修工独孤月被领队意外丢在了月球。不料月盾计划失败，独孤月成为了"宇宙最后的人类"，开始了他在月球上破罐子破摔的生活…",
        "posterUrl": "https://example.com/poster3.jpg",
        "status": "ENDING"
      }
    ],
    "total": 3,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### 4.2 获取电影详情

#### 接口描述

获取指定电影的详细信息

#### 请求

- 方法：`GET`
- URL：`/api/movies/{id}`
- 路径参数：
  - `id`：电影ID

#### 响应

- 成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "movieId": 1,
    "title": "流浪地球2",
    "director": "郭帆",
    "actors": "吴京,刘德华,李雪健",
    "duration": 173,
    "releaseDate": "2023-01-22",
    "description": "太阳即将毁灭，人类在地球表面建造出巨大的推进器，寻找新家园。然而宇宙之路危机四伏，为了拯救地球，流浪地球时代的年轻人再次挺身而出，展开争分夺秒的生死之战。",
    "posterUrl": "https://example.com/poster1.jpg",
    "status": "SHOWING"
  }
}
```

- 失败响应（电影不存在）：

```json
{
  "code": 501,
  "message": "电影不存在",
  "data": null
}
```

#### 测试用例

**请求**：
```bash
curl -X GET "http://localhost:8080/api/movies/1" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInVzZXJJZCI6MiwicGhvbmUiOiIxMzkwMDAwMDAwMSIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNjI1NDg2NDAwfQ.8Yw2M5MdzUVdq6D6X7X8X6X8X6X8X6X8"
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "movieId": 1,
    "title": "流浪地球2",
    "director": "郭帆",
    "actors": "吴京,刘德华,李雪健",
    "duration": 173,
    "releaseDate": "2023-01-22",
    "description": "太阳即将毁灭，人类在地球表面建造出巨大的推进器，寻找新家园。然而宇宙之路危机四伏，为了拯救地球，流浪地球时代的年轻人再次挺身而出，展开争分夺秒的生死之战。",
    "posterUrl": "https://example.com/poster1.jpg",
    "status": "SHOWING"
  }
}
```

### 4.3 获取正在上映的电影

#### 接口描述

获取所有正在上映的电影

#### 请求

- 方法：`GET`
- URL：`/api/movies/showing`

#### 响应

- 成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "movieId": 1,
      "title": "流浪地球2",
      "director": "郭帆",
      "actors": "吴京,刘德华,李雪健",
      "duration": 173,
      "releaseDate": "2023-01-22",
      "description": "太阳即将毁灭，人类在地球表面建造出巨大的推进器，寻找新家园。然而宇宙之路危机四伏，为了拯救地球，流浪地球时代的年轻人再次挺身而出，展开争分夺秒的生死之战。",
      "posterUrl": "https://example.com/poster1.jpg",
      "status": "SHOWING"
    },
    {
      "movieId": 2,
      "title": "满江红",
      "director": "张艺谋",
      "actors": "沈腾,易烊千玺,张译",
      "duration": 159,
      "releaseDate": "2023-01-22",
      "description": "南宋绍兴年间，岳飞死后四年，秦桧率领的朝廷腐败透顶，以致民不聊生。一个名为"满江红"的秘密组织崛起，志在推翻秦桧，恢复大宋荣光。",
      "posterUrl": "https://example.com/poster2.jpg",
      "status": "SHOWING"
    }
  ]
}
```

#### 测试用例

**请求**：
```bash
curl -X GET "http://localhost:8080/api/movies/showing" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInVzZXJJZCI6MiwicGhvbmUiOiIxMzkwMDAwMDAwMSIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNjI1NDg2NDAwfQ.8Yw2M5MdzUVdq6D6X7X8X6X8X6X8X6X8"
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "movieId": 1,
      "title": "流浪地球2",
      "director": "郭帆",
      "actors": "吴京,刘德华,李雪健",
      "duration": 173,
      "releaseDate": "2023-01-22",
      "description": "太阳即将毁灭，人类在地球表面建造出巨大的推进器，寻找新家园。然而宇宙之路危机四伏，为了拯救地球，流浪地球时代的年轻人再次挺身而出，展开争分夺秒的生死之战。",
      "posterUrl": "https://example.com/poster1.jpg",
      "status": "SHOWING"
    },
    {
      "movieId": 2,
      "title": "满江红",
      "director": "张艺谋",
      "actors": "沈腾,易烊千玺,张译",
      "duration": 159,
      "releaseDate": "2023-01-22",
      "description": "南宋绍兴年间，岳飞死后四年，秦桧率领的朝廷腐败透顶，以致民不聊生。一个名为"满江红"的秘密组织崛起，志在推翻秦桧，恢复大宋荣光。",
      "posterUrl": "https://example.com/poster2.jpg",
      "status": "SHOWING"
    }
  ]
}
```

### 4.4 获取即将下架的电影

#### 接口描述

获取所有即将下架的电影

#### 请求

- 方法：`GET`
- URL：`/api/movies/ending`

#### 响应

- 成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "movieId": 3,
      "title": "独行月球",
      "director": "张吃鱼",
      "actors": "沈腾,马丽",
      "duration": 122,
      "releaseDate": "2022-07-29",
      "description": "人类为抵御小行星的撞击，拯救地球，在月球部署了月盾计划。陨石提前来袭，全员紧急撤离时，维修工独孤月被领队意外丢在了月球。不料月盾计划失败，独孤月成为了"宇宙最后的人类"，开始了他在月球上破罐子破摔的生活…",
      "posterUrl": "https://example.com/poster3.jpg",
      "status": "ENDING"
    }
  ]
}
```

#### 测试用例

**请求**：
```bash
curl -X GET "http://localhost:8080/api/movies/ending" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInVzZXJJZCI6MiwicGhvbmUiOiIxMzkwMDAwMDAwMSIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNjI1NDg2NDAwfQ.8Yw2M5MdzUVdq6D6X7X8X6X8X6X8X6X8"
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "movieId": 3,
      "title": "独行月球",
      "director": "张吃鱼",
      "actors": "沈腾,马丽",
      "duration": 122,
      "releaseDate": "2022-07-29",
      "description": "人类为抵御小行星的撞击，拯救地球，在月球部署了月盾计划。陨石提前来袭，全员紧急撤离时，维修工独孤月被领队意外丢在了月球。不料月盾计划失败，独孤月成为了"宇宙最后的人类"，开始了他在月球上破罐子破摔的生活…",
      "posterUrl": "https://example.com/poster3.jpg",
      "status": "ENDING"
    }
  ]
}
```

## 5. 影厅和座位管理

### 5.1 获取座位详情

#### 接口描述

获取指定座位的详细信息

#### 请求

- 方法：`GET`
- URL：`/api/seats/{id}`
- 路径参数：
  - `id`：座位ID

#### 响应

- 成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "seatId": 1,
    "hallId": 1,
    "seatRow": 1,
    "seatNumber": 1,
    "type": "NORMAL"
  }
}
```

- 失败响应（座位不存在）：

```json
{
  "code": 501,
  "message": "座位不存在"
}
```
