-- =============================================
-- tlias 智能学习辅助系统 - 一键部署脚本
-- 适用环境: MySQL 5.7+ / 8.0+
-- 字符集: utf8mb4
-- =============================================

-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS `tlias` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `tlias`;

-- 2. 创建部门表 (dept) - 无外键依赖，优先创建
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `name` VARCHAR(50) NOT NULL COMMENT '部门名称',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 3. 创建员工表 (emp) - 依赖 dept
DROP TABLE IF EXISTS `emp`;
CREATE TABLE `emp` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '员工ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `gender` TINYINT UNSIGNED NOT NULL COMMENT '性别: 1-男, 2-女',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `job` TINYINT UNSIGNED DEFAULT NULL COMMENT '职位: 1-班主任, 2-讲师, 3-学工主管, 4-教研主管, 5-咨询师',
  `salary` DECIMAL(10,2) DEFAULT NULL COMMENT '薪资',
  `image` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
  `entry_date` DATE DEFAULT NULL COMMENT '入职日期',
  `dept_id` INT UNSIGNED DEFAULT NULL COMMENT '部门ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_dept_id` (`dept_id`),
  CONSTRAINT `fk_emp_dept` FOREIGN KEY (`dept_id`) REFERENCES `dept`(`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- 4. 创建班级表 (clazz) - 依赖 emp (班主任)
DROP TABLE IF EXISTS `clazz`;
CREATE TABLE `clazz` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '班级ID',
  `name` VARCHAR(100) NOT NULL COMMENT '班级名称',
  `room` VARCHAR(20) DEFAULT NULL COMMENT '教室',
  `begin_date` DATE DEFAULT NULL COMMENT '开班日期',
  `end_date` DATE DEFAULT NULL COMMENT '结班日期',
  `master_id` INT UNSIGNED DEFAULT NULL COMMENT '班主任ID',
  `subject` TINYINT UNSIGNED DEFAULT NULL COMMENT '学科: 1-JavaEE, 2-前端, 3-大数据, 4-其他',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_master_id` (`master_id`),
  CONSTRAINT `fk_clazz_emp` FOREIGN KEY (`master_id`) REFERENCES `emp`(`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- 5. 创建学生表 (student) - 依赖 clazz
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '学生ID',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `no` VARCHAR(20) NOT NULL COMMENT '学号',
  `gender` TINYINT UNSIGNED NOT NULL COMMENT '性别: 1-男, 2-女',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `id_card` VARCHAR(20) DEFAULT NULL COMMENT '身份证号',
  `is_college` TINYINT UNSIGNED DEFAULT NULL COMMENT '是否大专以上: 0-否, 1-是',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '地址',
  `degree` TINYINT UNSIGNED DEFAULT NULL COMMENT '学历: 1-初中, 2-高中, 3-大专, 4-本科, 5-硕士及以上',
  `graduation_date` DATE DEFAULT NULL COMMENT '毕业日期',
  `clazz_id` INT UNSIGNED DEFAULT NULL COMMENT '班级ID',
  `violation_count` INT UNSIGNED DEFAULT 0 COMMENT '违纪次数',
  `violation_score` INT UNSIGNED DEFAULT 0 COMMENT '违纪扣分',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_no` (`no`),
  KEY `idx_clazz_id` (`clazz_id`),
  CONSTRAINT `fk_student_clazz` FOREIGN KEY (`clazz_id`) REFERENCES `clazz`(`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- 6. 创建员工经历表 (emp_expr) - 依赖 emp
DROP TABLE IF EXISTS `emp_expr`;
CREATE TABLE `emp_expr` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '经历ID',
  `emp_id` INT UNSIGNED NOT NULL COMMENT '员工ID',
  `begin` DATE DEFAULT NULL COMMENT '开始日期',
  `end` DATE DEFAULT NULL COMMENT '结束日期',
  `company` VARCHAR(100) DEFAULT NULL COMMENT '公司名称',
  `job` VARCHAR(100) DEFAULT NULL COMMENT '职位/工作内容',
  PRIMARY KEY (`id`),
  KEY `idx_emp_id` (`emp_id`),
  CONSTRAINT `fk_emp_expr_emp` FOREIGN KEY (`emp_id`) REFERENCES `emp`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工工作经历表';

-- 7. 创建操作日志表 (operate_log) - 依赖 emp
DROP TABLE IF EXISTS `operate_log`;
CREATE TABLE `operate_log` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `operate_emp_id` INT UNSIGNED DEFAULT NULL COMMENT '操作人ID',
  `operate_time` DATETIME DEFAULT NULL COMMENT '操作时间',
  `class_name` VARCHAR(200) DEFAULT NULL COMMENT '类名',
  `method_name` VARCHAR(200) DEFAULT NULL COMMENT '方法名',
  `method_params` TEXT COMMENT '方法参数',
  `return_value` TEXT COMMENT '返回值',
  `cost_time` BIGINT DEFAULT NULL COMMENT '耗时(ms)',
  PRIMARY KEY (`id`),
  KEY `idx_operate_emp_id` (`operate_emp_id`),
  CONSTRAINT `fk_log_emp` FOREIGN KEY (`operate_emp_id`) REFERENCES `emp`(`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- =============================================
-- 8. 插入数据 (严格按外键依赖顺序)
-- =============================================

-- 8.1 部门数据
INSERT INTO `dept` (`id`, `name`, `create_time`, `update_time`) VALUES
(1, '学工部', '2023-09-25 09:47:40', '2024-07-25 09:47:40'),
(2, '教研部', '2023-09-25 09:47:40', '2024-08-09 15:17:04'),
(3, '咨询部', '2023-09-25 09:47:40', '2024-07-30 21:26:24'),
(4, '就业部', '2023-09-25 09:47:40', '2024-07-25 09:47:40'),
(5, '人事部', '2023-09-25 09:47:40', '2024-07-25 09:47:40'),
(6, '行政部', '2023-11-30 20:56:37', '2024-07-30 20:56:37'),
(7, '测试1', '2026-03-26 22:12:01', '2026-03-26 22:12:01');

-- 8.2 员工数据
INSERT INTO `emp` (`id`, `username`, `password`, `name`, `gender`, `phone`, `job`, `salary`, `image`, `entry_date`, `dept_id`, `create_time`, `update_time`) VALUES
(1, 'shinaian', '123456', '施耐庵', 1, '13309090001', 4, 15000.00, 'https://fastcdn.mihoyo.com/content-v2/plat/100954/e0a9576fddc0ebb07e2533e0965e5d0d_8056648887776496971.png', '2000-01-01', 2, '2023-10-20 16:35:33', '2023-11-16 16:11:26'),
(2, 'songjiang', '123456', '宋江', 1, '13309090002', 2, 8600.00, 'https://fastcdn.mihoyo.com/content-v2/plat/100954/e0a9576fddc0ebb07e2533e0965e5d0d_8056648887776496971.png', '2015-01-01', 2, '2023-10-20 16:35:33', '2023-10-20 16:35:37'),
(3, 'lujunyi', '123456', '卢俊义', 1, '13309090003', 2, 8900.00, 'https://fastcdn.mihoyo.com/content-v2/plat/100954/e0a9576fddc0ebb07e2533e0965e5d0d_8056648887776496971.png', '2008-05-01', 2, '2023-10-20 16:35:33', '2023-10-20 16:35:39'),
(4, 'wuyong', '123456', '吴用', 1, '13309090004', 2, 9200.00, 'https://fastcdn.mihoyo.com/content-v2/plat/100954/e0a9576fddc0ebb07e2533e0965e5d0d_8056648887776496971.png', '2007-01-01', 2, '2023-10-20 16:35:33', '2023-10-20 16:35:41'),
(5, 'gongsunsheng', '123456', '公孙胜', 1, '13309090005', 2, 9500.00, 'https://fastcdn.mihoyo.com/content-v2/plat/100954/e0a9576fddc0ebb07e2533e0965e5d0d_8056648887776496971.png', '2012-12-05', 2, '2023-10-20 16:35:33', '2023-10-20 16:35:43'),
(6, 'huosanniang', '123456', '扈三娘', 2, '13309090006', 3, 6500.00, 'https://fastcdn.mihoyo.com/content-v2/plat/100954/e0a9576fddc0ebb07e2533e0965e5d0d_8056648887776496971.png', '2013-09-05', 1, '2023-10-20 16:35:33', '2023-10-20 16:35:45'),
(7, 'chaijin', '123456', '柴进', 1, '13309090007', 1, 4700.00, 'https://fastcdn.mihoyo.com/content-v2/plat/100954/e0a9576fddc0ebb07e2533e0965e5d0d_8056648887776496971.png', '2005-08-01', 1, '2023-10-20 16:35:33', '2023-10-20 16:35:47'),
(8, 'likui', '123456', '李逵', 1, '13309090008', 1, 4800.00, 'https://fastcdn.mihoyo.com/content-v2/plat/100954/e0a9576fddc0ebb07e2533e0965e5d0d_8056648887776496971.png', '2014-11-09', 1, '2023-10-20 16:35:33', '2023-10-20 16:35:49'),
(9, 'wusong', '123456', '武松', 1, '13309090009', 1, 4900.00, 'https://fastcdn.mihoyo.com/content-v2/plat/100954/e0a9576fddc0ebb07e2533e0965e5d0d_8056648887776496971.png', '2011-03-11', 1, '2023-10-20 16:35:33', '2023-10-20 16:35:51'),
(10, 'linchong', '123456', '林冲', 1, '13309090010', 1, 5000.00, 'https://fastcdn.mihoyo.com/content-v2/plat/100954/e0a9576fddc0ebb07e2533e0965e5d0d_8056648887776496971.png', '2013-09-05', 1, '2023-10-20 16:35:33', '2023-10-20 16:35:53'),
(11, 'huyanzhuo', '123456', '呼延灼', 1, '13309090011', 2, 9700.00, 'https://fastcdn.mihoyo.com/content-v2/plat/100954/e0a9576fddc0ebb07e2533e0965e5d0d_8056648887776496971.png', '2007-02-01', 2, '2023-10-20 16:35:33', '2023-10-20 16:35:55'),
(12, 'xiaoliguang', '123456', '小李广', 1, '13309090012', 2, 10000.00, 'https://fastcdn.mihoyo.com/content-v2/plat/100954/e0a9576fddc0ebb07e2533e0965e5d0d_8056648887776496971.png', '2008-08-18', 2, '2023-10-20 16:35:33', '2023-10-20 16:35:57'),
(13, 'yangzhi', '123456', '杨志', 1, '13309090013', 1, 5300.00, 'https://fastcdn.mihoyo.com/content-v2/plat/100954/e0a9576fddc0ebb07e2533e0965e5d0d_8056648887776496971.png', '2012-11-01', 1, '2023-10-20 16:35:33', '2023-10-20 16:35:59'),
(14, 'shijin', '123456', '史进', 1, '13309090014', 2, 10600.00, 'https://webstatic.mihoyo.com/upload/op-public/2021/10/03/4080f2eb748823d8c38507c3d7b69b36_936817768851375073.png', '2002-08-01', 2, '2023-10-20 16:35:33', '2023-10-20 16:36:01'),
(15, 'sunerniang', '123456', '孙二娘', 2, '13309090015', 2, 10900.00, 'https://webstatic.mihoyo.com/upload/op-public/2021/10/03/4080f2eb748823d8c38507c3d7b69b36_936817768851375073.png', '2011-05-01', 2, '2023-10-20 16:35:33', '2023-10-20 16:36:03'),
(16, 'luzhishen', '123456', '鲁智深', 1, '13309090016', 2, 9600.00, 'https://webstatic.mihoyo.com/upload/op-public/2021/10/03/4080f2eb748823d8c38507c3d7b69b36_936817768851375073.png', '2010-01-01', 2, '2023-10-20 16:35:33', '2023-10-20 16:36:05'),
(17, 'liying', '12345678', '李应', 1, '13309090017', 1, 5800.00, 'https://webstatic.mihoyo.com/upload/op-public/2021/10/03/4080f2eb748823d8c38507c3d7b69b36_936817768851375073.png', '2015-03-21', 1, '2023-10-20 16:35:33', '2023-10-20 16:36:07'),
(18, 'shiqian', '123456', '时迁', 1, '13309090018', 2, 10200.00, 'https://webstatic.mihoyo.com/upload/op-public/2021/10/03/4080f2eb748823d8c38507c3d7b69b36_936817768851375073.png', '2015-01-01', 2, '2023-10-20 16:35:33', '2023-10-20 16:36:09'),
(19, 'gudasao', '123456', '顾大嫂', 2, '13309090019', 2, 10500.00, 'https://webstatic.mihoyo.com/upload/op-public/2022/05/27/3896559583929f643fbe39ec1d6ca1c9_1026345827445617861.png', '2008-01-01', 2, '2023-10-20 16:35:33', '2023-10-20 16:36:11'),
(20, 'ruanxiaoer', '123456', '阮小二', 1, '13309090020', 2, 10800.00, 'https://webstatic.mihoyo.com/upload/op-public/2022/05/27/3896559583929f643fbe39ec1d6ca1c9_1026345827445617861.png', '2018-01-01', 2, '2023-10-20 16:35:33', '2023-10-20 16:36:13'),
(21, 'ruanxiaowu', '123456', '阮小五', 1, '13309090021', 5, 5200.00, 'https://webstatic.mihoyo.com/upload/op-public/2022/05/27/3896559583929f643fbe39ec1d6ca1c9_1026345827445617861.png', '2015-01-01', 3, '2023-10-20 16:35:33', '2023-10-20 16:36:15'),
(22, 'ruanxiaoqi', '123456', '阮小七', 1, '13309090022', 5, 5500.00, 'https://webstatic.mihoyo.com/upload/op-public/2022/05/27/3896559583929f643fbe39ec1d6ca1c9_1026345827445617861.png', '2016-01-01', 3, '2023-10-20 16:35:33', '2023-10-20 16:36:17'),
(23, 'ruanji', '123456', '阮籍', 1, '13309090023', 5, 5800.00, 'https://webstatic.mihoyo.com/upload/op-public/2022/05/27/3896559583929f643fbe39ec1d6ca1c9_1026345827445617861.png', '2012-01-01', 3, '2023-10-20 16:35:33', '2023-10-20 16:36:19'),
(24, 'tongwei', '123456', '童威', 1, '13309090024', 5, 5000.00, 'https://webstatic.mihoyo.com/upload/op-public/2021/10/09/def1f2abcfc2af0bbe2e5900a60a5ee1_5699547505742166353.png', '2006-01-01', 3, '2023-10-20 16:35:33', '2023-10-20 16:36:21'),
(25, 'tongmeng', '123456', '童猛', 1, '13309090025', 5, 4800.00, 'https://webstatic.mihoyo.com/upload/op-public/2021/10/09/def1f2abcfc2af0bbe2e5900a60a5ee1_5699547505742166353.png', '2002-01-01', 3, '2023-10-20 16:35:33', '2023-10-20 16:36:23'),
(26, 'yanshun', '123456', '燕顺', 1, '13309090026', 5, 5400.00, 'https://webstatic.mihoyo.com/upload/op-public/2021/10/09/def1f2abcfc2af0bbe2e5900a60a5ee1_5699547505742166353.png', '2011-01-01', 3, '2023-10-20 16:35:33', '2023-11-08 22:12:46'),
(27, 'lijun', '123456', '李俊', 1, '13309090027', 2, 6600.00, 'https://webstatic.mihoyo.com/upload/op-public/2021/10/09/def1f2abcfc2af0bbe2e5900a60a5ee1_5699547505742166353.png', '2004-01-01', 2, '2023-10-20 16:35:33', '2023-11-16 17:56:59'),
(28, 'lizhong', '123456', '李忠', 1, '13309090028', 5, 5000.00, 'https://webstatic.mihoyo.com/upload/op-public/2021/10/09/def1f2abcfc2af0bbe2e5900a60a5ee1_5699547505742166353.png', '2007-01-01', 3, '2023-10-20 16:35:33', '2023-11-17 16:34:22'),
(30, 'liyun', '123456', '李云', 1, '13309090030', NULL, NULL, 'https://webstatic.mihoyo.com/upload/op-public/2021/10/09/def1f2abcfc2af0bbe2e5900a60a5ee1_5699547505742166353.png', '2020-03-01', NULL, '2023-10-20 16:35:33', '2023-10-20 16:36:31'),
(36, 'linghuchong', '123456', '令狐冲', 1, '18809091212', 2, 6800.00, 'https://webstatic.mihoyo.com/upload/op-public/2021/10/09/def1f2abcfc2af0bbe2e5900a60a5ee1_5699547505742166353.png', '2023-10-19', 2, '2023-10-20 20:44:54', '2023-11-09 09:41:04'),
(44, 'ceshi', '123456', '测试', 2, '14565456787', 3, 20000.00, 'http://localhost:8080/images/83fc292ce6714d949877a4549caf1bb4.jpg', '2026-05-18', 1, '2026-05-14 04:28:02', '2026-05-14 04:45:48');

-- 8.3 班级数据
INSERT INTO `clazz` (`id`, `name`, `room`, `begin_date`, `end_date`, `master_id`, `subject`, `create_time`, `update_time`) VALUES
(1, 'JavaEE就业163期', '212', '2024-04-30', '2024-06-29', 10, 1, '2024-06-01 17:08:23', '2024-06-01 17:39:58'),
(2, '前端就业90期', '210', '2026-05-30', '2026-07-12', 3, 2, '2024-06-01 17:45:12', '2024-06-01 17:45:12'),
(3, 'JavaEE就业165期', '108', '2024-06-15', '2024-12-25', 6, 1, '2024-06-01 17:45:40', '2024-06-01 17:45:40'),
(4, 'JavaEE就业166期', '105', '2024-07-20', '2024-02-20', 20, 1, '2024-06-01 17:46:10', '2024-06-01 17:46:10'),
(5, '大数据就业58期', '209', '2026-05-14', '2026-06-19', 7, 3, '2024-06-01 17:51:21', '2024-06-01 17:51:21'),
(6, 'JavaEE就业167期', '325', '2024-11-20', '2024-05-10', 36, 1, '2024-11-15 11:35:46', '2024-12-13 14:31:24');

-- 8.4 学生数据
INSERT INTO `student` (`id`, `name`, `no`, `gender`, `phone`, `id_card`, `is_college`, `address`, `degree`, `graduation_date`, `clazz_id`, `violation_count`, `violation_score`, `create_time`, `update_time`) VALUES
(1, '段誉', '2022000001', 1, '18800000001', '110120000300200001', 1, '北京市昌平区建材城西路1号', 1, '2021-07-01', 2, 2, 4, '2024-11-14 21:22:19', '2024-11-15 16:20:59'),
(2, '萧峰', '2022000002', 1, '18800210003', '110120000300200002', 1, '北京市昌平区建材城西路2号', 2, '2022-07-01', 1, 0, 0, '2024-11-14 21:22:19', '2024-11-14 21:22:19'),
(3, '虚竹', '2022000003', 1, '18800013001', '110120000300200003', 1, '北京市昌平区建材城西路3号', 2, '2024-07-01', 1, 0, 0, '2024-11-14 21:22:19', '2024-11-14 21:22:19'),
(4, '萧远山', '2022000004', 1, '18800003211', '110120000300200004', 1, '北京市昌平区建材城西路4号', 3, '2024-07-01', 1, 0, 0, '2024-11-14 21:22:19', '2024-11-14 21:22:19'),
(5, '阿朱', '2022000005', 2, '18800160002', '110120000300200005', 1, '北京市昌平区建材城西路5号', 4, '2020-07-01', 1, 0, 0, '2024-11-14 21:22:19', '2024-11-14 21:22:19'),
(6, '阿紫', '2022000006', 2, '18800000034', '110120000300200006', 1, '北京市昌平区建材城西路6号', 4, '2021-07-01', 2, 0, 0, '2024-11-14 21:22:19', '2024-11-14 21:22:19'),
(7, '游坦之', '2022000007', 1, '18800000067', '110120000300200007', 1, '北京市昌平区建材城西路7号', 4, '2022-07-01', 2, 0, 0, '2024-11-14 21:22:19', '2024-11-14 21:22:19'),
(8, '康敏', '2022000008', 2, '18800000077', '110120000300200008', 1, '北京市昌平区建材城西路8号', 5, '2024-07-01', 2, 0, 0, '2024-11-14 21:22:19', '2024-11-14 21:22:19'),
(9, '徐长老', '2022000009', 1, '18800000341', '110120000300200009', 1, '北京市昌平区建材城西路9号', 3, '2024-07-01', 2, 0, 0, '2024-11-14 21:22:19', '2024-11-14 21:22:19'),
(10, '云中鹤', '2022000010', 1, '18800006571', '110120000300200010', 1, '北京市昌平区建材城西路10号', 2, '2020-07-01', 2, 0, 0, '2024-11-14 21:22:19', '2024-11-14 21:22:19'),
(11, '钟万仇', '2022000011', 1, '18800000391', '110120000300200011', 1, '北京市昌平区建材城西路11号', 4, '2021-07-01', 1, 0, 0, '2024-11-14 21:22:19', '2024-11-15 16:21:24'),
(12, '崔百泉', '2022000012', 1, '18800000781', '110120000300200018', 1, '北京市昌平区建材城西路12号', 4, '2022-07-05', 3, 7, 19, '2024-11-14 21:22:19', '2024-12-13 14:33:58'),
(13, '耶律洪基', '2022000013', 1, '18800008901', '110120000300200013', 1, '北京市昌平区建材城西路13号', 4, '2024-07-01', 2, 0, 0, '2024-11-14 21:22:19', '2024-11-15 16:21:21'),
(14, '天山童姥', '2022000014', 2, '18800009201', '110120000300200014', 1, '北京市昌平区建材城西路14号', 4, '2024-07-01', 1, 0, 0, '2024-11-14 21:22:19', '2024-11-15 16:21:17'),
(15, '刘竹庄', '2022000015', 1, '18800009401', '110120000300200015', 1, '北京市昌平区建材城西路15号', 3, '2020-07-01', 4, 0, 0, '2024-11-14 21:22:19', '2024-11-14 21:22:19'),
(16, '李春来', '2022000016', 1, '18800008501', '110120000300200016', 1, '北京市昌平区建材城西路16号', 4, '2021-07-01', 4, 0, 0, '2024-11-14 21:22:19', '2024-11-14 21:22:19'),
(17, '王语嫣', '2022000017', 2, '18800007601', '110120000300200017', 1, '北京市昌平区建材城西路17号', 2, '2022-07-01', 4, 0, 0, '2024-11-14 21:22:19', '2024-11-14 21:22:19'),
(18, '郑成功', '2024001101', 1, '13309092345', '110110110110110110', 0, '北京市昌平区回龙观街道88号', 5, '2021-07-01', 3, 2, 7, '2024-11-15 16:26:18', '2024-11-15 16:40:10');

-- 8.5 员工经历数据
INSERT INTO `emp_expr` (`id`, `emp_id`, `begin`, `end`, `company`, `job`) VALUES
(9, 44, '2024-12-12', '2025-05-07', '米哈游', '崩坏星穹铁到内测开发');

-- 8.6 操作日志数据
INSERT INTO `operate_log` (`id`, `operate_emp_id`, `operate_time`, `class_name`, `method_name`, `method_params`, `return_value`, `cost_time`) VALUES
(3, 44, '2026-07-06 05:26:35', 'com.xzl.controller.ClazzController', 'addClazz', '[Clazz(id=null, name=cscs, room=cs, beginDate=2026-07-12, endDate=2026-07-20, masterId=7, subject=4, createTime=2026-07-06T05:26:35.801722600, updateTime=2026-07-06T05:26:35.802796300, masterName=null, status=null)]', 'Result(code=1, data=null, msg=success)', 10),
(4, 44, '2026-07-07 05:26:35', 'com.xzl.controller.ClazzController', 'delClazz', '[Clazz(id=null, name=cscs, room=cs, beginDate=2026-07-12, endDate=2026-07-20, masterId=7, subject=4, createTime=2026-07-06T05:26:35.801722600, updateTime=2026-07-06T05:26:35.802796300, masterName=null, status=null)]', 'Result(code=1, data=null, msg=success)', 10),
(5, 44, '2026-07-08 05:26:35', 'com.xzl.controller.ClazzController', 'updateClazz', '[Clazz(id=null, name=cscs, room=cs, beginDate=2026-07-12, endDate=2026-07-20, masterId=7, subject=4, createTime=2026-07-06T05:26:35.801722600, updateTime=2026-07-06T05:26:35.802796300, masterName=null, status=null)]', 'Result(code=1, data=null, msg=success)', 10),
(6, 44, '2026-07-06 06:24:20', 'com.xzl.controller.ClazzController', 'deleteClazz', '[7]', 'Result(code=1, data=null, msg=success)', 4),
(7, 44, '2026-07-13 05:22:48', 'com.xzl.controller.DeptController', 'delDept', '[8]', 'Result(code=1, data=null, msg=success)', 4309);