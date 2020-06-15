package bjork.udacity.capstone.repository;

import bjork.udacity.capstone.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByUserIdAndOpponentId(String userId, String opponentId);
}