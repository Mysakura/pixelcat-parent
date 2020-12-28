package com.example.demo;

import com.pixelcat.core.zk.subject.ConfigChangeListener;
import com.pixelcat.core.zk.subject.event.BaseConfigEvent;
import org.springframework.stereotype.Component;

/**
 * 用户监听示例
 */
@Component
public class CustomListener implements ConfigChangeListener {
    @Override
    public void action(BaseConfigEvent event) {
        System.out.println("==============");
        System.out.println(event.getEventType().getEventType());
        System.out.println(event.toString());
        System.out.println("==============");
    }
}
