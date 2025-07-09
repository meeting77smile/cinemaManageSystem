## 7. 票务管理

### 7.1 分页查询票务列表

#### 接口描述

分页查询票务列表，可根据用户ID、场次ID和状态进行筛选

#### 请求

- 方法：`GET`
- URL：`/api/tickets/page`
- 参数：
  - `current`：当前页码，默认为1
  - `size`：每页大小，默认为10
  - `userId`：用户ID，可选
  - `showtimeId`：场次ID，可选
  - `status`：状态，可选

#### 响应

- 成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "ticketId": 1,
        "showtimeId": 1,
        "seatId": 1,
        "userId": 2,
        "price": 50.00,
        "purchaseTime": "2023-07-04T15:30:00",
        "paymentMethod": "ONLINE",
        "status": "PAID"
      },
      {
        "ticketId": 2,
        "showtimeId": 1,
        "seatId": 2,
        "userId": 2,
        "price": 50.00,
        "purchaseTime": "2023-07-04T15:30:00",
        "paymentMethod": "ONLINE",
        "status": "PAID"
      }
    ],
    "total": 2,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

#### 测试用例

**请求**：
```bash
curl -X GET "http://localhost:8080/api/tickets/page?current=1&size=10&userId=2" \
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
        "ticketId": 1,
        "showtimeId": 1,
        "seatId": 1,
        "userId": 2,
        "price": 50.00,
        "purchaseTime": "2023-07-04T15:30:00",
        "paymentMethod": "ONLINE",
        "status": "PAID"
      },
      {
        "ticketId": 2,
        "showtimeId": 1,
        "seatId": 2,
        "userId": 2,
        "price": 50.00,
        "purchaseTime": "2023-07-04T15:30:00",
        "paymentMethod": "ONLINE",
        "status": "PAID"
      }
    ],
    "total": 2,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### 7.2 获取票务详情

#### 接口描述

获取指定票务的详细信息

#### 请求

- 方法：`GET`
- URL：`/api/tickets/{id}`
- 路径参数：
  - `id`：票ID

#### 响应

- 成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "ticketId": 1,
    "showtimeId": 1,
    "seatId": 1,
    "userId": 2,
    "price": 50.00,
    "purchaseTime": "2023-07-04T15:30:00",
    "paymentMethod": "ONLINE",
    "status": "PAID"
  }
}
```

- 失败响应（票务不存在）：

```json
{
  "code": 501,
  "message": "票务不存在",
  "data": null
}
```

#### 测试用例

**请求**：
```bash
curl -X GET "http://localhost:8080/api/tickets/1" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInVzZXJJZCI6MiwicGhvbmUiOiIxMzkwMDAwMDAwMSIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNjI1NDg2NDAwfQ.8Yw2M5MdzUVdq6D6X7X8X6X8X6X8X6X8"
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "ticketId": 1,
    "showtimeId": 1,
    "seatId": 1,
    "userId": 2,
    "price": 50.00,
    "purchaseTime": "2023-07-04T15:30:00",
    "paymentMethod": "ONLINE",
    "status": "PAID"
  }
}
```

### 7.3 手动选座购票

#### 接口描述

手动选择座位购票

#### 请求

- 方法：`POST`
- URL：`/api/tickets/manual`
- 参数：
  - `userId`：用户ID
  - `showtimeId`：场次ID
- 请求体：座位ID列表

```json
[1, 2, 3]
```

#### 响应

- 成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "ticketId": 4,
      "showtimeId": 1,
      "seatId": 1,
      "userId": 2,
      "price": 50.00,
      "purchaseTime": "2023-07-04T18:30:00",
      "paymentMethod": null,
      "status": "UNPAID"
    },
    {
      "ticketId": 5,
      "showtimeId": 1,
      "seatId": 2,
      "userId": 2,
      "price": 50.00,
      "purchaseTime": "2023-07-04T18:30:00",
      "paymentMethod": null,
      "status": "UNPAID"
    },
    {
      "ticketId": 6,
      "showtimeId": 1,
      "seatId": 3,
      "userId": 2,
      "price": 50.00,
      "purchaseTime": "2023-07-04T18:30:00",
      "paymentMethod": null,
      "status": "UNPAID"
    }
  ]
}
```

- 失败响应（座位已被占用）：

```json
{
  "code": 501,
  "message": "座位已被占用",
  "data": null
}
```

#### 测试用例

**请求**：
```bash
curl -X POST "http://localhost:8080/api/tickets/manual?userId=2&showtimeId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInVzZXJJZCI6MiwicGhvbmUiOiIxMzkwMDAwMDAwMSIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNjI1NDg2NDAwfQ.8Yw2M5MdzUVdq6D6X7X8X6X8X6X8X6X8" \
  -d '[3, 4, 5]'
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "ticketId": 4,
      "showtimeId": 1,
      "seatId": 3,
      "userId": 2,
      "price": 50.00,
      "purchaseTime": "2023-07-04T18:30:00",
      "paymentMethod": null,
      "status": "UNPAID"
    },
    {
      "ticketId": 5,
      "showtimeId": 1,
      "seatId": 4,
      "userId": 2,
      "price": 50.00,
      "purchaseTime": "2023-07-04T18:30:00",
      "paymentMethod": null,
      "status": "UNPAID"
    },
    {
      "ticketId": 6,
      "showtimeId": 1,
      "seatId": 5,
      "userId": 2,
      "price": 50.00,
      "purchaseTime": "2023-07-04T18:30:00",
      "paymentMethod": null,
      "status": "UNPAID"
    }
  ]
}
```

### 7.4 自动选座购票

#### 接口描述

自动选择座位购票

#### 请求

- 方法：`POST`
- URL：`/api/tickets/auto`
- 参数：
  - `userId`：用户ID
  - `showtimeId`：场次ID
  - `count`：票数

#### 响应

- 成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "ticketId": 7,
      "showtimeId": 1,
      "seatId": 6,
      "userId": 2,
      "price": 50.00,
      "purchaseTime": "2023-07-04T18:35:00",
      "paymentMethod": null,
      "status": "UNPAID"
    },
    {
      "ticketId": 8,
      "showtimeId": 1,
      "seatId": 7,
      "userId": 2,
      "price": 50.00,
      "purchaseTime": "2023-07-04T18:35:00",
      "paymentMethod": null,
      "status": "UNPAID"
    }
  ]
}
```

- 失败响应（座位不足）：

```json
{
  "code": 501,
  "message": "可用座位不足",
  "data": null
}
```

#### 测试用例

**请求**：
```bash
curl -X POST "http://localhost:8080/api/tickets/auto?userId=2&showtimeId=1&count=2" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInVzZXJJZCI6MiwicGhvbmUiOiIxMzkwMDAwMDAwMSIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNjI1NDg2NDAwfQ.8Yw2M5MdzUVdq6D6X7X8X6X8X6X8X6X8"
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "ticketId": 7,
      "showtimeId": 1,
      "seatId": 6,
      "userId": 2,
      "price": 50.00,
      "purchaseTime": "2023-07-04T18:35:00",
      "paymentMethod": null,
      "status": "UNPAID"
    },
    {
      "ticketId": 8,
      "showtimeId": 1,
      "seatId": 7,
      "userId": 2,
      "price": 50.00,
      "purchaseTime": "2023-07-04T18:35:00",
      "paymentMethod": null,
      "status": "UNPAID"
    }
  ]
}
```

### 7.5 支付票款

#### 接口描述

支付票款

#### 请求

- 方法：`POST`
- URL：`/api/tickets/{id}/pay`
- 路径参数：
  - `id`：票ID
- 参数：
  - `paymentMethod`：支付方式

#### 响应

- 成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

- 失败响应（票务不存在）：

```json
{
  "code": 501,
  "message": "票务不存在",
  "data": null
}
```

#### 测试用例

**请求**：
```bash
curl -X POST "http://localhost:8080/api/tickets/4/pay?paymentMethod=ONLINE" \
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

### 7.6 批量支付票款

#### 接口描述

批量支付票款

#### 请求

- 方法：`POST`
- URL：`/api/tickets/batch-pay`
- 参数：
  - `paymentMethod`：支付方式
- 请求体：票ID列表

```json
[4, 5, 6]
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

- 失败响应（票务不存在）：

```json
{
  "code": 501,
  "message": "部分票务不存在",
  "data": null
}
```

#### 测试用例

**请求**：
```bash
curl -X POST "http://localhost:8080/api/tickets/batch-pay?paymentMethod=ONLINE" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInVzZXJJZCI6MiwicGhvbmUiOiIxMzkwMDAwMDAwMSIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNjI1NDg2NDAwfQ.8Yw2M5MdzUVdq6D6X7X8X6X8X6X8X6X8" \
  -d '[4, 5, 6]'
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

### 7.7 取消票务

#### 接口描述

取消票务

#### 请求

- 方法：`POST`
- URL：`/api/tickets/{id}/cancel`
- 路径参数：
  - `id`：票ID

#### 响应

- 成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

- 失败响应（票务不存在）：

```json
{
  "code": 501,
  "message": "票务不存在",
  "data": null
}
```

#### 测试用例

**请求**：
```bash
curl -X POST "http://localhost:8080/api/tickets/4/cancel" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInVzZXJJZCI6MiwicGhvbmUiOiIxMzkwMDAwMDAwMSIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNjI1NDg2NDAwfQ.8Yw2M5MdzUVdq6D6X7X8X6X8X6X8X6X8"
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
