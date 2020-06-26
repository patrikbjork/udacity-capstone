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
        ChatMessage c1 = new ChatMessage();
        c1.setUserId("user1");
        c1.setOpponentId("op1");

        ChatMessage c2 = new ChatMessage();
        c2.setUserId("user1");
        c2.setOpponentId("op1");

        chatMessageRepository.save(c1);
        chatMessageRepository.save(c2);
    }

    @Test
    void findByUserIdAndOpponentId() {
        assertNotNull(chatMessageRepository.findByUserIdAndOpponentIdOrderById("user1", "op1"));
    }
}
