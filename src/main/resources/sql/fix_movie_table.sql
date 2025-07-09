-- 修复电影表actors字段的默认值问题
USE cinema_system;

-- 修改actors字段，添加默认值
ALTER TABLE movie MODIFY COLUMN actors VARCHAR(255) NOT NULL DEFAULT '' COMMENT '演员';

-- 更新现有数据，将NULL值设置为空字符串
UPDATE movie SET actors = '' WHERE actors IS NULL;

-- 验证修复结果
SELECT movie_id, title, actors FROM movie WHERE actors IS NULL; 