package me.kirillirik.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;

public final class Database {

    private static Database inst;

    private final HikariConfig config;
    private final HikariDataSource dataSource;
    private final QueryRunner runner;
    private final SyncQuery syncQuery;
    private final AsyncQuery asyncQuery;

    private Database() {
        config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl("jdbc:postgresql://172.20.8.18:5432/kp0092_09");
        config.setUsername("st0092");
        config.setPassword("qwerty92");

        dataSource = new HikariDataSource(config);
        runner = new QueryRunner(dataSource);
        syncQuery = new SyncQuery(runner);
        asyncQuery = new AsyncQuery(runner);

        System.out.println("Initiated or connected");
    }

    public static SyncQuery sync() {
        return inst.syncQuery;
    }

    public static AsyncQuery async() {
        return inst.asyncQuery;
    }

    public static void init() {
        inst = new Database();
    }

    public static void close() {
        inst.dataSource.close();
    }
}
