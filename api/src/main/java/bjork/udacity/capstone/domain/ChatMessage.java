package bjork.udacity.capstone.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
//@Index(name="CH_USERID_OPPONENT", columnList={"userId", "opponentId"})
@Table(indexes = { @Index(name = "IDX_CH_USERID_OPPONENT", columnList = "userId, opponentId") })
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
//    private String from;
//    private String recipient;
    private String data;
    private String userId;
    private String opponentId;
}
