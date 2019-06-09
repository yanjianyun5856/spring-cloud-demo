package com.yjy.stream.binder.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.Binder;
import org.springframework.cloud.stream.binder.Binding;
import org.springframework.cloud.stream.binder.ConsumerProperties;
import org.springframework.cloud.stream.binder.ProducerProperties;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.Assert;

import javax.jms.*;

public class ActiveMQMessageChannelBinder implements
        Binder<MessageChannel,ConsumerProperties,ProducerProperties> {

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 接收 ActiveMQ 消息
     * @param name
     * @param group
     * @param inputChannel
     * @param consumerProperties
     * @return
     */
    @Override
    public Binding<MessageChannel> bindConsumer
        (String name, String group, MessageChannel inputChannel, ConsumerProperties consumerProperties) {

        //创建链接
        ConnectionFactory connectionFactory =jmsTemplate.getConnectionFactory();
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            //启动链接
            connection.start();
            //创建会话 Session
            Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            //创建消息的目的
            Destination destination = session.createQueue("TEST");
            //创建消费者
            MessageConsumer messageConsumer = session.createConsumer(destination);
            messageConsumer.setMessageListener(message -> {
                ObjectMessage objectMessage = (ObjectMessage) message;
                try {
                    Object object = objectMessage.getObject();
                    inputChannel.send(new GenericMessage<Object>(object));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }

        return () -> {

        };
    }

    /**
     * 发送消息到 ActiveMQ
     * @param name
     * @param outputChannel
     * @param producerProperties
     * @return
     */
    @Override
    public Binding<MessageChannel> bindProducer
        (String name, MessageChannel outputChannel, ProducerProperties producerProperties) {

        Assert.isInstanceOf(SubscribableChannel.class,outputChannel,"binding is supported only for " +
                " SubscribableChannel instances");

        SubscribableChannel subscribableChannel = (SubscribableChannel) outputChannel;
        subscribableChannel.subscribe(message -> {
            //接收内部管道消息 来自于MessageChannel#send
            //时间并没有发送消息 ，而是此消息将要发送到 ActiveMQ Broker
            Object messageBody = message.getPayload();
            jmsTemplate.convertAndSend(name,messageBody);
        });

        return () -> {
            System.out.println("Unbinding");
        };
    }
}
