package bjork.udacity.capstone.websocket;

import bjork.udacity.capstone.domain.ChatMessage;
import bjork.udacity.capstone.domain.UserInfo;
import bjork.udacity.capstone.repository.ChatMessageRepository;
import bjork.udacity.capstone.repository.UserInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatWebSocketHandlerTest {

    @Mock
    UserInfoRepository userInfoRepository;

    @Mock
    ChatMessageRepository chatMessageRepository;

    @Mock
    RabbitTemplate rabbitTemplate;

    @InjectMocks
    ChatWebSocketHandler chatWebSocketHandler = new ChatWebSocketHandler();

    @BeforeEach
    public void setup() {

    }

    @Test
    void handleTextMessage() throws Exception {

        // Given
        WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);
        when(webSocketSession.getUri()).thenReturn(new URI("http://localhost/ws/chat/user1/user2"));

        ArgumentCaptor<ChatMessage> chatMessageArgumentCaptor = ArgumentCaptor.forClass(ChatMessage.class);

        // When
        chatWebSocketHandler.handleTextMessage(webSocketSession, new TextMessage("the payload"));

        // Then
        Mockito.verify(chatMessageRepository, Mockito.times(2)).save(chatMessageArgumentCaptor.capture());
        List<ChatMessage> allValues = chatMessageArgumentCaptor.getAllValues();

        assertEquals("user2", allValues.get(0).getUserId());
        assertEquals("user1", allValues.get(0).getOpponentId());

        assertEquals("user1", allValues.get(1).getUserId());
        assertEquals("user2", allValues.get(1).getOpponentId());
    }

    @Test
    void deliverChatMessage() throws Exception {
        // Prepare
        UserInfo userInfo = new UserInfo();
        userInfo.setSub("user1");
        userInfo.setName("name1");
        when(userInfoRepository.findById(eq("user1"))).thenReturn(Optional.of(userInfo));

        WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);
        when(webSocketSession.getUri()).thenReturn(new URI("http://localhost/ws/chat/user1/user2"));
        chatWebSocketHandler.afterConnectionEstablished(webSocketSession);

        // Given
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("recipient", "user1");

        // When
        chatWebSocketHandler.deliverChatMessage(new Message("the body".getBytes(), messageProperties));

        // Then
        Mockito.verify(webSocketSession).sendMessage(Mockito.any(WebSocketMessage.class));
    }
}
