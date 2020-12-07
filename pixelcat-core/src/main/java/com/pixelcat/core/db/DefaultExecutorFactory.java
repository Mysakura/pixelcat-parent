package com.pixelcat.core.db;

import com.pixelcat.core.db.executor.BaseExecutor;
import com.pixelcat.core.db.executor.Executor;
import com.pixelcat.core.db.executor.ExecutorFactory;
import com.pixelcat.core.db.parse.SimpleORMUtil;
import com.pixelcat.core.db.pool.DefaultDataSourceFactory;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
public class DefaultExecutorFactory implements ExecutorFactory {

    private DataSource dataSource;


    public DefaultExecutorFactory(String driver, String url, String username, String password) {
        dataSource = new DefaultDataSourceFactory().getDataSource(driver, url, username, password);
    }

    @Override
    public Executor newExecutor() {
        return new BaseExecutor(dataSource);
    }

    public static void main(String[] a) {
//        Executor executor = new DefaultExecutorFactory("com.mysql.jdbc.Driver",
//                "jdbc:mysql://10.20.26.47:3306/lcptpub?characterEncoding=utf8",
//                "root","handsome").newExecutor();
//        executor.add("insert into user values(?,?,?)", Arrays.asList(8, "BBB", 20));
//        executor.add("insert into user values(?,?,?)", Arrays.asList(9, "BBB", 20));
//        executor.add("insert into user values(?,?,?)", Arrays.asList(10, "CCC", 20));
//        executor.commit();

//        executor.getList(User.class, "select * from user", null).forEach(System.out::println);

//        List<User> list = new ArrayList<>();
//
//        for (int i = 11; i < 150; i++){
//            list.add(new User(i, "EEE", new Random().nextInt(100)));
//        }

//        int count = executor.updateById(new User(10, "DDD", 100));
//        int count = executor.batchAdd(list);
//        int count = executor.batchUpdateById(list);
//        System.out.println(count);
//        executor.getList(User.class, new User(null, "", null)).forEach(System.out::println);
//        executor.commit();
//        executor.close();
//        ExecutorService executorService = Executors.newFixedThreadPool(2);
//
//        executorService.submit(() -> {
//
//        });
        System.out.println(SimpleORMUtil.getInstance().parseDeleteSQLFromClass(User.class, Arrays.asList(1L,2L,3L,4L,5L,6L)).getSql());


    }


}

