package com.pixelcat.core.db.pool;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Objects;

@Slf4j
public class WrappedConnection implements InvocationHandler {

    private Connection realConnection;
    private Connection proxyConnection;
    private JdbcDataSourcePool pool;

    public WrappedConnection(Connection realConnection, JdbcDataSourcePool pool) {
        this.realConnection = realConnection;
        this.proxyConnection = (Connection) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{ Connection.class }, this);
        this.pool = pool;
    }

    public Connection getRealConnection() {
        return realConnection;
    }

    public Connection getProxyConnection() {
        return proxyConnection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WrappedConnection that = (WrappedConnection) o;
        return Objects.equals(realConnection, that.realConnection) &&
                Objects.equals(proxyConnection, that.proxyConnection) &&
                Objects.equals(pool, that.pool);
    }

    @Override
    public int hashCode() {
        return Objects.hash(realConnection, proxyConnection, pool);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        if ("close".equals(name)) {
            log.debug("回收连接：{}", this);
            this.pool.recycleConnection(this);
            return null;
        }else if ("rollback".equals(name)) {
            log.debug("rollback连接：{}", this);
            return method.invoke(this.realConnection, args);
        } else {
            return method.invoke(this.realConnection, args);
        }
    }
}
