package answers.model;

import answers.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class User {
    public User(String email, Integer hashedPassword, UserType userType) {
        this.id = null;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.userType = userType;
    }

    private final Long id;
    private String email;
    private Integer hashedPassword;
    private UserType userType;
}
