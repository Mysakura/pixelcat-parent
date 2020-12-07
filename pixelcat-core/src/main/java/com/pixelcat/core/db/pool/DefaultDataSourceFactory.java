package com.pixelcat.core.db.pool;

import javax.sql.DataSource;

public class DefaultDataSourceFactory implements DataSourceFactory {
    @Override
    public DataSource getDataSource(String driver, String url, String username, String password) {
        return new JdbcDataSourcePool(driver, url, username, password);
    }
}
