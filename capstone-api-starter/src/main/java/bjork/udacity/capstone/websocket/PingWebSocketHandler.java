package bjork.udacity.capstone.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class PingWebSocketHandler extends TextWebSocketHandler {

    private Map<String, WebSocketSession> userIdsToWebSocketSessions = new HashMap<>();

    public void pingUser(String recipient, String sender) throws IOException {
        if (userIdsToWebSocketSessions.containsKey(recipient)) {
            userIdsToWebSocketSessions.get(recipient).sendMessage(new TextMessage(sender));
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
