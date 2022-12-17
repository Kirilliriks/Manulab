package me.kirillirik.database;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.intellij.lang.annotations.Language;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class AsyncQuery {

    private final QueryRunner runner;
    private final Executor executor;

    public AsyncQuery(QueryRunner runner) {
        this.runner = runner;
        this.executor = Executors.newFixedThreadPool(3);
    }

    public <T> CompletableFuture<T> rs(@Language("SQL") String sql, ResultSetHandler<T> handler) {
        return asCompletableFuture(() -> runner.query(sql, handler));
    }

    public <T> CompletableFuture<T> asCompletableFuture(Callable<T> callable) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
