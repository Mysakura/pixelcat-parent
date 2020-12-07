package com.pixelcat.core.db.executor;

import com.pixelcat.core.exception.PixelCatException;
import com.pixelcat.core.db.parse.SimpleORMUtil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultResultSetHandler implements ResultSetHandler {
    @Override
    public <E> List<E> handleResultSets(Class<E> clazz, ResultSet resultSet) throws PixelCatException {
        try {
            List<E> list = new ArrayList<>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()){
                Map<String, Object> dataMap = new HashMap<>();
                for (int i = 1; i <= columnCount; i++){
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    dataMap.put(columnName, value);
                }
                list.add(SimpleORMUtil.getInstance().parseBeanFromDB(clazz, dataMap));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
