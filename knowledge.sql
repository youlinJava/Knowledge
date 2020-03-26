/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.7.28-log : Database - knowledge
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`knowledge` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `knowledge`;

/*Table structure for table `t_callcenter_black_list` */

DROP TABLE IF EXISTS `t_callcenter_black_list`;

CREATE TABLE `t_callcenter_black_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone_number` varchar(20) NOT NULL COMMENT '电话号码',
  `reason` varchar(200) NOT NULL COMMENT '原因',
  `create_by` varchar(60) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(60) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `creator_name` varchar(10) DEFAULT NULL COMMENT '创建人姓名',
  `updator_name` varchar(10) DEFAULT NULL COMMENT '修改人姓名',
  `del_flag` varchar(2) NOT NULL DEFAULT '0' COMMENT '删除标识,0 未删除 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='黑名单';

/*Data for the table `t_callcenter_black_list` */

/*Table structure for table `t_callcenter_common_language` */

DROP TABLE IF EXISTS `t_callcenter_common_language`;

CREATE TABLE `t_callcenter_common_language` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(50) NOT NULL COMMENT '分类',
  `content` varchar(500) NOT NULL COMMENT '内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='常用语';

/*Data for the table `t_callcenter_common_language` */

/*Table structure for table `t_callcenter_extension_number_setting` */

DROP TABLE IF EXISTS `t_callcenter_extension_number_setting`;

CREATE TABLE `t_callcenter_extension_number_setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `extension_number` varchar(10) NOT NULL COMMENT '分机号',
  `user_name` varchar(10) DEFAULT NULL COMMENT '用户姓名',
  `user_account` varchar(15) DEFAULT NULL COMMENT '员工号',
  `avatar_url` varchar(1000) DEFAULT NULL COMMENT '头像链接',
  `create_by` varchar(30) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(30) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(2) DEFAULT NULL COMMENT '删除标识,0 未删除 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='电话分机号管理（包括历史）';

/*Data for the table `t_callcenter_extension_number_setting` */

insert  into `t_callcenter_extension_number_setting`(`id`,`extension_number`,`user_name`,`user_account`,`avatar_url`,`create_by`,`create_time`,`update_by`,`update_time`,`del_flag`) values (1,'0001','黎明','988877','http://.sdfsd',NULL,NULL,NULL,NULL,'1'),(2,'0001','liping','93942342',NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `t_callcenter_holiday` */

DROP TABLE IF EXISTS `t_callcenter_holiday`;

CREATE TABLE `t_callcenter_holiday` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `calendar_day` date NOT NULL COMMENT '日期',
  `week_day` varchar(5) DEFAULT NULL COMMENT '星期',
  `work_day_flg` varchar(2) DEFAULT NULL COMMENT '1:工作日,0:休息日',
  `work_time` varchar(200) DEFAULT NULL COMMENT '当天工作时间段，9:00-11:30,13:00-18:00这种格式存储',
  `create_by` varchar(60) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(60) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `creator_name` varchar(10) DEFAULT NULL COMMENT '创建人姓名',
  `updator_name` varchar(10) DEFAULT NULL COMMENT '修改人姓名',
  `del_flag` varchar(2) NOT NULL DEFAULT '0' COMMENT '删除标识,0 未删除 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节假日管理';

/*Data for the table `t_callcenter_holiday` */

/*Table structure for table `t_callcenter_portal_call_rating` */

DROP TABLE IF EXISTS `t_callcenter_portal_call_rating`;

CREATE TABLE `t_callcenter_portal_call_rating` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rec_id` int(11) DEFAULT NULL COMMENT '通话记录表id,外键',
  `rate_item` varbinary(50) DEFAULT NULL COMMENT '评分项目',
  `rate_rules` varchar(500) DEFAULT NULL COMMENT '评分规则列表',
  `rate_score` int(11) DEFAULT NULL COMMENT '最高分',
  `score` int(11) DEFAULT NULL COMMENT '最终打分',
  `comments` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(60) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(60) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `creator_name` varchar(10) DEFAULT NULL COMMENT '创建人姓名',
  `updator_name` varchar(10) DEFAULT NULL COMMENT '修改人姓名',
  `del_flag` varchar(2) NOT NULL DEFAULT '0' COMMENT '删除标识,0 未删除 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='电话质检评分表';

/*Data for the table `t_callcenter_portal_call_rating` */

/*Table structure for table `t_callcenter_portal_call_record` */

DROP TABLE IF EXISTS `t_callcenter_portal_call_record`;

CREATE TABLE `t_callcenter_portal_call_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone_number` varchar(20) NOT NULL COMMENT '电话号码',
  `call_type` varchar(2) NOT NULL COMMENT '呼叫类型 0:呼入1:呼出',
  `call_start_time` datetime DEFAULT NULL COMMENT '呼入呼出时间',
  `talking_start_time` datetime DEFAULT NULL COMMENT '通话开始时间',
  `talking_end_time` datetime DEFAULT NULL COMMENT '通话结束时间',
  `employee_num` varchar(20) DEFAULT NULL COMMENT '员工编号',
  `employee_name` varchar(50) DEFAULT NULL COMMENT '员工姓名',
  `org_name` varchar(30) DEFAULT NULL COMMENT '所属单位(公司)',
  `employee_phone` varchar(20) DEFAULT NULL COMMENT '员工电话',
  `employee_email` varchar(50) DEFAULT NULL COMMENT '员工邮箱',
  `work_address` varchar(30) DEFAULT NULL COMMENT '工作地址',
  `contract_address` varchar(30) DEFAULT NULL COMMENT '合同地址',
  `employee_manager` varchar(50) DEFAULT NULL COMMENT '直属领导名字',
  `entry_time` datetime DEFAULT NULL COMMENT '入职时间',
  `employee_department` varchar(20) DEFAULT NULL COMMENT '所属部门名',
  `employee_position` varchar(20) DEFAULT NULL COMMENT '所属岗位',
  `contract_entity` varchar(20) DEFAULT NULL COMMENT '合同实体',
  `service_summary` varchar(200) DEFAULT NULL COMMENT '服务小结',
  `service_score` int(11) DEFAULT NULL COMMENT '服务评分',
  `extension_num` varchar(10) DEFAULT NULL COMMENT '接听电话的分机号',
  `voice_url` varchar(100) DEFAULT NULL COMMENT '录音url',
  `create_by` varchar(60) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(60) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `creator_name` varchar(10) DEFAULT NULL COMMENT '创建人姓名',
  `updator_name` varchar(10) DEFAULT NULL COMMENT '修改人姓名',
  `del_flag` varchar(2) NOT NULL DEFAULT '0' COMMENT '删除标识,0 未删除 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通话记录表,电话客服信息记录';

/*Data for the table `t_callcenter_portal_call_record` */

/*Table structure for table `t_callcenter_portal_ticket_call` */

DROP TABLE IF EXISTS `t_callcenter_portal_ticket_call`;

CREATE TABLE `t_callcenter_portal_ticket_call` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `call_rec_id` int(11) DEFAULT NULL COMMENT '通话记录id,外键',
  `ticket_code` varchar(20) DEFAULT NULL COMMENT '工单系统那边返回的工单号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工单和电话客服记录的关联';

/*Data for the table `t_callcenter_portal_ticket_call` */

/*Table structure for table `t_callcenter_portal_ticket_callback` */

DROP TABLE IF EXISTS `t_callcenter_portal_ticket_callback`;

CREATE TABLE `t_callcenter_portal_ticket_callback` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ticket_code` varchar(20) NOT NULL COMMENT '工单系统返回的code',
  `callback_id` int(11) NOT NULL COMMENT '回访表外键，先不设计问问需求',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工单回访历史表，这个表还没设计完，需要问一下每个回访对应是不是唯一一个通话记录';

/*Data for the table `t_callcenter_portal_ticket_callback` */

/*Table structure for table `t_callcenter_portal_ticket_web` */

DROP TABLE IF EXISTS `t_callcenter_portal_ticket_web`;

CREATE TABLE `t_callcenter_portal_ticket_web` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `web_rec_id` int(11) DEFAULT NULL COMMENT 'web客服记录id,外键',
  `ticket_code` varchar(20) DEFAULT NULL COMMENT '工单系统那边返回的工单号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工单和web客服记录的关联';

/*Data for the table `t_callcenter_portal_ticket_web` */

/*Table structure for table `t_callcenter_portal_web_rating` */

DROP TABLE IF EXISTS `t_callcenter_portal_web_rating`;

CREATE TABLE `t_callcenter_portal_web_rating` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rec_id` int(11) DEFAULT NULL COMMENT 'web服务记录表id,外键',
  `rate_item` varbinary(50) DEFAULT NULL COMMENT '评分项目',
  `rate_rules` varchar(500) DEFAULT NULL COMMENT '评分规则列表',
  `rate_score` int(11) DEFAULT NULL COMMENT '最高分',
  `score` int(11) DEFAULT NULL COMMENT '最终打分',
  `comments` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(60) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(60) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `creator_name` varchar(10) DEFAULT NULL COMMENT '创建人姓名',
  `updator_name` varchar(10) DEFAULT NULL COMMENT '修改人姓名',
  `del_flag` varchar(2) NOT NULL DEFAULT '0' COMMENT '删除标识,0 未删除 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网页客服质检评分表';

/*Data for the table `t_callcenter_portal_web_rating` */

/*Table structure for table `t_callcenter_portal_web_rec_msg` */

DROP TABLE IF EXISTS `t_callcenter_portal_web_rec_msg`;

CREATE TABLE `t_callcenter_portal_web_rec_msg` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `web_rec_id` int(11) NOT NULL COMMENT '消息关联的会话id,外键',
  `message` varchar(500) NOT NULL COMMENT '文字消息，文件和图片消息以后设计，考虑一下富文本',
  `create_by` varchar(60) DEFAULT NULL COMMENT '消息发送者id',
  `create_time` datetime DEFAULT NULL COMMENT '消息发送时间',
  `creator_name` varchar(10) DEFAULT NULL COMMENT '消息发送者姓名',
  `creator_type` varchar(2) DEFAULT NULL COMMENT '消息发送者类型 0客服 1客户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网页客服聊天内容';

/*Data for the table `t_callcenter_portal_web_rec_msg` */

/*Table structure for table `t_callcenter_portal_web_record` */

DROP TABLE IF EXISTS `t_callcenter_portal_web_record`;

CREATE TABLE `t_callcenter_portal_web_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `call_start_time` datetime DEFAULT NULL COMMENT '进入队列时间',
  `talking_start_time` datetime DEFAULT NULL COMMENT '会话开始时间',
  `talking_end_time` datetime DEFAULT NULL COMMENT '会话结束时间',
  `employee_num` varchar(20) DEFAULT NULL COMMENT '员工编号',
  `employee_name` varchar(50) DEFAULT NULL COMMENT '员工姓名',
  `org_name` varchar(30) DEFAULT NULL COMMENT '所属单位(公司)',
  `employee_phone` varchar(20) DEFAULT NULL COMMENT '员工电话',
  `employee_email` varchar(50) DEFAULT NULL COMMENT '员工邮箱',
  `work_address` varchar(30) DEFAULT NULL COMMENT '工作地址',
  `contract_address` varchar(30) DEFAULT NULL COMMENT '合同地址',
  `employee_manager` varchar(50) DEFAULT NULL COMMENT '直属领导名字',
  `entry_time` datetime DEFAULT NULL COMMENT '入职时间',
  `employee_department` varchar(20) DEFAULT NULL COMMENT '所属部门名',
  `employee_position` varchar(20) DEFAULT NULL COMMENT '所属岗位',
  `contract_entity` varchar(20) DEFAULT NULL COMMENT '合同实体',
  `service_summary` varchar(200) DEFAULT NULL COMMENT '服务小结',
  `service_score` int(11) DEFAULT NULL COMMENT '服务评分',
  `create_by` varchar(60) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(60) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `creator_name` varchar(10) DEFAULT NULL COMMENT '创建人姓名',
  `updator_name` varchar(10) DEFAULT NULL COMMENT '修改人姓名',
  `talking_close_reason` varchar(10) DEFAULT NULL COMMENT '会话关闭原因，超时/客服关闭/客户关闭',
  `del_flag` varchar(2) NOT NULL DEFAULT '0' COMMENT '删除标识,0 未删除 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网页客服记录表';

/*Data for the table `t_callcenter_portal_web_record` */

/*Table structure for table `t_callcenter_quality_rate` */

DROP TABLE IF EXISTS `t_callcenter_quality_rate`;

CREATE TABLE `t_callcenter_quality_rate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rate_type` varchar(2) NOT NULL COMMENT '0:网页客服评分 1：电话客服评分',
  `rate_item` varchar(50) NOT NULL COMMENT '评分项目',
  `rate_rules` varchar(500) DEFAULT NULL COMMENT '评分规则',
  `rate_score` int(11) NOT NULL COMMENT '分数',
  `create_by` varchar(30) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(30) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `creator_name` varchar(10) DEFAULT NULL COMMENT '创建人姓名',
  `updator_name` varchar(10) DEFAULT NULL COMMENT '修改人姓名',
  `del_flag` varchar(2) NOT NULL DEFAULT '0' COMMENT '删除标识,0 未删除 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='质检评分';

/*Data for the table `t_callcenter_quality_rate` */

/*Table structure for table `t_callcenter_sensitive_word` */

DROP TABLE IF EXISTS `t_callcenter_sensitive_word`;

CREATE TABLE `t_callcenter_sensitive_word` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key_word` varchar(100) NOT NULL COMMENT '关键字',
  `word_desc` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_by` varchar(60) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(60) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `creator_name` varchar(10) DEFAULT NULL COMMENT '创建人姓名',
  `updator_name` varchar(10) DEFAULT NULL COMMENT '修改人姓名',
  `del_flag` varchar(2) NOT NULL DEFAULT '0' COMMENT '删除标识,0 未删除 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='敏感词';

/*Data for the table `t_callcenter_sensitive_word` */

/*Table structure for table `t_callcenter_service_group` */

DROP TABLE IF EXISTS `t_callcenter_service_group`;

CREATE TABLE `t_callcenter_service_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(100) NOT NULL COMMENT '服务队列名',
  `group_desc` varchar(200) DEFAULT NULL COMMENT '队列描述',
  `group_type` varchar(2) NOT NULL COMMENT '服务类型 0:电话客服 1:网页客服 2：非即时咨询',
  `group_code` varchar(15) DEFAULT NULL COMMENT '分组代码，从ivr那边获得的分组代码',
  `create_by` varchar(60) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(60) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `creator_name` varchar(10) DEFAULT NULL COMMENT '创建人姓名',
  `updator_name` varchar(10) DEFAULT NULL COMMENT '修改人姓名',
  `del_flag` varchar(2) NOT NULL DEFAULT '0' COMMENT '删除标识,0 未删除 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务队列维护';

/*Data for the table `t_callcenter_service_group` */

/*Table structure for table `t_callcenter_service_group_business_type` */

DROP TABLE IF EXISTS `t_callcenter_service_group_business_type`;

CREATE TABLE `t_callcenter_service_group_business_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL COMMENT '服务组id,外键',
  `business_type_id` varchar(20) NOT NULL COMMENT '服务类型ID,从其他系统来外键',
  `business_type_name` varchar(20) DEFAULT NULL COMMENT '服务类型名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务组关联的业务类型表';

/*Data for the table `t_callcenter_service_group_business_type` */

/*Table structure for table `t_callcenter_service_group_users` */

DROP TABLE IF EXISTS `t_callcenter_service_group_users`;

CREATE TABLE `t_callcenter_service_group_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `serviec_group_id` int(11) DEFAULT NULL COMMENT '分组id，外键',
  `user_name` varchar(10) DEFAULT NULL COMMENT '用户名',
  `user_account` varchar(15) DEFAULT NULL COMMENT '用户代码',
  `create_by` varchar(60) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(60) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `creator_name` varchar(10) DEFAULT NULL COMMENT '创建人姓名',
  `updator_name` varchar(10) DEFAULT NULL COMMENT '修改人姓名',
  `del_flag` varchar(2) NOT NULL DEFAULT '0' COMMENT '删除标识,0 未删除 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客服分组下的用户，仅对网页客户分组和非即时咨询分组有效';

/*Data for the table `t_callcenter_service_group_users` */

/*Table structure for table `t_callcenter_talking_skill` */

DROP TABLE IF EXISTS `t_callcenter_talking_skill`;

CREATE TABLE `t_callcenter_talking_skill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(50) NOT NULL COMMENT '分类',
  `content` varchar(500) NOT NULL COMMENT '内容',
  `create_by` varchar(60) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(60) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `creator_name` varchar(10) DEFAULT NULL COMMENT '创建人姓名',
  `updator_name` varchar(10) DEFAULT NULL COMMENT '修改人姓名',
  `del_flag` varchar(2) NOT NULL DEFAULT '0' COMMENT '删除标识,0 未删除 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='来电话术语';

/*Data for the table `t_callcenter_talking_skill` */

/*Table structure for table `t_callcenter_web_customer_setting` */

DROP TABLE IF EXISTS `t_callcenter_web_customer_setting`;

CREATE TABLE `t_callcenter_web_customer_setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `web_customer_flg` varchar(2) DEFAULT '1' COMMENT '网页客服开关 0关 1开',
  `non_instant_consultation` varchar(2) DEFAULT '1' COMMENT '非即时咨询开关 0关 1开',
  `queue_prompt` varchar(200) DEFAULT NULL COMMENT '排队提示语',
  `welcome_msg` varchar(200) DEFAULT NULL COMMENT '接通欢迎语',
  `service_timeout` int(11) DEFAULT NULL COMMENT '客服超时时间',
  `service_timeout_msg` varchar(200) DEFAULT NULL COMMENT '客服超时提示语',
  `visitor_timeout` int(11) DEFAULT NULL COMMENT '访客超时时间',
  `visitor_timeout_msg` varchar(200) DEFAULT NULL COMMENT '访客超时提示语',
  `visitor_timeout_repeat` int(11) DEFAULT NULL COMMENT '访客超时语重复次数',
  `visitor_timeout_repeat_interval` int(11) DEFAULT NULL COMMENT '访客超时语重复间隔（秒）',
  `conversation_transfer_prompt` varchar(200) DEFAULT NULL COMMENT '转移对话提示语',
  `conversation_accept_prompt` varchar(200) DEFAULT NULL COMMENT '接管对话提示语',
  `service_disconnect_prompt` varchar(200) DEFAULT NULL COMMENT '客服断线提示语',
  `visitor_disconnect_prompt` varchar(200) DEFAULT NULL COMMENT '访客断线提示语',
  `service_session_close_prompt` varchar(200) DEFAULT NULL COMMENT '客服关闭会话提示语',
  `visitor_session_close_prompt` varchar(200) DEFAULT NULL COMMENT '访客客服关闭会话提示语',
  `system_close_sesson_prompt` varchar(200) DEFAULT NULL COMMENT '系统关闭会话提示语',
  `non_worktime_prompt` varchar(200) DEFAULT NULL COMMENT '非工作时间提示语',
  `sensitive_words_prompt` varchar(200) DEFAULT NULL COMMENT '敏感词提示语',
  `sensitive_words_screening_way` varchar(2) DEFAULT '0' COMMENT '敏感词屏蔽方式 0关键词屏蔽 1整句屏蔽',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='渠道设置';

/*Data for the table `t_callcenter_web_customer_setting` */

/*Table structure for table `t_knowledge` */

DROP TABLE IF EXISTS `t_knowledge`;

CREATE TABLE `t_knowledge` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `knowledge_code` varchar(20) DEFAULT NULL COMMENT '知识key，用来统一不同版本，采用毫秒级时间戳+2位随机数',
  `version_num` varchar(15) DEFAULT NULL COMMENT '版本号',
  `category_id` int(11) NOT NULL COMMENT '知识分类外键',
  `knowledge_title` varchar(50) NOT NULL COMMENT '知识标题',
  `valid_time` datetime NOT NULL COMMENT '有效期',
  `visible_to` varchar(10) DEFAULT NULL COMMENT '可见范围 普通员工，专业用户，呼叫中心 非共享员工，按位与1111',
  `product_id` int(11) NOT NULL COMMENT '产品外键',
  `type_id` int(11) DEFAULT NULL COMMENT '知识类别外键',
  `keyword` varchar(100) NOT NULL COMMENT '关键字用分号分隔',
  `knowledge_desc` varchar(200) DEFAULT NULL COMMENT '简介',
  `knowledge_content` text NOT NULL COMMENT '内容',
  `knowledge_status` varchar(2) DEFAULT NULL COMMENT '状态 0已保存 1 待审核 2 已发布 3已驳回',
  `knowledge_from` varchar(2) DEFAULT NULL COMMENT '来源，0 知识库 1 工单',
  `knowledge_top` varchar(2) DEFAULT NULL COMMENT '置顶标识 0 非置顶 1 置顶',
  `reject_reason` varchar(200) DEFAULT NULL COMMENT '驳回原因',
  `create_by` varchar(60) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(60) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `updator_name` varbinary(10) DEFAULT NULL COMMENT '更新人名',
  `creator_name` varchar(10) DEFAULT NULL COMMENT '创建人名',
  `product_name` varchar(20) DEFAULT NULL COMMENT '产品名',
  `del_flag` varchar(2) DEFAULT '0' COMMENT '删除标识,0 未删除 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='知识表';

/*Data for the table `t_knowledge` */

insert  into `t_knowledge`(`id`,`knowledge_code`,`version_num`,`category_id`,`knowledge_title`,`valid_time`,`visible_to`,`product_id`,`type_id`,`keyword`,`knowledge_desc`,`knowledge_content`,`knowledge_status`,`knowledge_from`,`knowledge_top`,`reject_reason`,`create_by`,`create_time`,`update_by`,`update_time`,`updator_name`,`creator_name`,`product_name`,`del_flag`) values (1,'2222','222',12312,'sdfsdf','2020-03-10 15:38:39',NULL,0,2,'',NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');

/*Table structure for table `t_knowledge_attachment` */

DROP TABLE IF EXISTS `t_knowledge_attachment`;

CREATE TABLE `t_knowledge_attachment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `knowledge_id` int(11) DEFAULT NULL COMMENT '知识id外键',
  `url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件路径',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='知识附件';

/*Data for the table `t_knowledge_attachment` */

/*Table structure for table `t_knowledge_category` */

DROP TABLE IF EXISTS `t_knowledge_category`;

CREATE TABLE `t_knowledge_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(32) NOT NULL COMMENT '分类名',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '上级分类',
  `create_by` varchar(60) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(60) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `creator_name` varchar(10) DEFAULT NULL COMMENT '创建人姓名',
  `updator_name` varchar(10) DEFAULT NULL COMMENT '修改人姓名',
  `del_flag` varchar(2) NOT NULL DEFAULT '0' COMMENT '删除标识,0 未删除 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='知识分类表';

/*Data for the table `t_knowledge_category` */

insert  into `t_knowledge_category`(`id`,`category_name`,`parent_id`,`create_by`,`create_time`,`update_by`,`update_time`,`creator_name`,`updator_name`,`del_flag`) values (1,'知识库',0,NULL,NULL,NULL,NULL,NULL,NULL,'0'),(2,'公积金',1,NULL,NULL,NULL,NULL,NULL,NULL,'0'),(3,'公积金卡办理',2,NULL,NULL,NULL,NULL,NULL,NULL,'0'),(4,'公积金卡办理流程',3,NULL,NULL,NULL,NULL,NULL,NULL,'0'),(5,'新员工办理手续',4,NULL,NULL,NULL,NULL,NULL,NULL,'0'),(6,'社保',1,NULL,NULL,NULL,NULL,NULL,NULL,'0'),(7,'入转离',1,NULL,NULL,NULL,NULL,NULL,NULL,'0'),(8,'薪酬核算',1,NULL,NULL,NULL,NULL,NULL,NULL,'0'),(10,'aaaa',1,'8294141','2020-03-16 11:30:01','8294141','2020-03-16 11:30:01','钱明明','钱明明','0'),(11,'test',1,'8294141','2020-03-16 11:41:01','8294141','2020-03-16 11:41:01','钱明明','钱明明','0'),(12,'test1',1,'8294141','2020-03-16 13:39:44','8294141','2020-03-16 13:39:44','钱明明','钱明明','0'),(13,'test2',1,'8294141','2020-03-16 13:40:51','8294141','2020-03-16 13:40:51','钱明明','钱明明','0'),(14,'test3',1,'8294141','2020-03-16 14:03:43','8294141','2020-03-16 14:03:43','钱明明','钱明明','0'),(15,'shebao1',6,'8294141','2020-03-17 09:24:15','8294141','2020-03-17 09:24:15','钱明明','钱明明','0');

/*Table structure for table `t_knowledge_category_org` */

DROP TABLE IF EXISTS `t_knowledge_category_org`;

CREATE TABLE `t_knowledge_category_org` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category_id` int(11) NOT NULL COMMENT '分类表的Id外键',
  `org_code` varchar(32) NOT NULL COMMENT '单位code',
  `org_name` varchar(30) DEFAULT NULL COMMENT '单位名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COMMENT='知识分类关联的单位权限表';

/*Data for the table `t_knowledge_category_org` */

insert  into `t_knowledge_category_org`(`id`,`category_id`,`org_code`,`org_name`) values (1,4,'集团',NULL),(2,2,'公司',NULL),(3,2,'郑州日产汽车有限公司',NULL),(4,7,'集团',NULL),(5,7,'公司',NULL),(6,6,'菜市场',NULL),(7,6,'火车站',NULL),(8,8,'飞机场',NULL),(9,10,'1218511100','东风商用车有限公司总经理办公室'),(10,10,'1218512500','东风商用车有限公司质量管理部'),(11,11,'1200000000','东风汽车集团股份有限公司'),(12,11,'1218500000','东风商用车有限公司'),(13,12,'1200000000','东风汽车集团股份有限公司'),(14,12,'1201010000','东风汽车集团股份有限公司办公室（党委办公室）'),(15,13,'1200000000','东风汽车集团股份有限公司'),(16,13,'1201010000','东风汽车集团股份有限公司办公室（党委办公室）'),(19,14,'1200000000','东风汽车集团股份有限公司'),(20,14,'1201010000','东风汽车集团股份有限公司办公室（党委办公室）'),(21,14,'1201020000','东风汽车集团股份有限公司战略规划部'),(25,15,'1200000000','东风汽车集团股份有限公司'),(26,15,'1201020000','东风汽车集团股份有限公司战略规划部'),(27,15,'1201030000','东风汽车集团股份有限公司经营管理部'),(28,15,'1201040000','东风汽车集团股份有限公司人事部');

/*Table structure for table `t_knowledge_category_product` */

DROP TABLE IF EXISTS `t_knowledge_category_product`;

CREATE TABLE `t_knowledge_category_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category_id` int(11) NOT NULL COMMENT '分类Id,外键',
  `product_id` varchar(20) NOT NULL COMMENT '关联产品Id,外键',
  `product_name` varchar(20) DEFAULT NULL COMMENT '关联的产品名字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='知识分类关联的产品表';

/*Data for the table `t_knowledge_category_product` */

insert  into `t_knowledge_category_product`(`id`,`category_id`,`product_id`,`product_name`) values (1,2,'1',NULL),(2,2,'3',NULL),(3,3,'1',NULL),(4,10,'2','人事档案'),(5,10,'4','公积金'),(6,11,'1','入转离'),(7,11,'4','公积金'),(8,12,'1','入转离'),(9,12,'2','人事档案'),(10,12,'3','社保'),(11,12,'4','公积金'),(12,12,'5','其他'),(13,13,'1','入转离'),(14,13,'2','人事档案'),(15,13,'3','社保'),(16,13,'4','公积金'),(17,13,'5','其他'),(20,14,'1','入转离'),(21,14,'4','公积金'),(24,15,'1','入转离'),(25,15,'4','公积金'),(26,15,'5','其他');

/*Table structure for table `t_knowledge_employeetype` */

DROP TABLE IF EXISTS `t_knowledge_employeetype`;

CREATE TABLE `t_knowledge_employeetype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `knowledge_id` int(11) NOT NULL COMMENT '知识id外键',
  `employeetype_id` varchar(11) NOT NULL COMMENT '员工类型id',
  `employeetype_name` varchar(20) DEFAULT NULL COMMENT '员工类型名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='知识关联的人员类型';

/*Data for the table `t_knowledge_employeetype` */

/*Table structure for table `t_knowledge_jobposition` */

DROP TABLE IF EXISTS `t_knowledge_jobposition`;

CREATE TABLE `t_knowledge_jobposition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `knowledge_id` int(11) NOT NULL COMMENT '知识Id外键',
  `job_position_code` varchar(32) NOT NULL COMMENT '岗位序列code',
  `job_position_name` varchar(30) DEFAULT NULL COMMENT '岗位序列名字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='知识关联的岗位序列';

/*Data for the table `t_knowledge_jobposition` */

/*Table structure for table `t_knowledge_org` */

DROP TABLE IF EXISTS `t_knowledge_org`;

CREATE TABLE `t_knowledge_org` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `knowledge_id` int(11) NOT NULL COMMENT '知识id外键',
  `org_code` varchar(32) NOT NULL COMMENT '组织代码',
  `org_name` varchar(32) DEFAULT NULL COMMENT '组织名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='知识关联组织表';

/*Data for the table `t_knowledge_org` */

/*Table structure for table `t_knowledge_region` */

DROP TABLE IF EXISTS `t_knowledge_region`;

CREATE TABLE `t_knowledge_region` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `knowledge_id` int(11) NOT NULL COMMENT '知识id',
  `city_code` varchar(20) DEFAULT NULL COMMENT '城市code',
  `city_name` varchar(20) DEFAULT NULL COMMENT '城市名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='知识关联的地域表';

/*Data for the table `t_knowledge_region` */

/*Table structure for table `t_knowledge_type` */

DROP TABLE IF EXISTS `t_knowledge_type`;

CREATE TABLE `t_knowledge_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type_name` varchar(50) NOT NULL COMMENT '知识类别',
  `type_desc` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_by` varchar(60) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(60) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `creator_name` varchar(10) DEFAULT NULL COMMENT '创建人姓名',
  `updator_name` varchar(10) DEFAULT NULL COMMENT '修改人姓名',
  `del_flag` varchar(2) DEFAULT '0' COMMENT '删除标识,0 未删除 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='知识类别表';

/*Data for the table `t_knowledge_type` */

insert  into `t_knowledge_type`(`id`,`type_name`,`type_desc`,`create_by`,`create_time`,`update_by`,`update_time`,`creator_name`,`updator_name`,`del_flag`) values (1,'分类一','分类 1','8294141','2020-03-10 13:31:13','8294141','2020-03-10 13:55:18','钱明明1','钱明明','0'),(2,'分类而','分类2','8294141','2020-03-10 13:32:30',NULL,NULL,'2',NULL,'0'),(4,'分类333341','分类33334','8294141','2020-03-17 13:22:37','8294141','2020-03-11 11:46:01','钱明明','钱明明','0'),(5,'分类5','分类5','8294141','2020-03-17 13:15:37',NULL,NULL,'钱明明',NULL,'0'),(6,'分类6','分类6',NULL,NULL,NULL,NULL,NULL,NULL,'0'),(7,'分类7','分类7',NULL,NULL,NULL,NULL,NULL,NULL,'0'),(8,'q343','423423423','8294141',NULL,'8294141',NULL,'???','???','0'),(9,'sdfsdafsd','sadfdsaf','8294141','2020-03-11 11:24:06','8294141','2020-03-11 11:24:06','钱明明','钱明明','0'),(10,'11111','111112','8294141','2020-03-11 11:28:18','8294141','2020-03-11 11:28:18','钱明明','钱明明','0'),(11,'21312','123213','8294141','2020-03-12 12:50:18','8294141','2020-03-12 12:50:18','钱明明','钱明明','0'),(12,'324324','234324','8294141','2020-03-12 12:51:06','8294141','2020-03-12 12:51:06','钱明明','钱明明','0');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
