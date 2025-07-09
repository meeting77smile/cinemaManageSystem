# 影院管理系统

一个基于Spring Boot和Vue.js的影院管理系统，实现了电影院的影片管理、影厅管理、座位管理和票务管理等功能。

## 功能特点

- **电影管理**：管理正在上映和即将下架的电影信息
- **影院管理**：管理影院信息，包括影厅和座位
- **座位管理**：显示每个影厅的座位数和空座数
- **选座功能**：支持自动选座和手动选座两种模式
- **用户管理**：用户注册、登录和个人信息管理
- **票务管理**：购票、支付和订单管理

## 技术栈

### 后端

- **Spring Boot**：提供RESTful API
- **Spring Security**：用户认证和授权
- **MyBatis Plus**：ORM框架，简化数据库操作
- **MySQL**：数据库
- **Knife4j**：API文档和调试工具

### 前端

- **HTML/CSS/JavaScript**：前端基础技术
- **Vue.js**：前端框架（通过CDN引入，无需构建）
- **Fetch API**：处理HTTP请求

## 项目结构

```
CinemaManageSystem/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── cinema/
│   │   │           ├── common/        # 通用类
│   │   │           ├── config/        # 配置类
│   │   │           ├── controller/    # 控制器
│   │   │           ├── entity/        # 实体类
│   │   │           ├── mapper/        # MyBatis映射接口
│   │   │           ├── service/       # 服务接口
│   │   │           │   └── impl/      # 服务实现
│   │   │           └── util/          # 工具类
│   │   └── resources/
│   │       ├── static/                # 前端静态资源
│   │       │   ├── login.html         # 登录/注册页面
│   │       │   ├── index.html         # 首页
│   │       │   ├── movie-detail.html  # 电影详情页
│   │       │   ├── seat-selection.html # 选座页面
│   │       │   └── ...                # 其他页面
│   │       ├── application.yml        # 应用配置
│   │       └── cinema_system.sql      # 数据库脚本
│   └── test/                          # 测试代码
└── pom.xml                            # Maven配置
```

## 数据库设计

系统包含以下主要实体：

- **Cinema**：影院信息
- **Movie**：电影信息
- **Hall**：影厅信息
- **Seat**：座位信息
- **Showtime**：放映场次
- **User**：用户信息
- **Ticket**：票务信息

## API接口

系统提供了RESTful API，主要包括：

- `/api/movies`：电影相关接口
- `/api/cinemas`：影院相关接口
- `/api/halls`：影厅相关接口
- `/api/seats`：座位相关接口
- `/api/showtimes`：场次相关接口
- `/api/users`：用户相关接口
- `/api/tickets`：票务相关接口

详细API文档可通过Knife4j查看：`http://localhost:8080/doc.html`

## 功能展示

### 用户功能

1. **浏览电影**：用户可以在首页浏览正在上映和即将下架的电影
2. **查看电影详情**：点击电影卡片可以查看电影详情和放映场次
3. **选座购票**：
   - 手动选座：用户可以在座位图上手动选择座位
   - 自动选座：系统会自动为用户选择最佳座位
4. **查看订单**：用户可以在个人中心查看订单历史

### 管理员功能

1. **电影管理**：添加、编辑、删除电影，管理电影状态
2. **影院管理**：管理影院信息
3. **影厅管理**：管理影厅和座位布局
4. **场次管理**：安排电影放映场次
5. **用户管理**：管理用户账号
6. **票务管理**：查看票务统计和详情

## 安装和运行

### 环境要求

- JDK 11+
- Maven 3.6+
- MySQL 8.0+

### 步骤

1. 克隆项目到本地
   ```
   git clone https://github.com/yourusername/CinemaManageSystem.git
   ```

2. 创建数据库并导入SQL脚本
   ```
   mysql -u root -p
   CREATE DATABASE cinema_system;
   USE cinema_system;
   SOURCE src/main/resources/cinema_system.sql;
   ```

3. 修改配置文件
   编辑 `src/main/resources/application.yml`，配置数据库连接信息

4. 编译和运行
   ```
   mvn clean package
   java -jar target/cinema-manage-system.jar
   ```

5. 访问系统
   打开浏览器，访问 `http://localhost:8080`

## 许可证

本项目采用 MIT 许可证
