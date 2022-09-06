package tech.outspace.papershare.model.entity.objs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import tech.outspace.papershare.utils.general.SnowFlake;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "check_code", indexes = {@Index(name = "user_email_index", columnList = "email")})
public class CheckCode implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "expire_time", nullable = false)
    private LocalDateTime expireTime;

    @Transient
    private SnowFlake idGen = new SnowFlake(0, 2);

    public CheckCode(String email, String code, LocalDateTime expireTime) {
        this.id = idGen.NextId();
        this.email = email;
        this.code = code;
        this.expireTime = expireTime;
    }

    public CheckCode(String email, Integer code, LocalDateTime expireTime) {
        this.id = idGen.NextId();
        this.email = email;
        this.code = code.toString();
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "CheckCode{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", code='" + code + '\'' +
                ", expireTime=" + expireTime +
                '}';
    }
}
