package com.pixelcat.core.db.pool;


import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Slf4j
public class JdbcDataSourcePool implements DataSource {

    private final List<WrappedConnection> idleConnections = new ArrayList<>();
    private final List<WrappedConnection> activeConnections = new ArrayList<>();

    private static Map<String, Driver> registeredDrivers = new ConcurrentHashMap<>();
    private String driver;
    private String url;
    private String username;
    private String password;
    private long waitTimeForConnection = 2000;

    private int maxActiveConnections = 10;
    private int maxIdleConnections = 5;

    private final Object lock = new Object();

    public JdbcDataSourcePool(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private void printConnLog(){
        if (log.isDebugEnabled()){
            log.debug("活跃：{}，空闲：{}", activeConnections, idleConnections);
        }
    }

    /**
     * 回收连接
     * idleConnections      +1
     * activeConnections    -1
     * @param wc
     */
    public void recycleConnection(WrappedConnection wc) throws SQLException {
        synchronized (lock) {
            activeConnections.remove(wc);
            if (!wc.getRealConnection().getAutoCommit()) {
                wc.getRealConnection().rollback();
            }
            // 放进空闲队列
            if (idleConnections.size() < maxIdleConnections){
                WrappedConnection newConn = new WrappedConnection(wc.getRealConnection(), this);
                idleConnections.add(newConn);
                lock.notifyAll();
            } else {
                // 关闭
                wc.getRealConnection().close();
            }
            printConnLog();
        }
    }

    /**
     * 提供连接
     * idleConnections      -1
     * activeConnections    +1
     * @return
     */
    private WrappedConnection provideConnection(String username, String password) throws SQLException {
        WrappedConnection conn = null;
        while (conn == null){
            synchronized (lock){
                // 有空闲连接，复用
                if (!idleConnections.isEmpty()){
                    conn = idleConnections.remove(0);
                }else if (activeConnections.size() < maxActiveConnections){
                    // 无空闲连接，并且未超过最大活跃连接数，新建
                    conn = new WrappedConnection(doGetConnection(username, password), this);
                }else {
                    try {
                        // 等待
                        lock.wait(waitTimeForConnection);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                if (conn != null){
                    activeConnections.add(conn);
                }
                printConnLog();
            }
        }
        return conn;
    }

    private synchronized void initializeDriver() throws SQLException {
        if (!registeredDrivers.containsKey(this.driver)) {
            try {
                Class driverType = Class.forName(this.driver);
                Driver driverInstance = (Driver)driverType.newInstance();
                DriverManager.registerDriver(driverInstance);
                registeredDrivers.put(this.driver, driverInstance);
            } catch (Exception e) {
                throw new SQLException("Error during load driver. Cause: " + e);
            }
        }
    }

    private Connection doGetConnection(String username, String password) throws SQLException {
        initializeDriver();
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return provideConnection(username, password).getProxyConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return provideConnection(username, password).getProxyConnection();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
