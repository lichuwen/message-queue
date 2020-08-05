package com.thoughtworks.springbootemployee.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;



public class Producer {
    public static void main(String[] args) {
        new ProducerThread("tcp://localhost:61616", "topic1").start();
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

                Destination destination = session.createTopic(destinationUrl);

                MessageProducer producer = session.createProducer(destination);

                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                String text = "Hi,Karen,this is topic1!";
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
