package com.pixelcat.core.db.executor;

import com.pixelcat.core.exception.PixelCatException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public interface StatementHandler {

    PreparedStatement preparedStatement(Connection connection, String sql) throws PixelCatException;

    int update(PreparedStatement ps, List<Object> params) throws PixelCatException;

    int batchUpdate(PreparedStatement ps, List<List<Object>> params, int batchSize) throws PixelCatException;

    <E> List<E> list(Class<E> clazz, PreparedStatement ps, List<Object> params);

}
