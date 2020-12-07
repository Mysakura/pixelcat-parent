package com.pixelcat.core.db.pool;


import javax.sql.DataSource;

public interface DataSourceFactory {

    DataSource getDataSource(String driver, String url, String username, String password);
}
