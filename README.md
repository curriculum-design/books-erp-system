# 图书馆进销存系统

一、基础信息管理

1. 图书类别
2. 出版社
3. 图书
4. 仓库信息

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
id primary key autoincrement 类型id 
name 类型名称
create_user 创建人
create_update 创建时间
```
2. 出版社表 base_publisher

3. 图书表 base_book
4. 仓库信息表 base_storage
5. 进货订单 erp_purchase, erp_purchase_item
6. 入库订单 erp_godown_entry, erp_godown_entry_item
7. 出库订单 erp_outbound, erp_outbound_item
8. 图书订单表 erp_book_order

>  数据库相关表之间的完整性约束
