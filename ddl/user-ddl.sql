
-- 用户信息表
create table user_basic_info(
user_id int primary key auto_increment comment '用户ID,自增长',
login_name varchar(50) primary key comment '登录名,PK',
password varchar(100) not null comment '登录密码(使用加密方式)',
user_name varchar(50) not null comment '用户名',
user_email varchar(80) not null default '' comment '邮箱信息',
phone char(11) DEFAULT '' comment '手机号码',
avatar varchar(200) DEFAULT 'http://localhost/static/image/avatar' COMMENT '头像地址,提供一个默认头像地址',
status tinyint not null default 1 comment '用户状态:0、删除 1、正常 2、禁用',
remark varchar(500) not null default '' comment '用户备注',
create_time datetime not null default current_timestamp comment '创建时间',
update_time datatime not null default current_timestamp on update current_timestamp comment '更新时间'
)ENGINE InnoDB default charset=utf8mb4 comment '用户信息表'