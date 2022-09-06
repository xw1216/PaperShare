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
@Table(name = "session")
public class Session implements Serializable {
    @Id
    @Column(name = "id", nullable = false, length = 127)
    private String id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "secret", nullable = false)
    private String secret;

    @Column(name = "expire_time", nullable = false)
    private LocalDateTime expireTime;

    @Column(name = "close", nullable = false)
    private Boolean close = false;

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_session_user_id"))
    private User user;

    @Transient
    private SnowFlake idGen = new SnowFlake(0, 1);

    public Session(String userId, String secret, LocalDateTime expireTime, Boolean close) {
        this.id = idGen.NextId();
        this.userId = userId;
        this.secret = secret;
        this.expireTime = expireTime;
        this.close = close;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", userId=" + userId +
                ", secret='" + secret + '\'' +
                ", expireTime=" + expireTime +
                ", close=" + close +
                '}';
    }
}
