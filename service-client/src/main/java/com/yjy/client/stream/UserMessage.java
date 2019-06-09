package com.yjy.client.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface UserMessage {

    @Output("activemq-out")
    MessageChannel activeMQOut();

}
