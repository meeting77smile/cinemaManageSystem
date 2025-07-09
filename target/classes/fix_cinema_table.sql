-- 修复cinema表的total_halls字段，添加默认值
USE cinema_system;

-- 修改total_halls字段，添加默认值
ALTER TABLE cinema MODIFY COLUMN total_halls INT NOT NULL DEFAULT 0 COMMENT '总厅数';

-- 为现有数据设置默认值（如果为NULL）
UPDATE cinema SET total_halls = 0 WHERE total_halls IS NULL; 