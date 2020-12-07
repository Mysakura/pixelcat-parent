package com.pixelcat.core.db.executor;

import java.util.List;

public interface Executor {

    void commit();

    void rollback();

    void close();

    /**
     * 推荐
     * @param obj
     * @param <T>
     * @return
     */
    <T> int add(T obj);

    /**
     * 推荐
     * @param obj
     * @param <T>
     * @return
     */
    <T> int updateById(T obj);

    int add(String sql, List<Object> params);

    int update(String sql, List<Object> params);

    <T> int delete(Class<T> clazz, List<Long> ids);

    /**
     * 推荐
     * @param objList
     * @param <E>
     * @return
     */
    <E> int batchAdd(List<E> objList);

    /**
     * 推荐
     * @param objList
     * @param <E>
     * @return
     */
    <E> int batchUpdateById(List<E> objList);

    void batchAdd(String sql, List<List<Object>> params);

    void batchUpdate(String sql, List<List<Object>> params);

    <T> T getOne(Class<T> clazz, String sql, List<Object> params);

    <E> List<E> getList(Class<E> clazz, String sql, List<Object> params);

    /**
     * 简单查询
     * 只能查询单个表，条件根据传入的实体类的字段有无决定
     * @param obj
     * @param <E>
     * @return
     */
    <E> List<E> getList(Class<E> clazz, E obj);
}
