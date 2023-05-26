import com.cuping.cupingbe.global.jwt.refreshToken.RefreshToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private RefreshToken refreshTokenId;
    private String ownerId;
    private String nickname;
    private String password;
    private String authImageUrl; // binarycode

}