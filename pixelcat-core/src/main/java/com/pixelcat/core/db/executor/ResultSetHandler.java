package com.pixelcat.core.db.executor;

import com.pixelcat.core.exception.PixelCatException;

import java.sql.ResultSet;
import java.util.List;

public interface ResultSetHandler {

    <E> List<E> handleResultSets(Class<E> clazz, ResultSet resultSet) throws PixelCatException;
}
