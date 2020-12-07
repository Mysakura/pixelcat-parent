package com.pixelcat.core.db.executor;

import com.pixelcat.core.exception.PixelCatException;
import com.pixelcat.core.db.parse.SimpleORMUtil;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BaseExecutor implements Executor {

    private static final int BATCH_SIZE = 100;
    private final DataSource dataSource;
    private final StatementHandler statementHandler;
    private final Connection connection;

    public BaseExecutor(DataSource dataSource) {
        this.dataSource = dataSource;
        try {
            this.connection = dataSource.getConnection();
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new PixelCatException("获取连接异常！", e);
        }
        this.statementHandler = new PreparedStatementHandler(dataSource, this, new DefaultResultSetHandler());
    }

    @Override
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new PixelCatException("提交事务异常！", e);
        }
    }

    @Override
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new PixelCatException("回滚异常！", e);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new PixelCatException("关闭连接异常！", e);
        }
    }

    @Override
    public <T> int add(T obj) {
        SimpleORMUtil.SqlAndParam sp = SimpleORMUtil.getInstance().parseInsertSQLFromClass(obj);
        PreparedStatement ps = statementHandler.preparedStatement(connection, sp.getSql());
        return statementHandler.update(ps, sp.getParams());
    }

    @Override
    public <T> int updateById(T obj) {
        SimpleORMUtil.SqlAndParam sp = SimpleORMUtil.getInstance().parseUpdateSQLFromClass(obj);
        PreparedStatement ps = statementHandler.preparedStatement(connection, sp.getSql());
        return statementHandler.update(ps, sp.getParams());
    }

    @Override
    public int add(String sql, List<Object> params) {
        PreparedStatement ps = statementHandler.preparedStatement(connection, sql);
        return statementHandler.update(ps, params);
    }

    @Override
    public int update(String sql, List<Object> params) {
        PreparedStatement ps = statementHandler.preparedStatement(connection, sql);
        return statementHandler.update(ps, params);
    }

    @Override
    public <T> int delete(Class<T> clazz, List<Long> ids) {
        SimpleORMUtil.SqlAndParam sp = SimpleORMUtil.getInstance().parseDeleteSQLFromClass(clazz, ids);
        PreparedStatement ps = statementHandler.preparedStatement(connection, sp.getSql());
        return statementHandler.update(ps, sp.getParams());
    }

    @Override
    public <E> int batchAdd(List<E> objList) {
        SimpleORMUtil.SqlAndParam sp = SimpleORMUtil.getInstance().parseInsertSQLFromMultiClass(objList);
        PreparedStatement ps = statementHandler.preparedStatement(connection, sp.getSql());
        return statementHandler.batchUpdate(ps, sp.getParams2(), BATCH_SIZE);
    }

    @Override
    public <E> int batchUpdateById(List<E> objList) {
        SimpleORMUtil.SqlAndParam sp = SimpleORMUtil.getInstance().parseUpdateSQLFromMultiClass(objList);
        PreparedStatement ps = statementHandler.preparedStatement(connection, sp.getSql());
        return statementHandler.batchUpdate(ps, sp.getParams2(), BATCH_SIZE);
    }

    @Override
    public void batchAdd(String sql, List<List<Object>> params) {
        PreparedStatement ps = statementHandler.preparedStatement(connection, sql);
        statementHandler.batchUpdate(ps, params, BATCH_SIZE);
    }

    @Override
    public void batchUpdate(String sql, List<List<Object>> params) {
        PreparedStatement ps = statementHandler.preparedStatement(connection, sql);
        statementHandler.batchUpdate(ps, params, BATCH_SIZE);
    }

    @Override
    public <T> T getOne(Class<T> clazz, String sql, List<Object> params) {
        List<T> list = getList(clazz, sql, params);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public <E> List<E> getList(Class<E> clazz, String sql, List<Object> params) {
        PreparedStatement ps = statementHandler.preparedStatement(connection, sql);
        return statementHandler.list(clazz, ps, params);
    }

    @Override
    public <E> List<E> getList(Class<E> clazz, E obj) {
        SimpleORMUtil.SqlAndParam sp = SimpleORMUtil.getInstance().parseListSQLFromClass(obj);
        PreparedStatement ps = statementHandler.preparedStatement(connection, sp.getSql());
        return statementHandler.list(clazz, ps, sp.getParams());
    }
}
