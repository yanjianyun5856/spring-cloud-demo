package com.yjy.client.jms.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

public class ActiveMQDemo {

    public static void main(String[] args) throws Exception {

        sendMessage();

        receiveMessage();
    }

    public static void receiveMessage() throws Exception {
        //创建链接
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = connectionFactory.createConnection();

        //启动链接
        connection.start();

        //创建会话 Session
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //创建消息的目的
        Destination destination = session.createQueue("TEST");

        //创建消息的消费者
        MessageConsumer consumer = session.createConsumer(destination);
        //获取消息
        Message message = consumer.receive(100);

        if(message instanceof TextMessage){
            TextMessage textMessage = (TextMessage) message;
            System.out.println(textMessage.getText());
        }
        consumer.close();
        session.close();
        connection.stop();
        connection.close();

    }


        //发送消息
    public static void sendMessage() throws Exception {
        //创建链接
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = connectionFactory.createConnection();

        //创建会话 Session
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //创建消息的目的
        Destination destination = session.createQueue("TEST");
        //创建消息生产者
        MessageProducer producer = session.createProducer(destination);

        //创建文本消息
        ActiveMQTextMessage mqMessage = new ActiveMQTextMessage();
        mqMessage.setText("hello world");

        //发送消息
        producer.send(mqMessage);

        producer.close();
        session.close();
        connection.close();


    }

}
