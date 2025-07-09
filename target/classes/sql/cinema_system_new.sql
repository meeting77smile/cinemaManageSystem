/*
 Navicat Premium Data Transfer

 Source Server         : cinemaManagement@localhost
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : cinema_system

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 09/07/2025 15:11:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cinema
-- ----------------------------
DROP TABLE IF EXISTS `cinema`;
CREATE TABLE `cinema`  (
  `cinema_id` int NOT NULL AUTO_INCREMENT COMMENT '影院ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '影院名称',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '影院地址',
  `contact` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系电话',
  `total_halls` int NOT NULL DEFAULT 0 COMMENT '总厅数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`cinema_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '影院信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cinema
-- ----------------------------
INSERT INTO `cinema` VALUES (1, '星光影院', '北京市朝阳区星光大道1号', '010-12345678', 5, '2025-07-04 16:45:45', '2025-07-04 16:45:45', 0);
INSERT INTO `cinema` VALUES (2, '环球影城', '上海市浦东新区环球路88号', '021-87654321', 8, '2025-07-04 16:45:45', '2025-07-04 16:45:45', 0);
INSERT INTO `cinema` VALUES (3, 'a影院', '辽宁省大连市', '010-23456789', 2, '2025-07-07 12:24:28', '2025-07-08 23:45:36', 1);

-- ----------------------------
-- Table structure for cinema_review
-- ----------------------------
DROP TABLE IF EXISTS `cinema_review`;
CREATE TABLE `cinema_review`  (
  `review_id` int NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `cinema_id` int NOT NULL COMMENT '影院ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `rating` int NOT NULL COMMENT '评分(1-5)',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '评价内容',
  `review_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`review_id`) USING BTREE,
  INDEX `cinema_id`(`cinema_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `cinema_review_ibfk_1` FOREIGN KEY (`cinema_id`) REFERENCES `cinema` (`cinema_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cinema_review_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '影院评价表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cinema_review
-- ----------------------------
INSERT INTO `cinema_review` VALUES (1, 1, 4, 5, '不错不错', '2025-07-07 01:45:04', '2025-07-07 09:45:04', '2025-07-07 09:45:04', 0);
INSERT INTO `cinema_review` VALUES (2, 1, 8, 4, '好！！！', '2025-07-07 01:45:56', '2025-07-07 09:45:56', '2025-07-07 09:45:56', 0);

-- ----------------------------
-- Table structure for hall
-- ----------------------------
DROP TABLE IF EXISTS `hall`;
CREATE TABLE `hall`  (
  `hall_id` int NOT NULL AUTO_INCREMENT COMMENT '影厅ID',
  `cinema_id` int NOT NULL COMMENT '所属影院ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '影厅名称',
  `capacity` int NOT NULL COMMENT '座位容量',
  `available_seats` int NOT NULL COMMENT '可用座位数',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '影厅类型',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`hall_id`) USING BTREE,
  INDEX `cinema_id`(`cinema_id` ASC) USING BTREE,
  CONSTRAINT `hall_ibfk_1` FOREIGN KEY (`cinema_id`) REFERENCES `cinema` (`cinema_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '影厅信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hall
-- ----------------------------
INSERT INTO `hall` VALUES (1, 1, '1号厅', 120, 120, 'IMAX', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `hall` VALUES (2, 1, '2号厅', 80, 80, '3D', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `hall` VALUES (3, 2, '1号厅', 150, 150, 'IMAX', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `hall` VALUES (4, 2, '2号厅', 100, 100, '3D', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `hall` VALUES (5, 2, '3号厅', 90, 90, '2D', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `hall` VALUES (6, 3, '1号厅', 10, 10, 'VIP', '2025-07-07 14:43:16', '2025-07-07 14:43:16', 0);
INSERT INTO `hall` VALUES (7, 3, '2号厅', 66, 66, 'IMAX', '2025-07-07 14:43:49', '2025-07-07 14:43:49', 0);
INSERT INTO `hall` VALUES (8, 1, '1号厅', 50, 50, 'IMAX', '2025-07-08 23:44:16', '2025-07-09 11:48:48', 1);
INSERT INTO `hall` VALUES (9, 3, '2号厅', 100, 100, '4D', '2025-07-08 23:45:03', '2025-07-08 23:45:03', 0);
INSERT INTO `hall` VALUES (10, 1, '3', 11, 11, '4D', '2025-07-09 11:49:13', '2025-07-09 11:49:17', 1);
INSERT INTO `hall` VALUES (11, 1, '4', 11, 11, 'IMAX', '2025-07-09 11:58:10', '2025-07-09 11:58:14', 1);
INSERT INTO `hall` VALUES (12, 1, '3', 1, 1, '4D', '2025-07-09 12:01:50', '2025-07-09 12:01:55', 1);
INSERT INTO `hall` VALUES (13, 1, '3号厅', 9, 9, '4D', '2025-07-09 14:24:34', '2025-07-09 14:24:34', 0);

-- ----------------------------
-- Table structure for movie
-- ----------------------------
DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie`  (
  `movie_id` int NOT NULL AUTO_INCREMENT COMMENT '电影ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电影标题',
  `director` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '导演',
  `actors` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '演员',
  `duration` int NOT NULL COMMENT '时长(分钟)',
  `release_date` date NOT NULL COMMENT '上映日期',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电影描述',
  `poster_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '海报URL',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'SHOWING' COMMENT '状态(SHOWING:上映中, ENDING:即将下架, ENDED:已下架)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`movie_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '电影信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of movie
-- ----------------------------
INSERT INTO `movie` VALUES (1, '流浪地球2', '郭帆', '吴京,刘德华,李雪健', 173, '2023-01-22', '太阳即将毁灭，人类在地球表面建造出巨大的推进器，寻找新家园。然而宇宙之路危机四伏，为了拯救地球，流浪地球时代的年轻人再次挺身而出，展开争分夺秒的生死之战。', 'https://cdn.sputniknews.cn/img/07e7/01/1b/1047408966_0:0:2048:2867_1920x0_80_0_0_0283a927c86527d33ad95832efda032a.jpg', 'SHOWING', '2025-07-04 16:45:46', '2025-07-09 14:23:45', 0);
INSERT INTO `movie` VALUES (2, '满江红', '张艺谋', '沈腾,易烊千玺,张译', 159, '2023-01-22', '南宋绍兴年间，岳飞死后四年，秦桧率领的朝廷腐败透顶，以致民不聊生。一个名为\"满江红\"的秘密组织崛起，志在推翻秦桧，恢复大宋荣光。', 'https://ts2.tc.mm.bing.net/th/id/OIP-C.thMqsVTX_jVZX0RvUkGD6wHaKY?rs=1&pid=ImgDetMain&o=7&rm=3', 'SHOWING', '2025-07-04 16:45:46', '2025-07-05 17:35:43', 0);
INSERT INTO `movie` VALUES (3, '独行月球', '张吃鱼', '沈腾,马丽', 122, '2022-07-29', '人类为抵御小行星的撞击，拯救地球，在月球部署了月盾计划。陨石提前来袭，全员紧急撤离时，维修工独孤月被领队意外丢在了月球。不料月盾计划失败，独孤月成为了\"宇宙最后的人类\"，开始了他在月球上破罐子破摔的生活…', 'https://n.sinaimg.cn/front20220729ac/56/w690h966/20220729/2fdf-faa716e0b229625db478f1a3bdb219fb.jpg', 'ENDING', '2025-07-04 16:45:46', '2025-07-09 14:24:02', 0);
INSERT INTO `movie` VALUES (4, '哪吒2', '饺子', '哪吒，敖丙', 120, '2025-07-10', '《哪吒2》讲述了哪吒和敖丙在天劫之后的故事，他们为了拯救自己的肉身，面临重重困难和挑战，并与四海龙王展开了一场惊心动魄的战斗。 影片中，太乙真人决定使用七色宝莲为二人重塑肉身，但过程并不顺利。 同时，申公豹放出了被囚禁的四龙王，导致东海龙王敖光等人形首次曝光，并表示要让陈塘关鸡犬不留。 为了守护陈塘关，哪吒与四海龙王展开了激战。', 'https://tse4-mm.cn.bing.net/th/id/OIP-C.3M1LqdA6W1RWM6r1dIBHmQHaKX?w=179&h=250&c=7&r=0&o=7&dpr=1.9&pid=1.7&rm=3', 'ENDED', '2025-07-07 14:15:09', '2025-07-09 14:23:58', 0);
INSERT INTO `movie` VALUES (8, '1', '11', '1', 1, '2025-07-08', '', '1', 'SHOWING', '2025-07-08 13:51:49', '2025-07-08 13:52:08', 1);
INSERT INTO `movie` VALUES (9, '1', '1', '1', 1, '2025-07-08', '', '', 'SHOWING', '2025-07-08 14:03:13', '2025-07-08 14:14:57', 1);
INSERT INTO `movie` VALUES (10, '2', '2', '2', 2, '2025-07-08', '', '', 'SHOWING', '2025-07-08 14:17:25', '2025-07-08 23:45:17', 1);

-- ----------------------------
-- Table structure for seat
-- ----------------------------
DROP TABLE IF EXISTS `seat`;
CREATE TABLE `seat`  (
  `seat_id` int NOT NULL AUTO_INCREMENT COMMENT '座位ID',
  `hall_id` int NOT NULL COMMENT '所属影厅ID',
  `seat_row` int NOT NULL COMMENT '排号',
  `seat_number` int NOT NULL COMMENT '座位号',
  `is_available` tinyint(1) NULL DEFAULT 1 COMMENT '是否可用',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'NORMAL' COMMENT '座位类型',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`seat_id`) USING BTREE,
  INDEX `hall_id`(`hall_id` ASC) USING BTREE,
  CONSTRAINT `seat_ibfk_1` FOREIGN KEY (`hall_id`) REFERENCES `hall` (`hall_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '座位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of seat
-- ----------------------------
INSERT INTO `seat` VALUES (1, 1, 1, 1, 1, 'NORMAL', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `seat` VALUES (2, 1, 1, 2, 1, 'NORMAL', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `seat` VALUES (3, 1, 1, 3, 1, 'NORMAL', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `seat` VALUES (4, 1, 2, 1, 1, 'NORMAL', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `seat` VALUES (5, 1, 2, 2, 1, 'NORMAL', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `seat` VALUES (6, 1, 2, 3, 1, 'NORMAL', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `seat` VALUES (7, 1, 3, 1, 1, 'NORMAL', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `seat` VALUES (8, 1, 3, 2, 1, 'NORMAL', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `seat` VALUES (9, 1, 3, 3, 1, 'NORMAL', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);

-- ----------------------------
-- Table structure for showtime
-- ----------------------------
DROP TABLE IF EXISTS `showtime`;
CREATE TABLE `showtime`  (
  `showtime_id` int NOT NULL AUTO_INCREMENT COMMENT '场次ID',
  `movie_id` int NOT NULL COMMENT '电影ID',
  `hall_id` int NOT NULL COMMENT '影厅ID',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `ticket_price` decimal(10, 2) NOT NULL COMMENT '票价',
  `available_tickets` int NOT NULL COMMENT '可用票数',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'NORMAL' COMMENT '状态',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`showtime_id`) USING BTREE,
  INDEX `movie_id`(`movie_id` ASC) USING BTREE,
  INDEX `hall_id`(`hall_id` ASC) USING BTREE,
  CONSTRAINT `showtime_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `showtime_ibfk_2` FOREIGN KEY (`hall_id`) REFERENCES `hall` (`hall_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '放映场次表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of showtime
-- ----------------------------
INSERT INTO `showtime` VALUES (1, 1, 1, '2023-07-05 10:00:00', '2023-07-05 13:00:00', 50.00, 120, 'NORMAL', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `showtime` VALUES (2, 2, 2, '2023-07-05 14:00:00', '2023-07-05 16:40:00', 45.00, 80, 'NORMAL', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `showtime` VALUES (3, 3, 3, '2023-07-05 19:00:00', '2023-07-05 21:10:00', 40.00, 150, 'NORMAL', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `showtime` VALUES (4, 2, 1, '2025-07-08 04:12:00', '2025-07-08 15:05:00', 39.00, 120, 'NORMAL', '2025-07-07 12:12:44', '2025-07-08 10:16:08', 0);
INSERT INTO `showtime` VALUES (5, 4, 7, '2025-07-10 14:44:00', '2025-07-10 16:44:00', 99.00, 66, 'NORMAL', '2025-07-07 14:44:21', '2025-07-09 12:06:21', 1);
INSERT INTO `showtime` VALUES (6, 9, 1, '2025-07-09 14:04:00', '2025-07-09 14:05:00', 20.00, 119, 'NORMAL', '2025-07-08 14:04:22', '2025-07-08 22:29:40', 1);
INSERT INTO `showtime` VALUES (7, 10, 1, '2025-07-09 14:17:00', '2025-07-09 14:19:00', 111.00, 117, 'NORMAL', '2025-07-08 14:17:40', '2025-07-09 12:07:15', 1);
INSERT INTO `showtime` VALUES (8, 1, 1, '2025-07-11 07:08:00', '2025-07-08 18:01:00', 111.00, 120, 'NORMAL', '2025-07-08 15:08:37', '2025-07-09 14:22:53', 0);
INSERT INTO `showtime` VALUES (9, 1, 1, '2025-07-10 16:31:00', '2025-07-10 19:24:00', 111.00, 120, 'NORMAL', '2025-07-08 16:31:10', '2025-07-08 22:52:23', 1);
INSERT INTO `showtime` VALUES (10, 2, 1, '2025-07-10 22:20:00', '2025-07-11 00:59:00', 123.00, 120, 'NORMAL', '2025-07-08 22:20:24', '2025-07-08 22:30:00', 1);

-- ----------------------------
-- Table structure for ticket
-- ----------------------------
DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket`  (
  `ticket_id` int NOT NULL AUTO_INCREMENT COMMENT '票ID',
  `showtime_id` int NOT NULL COMMENT '场次ID',
  `seat_id` int NOT NULL COMMENT '座位ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `purchase_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '购买时间',
  `payment_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'UNPAID' COMMENT '状态',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`ticket_id`) USING BTREE,
  INDEX `showtime_id`(`showtime_id` ASC) USING BTREE,
  INDEX `seat_id`(`seat_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `ticket_ibfk_1` FOREIGN KEY (`showtime_id`) REFERENCES `showtime` (`showtime_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ticket_ibfk_2` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`seat_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ticket_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '票务信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ticket
-- ----------------------------
INSERT INTO `ticket` VALUES (1, 1, 1, 2, 50.00, '2023-07-04 15:30:00', 'ONLINE', 'PAID', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `ticket` VALUES (2, 1, 2, 2, 50.00, '2023-07-04 15:30:00', 'ONLINE', 'PAID', '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0);
INSERT INTO `ticket` VALUES (4, 6, 1, 4, 20.00, '2025-07-08 14:05:04', NULL, 'CANCELED', '2025-07-08 14:05:04', '2025-07-08 14:05:04', 0);
INSERT INTO `ticket` VALUES (5, 6, 9, 4, 20.00, '2025-07-08 14:06:14', NULL, 'UNPAID', '2025-07-08 14:06:14', '2025-07-08 14:06:14', 0);
INSERT INTO `ticket` VALUES (6, 7, 9, 7, 111.00, '2025-07-08 15:01:09', NULL, 'UNPAID', '2025-07-08 15:01:09', '2025-07-09 12:07:15', 1);
INSERT INTO `ticket` VALUES (7, 7, 1, 7, 111.00, '2025-07-08 15:01:52', NULL, 'UNPAID', '2025-07-08 15:01:52', '2025-07-09 12:07:15', 1);
INSERT INTO `ticket` VALUES (8, 7, 7, 7, 111.00, '2025-07-08 15:07:33', NULL, 'UNPAID', '2025-07-08 15:07:33', '2025-07-09 12:07:15', 1);
INSERT INTO `ticket` VALUES (9, 7, 2, 4, 111.00, '2025-07-08 16:30:22', 'BALANCE', 'CANCELED', '2025-07-08 16:30:22', '2025-07-09 12:07:15', 1);
INSERT INTO `ticket` VALUES (10, 9, 9, 4, 111.00, '2025-07-08 16:31:21', 'BALANCE', 'CANCELED', '2025-07-08 16:31:21', '2025-07-08 16:31:21', 0);
INSERT INTO `ticket` VALUES (11, 9, 1, 4, 111.00, '2025-07-08 16:31:30', 'BALANCE', 'CANCELED', '2025-07-08 16:31:30', '2025-07-08 16:31:30', 0);
INSERT INTO `ticket` VALUES (12, 7, 6, 4, 111.00, '2025-07-08 18:08:39', 'BALANCE', 'CANCELED', '2025-07-08 18:08:39', '2025-07-09 12:07:15', 1);
INSERT INTO `ticket` VALUES (13, 9, 7, 4, 111.00, '2025-07-08 18:09:03', 'BALANCE', 'CANCELED', '2025-07-08 18:09:03', '2025-07-08 18:09:03', 0);
INSERT INTO `ticket` VALUES (14, 9, 5, 4, 111.00, '2025-07-08 20:35:49', 'BALANCE', 'CANCELED', '2025-07-08 20:35:49', '2025-07-08 20:35:49', 0);
INSERT INTO `ticket` VALUES (15, 7, 8, 4, 111.00, '2025-07-08 21:18:42', 'BALANCE', 'CANCELED', '2025-07-08 21:18:42', '2025-07-09 12:07:15', 1);
INSERT INTO `ticket` VALUES (16, 7, 5, 4, 111.00, '2025-07-08 21:43:52', 'BALANCE', 'CANCELED', '2025-07-08 21:43:52', '2025-07-09 12:07:15', 1);
INSERT INTO `ticket` VALUES (17, 9, 6, 4, 111.00, '2025-07-08 22:01:54', 'BALANCE', 'CANCELED', '2025-07-08 22:01:54', '2025-07-08 22:01:54', 0);
INSERT INTO `ticket` VALUES (18, 9, 8, 4, 111.00, '2025-07-08 22:02:25', 'BALANCE', 'CANCELED', '2025-07-08 22:02:25', '2025-07-08 22:02:25', 0);
INSERT INTO `ticket` VALUES (19, 9, 3, 4, 111.00, '2025-07-08 22:08:05', 'BALANCE', 'CANCELED', '2025-07-08 22:08:05', '2025-07-08 22:08:05', 0);
INSERT INTO `ticket` VALUES (20, 9, 1, 4, 111.00, '2025-07-08 22:18:53', 'BALANCE', 'CANCELED', '2025-07-08 22:18:53', '2025-07-08 22:18:53', 0);
INSERT INTO `ticket` VALUES (21, 8, 9, 4, 111.00, '2025-07-09 12:07:46', NULL, 'CANCELED', '2025-07-09 12:07:46', '2025-07-09 12:07:46', 0);
INSERT INTO `ticket` VALUES (22, 8, 4, 4, 111.00, '2025-07-09 13:42:13', 'BALANCE', 'CANCELED', '2025-07-09 13:42:13', '2025-07-09 13:42:13', 0);
INSERT INTO `ticket` VALUES (23, 8, 2, 4, 111.00, '2025-07-09 13:42:38', 'BALANCE', 'CANCELED', '2025-07-09 13:42:38', '2025-07-09 13:42:38', 0);
INSERT INTO `ticket` VALUES (24, 8, 7, 4, 111.00, '2025-07-09 13:43:10', 'BALANCE', 'CANCELED', '2025-07-09 13:43:10', '2025-07-09 13:43:10', 0);
INSERT INTO `ticket` VALUES (25, 8, 1, 4, 111.00, '2025-07-09 14:21:40', 'BALANCE', 'CANCELED', '2025-07-09 14:21:40', '2025-07-09 14:21:40', 0);
INSERT INTO `ticket` VALUES (26, 8, 7, 4, 111.00, '2025-07-09 14:22:21', 'BALANCE', 'CANCELED', '2025-07-09 14:22:21', '2025-07-09 14:22:21', 0);
INSERT INTO `ticket` VALUES (27, 8, 3, 4, 111.00, '2025-07-09 14:22:48', NULL, 'CANCELED', '2025-07-09 14:22:48', '2025-07-09 14:22:48', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `register_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'USER' COMMENT '角色(USER/ADMIN)',
  `balance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'ACTIVE' COMMENT '用户状态(ACTIVE/INACTIVE/BANNED)',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'admin@cinema.com', '13800000000', '2025-07-04 16:45:46', 'ADMIN', 1000.00, '2025-07-04 16:45:46', '2025-07-05 19:38:03', 0, 'ACTIVE');
INSERT INTO `user` VALUES (2, 'user1', '$2a$10$8qx7UT.zBJIX5oPLY4XczOWgZTWGNMWj/NE4TJgGTnLd0gv9QgDXW', 'user1@example.com', '13900000001', '2025-07-04 16:45:46', 'USER', 200.00, '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0, 'ACTIVE');
INSERT INTO `user` VALUES (3, 'user2', '$2a$10$8qx7UT.zBJIX5oPLY4XczOWgZTWGNMWj/NE4TJgGTnLd0gv9QgDXW', 'user2@example.com', '13900000002', '2025-07-04 16:45:46', 'USER', 300.00, '2025-07-04 16:45:46', '2025-07-04 16:45:46', 0, 'ACTIVE');
INSERT INTO `user` VALUES (4, 'test01', 'e10adc3949ba59abbe56e057f20f883e', '2054865827@qq.com', '18896360521', '2025-07-04 17:46:54', 'USER', 2200.00, '2025-07-04 17:46:54', '2025-07-09 14:26:08', 0, 'ACTIVE');
INSERT INTO `user` VALUES (7, 'admin01', 'e10adc3949ba59abbe56e057f20f883e', '', '', '2025-07-05 11:20:32', 'ADMIN', 0.00, '2025-07-05 11:20:32', '2025-07-05 18:28:15', 0, 'ACTIVE');
INSERT INTO `user` VALUES (8, 'test02', 'e10adc3949ba59abbe56e057f20f883e', '123@qq.com', '18396360520', '2025-07-07 09:45:34', 'USER', 0.00, '2025-07-07 09:45:34', '2025-07-07 09:48:17', 0, 'BANNED');

SET FOREIGN_KEY_CHECKS = 1;
