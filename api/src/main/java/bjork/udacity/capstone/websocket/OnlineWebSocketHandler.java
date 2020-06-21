package bjork.udacity.capstone.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static bjork.udacity.capstone.config.RabbitMQConfig.topicExchangeName;
import static bjork.udacity.capstone.config.RabbitMQConfig.userOnlineCheckQueueName;

@Component
@Slf4j
public class OnlineWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Map<String, WebSocketSession> userIdsToWebSocketSessions = new HashMap<>();

    public void pingUser(String recipient, String sender) {
        if (userIdsToWebSocketSessions.containsKey(recipient)) {
            try {
                userIdsToWebSocketSessions.get(recipient).sendMessage(new TextMessage(sender));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

//    @RabbitListener(queues = userOnlineCheckQueueName)
    public void handleOnlineCheckQuery(Message message) {
        log.info("Got online check request: " + new String(message.getBody()));
        String sessionId = message.getMessageProperties().getHeader("sessionId");

        String replyTo = message.getMessageProperties().getReplyTo();

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("sessionId", sessionId);

        String userId = new String(message.getBody());
        WebSocketSession webSocketSession = userIdsToWebSocketSessions.get(userId);

        if (webSocketSession != null) {
            String payload = userId + ":online";

            log.info("Sending RabbitMQ reply message: " + payload);

            Message rabbitMessage = new Message(payload.getBytes(), messageProperties);
            rabbitTemplate.send(topicExchangeName, "user-online-check-reply-" + replyTo, rabbitMessage);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        /*JsonNode tree = JsonMapper.builder().build().readTree(message.getPayload());

        String to = tree.get("to").asText();
        String text = tree.get("text").asText();*/
//        String recipient = session.getUri().getPath().split("/chat/")[1].split("/")[1];

//        userIdsToWebSocketSessions.get(recipient).sendMessage(message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String user = session.getUri().getPath().split("/ping/")[1]; // TODO use real token and decode and verify

        userIdsToWebSocketSessions.put(user, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        String user = session.getUri().getPath().split("/ping/")[1]; // TODO use real token and decode and verify

        userIdsToWebSocketSessions.remove(user);

        super.afterConnectionClosed(session, status);
    }
}
