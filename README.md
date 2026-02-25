# 洗衣店平台后端项目

## 项目简介

这是一个完整的洗衣店管理系统后端，基于Spring Boot 3.2.0开发，提供RESTful API接口。

## 技术栈

- **框架**: Spring Boot 3.2.0
- **数据库**: MySQL 8.0
- **ORM框架**: MyBatis Plus 3.5.5
- **安全**: Spring Security + JWT
- **API文档**: SpringDoc OpenAPI 2.3.0
- **其他**: Lombok、Validation、Fastjson2

## 功能模块

### 1. 洗衣服务管理
- 服务分类管理（干洗/水洗/熨烫/洗护保养/奢侈品护理）
- 服务信息管理（服务类型、价格、预计完成时长）
- 服务状态管理（上架/下架）

### 2. 订单管理
- 用户下单（支持到店自取/上门取送）
- 订单状态自动流转（待支付→待接单→洗护中→待取件→已完成）
- 订单完成前30分钟提醒通知
- 订单取消/退款审核

### 3. 服务记录
- 根据衣物签收和交付自动记录洗护时长
- 5星评价系统（评分+文字内容）
- 电子消费凭证/洗护明细单生成

### 4. 会员与优惠
- 会员等级体系（普通/银卡/金卡）
- 会员积分累计与抵扣
- 优惠券管理（满减/折扣/免运费）
- 储值卡充值与消费

### 5. 门店管理
- 多门店信息维护（地址/营业时间/联系方式）
- 门店库存管理（洗护耗材/包装物料）
- 门店员工账号与权限分配

## 项目结构

```
├── src/main/java/com/washshop/
│   ├── WashShopApplication.java    # 启动类
│   ├── config/                      # 配置类
│   ├── controller/                  # 控制器层
│   ├── dto/                         # 数据传输对象
│   ├── entity/                      # 实体类
│   ├── enums/                       # 枚举类
│   ├── handler/                     # 处理器
│   ├── mapper/                      # 数据访问层
│   ├── service/                     # 业务逻辑层
│   ├── task/                        # 定时任务
│   └── vo/                          # 视图对象
├── src/main/resources/
│   ├── application.yml              # 配置文件
│   ├── db/schema.sql                # 数据库脚本
│   └── mapper/                      # MyBatis映射文件
└── pom.xml                          # Maven配置
```

## 快速开始

### 1. 数据库配置

1. 创建MySQL数据库：
```sql
CREATE DATABASE washshop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行初始化脚本：
```bash
mysql -u root -p washshop < src/main/resources/db/schema.sql
```

3. 修改数据库连接配置（application.yml）：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/washshop?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 2. 运行项目

```bash
mvn spring-boot:run
```

或

```bash
mvn clean package
java -jar target/washshop-platform-1.0.0.jar
```

### 3. 访问API文档

启动后访问：http://localhost:8080/api/swagger-ui.html

## API接口概览

### 服务分类管理
- POST /api/service-category - 创建分类
- PUT /api/service-category/{id} - 更新分类
- DELETE /api/service-category/{id} - 删除分类
- GET /api/service-category/list - 获取所有分类
- GET /api/service-category/active - 获取启用的分类

### 洗衣服务管理
- POST /api/wash-service - 创建服务
- PUT /api/wash-service/{id} - 更新服务
- DELETE /api/wash-service/{id} - 删除服务
- GET /api/wash-service/page - 分页查询服务
- GET /api/wash-service/active - 获取可用服务

### 订单管理
- POST /api/order - 创建订单
- POST /api/order/{orderId}/pay - 支付订单
- POST /api/order/{orderId}/accept - 接单
- POST /api/order/{orderId}/start-wash - 开始洗护
- POST /api/order/{orderId}/finish-wash - 完成洗护
- POST /api/order/{orderId}/ready - 待取件
- POST /api/order/{orderId}/complete - 完成订单
- POST /api/order/{orderId}/cancel - 取消订单
- GET /api/order/my-orders - 获取我的订单

### 会员管理
- GET /api/member/info - 获取会员信息
- GET /api/member/points-records - 获取积分记录

### 优惠券管理
- POST /api/coupon - 创建优惠券
- GET /api/coupon/active - 获取可用优惠券
- POST /api/coupon/{couponId}/receive - 领取优惠券
- GET /api/coupon/my-coupons - 获取我的优惠券

### 门店管理
- POST /api/store - 创建门店
- GET /api/store/active - 获取营业门店
- GET /api/store/page - 分页查询门店

### 库存管理
- POST /api/store-inventory - 创建库存
- GET /api/store-inventory/store/{storeId} - 获取门店库存
- GET /api/store-inventory/low-stock/{storeId} - 获取低库存物品
- POST /api/store-inventory/{id}/add-stock - 增加库存
- POST /api/store-inventory/{id}/deduct-stock - 减少库存

## 订单状态流转

```
待支付 → 待接单 → 洗护中 → 待取件 → 已完成
   ↓         ↓        ↓
已取消    退款中 → 已退款
```

## 会员等级规则

| 等级 | 名称 | 所需积分 |
|------|------|----------|
| 0    | 普通会员 | 0 |
| 1    | 银卡会员 | 1000 |
| 2    | 金卡会员 | 5000 |

## 开发团队

- 技术支持：support@washshop.com

## 许可证

Apache License 2.0
