package bjork.udacity.capstone.config;

import bjork.udacity.capstone.websocket.ChatWebSocketHandler;
import bjork.udacity.capstone.websocket.OnlineWebSocketHandler;
import bjork.udacity.capstone.websocket.UserOnlineCheckWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfigAlt implements WebSocketConfigurer {

    @Autowired
    private ChatWebSocketHandler chatWebSocketHandler;

    @Autowired
    private OnlineWebSocketHandler pingWebSocketHandler;

    @Autowired
    private UserOnlineCheckWebSocketHandler userOnlineCheckWebSocketHandler;

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/chat/{token}/{recipient}").setAllowedOrigins("*");
        registry.addHandler(pingWebSocketHandler, "/ping/{token}").setAllowedOrigins("*");
        registry.addHandler(userOnlineCheckWebSocketHandler, "/user-online-check/{token}").setAllowedOrigins("*");
    }

}
