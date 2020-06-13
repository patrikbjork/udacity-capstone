package bjork.udacity.capstone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

//@Configuration
public class SocketSecurityConfig 
  extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    /*@Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
          .simpDestMatchers("/api/**").authenticated()
          .anyMessage().authenticated();
    }   */
}
