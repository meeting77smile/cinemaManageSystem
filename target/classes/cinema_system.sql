-- 创建数据库
CREATE DATABASE IF NOT EXISTS cinema_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE cinema_system;

-- 影院表
CREATE TABLE IF NOT EXISTS cinema (
    cinema_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '影院ID',
    name VARCHAR(100) NOT NULL COMMENT '影院名称',
    address VARCHAR(255) NOT NULL COMMENT '影院地址',
    contact VARCHAR(50) NOT NULL COMMENT '联系电话',
    total_halls INT NOT NULL DEFAULT 0 COMMENT '总厅数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
) COMMENT='影院信息表';

-- 电影表
CREATE TABLE IF NOT EXISTS movie (
    movie_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '电影ID',
    title VARCHAR(100) NOT NULL COMMENT '电影标题',
    director VARCHAR(100) NOT NULL COMMENT '导演',
    actors VARCHAR(255) NOT NULL DEFAULT '' COMMENT '演员',
    duration INT NOT NULL COMMENT '时长(分钟)',
    release_date DATE NOT NULL COMMENT '上映日期',
    description TEXT NOT NULL COMMENT '电影描述',
    poster_url VARCHAR(255) COMMENT '海报URL',
    status VARCHAR(20) DEFAULT 'SHOWING' COMMENT '状态(SHOWING:上映中, ENDING:即将下架, ENDED:已下架)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
) COMMENT='电影信息表';

-- 影厅表
CREATE TABLE IF NOT EXISTS hall (
    hall_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '影厅ID',
    cinema_id INT NOT NULL COMMENT '所属影院ID',
    name VARCHAR(50) NOT NULL COMMENT '影厅名称',
    capacity INT NOT NULL COMMENT '座位容量',
    available_seats INT NOT NULL COMMENT '可用座位数',
    type VARCHAR(50) NOT NULL COMMENT '影厅类型',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    FOREIGN KEY (cinema_id) REFERENCES cinema(cinema_id)
) COMMENT='影厅信息表';

-- 座位表
CREATE TABLE IF NOT EXISTS seat (
    seat_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '座位ID',
    hall_id INT NOT NULL COMMENT '所属影厅ID',
    seat_row INT NOT NULL COMMENT '排号',
    seat_number INT NOT NULL COMMENT '座位号',
    is_available TINYINT(1) DEFAULT 1 COMMENT '是否可用',
    type VARCHAR(50) DEFAULT 'NORMAL' COMMENT '座位类型',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    FOREIGN KEY (hall_id) REFERENCES hall(hall_id)
) COMMENT='座位信息表';

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    user_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '电话',
    register_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    role VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色(USER/ADMIN)',
    balance DECIMAL(10,2) DEFAULT 0.00 COMMENT '账户余额',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '用户状态(ACTIVE/INACTIVE/BANNED)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
) COMMENT='用户信息表';

-- 放映场次表
CREATE TABLE IF NOT EXISTS showtime (
    showtime_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '场次ID',
    movie_id INT NOT NULL COMMENT '电影ID',
    hall_id INT NOT NULL COMMENT '影厅ID',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    ticket_price DECIMAL(10,2) NOT NULL COMMENT '票价',
    available_tickets INT NOT NULL COMMENT '可用票数',
    status VARCHAR(20) NOT NULL DEFAULT 'NORMAL' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    FOREIGN KEY (movie_id) REFERENCES movie(movie_id),
    FOREIGN KEY (hall_id) REFERENCES hall(hall_id)
) COMMENT='放映场次表';

-- 票务表
CREATE TABLE IF NOT EXISTS ticket (
    ticket_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '票ID',
    showtime_id INT NOT NULL COMMENT '场次ID',
    seat_id INT NOT NULL COMMENT '座位ID',
    user_id INT NOT NULL COMMENT '用户ID',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    purchase_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '购买时间',
    payment_method VARCHAR(50) COMMENT '支付方式',
    status VARCHAR(20) NOT NULL DEFAULT 'UNPAID' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    FOREIGN KEY (showtime_id) REFERENCES showtime(showtime_id),
    FOREIGN KEY (seat_id) REFERENCES seat(seat_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
) COMMENT='票务信息表';

-- 插入测试数据
-- 影院
INSERT INTO cinema (name, address, contact, total_halls) VALUES 
('星光影院', '北京市朝阳区星光大道1号', '010-12345678', 5),
('环球影城', '上海市浦东新区环球路88号', '021-87654321', 8);

-- 电影
INSERT INTO movie (title, director, actors, duration, release_date, description, poster_url, status) VALUES 
('流浪地球2', '郭帆', '吴京,刘德华,李雪健', 173, '2023-01-22', '太阳即将毁灭，人类在地球表面建造出巨大的推进器，寻找新家园。然而宇宙之路危机四伏，为了拯救地球，流浪地球时代的年轻人再次挺身而出，展开争分夺秒的生死之战。', 'https://example.com/poster1.jpg', 'SHOWING'),
('满江红', '张艺谋', '沈腾,易烊千玺,张译', 159, '2023-01-22', '南宋绍兴年间，岳飞死后四年，秦桧率领的朝廷腐败透顶，以致民不聊生。一个名为"满江红"的秘密组织崛起，志在推翻秦桧，恢复大宋荣光。', 'https://example.com/poster2.jpg', 'SHOWING'),
('独行月球', '张吃鱼', '沈腾,马丽', 122, '2022-07-29', '人类为抵御小行星的撞击，拯救地球，在月球部署了月盾计划。陨石提前来袭，全员紧急撤离时，维修工独孤月被领队意外丢在了月球。不料月盾计划失败，独孤月成为了"宇宙最后的人类"，开始了他在月球上破罐子破摔的生活…', 'https://example.com/poster3.jpg', 'ENDING');

-- 用户
INSERT INTO user (username, password, email, phone, role, balance, status) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'admin@cinema.com', '13800000000', 'ADMIN', 1000.00, 'ACTIVE'),
('user1', '$2a$10$8qx7UT.zBJIX5oPLY4XczOWgZTWGNMWj/NE4TJgGTnLd0gv9QgDXW', 'user1@example.com', '13900000001', 'USER', 200.00, 'ACTIVE'),
('user2', '$2a$10$8qx7UT.zBJIX5oPLY4XczOWgZTWGNMWj/NE4TJgGTnLd0gv9QgDXW', 'user2@example.com', '13900000002', 'USER', 300.00, 'ACTIVE');

-- 影厅
INSERT INTO hall (cinema_id, name, capacity, available_seats, type) VALUES 
(1, '1号厅', 120, 120, 'IMAX'),
(1, '2号厅', 80, 80, '3D'),
(2, '1号厅', 150, 150, 'IMAX'),
(2, '2号厅', 100, 100, '3D'),
(2, '3号厅', 90, 90, '2D');

-- 座位 (仅插入部分示例数据)
-- 1号影院1号厅的部分座位
INSERT INTO seat (hall_id, seat_row, seat_number, is_available, type) VALUES 
(1, 1, 1, 1, 'NORMAL'), (1, 1, 2, 1, 'NORMAL'), (1, 1, 3, 1, 'NORMAL'),
(1, 2, 1, 1, 'NORMAL'), (1, 2, 2, 1, 'NORMAL'), (1, 2, 3, 1, 'NORMAL'),
(1, 3, 1, 1, 'NORMAL'), (1, 3, 2, 1, 'NORMAL'), (1, 3, 3, 1, 'NORMAL');

-- 放映场次
INSERT INTO showtime (movie_id, hall_id, start_time, end_time, ticket_price, available_tickets, status) VALUES 
(1, 1, '2023-07-05 10:00:00', '2023-07-05 13:00:00', 50.00, 120, 'NORMAL'),
(2, 2, '2023-07-05 14:00:00', '2023-07-05 16:40:00', 45.00, 80, 'NORMAL'),
(3, 3, '2023-07-05 19:00:00', '2023-07-05 21:10:00', 40.00, 150, 'NORMAL');

-- 票务 (示例数据)
INSERT INTO ticket (showtime_id, seat_id, user_id, price, purchase_time, payment_method, status) VALUES 
(1, 1, 2, 50.00, '2023-07-04 15:30:00', 'ONLINE', 'PAID'),
(1, 2, 2, 50.00, '2023-07-04 15:30:00', 'ONLINE', 'PAID'),
(2, 4, 3, 45.00, '2023-07-04 16:45:00', 'ONLINE', 'PAID');

#在用户表user中新增status用户状态字段
ALTER TABLE user ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '用户状态(ACTIVE/INACTIVE/BANNED)';
UPDATE user SET status = 'ACTIVE' WHERE status IS NULL;

-- 影院评价表
CREATE TABLE IF NOT EXISTS cinema_review (
    review_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '评价ID',
    cinema_id INT NOT NULL COMMENT '影院ID',
    user_id INT NOT NULL COMMENT '用户ID',
    rating INT NOT NULL COMMENT '评分(1-5)',
    content TEXT COMMENT '评价内容',
    review_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    FOREIGN KEY (cinema_id) REFERENCES cinema(cinema_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
) COMMENT='影院评价表';

#修复添加影院时的一些bug
USE cinema_system;
ALTER TABLE cinema MODIFY COLUMN total_halls INT NOT NULL DEFAULT 0 COMMENT '总厅数';
UPDATE cinema SET total_halls = 0 WHERE total_halls IS NULL;

#修复添加电影时的一些bug
ALTER TABLE movie MODIFY COLUMN actors VARCHAR(255) NOT NULL DEFAULT '' COMMENT '演员';
UPDATE movie SET actors = '' WHERE actors IS NULL;

#修复支付时的一些bug
UPDATE user SET balance = 0.00 WHERE balance IS NULL;
ALTER TABLE user MODIFY COLUMN balance DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '账户余额';

SELECT s.showtime_id, s.movie_id, m.movie_id AS movie_exists
FROM showtime s
         LEFT JOIN movie m ON s.movie_id = m.movie_id
WHERE m.movie_id IS NULL;

-- 检查所有票务的seat_id和showtime_id对应的hall_id是否一致
SELECT t.ticket_id, t.showtime_id, t.seat_id, s.hall_id AS showtime_hall, seat.hall_id AS seat_hall
FROM ticket t
         JOIN showtime s ON t.showtime_id = s.showtime_id
         JOIN seat ON t.seat_id = seat.seat_id
WHERE s.hall_id != seat.hall_id;

DELETE FROM ticket WHERE ticket_id = 3;