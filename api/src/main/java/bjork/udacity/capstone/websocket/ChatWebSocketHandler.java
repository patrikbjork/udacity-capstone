package bjork.udacity.capstone.websocket;

import bjork.udacity.capstone.config.RabbitMQConfig;
import bjork.udacity.capstone.domain.ChatMessage;
import bjork.udacity.capstone.domain.UserInfo;
import bjork.udacity.capstone.repository.ChatMessageRepository;
import bjork.udacity.capstone.repository.UserInfoRepository;
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
import java.util.Optional;

import static bjork.udacity.capstone.config.RabbitMQConfig.*;

@Component
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private OnlineWebSocketHandler pingWebSocketHandler;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Map<String, WebSocketSession> userIdsToWebSocketSessions = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        /*JsonNode tree = JsonMapper.builder().build().readTree(message.getPayload());

        String to = tree.get("to").asText();
        String text = tree.get("text").asText();*/
        String recipient = session.getUri().getPath().split("/chat/")[1].split("/")[1];
        String sender = session.getUri().getPath().split("/chat/")[1].split("/")[0];

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setData(message.getPayload());
        chatMessage.setUserId(recipient);
        chatMessage.setOpponentId(sender);
        chatMessageRepository.save(chatMessage);

        // Just switch userId and opponentId so both users get the same message as theirs
        chatMessage = new ChatMessage();
        chatMessage.setData(message.getPayload());
        chatMessage.setUserId(sender);
        chatMessage.setOpponentId(recipient);
        chatMessageRepository.save(chatMessage);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("recipient", recipient);

        Message rabbitMQMessage = new Message(message.asBytes(), messageProperties);
        rabbitTemplate.send(chatFanoutExchangeName, "chat-message", rabbitMQMessage);
    }

//    @RabbitListener(queues = "chat-message-queue")
    public void deliverChatMessage(Message message) {
        String recipient = message.getMessageProperties().getHeader("recipient");

        log.info("Will see if chat message recipient is on my node: " + recipient);

        WebSocketSession session = userIdsToWebSocketSessions.get(recipient);
        if (session != null) {
            log.info("Found: " + recipient);
            try {
                session.sendMessage(new TextMessage(message.getBody()));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            log.info("Didn't find: " + recipient);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = session.getUri().getPath().split("/chat/")[1].split("/")[0]; // TODO use real token and decode and verify
        String recipient = session.getUri().getPath().split("/chat/")[1].split("/")[1]; // TODO use real token and decode and verify

        userIdsToWebSocketSessions.put(userId, session);

        Optional<UserInfo> byId = userInfoRepository.findById(userId);

        String text = userId + ";" + byId.get().getName() + ";" + recipient;
        Message message = new Message(text.getBytes(), new MessageProperties());

        rabbitTemplate.send(pingFanoutExchangeName, "", message);

//        pingWebSocketHandler.pingUser(recipient, userId + ":" + byId.get().getName());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        String user = session.getUri().getPath().split("/chat/")[1].split("/")[0]; // TODO use real token and decode and verify

        userIdsToWebSocketSessions.remove(user);

        super.afterConnectionClosed(session, status);
    }
}
