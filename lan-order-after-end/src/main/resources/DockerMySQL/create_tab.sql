# 创建数据库架构
create schema lan_order_data;

# 使用架构
use lan_order_data;

# 创建标签表 -- 还未投入使用
create table tb_category
(
    id       int null,
    category int null,
    store_id int null
);

# 创建进货表
create table tb_import_record
(
    id           int auto_increment
        primary key,
    created_date datetime     null,
    updated_date datetime     null,
    store_list   text         null,
    remark       varchar(255) null
);

# 创建优惠码表
create table tb_marketing
(
    id            int auto_increment
        primary key,
    code          varchar(255) not null,
    type_discount double       null,
    out_value     int          null,
    max_value     int          null,
    type          varchar(255) not null
);

# 创建订单表
create table tb_order
(
    id           int auto_increment
        primary key,
    created_date datetime       null,
    updated_date datetime       null,
    store_list   text           null,
    amount       decimal(10, 2) null
);

# 创建商品表
create table tb_store
(
    id           int auto_increment
        primary key,
    name         varchar(255)   null,
    price        decimal(10, 2) null,
    discount     decimal(10, 2) null,
    number       int            null,
    updated_date datetime       null,
    sort         varchar(255)   null,
    img_path         varchar(255)   null,
    created_date datetime       null
);


