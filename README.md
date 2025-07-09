# 影院管理系统

一个基于 Spring Boot 的影院管理系统，实现了影院、影厅、电影、座位、排片、购票、评价等全流程管理，支持用户与管理员两类角色。

## 主要功能

- **用户端功能**
  - 用户注册、登录、注销
  - 个人信息管理
  - 浏览电影、影院、场次
  - 选座购票、订单管理
  - 影院评价与反馈

- **管理员端功能**
  - 管理员登录、注销
  - 影院、影厅、电影、排片、座位、用户、订单、评价等全方位管理
  - 支持多条件查询、统计分析

## 技术架构

- **后端**：Spring Boot、MyBatis-Plus、MySQL、Knife4j
- **前端**：HTML/CSS/JavaScript（原生）、Vue.js（可选）
- **安全**：JWT 认证、全局异常处理
- **分层结构**：Controller（控制器）- Service（业务逻辑）- Mapper（数据访问）- Entity（实体类）- Util/Common（工具与通用类）

## 代码设计亮点

- **分层清晰**：控制器、服务、数据访问、实体、工具类分工明确，易于维护和扩展。
- **接口与实现分离**：Service 层全部采用接口+实现类模式，便于后期扩展和单元测试。
- **统一返回结构**：所有接口返回 `Result<T>`，前后端交互格式统一。
- **全局异常处理**：`GlobalExceptionHandler` 统一捕获业务和系统异常，提升健壮性。
- **JWT 认证**：`JwtUtil` 工具类实现用户登录后的身份认证与权限校验。
- **泛型与集合**：大量使用 `List<T>`、`Map<K,V>` 等泛型集合，提升代码复用性和类型安全。
- **座位选择算法**：`SeatSelectionUtil` 支持复杂选座逻辑扩展。
- **角色权限控制**：通过 `User.role` 字段实现用户/管理员权限隔离。

## 关键代码示例

- 统一返回结果
  ```java
  public class Result<T> { ... }
  ```
- 全局异常处理
  ```java
  @RestControllerAdvice
  public class GlobalExceptionHandler { ... }
  ```
- JWT 工具类
  ```java
  public class JwtUtil { ... }
  ```
- 座位选择算法
  ```java
  public class SeatSelectionUtil { ... }
  ```
- 分页与条件查询
  ```java
  public Page<Movie> getMoviesByCondition(...) { ... }
  ```

## 项目结构

```
CinemaManageSystem/
├── src/
│   ├── main/
│   │   ├── java/com/cinema/
│   │   │   ├── common/      # 通用类与异常
│   │   │   ├── config/      # 配置
│   │   │   ├── controller/  # 控制器
│   │   │   ├── entity/      # 实体类
│   │   │   ├── mapper/      # MyBatis映射
│   │   │   ├── service/     # 业务接口与实现
│   │   │   └── util/        # 工具类
│   │   └── resources/
│   │       ├── static/      # 前端页面
│   │       ├── application.yml
│   │       └── sql/         # 数据库脚本
└── pom.xml
```

## 数据库设计

- **Cinema**：影院
- **Movie**：电影
- **Hall**：影厅
- **Seat**：座位
- **Showtime**：场次
- **User**：用户
- **Ticket**：票务

## API接口

详细API文档见 Knife4j：`http://localhost:9001/doc.html`（本地运行时）或者`http://118.178.229.132:9001/doc.html`（远程访问API文档）

| 模块     | 方法   | 路径                                      | 说明           |
|----------|--------|-------------------------------------------|----------------|
| 用户     | POST   | /api/users/register                       | 用户注册       |
| 用户     | POST   | /api/users/login                          | 用户登录       |
| 用户     | GET    | /api/users/info                           | 获取用户信息   |
| 用户     | PUT    | /api/users/info                           | 更新用户信息   |
| 用户     | POST   | /api/users/recharge                       | 账户充值       |
| 用户     | GET    | /api/users/page                           | 分页查询用户   |
| 用户     | DELETE | /api/users/{id}                           | 删除用户       |
| 用户     | GET    | /api/users/{userId}/tickets               | 获取用户订单   |
| 用户     | GET    | /api/users/{userId}/history               | 获取观影历史   |
| 用户     | PUT    | /api/users/password                       | 修改密码       |
| 票务     | GET    | /api/tickets/page                         | 分页查询票务   |
| 票务     | GET    | /api/tickets/{id}                         | 获取票务详情   |
| 票务     | GET    | /api/tickets/user/{userId}                | 获取用户票务   |
| 票务     | GET    | /api/tickets/showtime/{showtimeId}        | 获取场次票务   |
| 票务     | POST   | /api/tickets/manual                       | 手动选座购票   |
| 票务     | POST   | /api/tickets/auto                         | 自动选座购票   |
| 票务     | POST   | /api/tickets/{id}/pay                     | 支付票款       |
| 票务     | POST   | /api/tickets/batch-pay                    | 批量支付票款   |
| 票务     | PUT    | /api/tickets/{id}/cancel                  | 取消票务       |
| 票务     | GET    | /api/tickets/statistics/today-count       | 今日订单数     |
| 票务     | GET    | /api/tickets/statistics/today-sales       | 今日销售额     |
| 场次     | GET    | /api/showtimes/page                       | 分页查询场次   |
| 场次     | GET    | /api/showtimes/{id}                       | 获取场次详情   |
| 场次     | GET    | /api/showtimes/movie/{movieId}            | 获取电影场次   |
| 场次     | GET    | /api/showtimes/hall/{hallId}              | 获取影厅场次   |
| 场次     | GET    | /api/showtimes/{id}/seats                  | 获取场次可用座位 |
| 场次     | POST   | /api/showtimes                            | 新增场次       |
| 场次     | PUT    | /api/showtimes                            | 更新场次       |
| 场次     | DELETE | /api/showtimes/{id}                       | 删除场次       |
| 场次     | POST   | /api/showtimes/{showtimeId}/seats/lock    | 锁定选中座位   |
| 场次     | POST   | /api/showtimes/{showtimeId}/seats/auto-select | 自动选座购票 |
| 座位     | GET    | /api/seats/{id}                           | 获取座位详情   |
| 座位     | GET    | /api/seats/hall/{hallId}                  | 获取影厅座位   |
| 座位     | GET    | /api/seats/showtime/{showtimeId}          | 获取场次可用座位 |
| 座位     | POST   | /api/seats                                | 新增座位       |
| 座位     | PUT    | /api/seats                                | 更新座位       |
| 座位     | DELETE | /api/seats/{id}                           | 删除座位       |
| 座位     | GET    | /api/seats/auto-select                    | 自动选座       |
| 电影     | GET    | /api/movies                               | 获取所有电影   |
| 电影     | GET    | /api/movies/page                          | 分页查询电影   |
| 电影     | GET    | /api/movies/{id}                          | 获取电影详情   |
| 电影     | GET    | /api/movies/showing                       | 正在上映电影   |
| 电影     | GET    | /api/movies/ending                        | 即将下架电影   |
| 电影     | GET    | /api/movies/{id}/showtimes                | 获取电影场次   |
| 电影     | POST   | /api/movies                               | 新增电影       |
| 电影     | PUT    | /api/movies                               | 更新电影       |
| 电影     | DELETE | /api/movies/{id}                          | 删除电影       |
| 影厅     | GET    | /api/halls/page                           | 分页查询影厅   |
| 影厅     | GET    | /api/halls/{id}                           | 获取影厅详情   |
| 影厅     | GET    | /api/halls/{id}/seats                     | 获取影厅座位   |
| 影厅     | GET    | /api/halls/{id}/showtimes                 | 获取影厅场次   |
| 影厅     | POST   | /api/halls                                | 新增影厅       |
| 影厅     | PUT    | /api/halls                                | 更新影厅       |
| 影厅     | DELETE | /api/halls/{id}                           | 删除影厅       |
| 影厅     | POST   | /api/halls/{id}/seats                     | 批量创建座位   |
| 影院     | GET    | /api/cinemas                              | 获取所有影院   |
| 影院     | GET    | /api/cinemas/page                         | 分页查询影院   |
| 影院     | GET    | /api/cinemas/{id}                         | 获取影院详情   |
| 影院     | GET    | /api/cinemas/{id}/halls                   | 获取影院影厅   |
| 影院     | POST   | /api/cinemas                              | 新增影院       |
| 影院     | PUT    | /api/cinemas                              | 更新影院       |
| 影院     | DELETE | /api/cinemas/{id}                         | 删除影院       |
| 影院     | GET    | /api/cinemas/{cinemaId}/reviews           | 获取影院评价   |
| 影院     | POST   | /api/cinemas/{cinemaId}/reviews           | 提交影院评价   |
| 管理员   | GET    | /api/admin/statistics                     | 获取系统统计   |
| 管理员   | GET    | /api/admin/users                          | 获取所有用户   |
| 管理员   | PUT    | /api/admin/users/{userId}/status          | 修改用户状态   |

## 安装和运行

1. 克隆项目
   ```bash
   git clone https://github.com/meeting77smile/cinemaManageSystem.git
   ```
2. 创建数据库并导入SQL
   ```sql
   CREATE DATABASE cinema_system;
   USE cinema_system;
   SOURCE src/main/resources/sql/cinema_system.sql;
   ```
3. 配置数据库连接（`application.yml`）
4. 编译并运行
   ```bash
   mvn clean package
   java -jar target/cinema-manage-system-0.0.1-SNAPSHOT.jar
   ```
5. 访问系统：`http://118.178.229.132:9002/frontEnd/login.html`

## 许可证

MIT License

---
如需详细功能说明、操作指南、功能框图和代码设计，请参见根目录下相关文档。
