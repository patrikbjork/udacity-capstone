package bjork.udacity.capstone.config;

import bjork.udacity.capstone.websocket.ChatWebSocketHandler;
import bjork.udacity.capstone.websocket.OnlineWebSocketHandler;
import bjork.udacity.capstone.websocket.UserOnlineCheckWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

@Configuration
@Slf4j
public class RabbitMQConfig {

    @Autowired
    private OnlineWebSocketHandler onlineWebSocketHandler;

    @Autowired
    private UserOnlineCheckWebSocketHandler userOnlineCheckWebSocketHandler;

    @Autowired
    private ChatWebSocketHandler chatWebSocketHandler;

    public static final String topicExchangeName = "user-online-topic-exchange";
    public static final String userOnlineFanoutExchangeName = "user-online-fanout-exchange";
    public static final String chatFanoutExchangeName = "chat-fanout-exchange";
    public static final String pingFanoutExchangeName = "ping-fanout-exchange";

    public static final String uuid = UUID.randomUUID().toString();
    public static final String userOnlineCheckQueueName = "user-online-check-queue-" + uuid;
    public static final String replyQueueName = "user-online-check-reply-queue-" + uuid;
    public static final String chatMessageQueueName = "chat-message-queue-" + uuid;
    public static final String pingQueueName = "ping-queue-" + uuid;
    private final String hostName;

    public RabbitMQConfig() throws UnknownHostException {
        hostName = InetAddress.getLocalHost().getHostName();
        log.info("hostName: " + hostName);
    }

    @Bean
    Queue queue() {
        return new Queue(userOnlineCheckQueueName, false);
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
    Queue pingMessageQueue() {
        return new Queue(pingQueueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    FanoutExchange userOnlineExchange() {
        return new FanoutExchange(userOnlineFanoutExchangeName);
    }

    @Bean
    FanoutExchange pingExchange() {
        return new FanoutExchange(pingFanoutExchangeName);
    }

    @Bean
    FanoutExchange chatExchange() {
        return new FanoutExchange(chatFanoutExchangeName);
    }

    /*@Bean
    DirectExchange directExchange() {
        return new DirectExchange(directExchangeName);
    }*/

    @Bean
    Binding userOnlineBinding() {
        return BindingBuilder.bind(queue()).to(userOnlineExchange());
    }

    @Bean
    Binding chatBinding() {
        return BindingBuilder.bind(chatMessageQueue()).to(chatExchange());
    }

    @Bean
    Binding userOnlineReplyBinding(TopicExchange exchange) throws UnknownHostException {
        return BindingBuilder.bind(replyQueue()).to(exchange).with("user-online-check-reply-" + uuid);
    }

    @Bean
    Binding pingBinding() {
        return BindingBuilder.bind(pingMessageQueue()).to(pingExchange());
    }

    /*@Bean
    public Declarables fanoutBindings() {
//        Queue fanoutQueue1 = new Queue("fanout.queue1", false);
//        Queue fanoutQueue2 = new Queue("fanout.queue2", false);
//        FanoutExchange fanoutExchange = new FanoutExchange("fanout.exchange");

        return new Declarables(
                queue(),
//                fanoutQueue2,
                fanoutExchange(),
                BindingBuilder.bind(queue()).to(fanoutExchange()));
    }*/

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             @Qualifier("messageListener") MessageListener messageListener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(userOnlineCheckQueueName);
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
    }

    @Bean
    SimpleMessageListenerContainer chatContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(chatMessageQueueName);
        container.setMessageListener(chatMessageListener());
        return container;
    }

    @Bean
    SimpleMessageListenerContainer pingContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(pingQueueName);
        container.setMessageListener(pingListener());
        return container;
    }

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

    @Bean
    MessageListener messageListener(RabbitTemplate rabbitTemplate) {
        return message -> {
            onlineWebSocketHandler.handleOnlineCheckQuery(message);
            /*String body = new String(message.getBody());
            System.out.println("Checking user: " + body);

            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setCorrelationId(message.getMessageProperties().getCorrelationId());

            Message reply = new Message((body + ":online").getBytes(), messageProperties);

            rabbitTemplate.send(topicExchangeName, "user-online-check-reply." + hostName, reply);*/
        };
    }

    @Bean("replyMessageListener")
    MessageListener replyMessageListener() {
        return message -> {
            userOnlineCheckWebSocketHandler.listenToReply(message);
        };
    }
    @Bean("chatMessageListener")
    MessageListener chatMessageListener() {
        return message -> {
            chatWebSocketHandler.deliverChatMessage(message);
        };
    }
    @Bean("pingListener")
    MessageListener pingListener() {
        return message -> {
            String[] split = new String(message.getBody()).split(";");
            String senderId = split[0];
            String senderUserName = split[1];
            String recipient = split[2];
            log.info("Pinging " + recipient + " by " + senderId + " (" + senderUserName + ")");
            onlineWebSocketHandler.pingUser(recipient, senderId + ":" + senderUserName);
        };
    }
    /*@Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }*/
}
