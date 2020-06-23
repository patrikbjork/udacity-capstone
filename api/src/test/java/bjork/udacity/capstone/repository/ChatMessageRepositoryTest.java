package bjork.udacity.capstone.repository;

import bjork.udacity.capstone.domain.ChatMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ChatMessageRepositoryTest {

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @BeforeEach
    public void setup() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUserId("user1");
        chatMessage.setOpponentId("op1");

        chatMessageRepository.save(chatMessage);
    }

    @Test
    void findByUserIdAndOpponentId() {
        assertNotNull(chatMessageRepository.findByUserIdAndOpponentId("user1", "op1"));
    }
}
