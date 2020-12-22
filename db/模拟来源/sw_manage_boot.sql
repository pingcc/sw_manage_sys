/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50641
 Source Host           : localhost:3306
 Source Schema         : sw_manage_boot

 Target Server Type    : MySQL
 Target Server Version : 50641
 File Encoding         : 65001

 Date: 19/11/2020 16:41:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sw_article_approver_r
-- ----------------------------
DROP TABLE IF EXISTS `sw_article_approver_r`;
CREATE TABLE `sw_article_approver_r`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `article_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文章id',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批人id',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批人名称',
  `real_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批人名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '点评',
  `is_deleted` int(11) NULL DEFAULT NULL COMMENT '刪除标识0-1',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批状态:DRAFT|草稿,APPROVAL|审批中,REFUSE|驳回,ALLOW|通过',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章审批记录表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sw_article_manage
-- ----------------------------
DROP TABLE IF EXISTS `sw_article_manage`;
CREATE TABLE `sw_article_manage`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `article_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文章标题',
  `article_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章内容',
  `is_publish` int(11) NULL DEFAULT NULL COMMENT '是否允许发布0-1',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批状态:DRAFT|草稿,APPROVAL|审批中,REFUSE|驳回,ALLOW|通过',
  `author_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文章作者realname',
  `guide_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '指导人',
  `guide_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '指导人名称',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `article_level` int(11) NULL DEFAULT NULL COMMENT '文章评优等级',
  `sys_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '机构编码',
  `sys_bp_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '机构年级编码',
  `is_deleted` int(11) NULL DEFAULT NULL COMMENT '刪除标识0-1',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章管理表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sw_article_manage
-- ----------------------------
INSERT INTO `sw_article_manage` VALUES ('1', '我的学校', '我的学校是最美的！', 1, 'ALLOW', '学生01', 'ls01', '老师01', '0-0', 10, 'A01A01A01', 'A01A01', 0, 'xs01', NULL, '2020-09-29 15:37:00', NULL);
INSERT INTO `sw_article_manage` VALUES ('1321732716553818113', '我的家!', '我的家是最美的、啦啦啦啦，我的家是最美的、啦啦啦啦', 1, 'ALLOW', '学生01', 'ls01', '老师01', '嘻嘻嘻', 10, 'A01', 'A01A01', 0, 'admin', 'admin', '2020-10-29 08:36:51', '2020-10-29 08:42:33');

-- ----------------------------
-- Table structure for sw_books_attr
-- ----------------------------
DROP TABLE IF EXISTS `sw_books_attr`;
CREATE TABLE `sw_books_attr`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `books_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书籍id',
  `books_pos_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书籍摆放编号',
  `book_sale` int(11) NULL DEFAULT NULL COMMENT '书籍状态|book_sale',
  `days` int(11) NULL DEFAULT NULL COMMENT '书籍允许借出天数',
  `borrow_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '借书人',
  `borrow_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '借书人名称',
  `borrow_time` datetime(0) NULL DEFAULT NULL COMMENT '借书时间',
  `sys_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '机构编码',
  `is_deleted` int(11) NULL DEFAULT NULL COMMENT '刪除标识0-1',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_pos_code`(`books_pos_code`) USING BTREE,
  INDEX `idx_book_id`(`books_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '书籍属性表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sw_books_attr
-- ----------------------------
INSERT INTO `sw_books_attr` VALUES ('1322122698934083586', '1322122698799865857', 'A0819', 2, 30, 'xs09', '学生09', '2020-11-18 11:12:56', 'A01', 0, 'admin', 'admin', '2020-10-30 10:26:30', '2020-11-18 11:12:56');
INSERT INTO `sw_books_attr` VALUES ('1328948646891474945', '1328948646769840129', 'A0820', 2, 2, 'xs11', '学生11', '2020-11-18 11:27:24', 'A01', 0, 'admin', 'admin', '2020-11-18 06:30:23', '2020-11-18 11:27:24');

-- ----------------------------
-- Table structure for sw_books_manage
-- ----------------------------
DROP TABLE IF EXISTS `sw_books_manage`;
CREATE TABLE `sw_books_manage`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `is_sale` int(11) NOT NULL COMMENT '是否上架0-1',
  `books_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '书籍名称',
  `books_desc` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书籍描述',
  `books_bar_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书籍条码',
  `books_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '书籍编码，系统自动生成',
  `sys_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '机构编码',
  `is_deleted` int(11) NULL DEFAULT NULL COMMENT '刪除标识0-1',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`books_code`) USING BTREE,
  UNIQUE INDEX `uk__bar_code`(`books_bar_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '书籍管理表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sw_books_manage
-- ----------------------------
INSERT INTO `sw_books_manage` VALUES ('1322122698799865857', 1, '人性的弱点', '美国“成人教育之父”戴尔·卡耐基所著的《人性的弱点》，汇集了卡耐基的思想精华和最激动人心的内容，是作者最成功的励志经典，书的唯一目的就是帮助解决所面临的最大问题：如何在日常生活、商务活动与社会交往中与人打交道，并有效地影响他人；如何击败人类的生存之敌——忧虑，以创造一种幸福美好的人生。 卡耐基-《人性的弱点》，讲做人要平和、真诚，沟通的成功在于尽量避免争辩，最常见的情况是在争辩中取得了胜利却失去了成功的机会。 这本书适合长时间的品味，以至自觉地养成良好的习惯及逾越的品格。没有什么励志书是可以让你一下子就有脱胎换骨的改变的，最好的是多读好书，多实践，并最终形成优秀的行事习惯吧！强烈推荐：【独家】《人性的弱点全集》实体书籍100%纯手打。', '202010301000', '2000000001', 'A01', 0, 'admin', 'admin', '2020-10-30 10:26:30', '2020-11-18 11:13:20');
INSERT INTO `sw_books_manage` VALUES ('1328948646769840129', 1, '平凡的世界', '《平凡的世界》是中国作家路遥创作的一部全景式地表现中国当代城乡社会生活的百万字长篇小说。全书共三部。1986年12月首次出版。\n该书以中国70年代中期到80年代中期十年间为背景，通过复杂的矛盾纠葛，以孙少安和孙少平两兄弟为中心，刻画了当时社会各阶层众多普通人的形象；劳动与爱情、挫折与追求、痛苦与欢乐、日常生活与巨大社会冲突纷繁地交织在一起，深刻地展示了普通人在大时代历史进程中所走过的艰难曲折的道路。', '202010301001', '2000000002', 'A01', 0, 'admin', 'admin', '2020-11-18 06:30:23', '2020-11-18 11:20:00');

-- ----------------------------
-- Table structure for sw_books_trx
-- ----------------------------
DROP TABLE IF EXISTS `sw_books_trx`;
CREATE TABLE `sw_books_trx`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `books_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书籍id',
  `trx_type` int(11) NULL DEFAULT NULL COMMENT '交易状态1正常|2超时',
  `is_book_normal` int(11) NULL DEFAULT NULL COMMENT '书本是否正常，1|正常2|破损|3丢失',
  `borrow_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '借书人',
  `borrow_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '还书人',
  `borrow_time` datetime(0) NOT NULL COMMENT '借书时间',
  `deadline_time` datetime(0) NOT NULL COMMENT '还书截止时间',
  `trx_deadline_days` int(11) NULL DEFAULT NULL COMMENT '交易超时天数',
  `days` int(11) NULL DEFAULT NULL COMMENT '书籍允许借出的天数',
  `sys_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '机构编码',
  `sys_bp_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '年级编码',
  `is_deleted` int(11) NULL DEFAULT NULL COMMENT '刪除标识0-1',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `sys_org_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班级名称',
  `sys_bp_org_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年级',
  `return_time` datetime(0) NULL DEFAULT NULL COMMENT '还书时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_books_id`(`books_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '书籍交易表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sw_books_trx
-- ----------------------------
INSERT INTO `sw_books_trx` VALUES ('1329007823810715649', '1328948646769840129', 1, 2, 'xs02', '学生02', '2020-11-18 10:25:32', '2020-12-18 10:25:32', NULL, 30, 'A01A02A01', 'A01A02', 0, 'admin', 'admin', '2020-11-18 10:25:32', '2020-11-18 10:50:09', '一班', '一年级', '2020-11-18 10:50:09');
INSERT INTO `sw_books_trx` VALUES ('1329014957248077826', '1328948646769840129', 1, 1, 'xs13', '学生13', '2020-11-18 10:53:52', '2020-12-18 10:53:52', NULL, 30, 'A01A14A01', 'A01A14', 0, 'admin', 'admin', '2020-11-18 10:53:52', '2020-11-18 11:25:40', '计算机科学与技术专业', '大一', '2020-11-18 11:25:40');
INSERT INTO `sw_books_trx` VALUES ('1329019752616730625', '1322122698799865857', NULL, NULL, 'xs09', '学生09', '2020-11-18 11:12:56', '2020-12-18 11:12:56', NULL, 30, 'A01A14A05', 'A01A14', 0, 'admin', NULL, '2020-11-18 11:12:56', NULL, '戏剧影视文学', '大一', NULL);
INSERT INTO `sw_books_trx` VALUES ('1329023393431986177', '1328948646769840129', NULL, NULL, 'xs11', '学生11', '2020-11-18 11:27:24', '2020-11-20 11:27:24', NULL, 2, 'A01A14A02', 'A01A14', 0, 'admin', NULL, '2020-11-18 11:27:24', NULL, '软件工程专业', '大一', NULL);

-- ----------------------------
-- Table structure for sw_flow_approver
-- ----------------------------
DROP TABLE IF EXISTS `sw_flow_approver`;
CREATE TABLE `sw_flow_approver`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `function_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程分类-对应flow_type',
  `level_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '级别名称',
  `level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '当前节点审核状态',
  `next_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '下一个节点审核状态',
  `role_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色code',
  `is_start` int(11) NULL DEFAULT NULL COMMENT '是否开起',
  `is_deleted` int(11) NULL DEFAULT NULL COMMENT '刪除标识0-1',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '业务审核' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sw_flow_approver
-- ----------------------------
INSERT INTO `sw_flow_approver` VALUES ('1', '1', '一级审核', '1', '2', 'snow_group_leader', 1, 0, 'admin', 'admin', '2020-01-09 16:46:21', '2020-07-28 10:43:37');
INSERT INTO `sw_flow_approver` VALUES ('1294177961754853378', '2', '初审', '1', '2', 'snow_group_leader', 1, 0, 'admin', 'admin', '2020-08-14 15:44:05', '2020-08-14 15:45:05');
INSERT INTO `sw_flow_approver` VALUES ('2', '1', '二级审核', '2', '3', 'snow_supervisor', 1, 0, 'admin', 'admin', '2020-07-28 10:44:17', '2020-08-14 17:59:00');
INSERT INTO `sw_flow_approver` VALUES ('3', '1', '三级审核', '4', '3', 'snow_finance', 0, 0, 'admin', 'admin', '2020-08-03 10:44:46', '2020-08-14 17:58:55');

-- ----------------------------
-- Table structure for sw_student_score
-- ----------------------------
DROP TABLE IF EXISTS `sw_student_score`;
CREATE TABLE `sw_student_score`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `student_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学生id',
  `student_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学生学号',
  `student_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学生昵称',
  `student_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学生姓名',
  `examine_score` decimal(5, 2) NULL DEFAULT NULL COMMENT '考试分数',
  `examine_sub` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '考试科目',
  `examine_time` date NOT NULL COMMENT '考试时间',
  `examine_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '考试类型',
  `sys_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班级编码',
  `sys_bp_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年级编码',
  `is_deleted` int(11) NULL DEFAULT NULL COMMENT '刪除标识0-1',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `sys_org_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '班级名称',
  `sys_bp_org_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '年级',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_key`(`student_no`, `examine_sub`, `examine_time`) USING BTREE COMMENT '学号、考试时间、科目联合索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学生分数表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sw_student_score
-- ----------------------------
INSERT INTO `sw_student_score` VALUES ('1328660010589376513', NULL, '202001011000009', 'xs01', '学生01', 99.00, '数学', '2020-11-14', '第一次模拟考试', 'A01A01A01', 'A01A01', 0, 'ls01', 'ls01', '2020-11-17 11:23:26', '2020-11-17 11:36:58', '一班', '幼儿园');
INSERT INTO `sw_student_score` VALUES ('1328660010606153730', NULL, '202001011000009', 'xs01', '学生01', 100.00, '数学', '2020-10-16', '第一次模拟考试', 'A01A01A01', 'A01A01', 0, 'ls01', NULL, '2020-11-17 11:23:26', NULL, '一班', '幼儿园');
INSERT INTO `sw_student_score` VALUES ('1328663386781798402', NULL, '202001011000009', 'xs01', '学生01', 98.90, '语文', '2020-11-16', '第二次模拟考试', 'A01A01A01', 'A01A01', 0, 'ls01', NULL, '2020-11-17 11:36:51', NULL, '一班', '幼儿园');

SET FOREIGN_KEY_CHECKS = 1;
