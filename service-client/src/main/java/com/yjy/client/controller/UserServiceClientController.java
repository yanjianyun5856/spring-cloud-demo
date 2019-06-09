package com.yjy.client.controller;

import com.yjy.api.domain.User;
import com.yjy.api.service.UserService;
import com.yjy.client.stream.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserServiceClientController implements UserService {

    @Autowired
    private UserService userService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public UserServiceClientController(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private UserMessage userMessage;

    @PostMapping("/user/save/message/activemq")
    public boolean saveUserByActiveMQMessage(@RequestBody User user) {
        jmsTemplate.convertAndSend(user);

        return true;
    }

    @PostMapping("/user/save/message/activemq/stream")
    public boolean saveUserByActiveMQStrean(@RequestBody User user) {
        MessageChannel messageChannel = userMessage.activeMQOut();
        GenericMessage message = new GenericMessage(user);

        return  messageChannel.send(message);
    }

    @PostMapping("/user/save/message")
    public boolean saveUserByMessage(@RequestBody User user) {
        System.out.println("------------------------------------------"+user.getName());
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("yjy-users", "0", user);
        return future.isDone();
    }

    @Override
    public boolean saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @Override
    public List<User> findUserAll() throws InterruptedException {
        return userService.findUserAll();
    }
}
