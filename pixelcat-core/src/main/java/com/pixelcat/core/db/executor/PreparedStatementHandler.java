package com.pixelcat.core.db.executor;

import com.pixelcat.core.exception.PixelCatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Slf4j
public class PreparedStatementHandler implements StatementHandler {

    private final DataSource dataSource;
    private final Executor executor;
    private final ResultSetHandler resultSetHandler;

    public PreparedStatementHandler(DataSource dataSource, Executor executor, ResultSetHandler resultSetHandler) {
        this.dataSource = dataSource;
        this.executor = executor;
        this.resultSetHandler = resultSetHandler;
    }

    private void closeStatement(Statement statement){
        if (statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                throw new PixelCatException("关闭Statement异常！", e);
            }
        }
    }

    private void rollback() {
        executor.rollback();
    }

    private void setParams(PreparedStatement statement, List<Object> params) throws SQLException {
        if (!CollectionUtils.isEmpty(params)){
            for (int i = 0; i < params.size(); i++){
                statement.setObject((i+1), params.get(i));
            }
        }
    }

    private void printSQL(Statement ps) {
        if (log.isDebugEnabled()){
            log.debug("SQL:{}", ps.toString());
        }
    }

    @Override
    public PreparedStatement preparedStatement(Connection connection, String sql) throws PixelCatException {
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new PixelCatException("com.pixelcat.core.db.executor.PreparedStatementHandler.preparedStatement异常！", e);
        }
    }


    @Override
    public int update(PreparedStatement ps, List<Object> params) throws PixelCatException {
        try {
            setParams(ps, params);
            printSQL(ps);
            ps.executeUpdate();
            return ps.getUpdateCount();
        } catch (SQLException e) {
            rollback();
            throw new PixelCatException("com.pixelcat.core.db.executor.PreparedStatementHandler.update异常！", e);
        } finally {
            closeStatement(ps);
        }
    }


    @Override
    public int batchUpdate(PreparedStatement ps, List<List<Object>> params, int batchSize) throws PixelCatException {
        try {
            int updateCount = 0;
            if (!CollectionUtils.isEmpty(params)){
                for (int i = 0; i < params.size(); i++){
                    List<Object> current = params.get(i);
                    setParams(ps, current);
                    ps.addBatch();
                    if ((i+1) % batchSize == 0){
                        printSQL(ps);
                        int[] batch = ps.executeBatch();
                        updateCount += batch.length;
                    }
                }
                if (params.size() % batchSize != 0) {
                    printSQL(ps);
                    int[] batch = ps.executeBatch();
                    updateCount += batch.length;
                }
                ps.clearBatch();
            }
            return updateCount;
        } catch (SQLException e) {
            rollback();
            throw new PixelCatException("com.pixelcat.core.db.executor.PreparedStatementHandler.batchUpdate异常！", e);
        } finally {
            closeStatement(ps);
        }
    }

    @Override
    public <E> List<E> list(Class<E> clazz, PreparedStatement ps, List<Object> params) {
        try {
            setParams(ps, params);
            printSQL(ps);
            ResultSet resultSet = ps.executeQuery();
            return resultSetHandler.handleResultSets(clazz, resultSet);
        } catch (SQLException e) {
            rollback();
            throw new PixelCatException("com.pixelcat.core.db.executor.PreparedStatementHandler.list异常！", e);
        } finally {
            closeStatement(ps);
        }
    }

    @Override
    public <T> int count(Class<T> clazz, PreparedStatement ps, List<Object> params) {
        try {
            setParams(ps, params);
            printSQL(ps);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException e) {
            rollback();
            throw new PixelCatException("com.pixelcat.core.db.executor.PreparedStatementHandler.list异常！", e);
        } finally {
            closeStatement(ps);
        }
    }
}
