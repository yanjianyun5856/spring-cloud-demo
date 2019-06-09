package com.yjy.stream.binder.activemq;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.bind.Binder;

/**
 * 自动装配
 */
@Configuration
@ConditionalOnMissingBean(Binder.class)
public class ActiveMQStreamBinderAutoConfiguration {

    @Bean
    public ActiveMQMessageChannelBinder activeMQMessageChannelBinder(){
        return new ActiveMQMessageChannelBinder();
    }
}
