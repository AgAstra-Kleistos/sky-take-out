# 苍穹外卖 - 餐饮管理平台

## 项目简介
面向餐饮企业的O2O订单管理系统，包含管理端和客户端。管理端负责员工、菜品、分类、套餐、订单管理；客户端支持微信登录、商品浏览、购物车、下单、订单查询等功能。

## 技术栈
- **后端框架**：Spring Boot、Spring MVC、MyBatis/MyBatis-Plus
- **数据库**：MySQL、Redis
- **工具**：Maven、Git、Apifox
- **其他**：JWT、Spring Task

## 快速启动
1. 克隆项目：`git clone https://github.com/AgAstra-Kleistos/sky-take-out.git`
2. 导入MySQL数据库脚本（项目根目录下 `/db` 文件夹）
3. 修改 `application-dev.yml` 中的数据库、Redis、微信配置
4. 启动 `SkyApplication` 主类
5. 访问接口文档：`http://localhost:8080/doc.html`

## 主要功能
### 管理端
- 员工登录与账号管理
- 菜品、分类、套餐管理
- 订单管理与状态跟踪
- 数据统计（图形报表、Excel导出）

### 客户端
- 微信登录
- 商品浏览（Redis缓存优化）
- 购物车管理
- 用户下单与订单查询

## 项目亮点
- 使用Redis缓存高频访问的菜品数据，提升查询效率
- 基于JWT实现身份认证与接口拦截
- 使用Spring Task定时处理超时订单
- 对接微信支付与登录接口
- 提交记录按模块划分，清晰可追溯
