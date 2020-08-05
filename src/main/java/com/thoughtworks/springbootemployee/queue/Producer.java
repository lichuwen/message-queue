package com.thoughtworks.springbootemployee.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Producer {
    public static void main(String[] args) {
        new ProducerThread("tcp://localhost:61616", "queue1").start();
    }

    static class ProducerThread extends Thread {
        String brokerUrl;
        String destinationUrl;

        public ProducerThread(String brokerUrl, String destinationUrl) {
            this.brokerUrl = brokerUrl;
            this.destinationUrl = destinationUrl;
        }

        @Override
        public void run() {
            ActiveMQConnectionFactory connectionFactory;
            Connection conn;
            Session session;

            try {

                connectionFactory = new ActiveMQConnectionFactory(brokerUrl);

                conn = connectionFactory.createConnection();
                conn.start();

                session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

                Destination destination = session.createQueue(destinationUrl);

                MessageProducer producer = session.createProducer(destination);

                producer.setDeliveryMode(DeliveryMode.PERSISTENT);

                String text = "Hi,Doraemon,this is queue1!";
                TextMessage message = session.createTextMessage(text);
                for (int i = 0; i < 1; i++) {
                    producer.send(message);
                }
                session.close();
                conn.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
