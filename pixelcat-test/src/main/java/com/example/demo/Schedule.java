package com.example.demo;

import com.pixelcat.core.zk.ZkServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Schedule {

    @Autowired
    private ZkServer zkServer;

    @Scheduled(cron = "0/5 * * * * *")
    public void work() throws Exception {
//        zkServer.setPathValue("/pixelcat/root/user/local/java", "D");

//        zkServer.setPathValue("/pixelcat/root/user/local/java", (char)new Random().nextInt(100)+"");
        System.out.println(zkServer.listPath("/"));
    }

}
