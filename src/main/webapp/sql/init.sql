create table `t_user` (
  `id` bigint(20) unsigned not null default '0',
  `age` bigint(20) unsigned default null comment '用户id',
  `name` varchar(30) default null comment '用户登录帐号',
  `create_time` datetime not null default current_timestamp comment '创建时间',
  primary key (`id`),
  key `idx_user_userid` (`user_id`)
) engine=innodb default charset=utf8 comment='用户名';