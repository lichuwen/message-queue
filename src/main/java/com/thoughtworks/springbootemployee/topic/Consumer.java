package com.thoughtworks.springbootemployee.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class Consumer {
    public static void main(String[] args) {
        new ConsumerThread("tcp://localhost:61616", "topic1").start();
        new ConsumerThread("tcp://localhost:61616", "topic1").start();
        new ConsumerThread("tcp://localhost:61616", "topic1").start();
    }

}


class ConsumerThread extends Thread {

    String brokerUrl;
    String destinationUrl;

    public ConsumerThread(String brokerUrl, String destinationUrl) {
        this.brokerUrl = brokerUrl;
        this.destinationUrl = destinationUrl;
    }

    @Override
    public void run() {
        ActiveMQConnectionFactory connectionFactory;
        Connection conn;
        Session session;
        MessageConsumer consumer;

        try {

            connectionFactory = new ActiveMQConnectionFactory(this.brokerUrl);
            conn = connectionFactory.createConnection();
            conn.start();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(destinationUrl);

            consumer = session.createConsumer(destination);

            Message message = consumer.receive();
            if (message instanceof TextMessage) {
                System.out.println("receive message：" + ((TextMessage) message).getText());
            } else {
                System.out.println(message);
            }

            consumer.close();
            session.close();
            conn.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
