package me.kirillirik.database;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;

public final class SyncQuery {

    private final QueryRunner runner;

    public SyncQuery(QueryRunner runner) {
        this.runner = runner;
    }

    public <T> T insert(@Language("SQL") String sql, ResultSetHandler<T> handler, Object... params) {
        try {
            return runner.insert(sql, handler, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T rs(@Language("SQL") String sql, ResultSetHandler<T> handler) {
        try {
            return runner.query(sql, handler);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T rs(@Language("SQL") String sql, ResultSetHandler<T> handler, Object... params) {
        try {
            return runner.query(sql, handler, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
