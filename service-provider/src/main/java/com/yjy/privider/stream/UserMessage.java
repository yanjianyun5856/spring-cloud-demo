package com.yjy.privider.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 用户 消息 stream 接口定义
 */
public interface UserMessage {

    String INPUT = "user-message";

    @Input(INPUT)// 管道名称
    SubscribableChannel input();

    @Input("activemq-in")// 管道名称
    SubscribableChannel activeMQIn();
}
