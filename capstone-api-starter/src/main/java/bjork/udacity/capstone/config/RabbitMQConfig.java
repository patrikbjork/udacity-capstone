package bjork.udacity.capstone.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class RabbitMQConfig {

    public static final String topicExchangeName = "user-online-topic-exchange";

    public static final String queueName = "user-online-check-queue";
    public static final String replyQueueName = "user-online-check-reply-queue";
    public static final String chatMessageQueueName = "chat-message-queue";
    private final String hostName;

    public RabbitMQConfig() throws UnknownHostException {
        hostName = InetAddress.getLocalHost().getHostName();
    }

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    Queue replyQueue() {
        return new Queue(replyQueueName, false);
    }

    @Bean
    Queue chatMessageQueue() {
        return new Queue(chatMessageQueueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    /*@Bean
    DirectExchange directExchange() {
        return new DirectExchange(directExchangeName);
    }*/

    @Bean
    Binding binding(TopicExchange exchange) {
        return BindingBuilder.bind(queue()).to(exchange).with("user-online-check-query");
    }

    @Bean
    Binding replyBinding(TopicExchange exchange) throws UnknownHostException {
        return BindingBuilder.bind(replyQueue()).to(exchange).with("user-online-check-reply." + hostName);
    }

    @Bean
    Binding chatMessageBinding(TopicExchange exchange) throws UnknownHostException {
        return BindingBuilder.bind(chatMessageQueue()).to(exchange).with("chat-message");
    }

    /*@Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             @Qualifier("messageListener") MessageListener messageListener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(messageListener);
        return container;
    }

    @Bean
    SimpleMessageListenerContainer replyContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(replyQueueName);
        container.setMessageListener(replyMessageListener());
        return container;
    }*/

    /*@RabbitListener(queues = "user-online-check-queue")
    public void listen(Message message) {
        System.out.println(message);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setCorrelationId(message.getMessageProperties().getCorrelationId());

        String body = new String(message.getBody());
        Message reply = new Message((body + ":online").getBytes(), messageProperties);

        this.rabbitTemplate.send(topicExchangeName, "user-online-check-reply." + hostName, reply);
    }*/

    /*@RabbitListener(queues = "user-online-check-reply-queue")
    public void listenToReply(Message message) {
        System.out.println(message);
    }*/

    /*@Bean
    MessageListener messageListener(RabbitTemplate rabbitTemplate) {
        return message -> {
            String body = new String(message.getBody());
            System.out.println("Checking user: " + body);

            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setCorrelationId(message.getMessageProperties().getCorrelationId());

            Message reply = new Message((body + ":online").getBytes(), messageProperties);

            rabbitTemplate.send(topicExchangeName, "user-online-check-reply." + hostName, reply);
        };
    }

    @Bean("replyMessageListener")
    MessageListener replyMessageListener() {
        return message -> {
            System.out.println("Checked user: " + message.toString());
        };
    }*/
    /*@Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }*/
}
