# 宠物店管理系统

SpringBoot + Vue 前后端分离项目，为宠物店提供会员、宠物档案、商品与库存、订单、充值、疫苗与医疗、索赔、财务报表、会员生命周期等一体化管理能力。

---

## 技术栈

| 分类     | 技术/版本                    |
|----------|------------------------------|
| 后端     | Spring Boot 2.7、MyBatis-Plus、JWT、Fastjson |
| 前端     | Vue 2、Element-UI、Axios、ECharts |
| 数据库   | MySQL 8.0                    |
| 运行环境 | JDK 1.8、Maven、Node.js      |

---

## 目录说明

| 路径 | 说明 |
|------|------|
| `springboot-base/` | 后端工程，端口 **8011**，统一前缀 `/api` |
| `vue-frontend/` | 前端工程，开发端口以 `npm run serve` 输出为准（常见 8080） |
| `pet_manager.sql` | 数据库初始化脚本（建库、建表、基础数据） |
| `migrate_*.sql` | 增量迁移脚本（逻辑删除、会员余额、会员生命周期等） |
| `seed_test_data_one_month.sql` | 约 1 个月业务测试数据（需在 UTF-8 下执行） |
| `truncate_before_seed.sql` | 清空业务表，便于重新导入测试数据 |
| `run_seed_utf8.bat` | Windows 下按 UTF-8 执行「清空 + 完整测试数据」导入 |
| `run_minimal_seed_utf8.bat` | Windows 下按 UTF-8 执行「清空 + 最少测试数据」导入 |
| `宠物店管理系统-详细功能说明文档.md` | 功能与接口详细说明 |

---

## 快速启动

### 1. 数据库

- 安装 MySQL 8.0，创建数据库（或执行脚本时自动创建）：
  ```bash
  mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS pet_manager DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
  ```
- 执行初始化脚本：
  ```bash
  mysql -u root -p pet_manager < pet_manager.sql
  ```
- 按需执行迁移脚本（顺序建议）：
  - `migrate_logic_delete_and_balance.sql`（逻辑删除、会员余额）
  - `migrate_member_lifecycle.sql`（会员生命周期：最后消费时间、等级规则、流失/沉睡）
  - 其他 `migrate_*.sql`、`add_*.sql` 按项目需要执行

数据库账号需与后端配置一致，默认在 `application.yml` 中为 `root` / `root`。

### 2. 后端

```bash
cd springboot-base
mvn spring-boot:run
```

或运行主类 `com.jsonll.base.JsonBaseApplication`。  
接口基地址：`http://localhost:8011/api`。

### 3. 前端

```bash
cd vue-frontend
npm install
npm run serve
```

浏览器访问终端提示的地址（如 `http://localhost:8080`）。  
前端通过代理或配置 baseURL 将 `/api` 请求转发到后端 8011。

### 4. 登录

- 默认账号：**admin** / **admin**
- 仅支持管理员登录，所有功能在登录后使用。

---

## 测试数据（UTF-8 中文）

导入脚本内含中文，需在 **UTF-8** 下执行，否则库中会显示为问号。

- **推荐（Windows）**：双击 `run_seed_utf8.bat`，或在 cmd 中执行：
  ```cmd
  mysql -u root -proot --default-character-set=utf8mb4 pet_manager < truncate_before_seed.sql
  mysql -u root -proot --default-character-set=utf8mb4 pet_manager < seed_test_data_one_month.sql
  ```
- 仅导入最少数据（2 个会员、2 个商品等）：执行 `clear_all_and_minimal_seed.sql`，同样需带 `--default-character-set=utf8mb4`。
- 不要使用 PowerShell 的 `Get-Content ... | mysql`，易导致编码错误。

---

## 功能模块概览

- **首页**：统计卡片、快捷入口  
- **会员中心**：会员管理、会员生命周期、充值流水、卡类型、会员卡、卡交易流水  
- **宠物服务**：宠物类型、宠物档案、疫苗记录、医疗记录  
- **商品与采购**：商品管理、供应商、采购管理  
- **库存管理**：库存管理、库存预警、库存盘点、库存流水  
- **销售与财务**：订单管理、索赔记录、财务报表  
- **系统设置**：数据字典、管理员管理  

详见 **《宠物店管理系统-详细功能说明文档.md》**。

---

## 其他说明

- **移动端**：宽度 &lt; 768px 时，列表操作列收窄为「操作」下拉，点击后选择详情/编辑/删除等。
- **中文编码**：后端已配置 UTF-8 请求/响应；数据库连接使用 `characterEncoding=UTF-8`；库与表建议 utf8mb4。
- **版本管理**：项目根目录已提供 `.gitignore`（Java/Maven、Node、IDE、日志、本地配置等）。
