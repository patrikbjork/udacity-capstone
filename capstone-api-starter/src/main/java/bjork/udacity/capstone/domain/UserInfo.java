package bjork.udacity.capstone.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class UserInfo {

    @Id
    private String sub;
    private String nickname;
    private String name;
    private String picture;
    private String email;
}
