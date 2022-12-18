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

    public int update(@Language("SQL") String sql, Object... params) {
        try {
            return runner.update(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public <T> T insert(@Language("SQL") String sql, ResultSetHandler<T> handler, Object... params) {
        try {
            return runner.insert(sql, handler, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public <T> T rs(@Language("SQL") String sql, ResultSetHandler<T> handler) {
        try {
            return runner.query(sql, handler);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public <T> T rs(@Language("SQL") String sql, ResultSetHandler<T> handler, Object... params) {
        try {
            return runner.query(sql, handler, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
