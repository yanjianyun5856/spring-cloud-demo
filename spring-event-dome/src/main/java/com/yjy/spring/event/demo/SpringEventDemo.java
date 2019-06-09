package com.yjy.spring.event.demo;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

public class SpringEventDemo {

    public static void main(String[] args) {

        //创建 Annotation 驱动的 spring 应用上下文
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //注册 EventConfiguration 到spring 上下文
        context.register(EventConfiguration.class);

        //启动上下文
        context.refresh();

        ApplicationEventPublisher publisher = context;

        publisher.publishEvent(new MyApplicationEvent("hello,world"));

    }

    public static class MyApplicationEvent extends ApplicationEvent{

        private static final long serialVersionUID = -3238070845623878824L;

        public MyApplicationEvent(String source) {
            super(source);
        }
    }

    @Configuration
    public static class EventConfiguration{
        /**
         * 监听
         * @param event
         */
        @EventListener
        public void onEvent(MyApplicationEvent event){
            System.out.println("监听到时间："+event);
        }
    }

}
