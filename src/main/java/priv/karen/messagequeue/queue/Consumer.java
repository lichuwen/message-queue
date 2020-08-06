package priv.karen.messagequeue.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class Consumer {
    public static void main(String[] args) {
        new ConsumerThread("tcp://localhost:61616", "queue1").start();
        new ConsumerThread("tcp://localhost:61616", "queue1").start();
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
            //Create connection (factory, object, session)
            connectionFactory = new ActiveMQConnectionFactory(this.brokerUrl);
            conn = connectionFactory.createConnection();
            conn.start();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //Create a peer-to-peer receiving destination
            Destination destination = session.createQueue(destinationUrl);

            //Create producer message
            consumer = session.createConsumer(destination);

            //receive message,Keep waiting without message
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
