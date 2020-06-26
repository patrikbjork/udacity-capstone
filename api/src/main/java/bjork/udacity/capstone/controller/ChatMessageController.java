package bjork.udacity.capstone.controller;

import bjork.udacity.capstone.domain.ChatMessage;
import bjork.udacity.capstone.repository.ChatMessageRepository;
import com.auth0.spring.security.api.authentication.AuthenticationJsonWebToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/secure/messages")
public class ChatMessageController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;
//    @MessageMapping("/chat")
//    @SendToUser("chatMessage")
//    @SendTo("/topic/messages")
//    @CrossOrigin(origins = "*")
    /*public OutputMessage send(ChatMessage message) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }*/

    @RequestMapping("/my-messages/{opponent}")
    public List<ChatMessage> getMyMessages(@PathVariable("opponent") String opponentId,
                                           HttpServletRequest request) {

        AuthenticationJsonWebToken principal = (AuthenticationJsonWebToken) request.getUserPrincipal();

        return chatMessageRepository.findByUserIdAndOpponentIdOrderById(principal.getName(), opponentId);
    }

}
