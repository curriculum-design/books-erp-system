# 图书馆进销存系统

一、基础信息管理

1. 图书类别
2. 出版社
3. 图书
4. 仓库信息 （无货架等，单纯仓库, 无库存调拨）
5. 供应商信息
6. 客户信息

二、进货管理（采购管理）
1. 进货订单
2. 入库订单

三、销售管理
1. 销售订单
2. 出库订单

四、报表管理
1. 进销存总表
2. 库存总表


三、 数据库

> 课设对数据库的强约束

1. 创建存储过程查询某段时间内各种图书的进货和销售情况
2. 创建视图查询各类图书的库存总数
3. 创建触发器当图书入库是自动修改相应图书的总数和存放仓库中的该图书的数量
4. 要求一单可以处理多种图书（比如销售设置销售单及其明细二张表）；

> 表结构设计

1. 图书类型表 base_book_type
```sql
id primary key autoincrement id 
name 类型名称
create_user 创建人
create_update 创建时间
```
2. 出版社表 base_publisher
```sql
id primary key autoincrement id 
name 类型名称
contact_name 联系人名称
contact_mobile 联系人手机号
create_user 创建人
create_update 创建时间
```
3. 图书表 base_book
```sql
id primary key autoincrement id 
code 图书编码
name 图书名称
type_id foreign key 图书类型ID
publisher_id foreign key 出版社ID
create_user 创建人
create_update 创建时间
```
4. 仓库信息表 base_storage
```sql
id primary key autoincrement id 
name 仓库名称
create_user 创建人
create_update 创建时间
```
5. 进货订单 erp_purchase, erp_purchase_item
```sql
table erp_purchase 进货单
id primary key autoincrement id 
supplier_id foreign key 供应商ID
operate_time 进货时间
status 状态（0: 新增  1: 到货）
create_user 创建人
create_update 创建时间

table erp_purchase_item 进货单明细
id primary key autoincrement id 
order_id foreign key 进货单id
book_id 图书id
quantity 数量
```
6. 入库订单 erp_godown_entry, erp_godown_entry_item
```sql
table erp_godown_entry 入库订单
id primary key autoincrement id 
supplier_id foreign key 供应商ID
operate_time 入库时间
status 状态（0: 新增  1: 到货）
create_user 创建人
create_update 创建时间

table erp_godown_entry_item 入库单明细
id primary key autoincrement id 
order_id foreign key 入库单IDi
book_id 图书id
quantity 数量
```
7. 出库订单 erp_outbound, erp_outbound_item
```sql
table erp_outbound 出库订单
id primary key autoincrement id 
supplier_id foreign key 供应商ID
operate_time 出库时间
status 状态（0: 新增  1: 到货）
create_user 创建人
create_update 创建时间

table erp_outbound_item 出库单明细
id primary key autoincrement id 
order_id foreign key 出库单ID
book_id 图书id
quantity 数量
```
8. 图书订单表 erp_book_order
```sql
table erp_book_order 图书订单表
id primary key autoincrement id 
supplier_id foreign key 供应商ID
purchase_time 进货时间
godown_entry_time 入库时间
outbound_time 出库时间
status 状态（10: 进货  20: 入库   30: 出库）
create_user 创建人
create_update 创建时间

```
9. 供应商信息
```sql
id primary key autoincrement id 
name 供应商名称
contact_name 联系人名称
contact_mobile 联系人手机号
create_user 创建人
create_update 创建时间
```
10. 客户信息
```sql
id primary key autoincrement id 
name 客户名称
contact_name 联系人名称
contact_mobile 联系人手机号
create_user 创建人
create_update 创建时间
```
>  数据库相关表之间的完整性约束
