package com.pixelcat.core.db.parse;

import com.pixelcat.core.exception.PixelCatException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
public class SimpleORMUtil {

    private SimpleORMUtil(){}

    private static class Inner{
        private static SimpleORMUtil instance = new SimpleORMUtil();
    }

    public static SimpleORMUtil getInstance(){
        return Inner.instance;
    }

    /**
     * 解析返回的结果集为指定的Bean
     * @param clazz
     * @param dataMap
     * @param <T>
     * @return
     */
    public <T> T parseBeanFromDB(Class<T> clazz, Map<String, Object> dataMap){
        try {
            T newInstance = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();

            for (Field f : fields){
                f.setAccessible(true);
                String name = f.getName();
                // 获取注解
                DbColumn annotation = f.getDeclaredAnnotation(DbColumn.class);
                if (annotation == null){
                    throw new PixelCatException("Class["+ clazz.getName() +"]的["+ name +"]成员变量缺失@DbColumn注解！");
                }
                String dbFieldName = annotation.value();

                Object o = dataMap.get(dbFieldName);
                if (log.isDebugEnabled()){
                    log.debug("Bean Field Name:{},DB Field Name:{},Value:{}", name, dbFieldName, o);
                }
                f.set(newInstance, o);
            }
            return newInstance;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PixelCatException("数据库字段转换为Bean失败！", e);
        }
    }

    /**
     * 获取表名
     * @param clazz
     * @return
     */
    private String getTableName(Class<?> clazz){
        DbTable table = clazz.getAnnotation(DbTable.class);
        if (table == null){
            throw new PixelCatException("Class["+ clazz.getName() +"]缺失@DbTable注解！");
        }
        return table.value();
    }

    /**
     * 获取字段映射
     * @param f
     * @return
     */
    private String getFieldName(Field f){
        f.setAccessible(true);
        DbColumn annotation = f.getDeclaredAnnotation(DbColumn.class);
        // 没有字段注解，认为他不需要操作此字段
        if (annotation == null){
            return null;
        }
        // 数据库字段名
        return annotation.value();
    }

    /**
     * 生成insert语句
     * @param obj
     * @param <T>
     * @return
     */
    public <T> SqlAndParam parseInsertSQLFromClass(T obj) {
        try {
            List<Object> params = new ArrayList<>();
            Class<?> clazz = obj.getClass();
            String tableName = getTableName(clazz);

            String sqlBegin = "insert into " + tableName + " ";
            List<String> sqlField = new ArrayList<>();
            List<String> sqlValue = new ArrayList<>();
            Field[] declaredFields = clazz.getDeclaredFields();

            for (Field f : declaredFields){
                // 数据库字段名
                String dbFieldName = getFieldName(f);
                // 字段名为空，跳过
                if (dbFieldName == null){
                    continue;
                }
                Object o = f.get(obj);
                // 传入属性为空，跳过
                if (o == null){
                    continue;
                }

                sqlField.add(dbFieldName);
                sqlValue.add("?");
                params.add(o);
            }
            String fieldStr = "(" + String.join(",", sqlField) + ") value";
            String fieldValueStr = "(" + String.join(",", sqlValue) + ")";
            String sql = sqlBegin + fieldStr + fieldValueStr;

            return new SqlAndParam(sql, params, null);
        } catch (IllegalAccessException e) {
            throw new PixelCatException("从Bean解析SQL失败！", e);
        }
    }

    /**
     * 生成按id更新的SQL
     * @param obj
     * @param <T>
     * @return
     */
    public <T> SqlAndParam parseUpdateSQLFromClass(T obj) {
        try {
            List<Object> params = new ArrayList<>();
            Class<?> clazz = obj.getClass();
            String tableName = getTableName(clazz);

            String sqlBegin = "update " + tableName;
            List<String> sqlField = new ArrayList<>();
            StringBuilder condition = new StringBuilder();
            boolean hasId = false;
            Object idValue = null;
            Field[] declaredFields = clazz.getDeclaredFields();

            for (int i = 0; i < declaredFields.length; i++){
                Field f = declaredFields[i];
                // 数据库字段名
                String dbFieldName = getFieldName(f);
                // 字段名为空，跳过
                if (dbFieldName == null){
                    continue;
                }
                Object o = f.get(obj);
                // 传入属性为空，跳过
                if (o == null){
                    continue;
                }

                DbId idAnnotation = f.getDeclaredAnnotation(DbId.class);
                // 寻找id标识
                if (idAnnotation != null){
                    hasId = true;
                    condition.append("where ");
                    condition.append(dbFieldName);
                    condition.append(" = ?");
                    idValue = o;
                    continue;
                }
                sqlField.add(dbFieldName + " = ? ");
                params.add(o);
            }
            if (!hasId){
                throw new PixelCatException("Class["+ clazz.getName() +"]缺失@DbId注解或@DbId注解的字段值为null！");
            }
            params.add(idValue);

            String sql = sqlBegin + " set " + String.join(",", sqlField) + condition.toString();

            return new SqlAndParam(sql, params, null);
        } catch (IllegalAccessException e) {
            throw new PixelCatException("从Bean解析SQL失败！", e);
        }
    }

    /**
     * 生成批量delete的SQL
     * @param clazz
     * @param ids
     * @param <T>
     * @return
     */
    public <T> SqlAndParam parseDeleteSQLFromClass(Class<T> clazz, List<Long> ids) {
        List<Object> params = new ArrayList<>();
        String tableName = getTableName(clazz);

        String sqlBegin = "delete from " + tableName + " ";
        StringBuilder condition = new StringBuilder();
        boolean hasId = false;
        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field f : declaredFields){
            // 数据库字段名
            String dbFieldName = getFieldName(f);
            // 字段名为空，跳过
            if (dbFieldName == null){
                continue;
            }

            DbId idAnnotation = f.getDeclaredAnnotation(DbId.class);
            // 寻找id标识
            if (idAnnotation != null){
                hasId = true;
                condition.append("where ");
                condition.append(dbFieldName);
                condition.append(" in (");
                break;
            }
        }
        if (!hasId){
            throw new PixelCatException("Class["+ clazz.getName() +"]缺失@DbId注解！");
        }
        for (int i = 0; i < ids.size(); i++){
            if (i == ids.size() - 1){
                condition.append("?)");
            } else {
                condition.append("?,");
            }
            params.add(ids.get(i));
        }
        String sql = sqlBegin + condition.toString();

        return new SqlAndParam(sql, params, null);
    }

    /**
     * 只解析一个SQL，多个参数
     * @param list
     * @param <E>
     * @return
     */
    public <E> SqlAndParam parseInsertSQLFromMultiClass(List<E> list) {
        if (CollectionUtils.isEmpty(list)){
            throw new PixelCatException("插入数据为空！");
        }
        List<List<Object>> params = new ArrayList<>();

        for (E obj : list){
            params.add(parseInsertParamsOnly(obj));
        }
        return new SqlAndParam(parseInsertSQLOnly(list.get(0)), null, params);
    }

    /**
     * 只解析一个SQL，多个参数
     * @param list
     * @param <E>
     * @return
     */
    public <E> SqlAndParam parseUpdateSQLFromMultiClass(List<E> list) {
        if (CollectionUtils.isEmpty(list)){
            throw new PixelCatException("待更新数据为空！");
        }
        List<List<Object>> params = new ArrayList<>();
        for (E obj : list){
            params.add(parseUpdateParamsOnly(obj));
        }
        return new SqlAndParam(parseUpdateSQLOnly(list.get(0)), null, params);
    }

    /**
     * 仅仅解析SQL
     * @param obj
     * @param <T>
     * @return
     */
    private <T> String parseInsertSQLOnly(T obj) {
        Class<?> clazz = obj.getClass();
        String tableName = getTableName(clazz);

        String sqlBegin = "insert into " + tableName + " ";
        List<String> sqlField = new ArrayList<>();
        List<String> sqlValue = new ArrayList<>();

        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field f : declaredFields){
            // 数据库字段名
            String dbFieldName = getFieldName(f);
            // 字段名为空，跳过
            if (dbFieldName == null){
                continue;
            }
            // 属性为空，跳过
            try {
                if (f.get(obj) == null){
                    continue;
                }
            } catch (IllegalAccessException e) {
                throw new PixelCatException("从Bean解析SQL失败！", e);
            }

            sqlField.add(dbFieldName);
            sqlValue.add("?");
        }
        String fieldStr = "(" + String.join(",", sqlField) + ") value";
        String fieldValueStr = "(" + String.join(",", sqlValue) + ")";
        return sqlBegin + fieldStr + fieldValueStr;
    }

    /**
     * 仅仅解析参数
     * @param obj
     * @param <T>
     * @return
     */
    private <T> List<Object> parseInsertParamsOnly(T obj) {
        try {
            List<Object> params = new ArrayList<>();
            Class<?> clazz = obj.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();

            for (Field f : declaredFields){
                // 数据库字段名
                String dbFieldName = getFieldName(f);
                // 字段名为空，跳过
                if (dbFieldName == null){
                    continue;
                }
                // 属性为空，跳过
                if (f.get(obj) == null){
                    continue;
                }
                params.add(f.get(obj));
            }

            return params;
        } catch (IllegalAccessException e) {
            throw new PixelCatException("从Bean解析SQL失败！", e);
        }
    }

    private <T> String parseUpdateSQLOnly(T obj) {
        try {
            Class<?> clazz = obj.getClass();
            String tableName = getTableName(clazz);

            String sqlBegin = "update " + tableName;
            List<String> sqlField = new ArrayList<>();
            StringBuilder condition = new StringBuilder();
            boolean hasId = false;
            Object idValue = null;

            Field[] declaredFields = clazz.getDeclaredFields();

            for (int i = 0; i < declaredFields.length; i++){
                Field f = declaredFields[i];
                // 数据库字段名
                String dbFieldName = getFieldName(f);
                // 字段名为空，跳过
                if (dbFieldName == null){
                    continue;
                }
                Object o = f.get(obj);
                // 传入属性为空，跳过
                if (o == null){
                    continue;
                }

                DbId idAnnotation = f.getDeclaredAnnotation(DbId.class);
                // 寻找id标识
                if (idAnnotation != null){
                    hasId = true;
                    condition.append("where ");
                    condition.append(dbFieldName);
                    condition.append(" = ?");
                    idValue = o;
                    continue;
                }
                sqlField.add(dbFieldName + " = ? ");
            }
            if (!hasId){
                throw new PixelCatException("Class["+ clazz.getName() +"]缺失@DbId注解或@DbId注解的字段值为null！");
            }

            return sqlBegin + " set " + String.join(",", sqlField) + condition.toString();

        } catch (IllegalAccessException e) {
            throw new PixelCatException("从Bean解析SQL失败！", e);
        }
    }

    private <T> List<Object> parseUpdateParamsOnly(T obj) {
        try {
            List<Object> params = new ArrayList<>();
            Class<?> clazz = obj.getClass();

            boolean hasId = false;
            Object idValue = null;

            Field[] declaredFields = clazz.getDeclaredFields();

            for (Field f : declaredFields){
                // 数据库字段名
                String dbFieldName = getFieldName(f);
                // 字段名为空，跳过
                if (dbFieldName == null){
                    continue;
                }
                Object o = f.get(obj);
                // 传入属性为空，跳过
                if (o == null){
                    continue;
                }
                DbId idAnnotation = f.getDeclaredAnnotation(DbId.class);
                // 寻找id标识
                if (idAnnotation != null){
                    hasId = true;
                    idValue = o;
                    continue;
                }

                params.add(o);
            }
            if (!hasId){
                throw new PixelCatException("Class["+ clazz.getName() +"]缺失@DbId注解或@DbId注解的字段值为null！");
            }
            params.add(idValue);

            return params;
        } catch (IllegalAccessException e) {
            throw new PixelCatException("从Bean解析SQL失败！", e);
        }
    }

    /**
     * 生成获取列表的SQL
     * @param obj
     * @param <T>
     * @return
     */
    public <T> SqlAndParam parseListSQLFromClass(T obj, boolean count, Integer limitStart, Integer limitEnd) {
        try {
            List<Object> params = new ArrayList<>();
            Class<?> clazz = obj.getClass();
            String tableName = getTableName(clazz);

            String sqlBegin = "select "+ (count ? "count(*)" : "*") +" from " + tableName;
            StringBuilder sqlValue = new StringBuilder();
            Field[] declaredFields = clazz.getDeclaredFields();
            boolean needWhere = false;

            // select * from user where id = ? and
            for (int i = 0; i < declaredFields.length; i++){
                Field f = declaredFields[i];
                // 数据库字段名
                String dbFieldName = getFieldName(f);
                // 字段名为空，跳过
                if (dbFieldName == null){
                    continue;
                }

                Object o = f.get(obj);
                // 属性有值
                if (o != null) {
                    if (o instanceof String){
                        String v = (String) o;
                        if (v.length() == 0){
                            continue;
                        }
                    }
                    needWhere = true;
                    sqlValue.append(dbFieldName);
                    sqlValue.append(" = ? and ");
                    params.add(o);
                }
            }
            String sql = sqlBegin;
            if (needWhere){
                sql += " where " + sqlValue.substring(0, sqlValue.lastIndexOf("and"));
            }
            if (limitStart != null && limitEnd != null){
                sql += "limit " + limitStart + "," + limitEnd;
            }
            return new SqlAndParam(sql, params, null);
        } catch (IllegalAccessException e) {
            throw new PixelCatException("从Bean解析SQL失败！", e);
        }
    }


    @Data
    public class SqlAndParam{
        private String sql;
        private List<Object> params;
        private List<List<Object>> params2;

        private SqlAndParam(String sql, List<Object> params, List<List<Object>> params2) {
            this.sql = sql;
            this.params = params;
            this.params2 = params2;
        }
    }

}
