package bjork.udacity.capstone.websocket;

import bjork.udacity.capstone.config.RabbitMQConfig;
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
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import static bjork.udacity.capstone.config.RabbitMQConfig.topicExchangeName;
import static bjork.udacity.capstone.config.RabbitMQConfig.userOnlineFanoutExchangeName;

@Component
@Slf4j
public class UserOnlineCheckWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Map<String, WebSocketSession> userIdsToWebSocketSessions = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        log.info("Received websocket message: " + message.getPayload());

        // TODO put centrally
//        String hostName = InetAddress.getLocalHost().getHostName();
        String replyTo = RabbitMQConfig.uuid;

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setReplyTo(replyTo);
        messageProperties.setHeader("sessionId", session.getId());

        Message rabbitMessage = new Message(message.getPayload().getBytes(), messageProperties);
        rabbitTemplate.send(userOnlineFanoutExchangeName, "user-online-check-query", rabbitMessage);
    }

//    @RabbitListener(queues = "user-online-check-reply-queue")
    public void listenToReply(Message message) {
        log.info("Got RabbitMQ reply message: " + new String(message.getBody()));

        String sessionId = message.getMessageProperties().getHeader("sessionId");

        WebSocketSession webSocketSession = userIdsToWebSocketSessions.get(sessionId);

        if (webSocketSession != null) {
            log.info("Even found the websocket session :)");
            try {
                webSocketSession.sendMessage(new TextMessage(new String(message.getBody())));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            log.info("Didn't find websocket session :(");
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        userIdsToWebSocketSessions.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

    }
}
