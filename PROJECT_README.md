# 商店平台后端项目

基于 Spring Boot 3.2.0 + Java 21 构建的商店平台后端系统。

## 技术栈

- **框架**: Spring Boot 3.2.0
- **语言**: Java 21
- **数据库**: MySQL 8.0
- **ORM**: Spring Data JPA
- **缓存**: Redis
- **文档**: SpringDoc OpenAPI (Swagger)
- **构建工具**: Maven

## 功能模块

### 1. 商品管理
- 商品CRUD操作（名称、价格、库存、规格）
- 商品分类管理（食品/服饰/家居/数码/美妆）
- 商品状态管理（在售/下架/缺货/预售）
- 商品规格管理
- 商品图片管理

**接口路径**: `/api/products`

### 2. 订单管理
- 顾客下单（支持线上支付/到店付款）
- 订单状态自动流转（待支付→待备货→待取货/待发货→已完成/已取消）
- 订单发货/备货完成后15分钟提醒通知
- 订单取消/退款审核
- 自动取消超时未支付订单（30分钟）

**接口路径**: `/api/orders`

### 3. 消费记录
- 根据下单和完成交易自动记录消费金额/积分
- 支持顾客对购买商品进行5星评价+文字+图片
- 为顾客生成电子消费凭证/购物小票
- 凭证二维码验证

**接口路径**: `/api/consumption`

### 4. 店铺互动
- 顾客发布商品使用体验分享
- 商品实拍照片墙（需审核）
- 店铺优惠券管理（固定金额/百分比折扣/免邮）
- 活动公告展示
- 顾客领取优惠券

**接口路径**: `/api/interaction`

### 5. 会员管理
- 会员等级体系（普通/银卡/金卡）
- 消费积分累计与抵扣（1积分=0.01元）
- 会员专属折扣/赠品
- 储值卡充值与消费
- 积分交易记录查询
- 储值交易记录查询

**接口路径**: `/api/members`

## 项目结构

```
src/main/java/org/example/
├── Application.java                 # 启动类
├── common/                          # 公共类
│   ├── BaseEntity.java             # 实体基类
│   ├── PageResult.java             # 分页结果
│   └── Result.java                 # 统一响应结果
├── config/                          # 配置类
│   ├── JpaConfig.java              # JPA配置
│   ├── OpenApiConfig.java          # Swagger配置
│   ├── RedisConfig.java            # Redis配置
│   └── SchedulingConfig.java       # 定时任务配置
├── controller/                      # 控制器层
│   ├── ConsumptionController.java  # 消费记录控制器
│   ├── MemberController.java       # 会员管理控制器
│   ├── OrderController.java        # 订单管理控制器
│   ├── ProductController.java      # 商品管理控制器
│   └── ShopInteractionController.java # 店铺互动控制器
├── dto/                             # 数据传输对象
│   ├── AnnouncementDTO.java
│   ├── ConsumptionRecordDTO.java
│   ├── CouponDTO.java
│   ├── CreateOrderDTO.java
│   ├── CustomerCouponDTO.java
│   ├── ExperienceShareDTO.java
│   ├── MemberDTO.java
│   ├── MemberGiftDTO.java
│   ├── OrderDTO.java
│   ├── OrderItemDTO.java
│   ├── OrderQueryDTO.java
│   ├── PhotoWallDTO.java
│   ├── PointsTransactionDTO.java
│   ├── ProductDTO.java
│   ├── ProductQueryDTO.java
│   ├── ProductSpecificationDTO.java
│   ├── ReceiptDTO.java
│   ├── RechargeDTO.java
│   ├── ReviewDTO.java
│   └── StoredValueTransactionDTO.java
├── entity/                          # 实体类
│   ├── Announcement.java
│   ├── ConsumptionRecord.java
│   ├── Coupon.java
│   ├── CustomerCoupon.java
│   ├── ExperienceShare.java
│   ├── Member.java
│   ├── MemberGift.java
│   ├── Order.java
│   ├── OrderItem.java
│   ├── PhotoWall.java
│   ├── PointsTransaction.java
│   ├── Product.java
│   ├── ProductSpecification.java
│   ├── Receipt.java
│   ├── Review.java
│   └── StoredValueTransaction.java
├── enums/                           # 枚举类
│   ├── MemberLevel.java
│   ├── OrderStatus.java
│   ├── PaymentMethod.java
│   ├── PickupType.java
│   ├── ProductCategory.java
│   └── ProductStatus.java
├── repository/                      # 数据访问层
│   ├── AnnouncementRepository.java
│   ├── ConsumptionRecordRepository.java
│   ├── CouponRepository.java
│   ├── CustomerCouponRepository.java
│   ├── ExperienceShareRepository.java
│   ├── MemberGiftRepository.java
│   ├── MemberRepository.java
│   ├── OrderItemRepository.java
│   ├── OrderRepository.java
│   ├── PhotoWallRepository.java
│   ├── PointsTransactionRepository.java
│   ├── ProductRepository.java
│   ├── ProductSpecificationRepository.java
│   ├── ReceiptRepository.java
│   ├── ReviewRepository.java
│   └── StoredValueTransactionRepository.java
├── service/                         # 服务接口层
│   ├── ConsumptionService.java
│   ├── MemberService.java
│   ├── OrderService.java
│   ├── ProductService.java
│   └── ShopInteractionService.java
├── service/impl/                    # 服务实现层
│   ├── ConsumptionServiceImpl.java
│   ├── MemberServiceImpl.java
│   ├── OrderNotificationService.java
│   ├── OrderServiceImpl.java
│   ├── ProductServiceImpl.java
│   └── ShopInteractionServiceImpl.java
└── util/                            # 工具类
    └── RedisUtil.java
```

## 数据库表结构

### 核心表
- `products` - 商品表
- `product_specifications` - 商品规格表
- `orders` - 订单表
- `order_items` - 订单商品项表
- `members` - 会员表
- `consumption_records` - 消费记录表
- `reviews` - 评价表
- `receipts` - 电子凭证表

### 互动表
- `experience_shares` - 体验分享表
- `photo_wall` - 照片墙表
- `coupons` - 优惠券表
- `customer_coupons` - 顾客优惠券表
- `announcements` - 公告表

### 会员相关表
- `stored_value_transactions` - 储值交易记录表
- `points_transactions` - 积分交易记录表
- `member_gifts` - 会员赠品表

## 启动项目

### 前置条件
1. 安装 MySQL 8.0 并创建数据库 `shop_platform`
2. 安装 Redis
3. 安装 Maven

### 启动步骤

```bash
# 1. 编译项目
mvn clean compile

# 2. 运行项目
mvn spring-boot:run
```

### 访问接口文档

启动成功后，访问 Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

## API 接口概览

| 模块 | 基础路径 | 功能 |
|------|----------|------|
| 商品管理 | `/api/products` | 商品CRUD、分类、状态管理 |
| 订单管理 | `/api/orders` | 下单、支付、状态流转、退款 |
| 消费记录 | `/api/consumption` | 消费记录、评价、电子凭证 |
| 店铺互动 | `/api/interaction` | 体验分享、照片墙、优惠券、公告 |
| 会员管理 | `/api/members` | 会员等级、积分、储值卡 |

## 特色功能

1. **订单状态自动流转**: 根据支付方式和操作自动推进订单状态
2. **定时任务**: 
   - 备货完成后15分钟自动发送提醒通知
   - 30分钟未支付订单自动取消
3. **会员等级自动升级**: 根据累计消费自动计算会员等级
4. **积分抵扣**: 支持积分抵扣订单金额（1积分=0.01元）
5. **储值卡**: 支持充值赠送、消费扣款
6. **电子凭证**: 生成带二维码和验证码的电子消费凭证
