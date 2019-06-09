package com.yjy.privider.service.impl;


import com.yjy.api.domain.User;
import com.yjy.api.service.UserService;
import com.yjy.privider.stream.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;


import static org.springframework.cloud.stream.messaging.Sink.INPUT;

@Service
public class MessageUserService {
    @Autowired
    private UserMessage userMessage;

    @Autowired
    @Qualifier("inMemoryUserService")
    private UserService userService;

    private void saveUser(byte[] data) {
        // message body 是字节流 byte[]
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            User user = (User) objectInputStream.readObject(); // 反序列化成 User 对象
            userService.saveUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void init() {

        SubscribableChannel subscribableChannel = userMessage.input();
        subscribableChannel.subscribe(message -> {
            System.out.println("Subscribe by SubscribableChannel");
            // message body 是字节流 byte[]
            byte[] body = (byte[]) message.getPayload();
            saveUser(body);

        });

        //监听activemq stream
        userMessage.activeMQIn().subscribe(message -> {
            if (message instanceof GenericMessage){
                GenericMessage genericMessage = (GenericMessage) message;
                User user = (User) genericMessage.getPayload();
                userService.saveUser(user);
            }
        });
    }


    @ServiceActivator(inputChannel = INPUT)
    public void listen(byte[] data) {
        System.out.println("Subscribe by @ServiceActivator");
        saveUser(data);
    }

    @StreamListener(INPUT)
    public void onMessage(byte[] data) {
        System.out.println("Subscribe by @StreamListener");
        saveUser(data);
    }

}
