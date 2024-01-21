# 创建数据库架构
create schema lan_order_data;

# 使用架构
use lan_order_data;

create table tb_category
(
    id       int null,
    category int null,
    store_id int null
);

create table tb_import_record
(
    id           int auto_increment
        primary key,
    created_date datetime     null,
    updated_date datetime     null,
    store_list   text         null,
    remark       varchar(255) null
);

create table tb_marketing
(
    id            int auto_increment
        primary key,
    code          varchar(255) not null,
    type_discount double       null,
    out_value     int          null,
    max_value     int          null,
    type          varchar(255) not null,
    url           varchar(255) null,
    thumbnail_url varchar(255) null,
    img_key       varchar(255) null
);

create table tb_order
(
    id           int auto_increment
        primary key,
    created_date datetime       null,
    updated_date datetime       null,
    store_list   text           null,
    amount       decimal(10, 2) null,
    tab_num      int            not null,
    code         varchar(255)   null
);

create index tb_order_tab_num_index
    on tb_order (tab_num);

create table tb_store
(
    id            int auto_increment
        primary key,
    name          varchar(255)   null,
    price         decimal(10, 2) null,
    discount      decimal(10, 2) null,
    number        int            null,
    updated_date  datetime       null,
    sort          varchar(255)   null,
    url           varchar(255)   null,
    created_date  datetime       null,
    thumbnail_url varchar(255)   null,
    img_key       varchar(255)   null
);

create table tb_tabs
(
    id            int auto_increment
        primary key,
    tab_num       int          null,
    img_key       varchar(255) null,
    url           varchar(255) null,
    thumbnail_url varchar(255) null
);