package com.jsonll.base.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 启动时自动创建缺失表、补全缺失列，保证前端所有接口所需表结构存在。
 * 覆盖：member_level_rule 等迁移表；各迁移脚本中的 ADD COLUMN。
 */
@Component
@Order(1)
public class StartupDbMigration implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupDbMigration.class);

    /** 需要 deleted 列的表（与实体类一致） */
    private static final String[] TABLES_WITH_DELETED = {
            "sys_admin", "member", "goods", "pet_type", "pet",
            "order_main", "order_item", "card_type", "member_card", "card_transaction",
            "purchase_record", "recharge_record", "claim_record", "vaccine_record", "medical_record",
            "inventory_flow", "sys_dict_type", "sys_dict_item", "supplier"
    };

    private final DataSource dataSource;

    public StartupDbMigration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) {
        try (Connection conn = dataSource.getConnection()) {
            String catalog = conn.getCatalog();
            if (catalog == null || catalog.isEmpty()) {
                return;
            }
            List<String> done = new ArrayList<>();

            // ---------- 1. 缺失表自动创建（CREATE TABLE IF NOT EXISTS） ----------
            createTableIfNotExists(conn, catalog, "member_level_rule",
                    "CREATE TABLE IF NOT EXISTS member_level_rule (" +
                            "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                            "level_name VARCHAR(50) NOT NULL COMMENT '等级名称'," +
                            "min_amount DECIMAL(12,2) NOT NULL DEFAULT 0," +
                            "max_amount DECIMAL(12,2) NOT NULL," +
                            "sort_order INT DEFAULT 0," +
                            "status INT DEFAULT 1," +
                            "create_time DATETIME DEFAULT CURRENT_TIMESTAMP," +
                            "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                            ") COMMENT '会员等级规则表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");
            createTableIfNotExists(conn, catalog, "member_lifecycle_config",
                    "CREATE TABLE IF NOT EXISTS member_lifecycle_config (" +
                            "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                            "config_key VARCHAR(50) NOT NULL UNIQUE," +
                            "config_value VARCHAR(200) NOT NULL," +
                            "remark VARCHAR(200)," +
                            "create_time DATETIME DEFAULT CURRENT_TIMESTAMP," +
                            "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                            ") COMMENT '会员生命周期配置' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");
            createTableIfNotExists(conn, catalog, "member_lifecycle_warning",
                    "CREATE TABLE IF NOT EXISTS member_lifecycle_warning (" +
                            "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                            "member_id BIGINT NOT NULL," +
                            "member_name VARCHAR(50)," +
                            "phone VARCHAR(20)," +
                            "warning_type VARCHAR(20) NOT NULL," +
                            "last_consume_time DATETIME," +
                            "register_days INT," +
                            "create_time DATETIME DEFAULT CURRENT_TIMESTAMP" +
                            ") COMMENT '会员生命周期预警' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");
            createTableIfNotExists(conn, catalog, "card_type",
                    "CREATE TABLE IF NOT EXISTS card_type (" +
                            "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                            "type_name VARCHAR(100) NOT NULL," +
                            "card_kind TINYINT NOT NULL DEFAULT 1," +
                            "face_value DECIMAL(12,2) DEFAULT 0," +
                            "price DECIMAL(12,2) NOT NULL," +
                            "valid_days INT DEFAULT 365," +
                            "total_times INT DEFAULT 0," +
                            "gift_rule VARCHAR(200)," +
                            "max_sub_cards INT DEFAULT 0," +
                            "sort INT DEFAULT 0," +
                            "status INT DEFAULT 1," +
                            "deleted TINYINT DEFAULT 0," +
                            "create_time DATETIME DEFAULT CURRENT_TIMESTAMP," +
                            "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                            ") COMMENT '卡类型表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");
            createTableIfNotExists(conn, catalog, "member_card",
                    "CREATE TABLE IF NOT EXISTS member_card (" +
                            "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                            "card_no VARCHAR(50) NOT NULL," +
                            "member_id BIGINT NOT NULL," +
                            "member_name VARCHAR(50)," +
                            "card_type_id BIGINT NOT NULL," +
                            "card_type_name VARCHAR(100)," +
                            "card_kind TINYINT NOT NULL DEFAULT 1," +
                            "balance DECIMAL(12,2) DEFAULT 0," +
                            "total_times INT DEFAULT 0," +
                            "remain_times INT DEFAULT 0," +
                            "expire_time DATETIME," +
                            "main_card_id BIGINT DEFAULT NULL," +
                            "status INT DEFAULT 1," +
                            "deleted TINYINT DEFAULT 0," +
                            "create_time DATETIME DEFAULT CURRENT_TIMESTAMP," +
                            "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                            ") COMMENT '会员卡表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");
            createTableIfNotExists(conn, catalog, "card_transaction",
                    "CREATE TABLE IF NOT EXISTS card_transaction (" +
                            "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                            "card_id BIGINT NOT NULL," +
                            "member_id BIGINT," +
                            "member_name VARCHAR(50)," +
                            "trans_type VARCHAR(20) NOT NULL," +
                            "amount DECIMAL(12,2) DEFAULT 0," +
                            "times_before INT DEFAULT 0," +
                            "times_change INT DEFAULT 0," +
                            "times_after INT DEFAULT 0," +
                            "balance_before DECIMAL(12,2) DEFAULT 0," +
                            "balance_after DECIMAL(12,2) DEFAULT 0," +
                            "remark VARCHAR(500)," +
                            "biz_order_no VARCHAR(50)," +
                            "deleted TINYINT DEFAULT 0," +
                            "create_time DATETIME DEFAULT CURRENT_TIMESTAMP" +
                            ") COMMENT '卡交易流水' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");
            createTableIfNotExists(conn, catalog, "stock_check_main",
                    "CREATE TABLE IF NOT EXISTS stock_check_main (" +
                            "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                            "check_no VARCHAR(50) NOT NULL," +
                            "check_date DATE," +
                            "status VARCHAR(20) DEFAULT '草稿'," +
                            "remark VARCHAR(500)," +
                            "create_time DATETIME DEFAULT CURRENT_TIMESTAMP," +
                            "confirm_time DATETIME" +
                            ") COMMENT '盘点单主表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");
            createTableIfNotExists(conn, catalog, "stock_check_item",
                    "CREATE TABLE IF NOT EXISTS stock_check_item (" +
                            "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                            "check_id BIGINT NOT NULL," +
                            "goods_id BIGINT NOT NULL," +
                            "goods_name VARCHAR(100)," +
                            "goods_code VARCHAR(50)," +
                            "book_quantity INT DEFAULT 0," +
                            "actual_quantity INT DEFAULT NULL," +
                            "diff_quantity INT DEFAULT NULL," +
                            "remark VARCHAR(200)," +
                            "create_time DATETIME DEFAULT CURRENT_TIMESTAMP" +
                            ") COMMENT '盘点明细表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");
            createTableIfNotExists(conn, catalog, "supplier",
                    "CREATE TABLE IF NOT EXISTS supplier (" +
                            "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                            "name VARCHAR(100) NOT NULL COMMENT '供应商名称'," +
                            "contact VARCHAR(50) COMMENT '联系人'," +
                            "phone VARCHAR(30) COMMENT '联系电话'," +
                            "address VARCHAR(200) COMMENT '地址'," +
                            "remark VARCHAR(500) COMMENT '备注'," +
                            "status INT DEFAULT 1 COMMENT '0禁用 1启用'," +
                            "deleted TINYINT DEFAULT 0," +
                            "create_time DATETIME DEFAULT CURRENT_TIMESTAMP," +
                            "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                            ") COMMENT '供应商表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");

            // 初始数据：仅当表为空时插入
            insertMemberLevelRuleIfEmpty(conn);
            insertMemberLifecycleConfigIfEmpty(conn);

            // ---------- 2. 缺失列补全（仅当表存在时） ----------
            // recharge_record 支付方式（仅当表存在时）
            if (hasTable(conn, catalog, "recharge_record") && !hasColumn(conn, catalog, "recharge_record", "pay_method")) {
                execute(conn, "ALTER TABLE recharge_record ADD COLUMN pay_method VARCHAR(50) DEFAULT '现金' COMMENT '支付方式' AFTER pay_amount");
                done.add("recharge_record.pay_method");
            }

            // 各表 deleted 列（仅当表存在时）
            for (String table : TABLES_WITH_DELETED) {
                if (hasTable(conn, catalog, table) && !hasColumn(conn, catalog, table, "deleted")) {
                    execute(conn, "ALTER TABLE " + table + " ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0否1是'");
                    done.add(table + ".deleted");
                }
            }

            // member：余额、最后消费时间、标签（仅当表存在时）
            if (hasTable(conn, catalog, "member")) {
                if (!hasColumn(conn, catalog, "member", "balance")) {
                    execute(conn, "ALTER TABLE member ADD COLUMN balance DECIMAL(12,2) DEFAULT 0 COMMENT '当前余额'");
                    done.add("member.balance");
                }
                if (!hasColumn(conn, catalog, "member", "last_consume_time")) {
                    execute(conn, "ALTER TABLE member ADD COLUMN last_consume_time DATETIME NULL COMMENT '最后消费时间'");
                    done.add("member.last_consume_time");
                }
                if (!hasColumn(conn, catalog, "member", "tags")) {
                    execute(conn, "ALTER TABLE member ADD COLUMN tags VARCHAR(500) NULL COMMENT '标签JSON数组'");
                    done.add("member.tags");
                }
            }

            // order_main 支付方式
            if (hasTable(conn, catalog, "order_main") && !hasColumn(conn, catalog, "order_main", "pay_method")) {
                execute(conn, "ALTER TABLE order_main ADD COLUMN pay_method VARCHAR(50) DEFAULT '现金' COMMENT '支付方式'");
                done.add("order_main.pay_method");
            }

            // order_item 单位成本
            if (hasTable(conn, catalog, "order_item") && !hasColumn(conn, catalog, "order_item", "cost")) {
                execute(conn, "ALTER TABLE order_item ADD COLUMN cost DECIMAL(12,2) DEFAULT 0 COMMENT '单位成本'");
                done.add("order_item.cost");
            }

            // goods 成本价、库存上下限
            if (hasTable(conn, catalog, "goods")) {
                if (!hasColumn(conn, catalog, "goods", "cost_price")) {
                    execute(conn, "ALTER TABLE goods ADD COLUMN cost_price DECIMAL(12,2) DEFAULT 0 COMMENT '成本价'");
                    done.add("goods.cost_price");
                }
                if (!hasColumn(conn, catalog, "goods", "stock_lower")) {
                    execute(conn, "ALTER TABLE goods ADD COLUMN stock_lower INT DEFAULT NULL COMMENT '库存下限'");
                    done.add("goods.stock_lower");
                }
                if (!hasColumn(conn, catalog, "goods", "stock_upper")) {
                    execute(conn, "ALTER TABLE goods ADD COLUMN stock_upper INT DEFAULT NULL COMMENT '库存上限'");
                    done.add("goods.stock_upper");
                }
            }

            // purchase_record 批次与保质期
            if (hasTable(conn, catalog, "purchase_record")) {
                if (!hasColumn(conn, catalog, "purchase_record", "batch_no")) {
                    execute(conn, "ALTER TABLE purchase_record ADD COLUMN batch_no VARCHAR(50) DEFAULT NULL COMMENT '批次号'");
                    done.add("purchase_record.batch_no");
                }
                if (!hasColumn(conn, catalog, "purchase_record", "production_date")) {
                    execute(conn, "ALTER TABLE purchase_record ADD COLUMN production_date DATE DEFAULT NULL COMMENT '生产日期'");
                    done.add("purchase_record.production_date");
                }
                if (!hasColumn(conn, catalog, "purchase_record", "expiry_date")) {
                    execute(conn, "ALTER TABLE purchase_record ADD COLUMN expiry_date DATE DEFAULT NULL COMMENT '到期日'");
                    done.add("purchase_record.expiry_date");
                }
            }

            // inventory_flow 业务类型
            if (hasTable(conn, catalog, "inventory_flow") && !hasColumn(conn, catalog, "inventory_flow", "biz_type")) {
                execute(conn, "ALTER TABLE inventory_flow ADD COLUMN biz_type VARCHAR(32) DEFAULT NULL COMMENT '业务类型'");
                done.add("inventory_flow.biz_type");
            }

            if (!done.isEmpty()) {
                log.info("启动迁移已补全列: {}", done);
            }
        } catch (Exception e) {
            log.warn("启动迁移执行异常: {}", e.getMessage());
        }
    }

    private static boolean hasTable(Connection conn, String catalog, String table) throws Exception {
        String sql = "SELECT 1 FROM information_schema.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, catalog);
            ps.setString(2, table);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static boolean hasColumn(Connection conn, String catalog, String table, String column) throws Exception {
        String sql = "SELECT 1 FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? AND COLUMN_NAME = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, catalog);
            ps.setString(2, table);
            ps.setString(3, column);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static void execute(Connection conn, String ddl) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(ddl);
        }
    }

    private static void createTableIfNotExists(Connection conn, String catalog, String table, String ddl) throws Exception {
        if (!hasTable(conn, catalog, table)) {
            execute(conn, ddl);
            log.info("启动迁移已创建表: {}", table);
        }
    }

    private static void insertMemberLevelRuleIfEmpty(Connection conn) {
        try {
            int count = 0;
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM member_level_rule")) {
                if (rs.next()) count = rs.getInt(1);
            }
            if (count == 0) {
                try (Statement st = conn.createStatement()) {
                    st.executeUpdate("INSERT INTO member_level_rule (level_name, min_amount, max_amount, sort_order) VALUES " +
                            "('普通会员', 0, 1000, 1), ('白银会员', 1000, 3000, 2), ('黄金会员', 3000, 10000, 3), ('钻石会员', 10000, 999999, 4)");
                }
                log.info("启动迁移已初始化 member_level_rule 数据");
            }
        } catch (Exception e) {
            log.debug("member_level_rule 初始数据: {}", e.getMessage());
        }
    }

    private static void insertMemberLifecycleConfigIfEmpty(Connection conn) {
        try {
            int count = 0;
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM member_lifecycle_config")) {
                if (rs.next()) count = rs.getInt(1);
            }
            if (count == 0) {
                try (Statement st = conn.createStatement()) {
                    st.executeUpdate("INSERT INTO member_lifecycle_config (config_key, config_value, remark) VALUES " +
                            "('churn_warning_days', '90', '流失预警天数'), ('sleeping_register_days', '30', '沉睡会员注册天数')");
                }
                log.info("启动迁移已初始化 member_lifecycle_config 数据");
            }
        } catch (Exception e) {
            log.debug("member_lifecycle_config 初始数据: {}", e.getMessage());
        }
    }
}
