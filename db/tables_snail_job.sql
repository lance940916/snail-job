CREATE database if NOT EXISTS `snail_job` default character set utf8mb4 collate utf8mb4_unicode_ci;
USE `snail_job`;

SHOW TABLES;

-- 执行器注册信息
DROP TABLE IF EXISTS `job_executor`;
CREATE TABLE IF NOT EXISTS `job_executor`
(
    `id`            INT(11)     NOT NULL AUTO_INCREMENT,
    `group_name`    VARCHAR(32) NOT NULL COMMENT '要执行的任务组名',
    `address`       VARCHAR(64) NOT NULL COMMENT '执行器地址',
    `registry_type` TINYINT     NOT NULL COMMENT '执行器地址类型：0=自动注册、1=手动录入',
    `update_time`   DATETIME    NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_g_a` (`group_name`, `address`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 执行器组信息
DROP TABLE IF EXISTS `job_group`;
CREATE TABLE IF NOT EXISTS `job_group`
(
    `id`           INT(11)      NOT NULL AUTO_INCREMENT,
    `title`        VARCHAR(32)  NOT NULL COMMENT '组的标题',
    `name`         VARCHAR(32)  NOT NULL COMMENT '任务组名称，唯一不重复',
    `type`         VARCHAR(32)  NOT NULL COMMENT '类型',
    `address_list` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '执行器节点地址列表，多地址用逗号分隔',
    `description`  VARCHAR(128) NOT NULL COMMENT '任务组描述',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 任务信息
DROP TABLE IF EXISTS `job_info`;
CREATE TABLE IF NOT EXISTS `job_info`
(
    `id`                        INT(11)      NOT NULL AUTO_INCREMENT,
    `group_name`                VARCHAR(32)  NOT NULL COMMENT '任务组名称',
    `cron`                      VARCHAR(50)  NOT NULL COMMENT '任务CRON表达式',
    `description`               VARCHAR(255) NOT NULL DEFAULT '' COMMENT '任务描述',
    `create_time`               DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
    `update_time`               DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',

    `author`                    VARCHAR(64)  NULL COMMENT '管理者',
    `alarm_email`               VARCHAR(255) NULL COMMENT '报警邮件',

    `executor_route_strategy`   VARCHAR(50)  NOT NULL COMMENT '执行器路由策略',
    `executor_handler`          VARCHAR(255) NOT NULL COMMENT '执行器任务handler',
    `executor_param`            VARCHAR(512) NULL COMMENT '执行器任务参数',
    `executor_block_strategy`   VARCHAR(50)  NOT NULL COMMENT '阻塞处理策略',
    `executor_timeout`          INT(11)      NOT NULL DEFAULT 0 COMMENT '任务执行超时时间，单位秒',
    `executor_fail_retry_count` TINYINT      NOT NULL DEFAULT 0 COMMENT '失败重试次数',

    `trigger_status`            TINYINT      NOT NULL DEFAULT '0' COMMENT '调度状态：0-停止，1-运行',
    `trigger_last_time`         BIGINT(13)   NOT NULL DEFAULT '0' COMMENT '上次调度时间',
    `trigger_next_time`         BIGINT(13)   NOT NULL DEFAULT '0' COMMENT '下次调度时间',

    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 任务日志
DROP TABLE IF EXISTS `job_log`;
CREATE TABLE IF NOT EXISTS `job_log`
(
    `id`                        BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `job_id`                    INT(11)      NOT NULL COMMENT '任务，主键ID',
    `group_name`                VARCHAR(32)  NOT NULL COMMENT '任务组名',

    `executor_address`          VARCHAR(255) NULL COMMENT '执行器地址，本次执行的地址',
    `executor_handler`          VARCHAR(255) NULL COMMENT '执行器任务handler',
    `executor_param`            VARCHAR(512) NULL COMMENT '执行器任务参数',
    `executor_fail_retry_count` TINYINT      NOT NULL DEFAULT 0 COMMENT '失败重试次数',

    `trigger_time`              DATETIME     NULL COMMENT '调度-时间',
    `trigger_code`              INT(11)      NULL COMMENT '调度-结果',
    `trigger_msg`               TEXT         NULL COMMENT '调度-日志',

    `exec_time`                 DATETIME     NULL COMMENT '执行-时间',
    `exec_code`                 INT(11)      NOT NULL DEFAULT 0 COMMENT '执行-结果',
    `exec_msg`                  TEXT         NULL COMMENT '执行-日志',

    `alarm_status`              TINYINT      NOT NULL DEFAULT '0' COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 首页任务执行报告
DROP TABLE IF EXISTS `job_log_report`;
CREATE TABLE IF NOT EXISTS `job_log_report`
(
    `id`            INT(11)  NOT NULL AUTO_INCREMENT,
    `trigger_time`  DATETIME NOT NULL COMMENT '调度-时间',
    `running_count` INT(11)  NOT NULL DEFAULT 0 COMMENT '运行中-日志数量',
    `success_count` INT(11)  NOT NULL DEFAULT 0 COMMENT '执行成功-日志数量',
    `fail_count`    INT(11)  NOT NULL DEFAULT 0 COMMENT '执行失败-日志数量',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 任务锁
DROP TABLE IF EXISTS `job_lock`;
CREATE TABLE IF NOT EXISTS `job_lock`
(
    `lock_name` VARCHAR(50) NOT NULL COMMENT '锁名称',
    PRIMARY KEY (`lock_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO `job_lock` (`lock_name`)
VALUES ('schedule_lock');

# CREATE TABLE `snail_job_logglue`
# (
#     `id`          int(11)      NOT NULL AUTO_INCREMENT,
#     `job_id`      int(11)      NOT NULL COMMENT '任务，主键ID',
#     `glue_type`   varchar(50) DEFAULT NULL COMMENT 'GLUE类型',
#     `glue_source` mediumtext COMMENT 'GLUE源代码',
#     `glue_remark` varchar(128) NOT NULL COMMENT 'GLUE备注',
#     `add_time`    datetime    DEFAULT NULL,
#     `update_time` datetime    DEFAULT NULL,
#     PRIMARY KEY (`id`)
# ) ENGINE = InnoDB
#   DEFAULT CHARSET = utf8mb4;

-- 用户表
# CREATE TABLE `snail_job_user`
# (
#     `id`         int(11)     NOT NULL AUTO_INCREMENT,
#     `username`   varchar(50) NOT NULL COMMENT '账号',
#     `password`   varchar(50) NOT NULL COMMENT '密码',
#     `role`       tinyint(4)  NOT NULL COMMENT '角色：0-普通用户、1-管理员',
#     `permission` varchar(255) DEFAULT NULL COMMENT '权限：执行器ID列表，多个逗号分割',
#     PRIMARY KEY (`id`),
#     UNIQUE KEY `i_username` (`username`) USING BTREE
# ) ENGINE = InnoDB
#   DEFAULT CHARSET = utf8mb4;

