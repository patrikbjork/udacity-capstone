package bjork.udacity.capstone.messaging.rabbitmq.test;

import java.net.InetAddress;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import bjork.udacity.capstone.config.RabbitMQConfig;
import bjork.udacity.capstone.messaging.rabbitmq.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class Runner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;
    private final TopicExchange topicExchange;

    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate, TopicExchange topicExchange) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
        this.topicExchange = topicExchange;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sending message...");

        MessageProperties messageProperties = new MessageProperties();

        String hostName = InetAddress.getLocalHost().getHostName();

        LOGGER.info("Setting reply-to: " + hostName);

        messageProperties.setReplyTo(hostName);
        messageProperties.setHeader("sessionId", UUID.randomUUID().toString());

        Message message = new Message("theUserId".getBytes(), messageProperties);

//        rabbitTemplate.send(RabbitMQConfig.topicExchangeName, "user-online-check-reply." + hostName, message);
        rabbitTemplate.send(RabbitMQConfig.topicExchangeName, "user-online-check-query", message);
//        rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeName, "foo.bar.baz", "Hello from RabbitMQ!");
        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    }

}
